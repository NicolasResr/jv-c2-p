package school.sptech;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.ClassOrderer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestClassOrder;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import school.sptech.provider.exceptions.ExceptionsProvider;

@DisplayName("Exceptions")
@TestClassOrder(ClassOrderer.OrderAnnotation.class)
class ExceptionsTest {

    @Nested
    @Order(1)
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @DisplayName("Exceptions 1. Herança")
    class HerancaTest {

        @ParameterizedTest(name = "{index}. {argumentSetName}")
        @ArgumentsSource(ExceptionsProvider.class)
        @Order(1)
        @DisplayName("1. Deve herdar da classe correta")
        void test1(Class<?> exception, Class<?> superclasse) {
            assertEquals(superclasse, exception.getSuperclass());
        }
    }
}
