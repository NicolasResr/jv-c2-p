package school.sptech;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static school.sptech.util.ReflectionUtil.assertLanca;
import static school.sptech.util.ReflectionUtil.construir;
import static school.sptech.util.ReflectionUtil.invocar;

import java.lang.reflect.Field;
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
import school.sptech.factory.ReservaFactory;
import school.sptech.hospede.Reserva;
import school.sptech.provider.DecimalInvalidoProvider;
import school.sptech.provider.InteiroInvalidoProvider;
import school.sptech.provider.reserva.AtributosReservaProvider;
import school.sptech.provider.reserva.GettersSettersReservaProvider;
import school.sptech.provider.reserva.CalcularValorProvider;
import school.sptech.provider.reserva.ReservaValidaProvider;
import school.sptech.util.EncapsulationUtil;

@DisplayName("Reserva")
@TestClassOrder(ClassOrderer.OrderAnnotation.class)
class ReservaTest {

    private static final Class<?>[] CONSTRUTOR = {Integer.class, Integer.class, Double.class};

    @Nested
    @Order(1)
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @DisplayName("Reserva 1. Atributos e Encapsulamento")
    class AtributosTest {

        @ParameterizedTest(name = "{index}. {argumentSetName}")
        @ArgumentsSource(AtributosReservaProvider.class)
        @Order(1)
        @DisplayName("1. Atributo deve existir com o tipo correto")
        void test1(String nome, Class<?> tipo) {
            EncapsulationUtil.validarAtributoExiste(Reserva.class, nome, tipo);
        }

        @ParameterizedTest(name = "{index}. {argumentSetName}")
        @ArgumentsSource(AtributosReservaProvider.class)
        @Order(2)
        @DisplayName("2. Atributo deve ser privado")
        void test2(String nome) {
            EncapsulationUtil.validarModificadorAtributo(Reserva.class, nome, false);
        }

        @ParameterizedTest(name = "{index}. {argumentSetName}")
        @ArgumentsSource(GettersSettersReservaProvider.class)
        @Order(3)
        @DisplayName("3. Atributo deve possuir os getters e setters esperados")
        void test3(String nome, Class<?> tipo, Boolean comGetter, Boolean comSetter) {
            EncapsulationUtil.validarGetterSetter(Reserva.class, nome, tipo, comGetter, comSetter);
        }
    }

    @Nested
    @Order(2)
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @DisplayName("Reserva 2. Construtor")
    class ConstrutorTest {

        @ParameterizedTest(name = "{index}. {argumentSetName}")
        @ArgumentsSource(InteiroInvalidoProvider.class)
        @Order(1)
        @DisplayName("1. Deve lancar DadoInvalidoException para numeroQuarto invalido")
        void test1(Integer numeroQuarto) {
            assertLanca(DadoInvalidoException.class,
                  () -> construir(Reserva.class, CONSTRUTOR, numeroQuarto, 3, 100.0));
        }

        @ParameterizedTest(name = "{index}. {argumentSetName}")
        @ArgumentsSource(InteiroInvalidoProvider.class)
        @Order(2)
        @DisplayName("2. Deve lancar DadoInvalidoException para qtdDiarias invalida")
        void test2(Integer qtdDiarias) {
            assertLanca(DadoInvalidoException.class,
                  () -> construir(Reserva.class, CONSTRUTOR, 101, qtdDiarias, 100.0));
        }

        @ParameterizedTest(name = "{index}. {argumentSetName}")
        @ArgumentsSource(DecimalInvalidoProvider.class)
        @Order(3)
        @DisplayName("3. Deve lancar DadoInvalidoException para valorDiaria invalido")
        void test3(Double valorDiaria) {
            assertLanca(DadoInvalidoException.class,
                  () -> construir(Reserva.class, CONSTRUTOR, 101, 3, valorDiaria));
        }

        @ParameterizedTest(name = "{index}. {argumentSetName}")
        @ArgumentsSource(ReservaValidaProvider.class)
        @Order(4)
        @DisplayName("4. Deve inicializar os atributos e a reserva como ativa")
        void test4(Integer numeroQuarto, Integer qtdDiarias, Double valorDiaria)
              throws Throwable {
            Reserva reserva = construir(Reserva.class, CONSTRUTOR, numeroQuarto, qtdDiarias,
                  valorDiaria);
            Map<String, Field> campos = ReservaFactory.campos();

            assertAll(
                  () -> assertEquals(numeroQuarto, campos.get("numeroQuarto").get(reserva)),
                  () -> assertEquals(qtdDiarias, campos.get("qtdDiarias").get(reserva)),
                  () -> assertEquals(valorDiaria, campos.get("valorDiaria").get(reserva)),
                  () -> assertEquals(true, campos.get("ativa").get(reserva))
            );
        }
    }

    @Nested
    @Order(3)
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @DisplayName("Reserva 3. Metodo - calcularValor")
    class CalcularValorTest {

        @ParameterizedTest(name = "{index}. {argumentSetName}")
        @ArgumentsSource(CalcularValorProvider.class)
        @Order(1)
        @DisplayName("1. Deve calcular o valor da reserva ativa")
        void test1(Integer qtdDiarias, Double valorDiaria, Double esperado) throws Throwable {
            Reserva reserva = ReservaFactory.ativa(101, qtdDiarias, valorDiaria);

            assertEquals(esperado, (Double) invocar(reserva, "calcularValor"), 0.01);
        }

        @ParameterizedTest(name = "{index}. {argumentSetName}")
        @ArgumentsSource(CalcularValorProvider.class)
        @Order(2)
        @DisplayName("2. Deve calcular o valor da reserva finalizada")
        void test2(Integer qtdDiarias, Double valorDiaria, Double esperado) throws Throwable {
            Reserva reserva = ReservaFactory.finalizada(101, qtdDiarias, valorDiaria);

            assertEquals(esperado, (Double) invocar(reserva, "calcularValor"), 0.01);
        }
    }

    @Nested
    @Order(4)
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @DisplayName("Reserva 4. Metodo - finalizar")
    class FinalizarTest {

        @ParameterizedTest(name = "{index}. {argumentSetName}")
        @ArgumentsSource(ReservaValidaProvider.class)
        @Order(1)
        @DisplayName("1. Deve marcar a reserva como nao ativa sem alterar os demais atributos")
        void test1(Integer numeroQuarto, Integer qtdDiarias, Double valorDiaria)
              throws Throwable {
            Reserva reserva = ReservaFactory.ativa(numeroQuarto, qtdDiarias, valorDiaria);
            Map<String, Field> campos = ReservaFactory.campos();

            invocar(reserva, "finalizar");

            assertAll(
                  () -> assertEquals(false, campos.get("ativa").get(reserva)),
                  () -> assertEquals(numeroQuarto, campos.get("numeroQuarto").get(reserva)),
                  () -> assertEquals(qtdDiarias, campos.get("qtdDiarias").get(reserva)),
                  () -> assertEquals(valorDiaria, campos.get("valorDiaria").get(reserva))
            );
        }
    }
}
