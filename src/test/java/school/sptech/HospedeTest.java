package school.sptech;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static school.sptech.util.ReflectionUtil.assertLanca;
import static school.sptech.util.ReflectionUtil.construir;
import static school.sptech.util.ReflectionUtil.invocar;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
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
import school.sptech.exceptions.LimiteReservasExcedidoException;
import school.sptech.factory.HospedeFactory;
import school.sptech.factory.ReservaFactory;
import school.sptech.hospede.Hospede;
import school.sptech.hospede.Reserva;
import school.sptech.provider.NullOrBlankProvider;
import school.sptech.provider.ReservaInvalidaProvider;
import school.sptech.provider.hospede.AdicionarReservaProvider;
import school.sptech.provider.hospede.AtributosHospedeProvider;
import school.sptech.provider.hospede.GettersSettersHospedeProvider;
import school.sptech.provider.hospede.CalcularTotalGastoProvider;
import school.sptech.provider.hospede.ContarReservasAtivasProvider;
import school.sptech.provider.hospede.HospedeValidoProvider;
import school.sptech.provider.hospede.LimiteReservasProvider;
import school.sptech.util.EncapsulationUtil;

@DisplayName("Hospede")
@TestClassOrder(ClassOrderer.OrderAnnotation.class)
class HospedeTest {

    private static final Class<?>[] CONSTRUTOR = {String.class, String.class};
    private static final Class<?>[] ADICIONAR = {Integer.class, Integer.class, Double.class};

    private static Object campoReserva(Object reserva, String campo)
          throws ReflectiveOperationException {
        return ReservaFactory.campos().get(campo).get(reserva);
    }

    @Nested
    @Order(1)
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @DisplayName("Hospede 1. Atributos e Encapsulamento")
    class AtributosTest {

        @ParameterizedTest(name = "{index}. {argumentSetName}")
        @ArgumentsSource(AtributosHospedeProvider.class)
        @Order(1)
        @DisplayName("1. Atributo deve existir com o tipo correto")
        void test1(String nome, Class<?> tipo) {
            EncapsulationUtil.validarAtributoExiste(Hospede.class, nome, tipo);
        }

        @ParameterizedTest(name = "{index}. {argumentSetName}")
        @ArgumentsSource(AtributosHospedeProvider.class)
        @Order(2)
        @DisplayName("2. Atributo deve ser protected")
        void test2(String nome) {
            EncapsulationUtil.validarModificadorAtributo(Hospede.class, nome, true);
        }

        @ParameterizedTest(name = "{index}. {argumentSetName}")
        @ArgumentsSource(GettersSettersHospedeProvider.class)
        @Order(3)
        @DisplayName("3. Atributo deve possuir os getters e setters esperados")
        void test3(String nome, Class<?> tipo, Boolean comGetter, Boolean comSetter) {
            EncapsulationUtil.validarGetterSetter(Hospede.class, nome, tipo, comGetter, comSetter);
        }
    }

    @Nested
    @Order(2)
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @DisplayName("Hospede 2. Construtor")
    class ConstrutorTest {

        @ParameterizedTest(name = "{index}. {argumentSetName}")
        @ArgumentsSource(NullOrBlankProvider.class)
        @Order(1)
        @DisplayName("1. Deve lancar DadoInvalidoException para nome invalido")
        void test1(String nome) {
            assertLanca(DadoInvalidoException.class,
                  () -> construir(Hospede.class, CONSTRUTOR, nome, "11122233344"));
        }

        @ParameterizedTest(name = "{index}. {argumentSetName}")
        @ArgumentsSource(NullOrBlankProvider.class)
        @Order(2)
        @DisplayName("2. Deve lancar DadoInvalidoException para cpf invalido")
        void test2(String cpf) {
            assertLanca(DadoInvalidoException.class,
                  () -> construir(Hospede.class, CONSTRUTOR, "Ana", cpf));
        }

        @ParameterizedTest(name = "{index}. {argumentSetName}")
        @ArgumentsSource(HospedeValidoProvider.class)
        @Order(3)
        @DisplayName("3. Deve inicializar nome, cpf, limite de 3 reservas e a lista vazia")
        void test3(String nome, String cpf) throws Throwable {
            Hospede hospede = construir(Hospede.class, CONSTRUTOR, nome, cpf);
            Map<String, Field> campos = HospedeFactory.campos();

            assertAll(
                  () -> assertEquals(nome, campos.get("nome").get(hospede)),
                  () -> assertEquals(cpf, campos.get("cpf").get(hospede)),
                  () -> assertEquals(3, campos.get("limiteReservasAtivas").get(hospede)),
                  () -> assertNotNull(campos.get("reservas").get(hospede)),
                  () -> assertTrue(HospedeFactory.reservas(hospede).isEmpty())
            );
        }
    }

    @Nested
    @Order(3)
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @DisplayName("Hospede 3. Metodo - contarReservasAtivas")
    class ContarReservasAtivasTest {

        @ParameterizedTest(name = "{index}. {argumentSetName}")
        @ArgumentsSource(ContarReservasAtivasProvider.class)
        @Order(1)
        @DisplayName("1. Deve contar apenas as reservas ativas")
        void test1(Object[] reservas, Integer esperado) throws Throwable {
            Hospede hospede = HospedeFactory.build("Ana", "11122233344", reservas);

            assertEquals(esperado, invocar(hospede, "contarReservasAtivas"));

            HospedeFactory.reservas(hospede).add(ReservaFactory.ativa(999, 1, 100.0));

            assertEquals(esperado + 1, invocar(hospede, "contarReservasAtivas"),
                  "Deve considerar a nova reserva ativa");
        }
    }

    @Nested
    @Order(4)
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @DisplayName("Hospede 4. Metodo - adicionarReserva")
    class AdicionarReservaTest {

        @ParameterizedTest(name = "{index}. {argumentSetName}")
        @ArgumentsSource(AdicionarReservaProvider.class)
        @Order(1)
        @DisplayName("1. Deve criar uma reserva ativa com os dados informados e adicionar na lista")
        void test1(Object[] iniciais, Integer numeroQuarto, Integer qtdDiarias,
              Double valorDiaria) throws Throwable {
            Hospede hospede = HospedeFactory.build("Ana", "11122233344", iniciais);

            invocar(hospede, "adicionarReserva", ADICIONAR, numeroQuarto, qtdDiarias,
                  valorDiaria);

            List<Object> reservas = HospedeFactory.reservas(hospede);

            assertEquals(iniciais.length + 1, reservas.size());
            Object nova = reservas.get(iniciais.length);

            for (int i = 0; i < iniciais.length; i++) {
                assertSame(iniciais[i], reservas.get(i),
                      "As reservas anteriores devem ser mantidas na mesma ordem");
            }

            assertAll(
                  () -> assertInstanceOf(Reserva.class, nova),
                  () -> assertEquals(numeroQuarto, campoReserva(nova, "numeroQuarto")),
                  () -> assertEquals(qtdDiarias, campoReserva(nova, "qtdDiarias")),
                  () -> assertEquals(valorDiaria, campoReserva(nova, "valorDiaria")),
                  () -> assertEquals(true, campoReserva(nova, "ativa"))
            );
        }

        @ParameterizedTest(name = "{index}. {argumentSetName}")
        @ArgumentsSource(LimiteReservasProvider.class)
        @Order(2)
        @DisplayName("2. Deve respeitar o limiteReservasAtivas e lancar LimiteReservasExcedidoException")
        void test2(Integer limite, Integer finalizadas) throws Throwable {
            Hospede hospede = HospedeFactory.buildComLimite("Ana", "11122233344", limite);
            List<Object> reservas = HospedeFactory.reservas(hospede);

            for (int i = 0; i < finalizadas; i++) {
                reservas.add(ReservaFactory.finalizada(500 + i, 2, 100.0));
            }

            for (int i = 0; i < limite; i++) {
                invocar(hospede, "adicionarReserva", ADICIONAR, 101 + i, 2, 100.0);
            }

            assertEquals(finalizadas + limite, reservas.size(),
                  "Deve permitir adicionar ate o limite de reservas ativas");

            assertLanca(LimiteReservasExcedidoException.class,
                  () -> invocar(hospede, "adicionarReserva", ADICIONAR, 999, 2, 100.0));
            assertEquals(finalizadas + limite, reservas.size(),
                  "Nenhuma reserva deve ser adicionada ao ultrapassar o limite");
        }

        @ParameterizedTest(name = "{index}. {argumentSetName}")
        @ArgumentsSource(ReservaInvalidaProvider.class)
        @Order(3)
        @DisplayName("3. Deve lancar DadoInvalidoException para dados invalidos e nao adicionar")
        void test3(Integer numeroQuarto, Integer qtdDiarias, Double valorDiaria)
              throws Throwable {
            Hospede hospede = HospedeFactory.build("Ana", "11122233344");

            assertLanca(DadoInvalidoException.class,
                  () -> invocar(hospede, "adicionarReserva", ADICIONAR, numeroQuarto,
                        qtdDiarias, valorDiaria));
            assertTrue(HospedeFactory.reservas(hospede).isEmpty());
        }
    }

    @Nested
    @Order(5)
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @DisplayName("Hospede 5. Metodo - calcularTotalGasto")
    class CalcularTotalGastoTest {

        @ParameterizedTest(name = "{index}. {argumentSetName}")
        @ArgumentsSource(CalcularTotalGastoProvider.class)
        @Order(1)
        @DisplayName("1. Deve somar o valor de todas as reservas")
        void test1(Object[] reservas, Double esperado) throws Throwable {
            Hospede hospede = HospedeFactory.build("Ana", "11122233344", reservas);

            assertEquals(esperado, (Double) invocar(hospede, "calcularTotalGasto"), 0.01);
        }
    }
}
