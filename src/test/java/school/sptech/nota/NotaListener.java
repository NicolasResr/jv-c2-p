package school.sptech.nota;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.junit.platform.engine.TestExecutionResult;
import org.junit.platform.engine.UniqueId;
import org.junit.platform.launcher.TestExecutionListener;
import org.junit.platform.launcher.TestIdentifier;
import org.junit.platform.launcher.TestPlan;

/**
 * Calcula a nota da prova ao final dos testes e gera target/nota.json, target/nota.csv e
 * target/nota-blocos.csv.
 * <p>
 * Usa os pesos do .github/classroom/autograding.json. Cada bloco (classe @Nested) vale
 * pontos x (cenarios aprovados / cenarios do bloco), a mesma regra do script de correcao.
 * Registrado em META-INF/services; roda com {@code mvn test -Pnota}.
 */
public class NotaListener implements TestExecutionListener {

    private static final Pattern OBJETO = Pattern.compile("\\{[^{}]*}");
    private static final Pattern NOME = Pattern.compile("\"name\"\\s*:\\s*\"((?:[^\"\\\\]|\\\\.)*)\"");
    private static final Pattern RUN = Pattern.compile("\"run\"\\s*:\\s*\"((?:[^\"\\\\]|\\\\.)*)\"");
    private static final Pattern PONTOS = Pattern.compile("\"points\"\\s*:\\s*([\\d.]+)");
    private static final Pattern TESTE = Pattern.compile("-Dtest=(\\S+)");

    private final Map<String, int[]> resultados = new LinkedHashMap<>();

    @Override
    public void executionFinished(TestIdentifier teste, TestExecutionResult resultado) {
        if (!teste.isTest()) {
            // Provider que falha antes de gerar os cenarios: conta como um cenario reprovado,
            // senao o teste sumiria do total do bloco e inflaria a nota
            if (resultado.getStatus() != TestExecutionResult.Status.SUCCESSFUL
                  && ultimoSegmento(teste).equals("test-template")) {
                resultados.computeIfAbsent(bloco(teste), chave -> new int[2])[1]++;
            }

            return;
        }

        int[] contagem = resultados.computeIfAbsent(bloco(teste), chave -> new int[2]);
        contagem[1]++;

        if (resultado.getStatus() == TestExecutionResult.Status.SUCCESSFUL) {
            contagem[0]++;
        }
    }

    @Override
    public void executionSkipped(TestIdentifier teste, String motivo) {
        if (teste.isTest()) {
            resultados.computeIfAbsent(bloco(teste), chave -> new int[2])[1]++;
        }
    }

    @Override
    public void testPlanExecutionFinished(TestPlan plano) {
        Path base = Path.of(System.getProperty("basedir", System.getProperty("user.dir")));
        Path autograding = base.resolve(".github/classroom/autograding.json");

        try {
            if (!Files.exists(autograding)) {
                System.out.println("[nota] autograding.json nao encontrado em " + autograding);
                return;
            }

            List<Bloco> blocos = lerBlocos(Files.readString(autograding, StandardCharsets.UTF_8));
            gravar(base, blocos);
        } catch (IOException e) {
            System.out.println("[nota] Nao foi possivel gerar a nota: " + e.getMessage());
        }
    }

    // Ex.: [class:school.sptech.HotelTest]/[nested-class:ReservarTest] -> HotelTest$ReservarTest
    private static String bloco(TestIdentifier teste) {
        UniqueId id = UniqueId.parse(teste.getUniqueId());
        StringBuilder chave = new StringBuilder();

        for (UniqueId.Segment segmento : id.getSegments()) {
            if (segmento.getType().equals("class")) {
                String classe = segmento.getValue();
                chave.append(classe.substring(classe.lastIndexOf('.') + 1));
            } else if (segmento.getType().equals("nested-class")) {
                chave.append('$').append(segmento.getValue());
            }
        }

        return chave.toString();
    }

    private static String ultimoSegmento(TestIdentifier teste) {
        return UniqueId.parse(teste.getUniqueId()).getLastSegment().getType();
    }

    private List<Bloco> lerBlocos(String json) {
        List<Bloco> blocos = new ArrayList<>();
        Matcher objeto = OBJETO.matcher(json);

        while (objeto.find()) {
            String texto = objeto.group();
            Matcher nome = NOME.matcher(texto);
            Matcher run = RUN.matcher(texto);
            Matcher pontos = PONTOS.matcher(texto);

            if (!nome.find() || !run.find() || !pontos.find()) {
                continue;
            }

            Matcher teste = TESTE.matcher(desescapar(run.group(1)));

            if (!teste.find()) {
                continue;
            }

            String chave = teste.group(1).replace("\\", "").replace("\"", "");
            int[] contagem = resultados.getOrDefault(chave, new int[2]);

            blocos.add(new Bloco(desescapar(nome.group(1)), chave,
                  Double.parseDouble(pontos.group(1)), contagem[0], contagem[1]));
        }

        return blocos;
    }

    private void gravar(Path base, List<Bloco> blocos) throws IOException {
        double pontuacao = 0.0;
        double maxima = 0.0;

        for (Bloco bloco : blocos) {
            pontuacao += bloco.obtido();
            maxima += bloco.pontos();
        }

        double nota = maxima == 0 ? 0.0 : pontuacao / maxima * 10;
        String dataHora = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);

        Path target = base.resolve("target");
        Files.createDirectories(target);

        StringBuilder json = new StringBuilder();
        json.append("{\n");
        json.append("  \"nota\": ").append(numero(nota)).append(",\n");
        json.append("  \"pontuacao\": ").append(numero(pontuacao)).append(",\n");
        json.append("  \"pontuacaoMaxima\": ").append(numero(maxima)).append(",\n");
        json.append("  \"dataHora\": ").append(texto(dataHora)).append(",\n");
        json.append("  \"blocos\": [\n");

        for (int i = 0; i < blocos.size(); i++) {
            Bloco bloco = blocos.get(i);
            json.append("    {")
                  .append("\"nome\": ").append(texto(bloco.nome()))
                  .append(", \"teste\": ").append(texto(bloco.teste()))
                  .append(", \"aprovados\": ").append(bloco.aprovados())
                  .append(", \"total\": ").append(bloco.total())
                  .append(", \"pontos\": ").append(numero(bloco.pontos()))
                  .append(", \"obtido\": ").append(numero(bloco.obtido()))
                  .append(i < blocos.size() - 1 ? "},\n" : "}\n");
        }

        json.append("  ]\n}\n");

        // nota.csv: uma unica linha, com os pontos obtidos em cada bloco
        StringBuilder csv = new StringBuilder("Nota Final,Pontuacao,Pontuacao Maxima");
        StringBuilder linha = new StringBuilder(String.join(",", numero(nota),
              numero(pontuacao), numero(maxima)));

        for (Bloco bloco : blocos) {
            csv.append(',').append(celula(bloco.nome()));
            linha.append(',').append(numero(bloco.obtido()));
        }

        csv.append('\n').append(linha).append('\n');

        // nota-blocos.csv: uma linha por bloco, com cada valor em sua propria coluna
        StringBuilder csvBlocos = new StringBuilder(
              "Bloco,Teste,Aprovados,Total,Peso,Pontos Obtidos\n");

        for (Bloco bloco : blocos) {
            csvBlocos.append(String.join(",", celula(bloco.nome()), celula(bloco.teste()),
                  String.valueOf(bloco.aprovados()),
                  String.valueOf(bloco.total()), numero(bloco.pontos()),
                  numero(bloco.obtido()))).append('\n');
        }

        Files.writeString(target.resolve("nota.json"), json, StandardCharsets.UTF_8);
        Files.writeString(target.resolve("nota.csv"), csv, StandardCharsets.UTF_8);
        Files.writeString(target.resolve("nota-blocos.csv"), csvBlocos, StandardCharsets.UTF_8);

        System.out.println();
        System.out.printf("[nota] Pontuacao: %s/%s | Nota: %s/10%n", numero(pontuacao),
              numero(maxima), numero(nota));
        System.out.println(
              "[nota] Detalhes em target/nota.json, target/nota.csv e target/nota-blocos.csv");
    }

    private static String desescapar(String valor) {
        return valor.replace("\\\"", "\"").replace("\\\\", "\\");
    }

    private static String numero(double valor) {
        return String.format(Locale.US, "%.2f", valor);
    }

    private static String texto(String valor) {
        return "\"" + valor.replace("\\", "\\\\").replace("\"", "\\\"") + "\"";
    }

    private static String celula(String valor) {
        return valor.contains(",") || valor.contains("\"")
              ? "\"" + valor.replace("\"", "\"\"") + "\""
              : valor;
    }

    private record Bloco(String nome, String teste, double pontos, int aprovados, int total) {

        double obtido() {
            return total == 0 ? 0.0 : pontos * aprovados / total;
        }
    }
}
