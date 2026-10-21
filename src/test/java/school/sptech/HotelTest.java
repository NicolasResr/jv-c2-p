package school.sptech;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static school.sptech.util.ReflectionUtil.assertLanca;
import static school.sptech.util.ReflectionUtil.construir;
import static school.sptech.util.ReflectionUtil.invocar;
import static school.sptech.util.ReflectionUtil.metodo;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.ClassOrderer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestClassOrder;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import school.sptech.exceptions.DadoInvalidoException;
import school.sptech.exceptions.HospedeNaoEncontradoException;
import school.sptech.exceptions.QuartoIndisponivelException;
import school.sptech.factory.HospedeFactory;
import school.sptech.factory.HospedeVipFactory;
import school.sptech.factory.HotelFactory;
import school.sptech.factory.ReservaFactory;
import school.sptech.hospede.Hospede;
import school.sptech.hospede.HospedeVip;
import school.sptech.provider.InteiroInvalidoProvider;
import school.sptech.provider.NullOrBlankProvider;
import school.sptech.provider.hotel.AtributosHotelProvider;
import school.sptech.provider.hotel.GettersSettersHotelProvider;
import school.sptech.provider.hotel.BuscarPorCpfProvider;
import school.sptech.provider.hotel.BuscarPorTipoProvider;
import school.sptech.provider.hotel.CadastrarHospedeProvider;
import school.sptech.provider.hotel.CpfInexistenteProvider;
import school.sptech.provider.hotel.CpfRepetidoProvider;
import school.sptech.provider.hotel.DisponibilidadeProvider;
import school.sptech.provider.hotel.FaturamentoTotalProvider;
import school.sptech.provider.hotel.HospedeQueMaisGastouProvider;
import school.sptech.provider.hotel.HospedesVipProvider;
import school.sptech.provider.hotel.NomeHotelProvider;
import school.sptech.util.EncapsulationUtil;

@DisplayName("Hotel")
@TestClassOrder(ClassOrderer.OrderAnnotation.class)
class HotelTest {

    private static final Class<?>[] NOME = {String.class};
    private static final Class<?>[] CPF = {String.class};
    private static final Class<?>[] QUARTO = {Integer.class};
    private static final Class<?>[] CADASTRAR = {Hospede.class};
    private static final Class<?>[] TIPO = {String.class};

    private static Hotel hotel(Object... hospedes) throws ReflectiveOperationException {
        return HotelFactory.build("Hotel SPTech", hospedes);
    }

    private static List<Integer> indicesVip(List<Object> hospedes) {
        List<Integer> indices = new ArrayList<>();

        for (int i = 0; i < hospedes.size(); i++) {
            if (hospedes.get(i) instanceof HospedeVip) {
                indices.add(i);
            }
        }

        return indices;
    }

    private static List<Object> porIndice(List<Object> hospedes, List<Integer> indices) {
        List<Object> resultado = new ArrayList<>();

        for (Integer indice : indices) {
            resultado.add(hospedes.get(indice));
        }

        return resultado;
    }

    private static void assertMesmosHospedes(List<?> esperado, Object resultado) {
        assertNotNull(resultado, "Deve retornar uma lista");
        List<?> lista = (List<?>) resultado;

        assertEquals(esperado.size(), lista.size(), "Quantidade de hospedes");

        for (int i = 0; i < esperado.size(); i++) {
            assertSame(esperado.get(i), lista.get(i), "Hospede na posicao " + i);
        }
    }

    @Nested
    @Order(1)
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @DisplayName("Hotel 01. Atributos e Encapsulamento")
    class AtributosTest {

        @ParameterizedTest(name = "{index}. {argumentSetName}")
        @ArgumentsSource(AtributosHotelProvider.class)
        @Order(1)
        @DisplayName("1. Atributo deve existir com o tipo correto")
        void test1(String nome, Class<?> tipo) {
            EncapsulationUtil.validarAtributoExiste(Hotel.class, nome, tipo);
        }

        @ParameterizedTest(name = "{index}. {argumentSetName}")
        @ArgumentsSource(AtributosHotelProvider.class)
        @Order(2)
        @DisplayName("2. Atributo deve ser privado")
        void test2(String nome) {
            EncapsulationUtil.validarModificadorAtributo(Hotel.class, nome, false);
        }

        @ParameterizedTest(name = "{index}. {argumentSetName}")
        @ArgumentsSource(GettersSettersHotelProvider.class)
        @Order(3)
        @DisplayName("3. Atributo deve possuir os getters e setters esperados")
        void test3(String nome, Class<?> tipo, Boolean comGetter, Boolean comSetter) {
            EncapsulationUtil.validarGetterSetter(Hotel.class, nome, tipo, comGetter, comSetter);
        }
    }

    @Nested
    @Order(2)
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @DisplayName("Hotel 02. Construtor")
    class ConstrutorTest {

        @ParameterizedTest(name = "{index}. {argumentSetName}")
        @ArgumentsSource(NullOrBlankProvider.class)
        @Order(1)
        @DisplayName("1. Deve lancar DadoInvalidoException para nome invalido")
        void test1(String nome) {
            assertLanca(DadoInvalidoException.class, () -> construir(Hotel.class, NOME, nome));
        }

        @ParameterizedTest(name = "{index}. {argumentSetName}")
        @ArgumentsSource(NomeHotelProvider.class)
        @Order(2)
        @DisplayName("2. Deve inicializar o nome e a lista de hospedes vazia")
        void test2(String nome) throws Throwable {
            Hotel hotel = construir(Hotel.class, NOME, nome);

            assertAll(
                  () -> assertEquals(nome, HotelFactory.campos().get("nome").get(hotel)),
                  () -> assertNotNull(HotelFactory.hospedes(hotel)),
                  () -> assertTrue(HotelFactory.hospedes(hotel).isEmpty())
            );
        }
    }

    @Nested
    @Order(3)
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @DisplayName("Hotel 03. Metodo - buscarPorCpf")
    class BuscarPorCpfTest {

        @ParameterizedTest(name = "{index}. {argumentSetName}")
        @ArgumentsSource(BuscarPorCpfProvider.class)
        @Order(1)
        @DisplayName("1. Deve retornar o hospede com o cpf informado")
        void test1(Object[] hospedes, String cpf, Integer indice) throws Throwable {
            Hotel hotel = hotel(hospedes);

            assertSame(hospedes[indice], invocar(hotel, "buscarPorCpf", CPF, cpf));
        }

        @ParameterizedTest(name = "{index}. {argumentSetName}")
        @ArgumentsSource(CpfInexistenteProvider.class)
        @Order(2)
        @DisplayName("2. Deve lancar HospedeNaoEncontradoException quando nao encontrar")
        void test2(Object[] hospedes, String cpf) throws Throwable {
            Hotel hotel = hotel(hospedes);

            assertLanca(HospedeNaoEncontradoException.class,
                  () -> invocar(hotel, "buscarPorCpf", CPF, cpf));
        }
    }

    @Nested
    @Order(4)
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @DisplayName("Hotel 04. Metodo - calcularFaturamentoTotal")
    class CalcularFaturamentoTotalTest {

        @ParameterizedTest(name = "{index}. {argumentSetName}")
        @ArgumentsSource(FaturamentoTotalProvider.class)
        @Order(1)
        @DisplayName("1. Deve somar o total gasto de todos os hospedes (aplicando desconto Vip)")
        void test1(Object[] hospedes, Double esperado) throws Throwable {
            Hotel hotel = hotel(hospedes);

            assertEquals(esperado, (Double) invocar(hotel, "calcularFaturamentoTotal"), 0.01);

            HotelFactory.hospedes(hotel).add(HospedeFactory.build("Novo", "999",
                  ReservaFactory.ativa(999, 3, 100.0)));

            assertEquals(esperado + 340.0, (Double) invocar(hotel, "calcularFaturamentoTotal"),
                  0.01, "Deve considerar o novo hospede que gastou 340.0");
        }
    }

    @Nested
    @Order(5)
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @DisplayName("Hotel 05. Metodo - buscarHospedeQueMaisGastou")
    class BuscarHospedeQueMaisGastouTest {

        @ParameterizedTest(name = "{index}. {argumentSetName}")
        @ArgumentsSource(HospedeQueMaisGastouProvider.class)
        @Order(1)
        @DisplayName("1. Deve retornar o hospede que mais gastou (null quando nao houver)")
        void test1(Object[] hospedes, Integer indice) throws Throwable {
            Hotel hotel = hotel(hospedes);
            Object resultado = invocar(hotel, "buscarHospedeQueMaisGastou");

            if (indice == null) {
                assertNull(resultado);
            } else {
                assertSame(hospedes[indice], resultado);
            }

            Hospede maior = HospedeFactory.build("Novo", "999",
                  ReservaFactory.finalizada(999, 12, 1000.0));
            HotelFactory.hospedes(hotel).add(maior);

            assertSame(maior, invocar(hotel, "buscarHospedeQueMaisGastou"),
                  "Deve retornar o novo hospede que gastou 10600.0");
        }
    }

    @Nested
    @Order(6)
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @DisplayName("Hotel 06. Metodo - buscarHospedesVip")
    class BuscarHospedesVipTest {

        @ParameterizedTest(name = "{index}. {argumentSetName}")
        @ArgumentsSource(HospedesVipProvider.class)
        @Order(1)
        @DisplayName("1. Deve retornar apenas os hospedes Vip na ordem de cadastro")
        void test1(Object[] hospedes, List<Integer> indices) throws Throwable {
            Hotel hotel = hotel(hospedes);
            List<Object> lista = HotelFactory.hospedes(hotel);

            assertMesmosHospedes(porIndice(lista, indices), invocar(hotel, "buscarHospedesVip"));

            lista.add(HospedeVipFactory.build("Novo Vip", "999", 10.0));

            assertMesmosHospedes(porIndice(lista, indicesVip(lista)),
                  invocar(hotel, "buscarHospedesVip"));
        }
    }

    @Nested
    @Order(7)
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @DisplayName("Hotel 07. Metodo - cadastrarHospede")
    class CadastrarHospedeTest {

        @ParameterizedTest(name = "{index}. {argumentSetName}")
        @ArgumentsSource(CadastrarHospedeProvider.class)
        @Order(1)
        @DisplayName("1. Deve adicionar o novo hospede")
        void test1(Object[] iniciais, Object novo) throws Throwable {
            Hotel hotel = hotel(iniciais);

            invocar(hotel, "cadastrarHospede", CADASTRAR, novo);

            List<Object> esperado = new ArrayList<>(Arrays.asList(iniciais));
            esperado.add(novo);

            assertMesmosHospedes(esperado, HotelFactory.hospedes(hotel));
        }

        @ParameterizedTest(name = "{index}. {argumentSetName}")
        @ArgumentsSource(CpfRepetidoProvider.class)
        @Order(2)
        @DisplayName("2. Nao deve cadastrar hospede com cpf ja cadastrado")
        void test2(Object[] iniciais, Object repetido, Object novo) throws Throwable {
            Hotel hotel = hotel(iniciais);

            assertDoesNotThrow(() -> invocar(hotel, "cadastrarHospede", CADASTRAR, repetido));
            assertMesmosHospedes(Arrays.asList(iniciais), HotelFactory.hospedes(hotel));

            invocar(hotel, "cadastrarHospede", CADASTRAR, novo);

            List<Object> esperado = new ArrayList<>(Arrays.asList(iniciais));
            esperado.add(novo);

            assertMesmosHospedes(esperado, HotelFactory.hospedes(hotel));
        }
    }

    @Nested
    @Order(8)
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @DisplayName("Hotel 08. Metodo - buscarPorTipo")
    class BuscarPorTipoTest {

        @ParameterizedTest(name = "{index}. {argumentSetName}")
        @ArgumentsSource(BuscarPorTipoProvider.class)
        @Order(1)
        @DisplayName("1. Deve retornar os hospedes do tipo informado na ordem de cadastro")
        void test1(Object[] hospedes, String tipo, List<Integer> indices) throws Throwable {
            Hotel hotel = hotel(hospedes);
            List<Object> lista = HotelFactory.hospedes(hotel);

            assertMesmosHospedes(porIndice(lista, indices),
                  invocar(hotel, "buscarPorTipo", TIPO, tipo));

            lista.add(HospedeVipFactory.build("Novo Vip", "998", 10.0));
            lista.add(HospedeFactory.build("Novo Classico", "999"));

            assertMesmosHospedes(porIndice(lista, indicesVip(lista)),
                  invocar(hotel, "buscarPorTipo", TIPO, "vip"));
        }
    }

    @Nested
    @Order(9)
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @DisplayName("Hotel 09. Metodo - verificarDisponibilidade")
    class VerificarDisponibilidadeTest {

        @ParameterizedTest(name = "{index}. {argumentSetName}")
        @ArgumentsSource(InteiroInvalidoProvider.class)
        @Order(1)
        @DisplayName("1. Deve lancar DadoInvalidoException para numeroQuarto invalido")
        void test1(Integer numeroQuarto) throws Throwable {
            Hotel hotel = hotel(HospedeFactory.build("Ana", "111",
                  ReservaFactory.ativa(101, 2, 100.0)));

            assertLanca(DadoInvalidoException.class,
                  () -> invocar(hotel, "verificarDisponibilidade", QUARTO, numeroQuarto));
        }

        @ParameterizedTest(name = "{index}. {argumentSetName}")
        @ArgumentsSource(DisponibilidadeProvider.class)
        @Order(2)
        @DisplayName("2. Deve lancar QuartoIndisponivelException apenas para quarto com reserva ativa")
        void test2(Object[] hospedes, Integer livre, Integer ocupado) throws Throwable {
            Method m = metodo(Hotel.class, "verificarDisponibilidade", QUARTO);
            assertTrue(Arrays.asList(m.getExceptionTypes())
                        .contains(QuartoIndisponivelException.class),
                  "Metodo verificarDisponibilidade deve declarar throws QuartoIndisponivelException");

            Hotel hotel = hotel(hospedes);

            assertAll(
                  () -> assertDoesNotThrow(
                        () -> invocar(hotel, "verificarDisponibilidade", QUARTO, livre),
                        "O quarto " + livre + " esta livre"),
                  () -> assertLanca(QuartoIndisponivelException.class,
                        () -> invocar(hotel, "verificarDisponibilidade", QUARTO, ocupado))
            );
        }
    }
}
