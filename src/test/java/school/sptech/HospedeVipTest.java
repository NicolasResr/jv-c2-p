package school.sptech;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestClassOrder;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import school.sptech.exceptions.DadoInvalidoException;
import school.sptech.exceptions.LimiteReservasExcedidoException;
import school.sptech.factory.HospedeFactory;
import school.sptech.factory.HospedeVipFactory;
import school.sptech.factory.ReservaFactory;
import school.sptech.hospede.Hospede;
import school.sptech.hospede.HospedeVip;
import school.sptech.provider.hospedevip.AtributosHospedeVipProvider;
import school.sptech.provider.hospedevip.CalcularTotalGastoVipProvider;
import school.sptech.provider.hospedevip.GettersSettersHospedeVipProvider;
import school.sptech.provider.hospedevip.LimiteVipProvider;
import school.sptech.provider.hospedevip.PercentualInvalidoProvider;
import school.sptech.provider.hospedevip.PercentualValidoProvider;
import school.sptech.util.EncapsulationUtil;

@DisplayName("HospedeVip")
@TestClassOrder(ClassOrderer.OrderAnnotation.class)
class HospedeVipTest {

    private static final Class<?>[] CONSTRUTOR = {String.class, String.class, Double.class};
    private static final Class<?>[] ADICIONAR = {Integer.class, Integer.class, Double.class};

    @Nested
    @Order(1)
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @DisplayName("HospedeVip 1. Heranca, Atributos e Encapsulamento")
    class AtributosTest {

        @Test
        @Order(1)
        @DisplayName("1. Deve herdar de Hospede sem declarar novamente os atributos herdados")
        void test1() {
            assertEquals(Hospede.class, HospedeVip.class.getSuperclass());
        }

        @ParameterizedTest(name = "{index}. {argumentSetName}")
        @ArgumentsSource(AtributosHospedeVipProvider.class)
        @Order(2)
        @DisplayName("2. Atributo deve existir com o tipo correto")
        void test2(String nome, Class<?> tipo) {
            EncapsulationUtil.validarAtributoExiste(HospedeVip.class, nome, tipo);
        }

        @ParameterizedTest(name = "{index}. {argumentSetName}")
        @ArgumentsSource(AtributosHospedeVipProvider.class)
        @Order(3)
        @DisplayName("3. Atributo deve ser privado")
        void test3(String nome) {
            EncapsulationUtil.validarModificadorAtributo(HospedeVip.class, nome, false);
        }

        @ParameterizedTest(name = "{index}. {argumentSetName}")
        @ArgumentsSource(GettersSettersHospedeVipProvider.class)
        @Order(4)
        @DisplayName("4. Atributo deve possuir os getters e setters esperados")
        void test4(String nome, Class<?> tipo, Boolean comGetter, Boolean comSetter) {
            EncapsulationUtil.validarGetterSetter(HospedeVip.class, nome, tipo, comGetter,
                  comSetter);
        }
    }

    @Nested
    @Order(2)
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @DisplayName("HospedeVip 2. Construtor")
    class ConstrutorTest {

        @ParameterizedTest(name = "{index}. {argumentSetName}")
        @ArgumentsSource(PercentualInvalidoProvider.class)
        @Order(1)
        @DisplayName("1. Deve lancar DadoInvalidoException para percentualDesconto invalido")
        void test1(Double percentual) {
            assertLanca(DadoInvalidoException.class,
                  () -> construir(HospedeVip.class, CONSTRUTOR, "Ana", "11122233344",
                        percentual));
        }

        @ParameterizedTest(name = "{index}. {argumentSetName}")
        @ArgumentsSource(PercentualValidoProvider.class)
        @Order(2)
        @DisplayName("2. Deve inicializar todos os atributos (limite de 10 reservas ativas)")
        void test2(String nome, String cpf, Double percentual) throws Throwable {
            HospedeVip vip = construir(HospedeVip.class, CONSTRUTOR, nome, cpf, percentual);
            Map<String, Field> campos = HospedeVipFactory.campos();

            assertAll(
                  () -> assertEquals(nome, campos.get("nome").get(vip)),
                  () -> assertEquals(cpf, campos.get("cpf").get(vip)),
                  () -> assertEquals(percentual, campos.get("percentualDesconto").get(vip)),
                  () -> assertEquals(10, campos.get("limiteReservasAtivas").get(vip)),
                  () -> assertTrue(HospedeFactory.reservas(vip).isEmpty())
            );
        }
    }

    @Nested
    @Order(3)
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @DisplayName("HospedeVip 3. Limite de reservas ativas")
    class LimiteTest {

        @ParameterizedTest(name = "{index}. {argumentSetName}")
        @ArgumentsSource(LimiteVipProvider.class)
        @Order(1)
        @DisplayName("1. adicionarReserva deve permitir ate 10 reservas ativas")
        void test1(Integer finalizadas) throws Throwable {
            Object[] reservasFinalizadas = new Object[finalizadas];

            for (int i = 0; i < finalizadas; i++) {
                reservasFinalizadas[i] = ReservaFactory.finalizada(500 + i, 2, 100.0);
            }

            HospedeVip vip = HospedeVipFactory.build("Ana", "11122233344", 10.0,
                  reservasFinalizadas);

            for (int quarto = 101; quarto <= 110; quarto++) {
                invocar(vip, "adicionarReserva", ADICIONAR, quarto, 2, 100.0);
            }

            List<Object> reservas = HospedeFactory.reservas(vip);

            assertAll(
                  () -> assertEquals(finalizadas + 10, reservas.size()),
                  () -> assertLanca(LimiteReservasExcedidoException.class,
                        () -> invocar(vip, "adicionarReserva", ADICIONAR, 111, 2, 100.0)),
                  () -> assertEquals(finalizadas + 10, reservas.size())
            );
        }
    }

    @Nested
    @Order(4)
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @DisplayName("HospedeVip 4. Metodo - calcularTotalGasto")
    class CalcularTotalGastoTest {

        @ParameterizedTest(name = "{index}. {argumentSetName}")
        @ArgumentsSource(CalcularTotalGastoVipProvider.class)
        @Order(1)
        @DisplayName("1. Deve sobrescrever o metodo e aplicar o percentual de desconto")
        void test1(Double percentual, Object[] reservas, Double esperado) throws Throwable {
            assertDoesNotThrow(() -> HospedeVip.class.getDeclaredMethod("calcularTotalGasto"),
                  "HospedeVip deve sobrescrever o metodo calcularTotalGasto");

            HospedeVip vip = HospedeVipFactory.build("Ana", "11122233344", percentual, reservas);

            assertEquals(esperado, (Double) invocar(vip, "calcularTotalGasto"), 0.01);
        }
    }
}
