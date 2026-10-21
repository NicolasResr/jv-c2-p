package school.sptech.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.function.Executable;

public class ReflectionUtil {

    public static Method metodo(Class<?> clazz, String nome, Class<?>... tipos) {
        try {
            return clazz.getMethod(nome, tipos);
        } catch (NoSuchMethodException e) {
            throw new AssertionError(
                  String.format("Método público %s não encontrado na classe %s", nome,
                        clazz.getSimpleName()), e);
        }
    }

    public static Object invocar(Object alvo, String nome, Class<?>[] tipos, Object... args)
          throws Throwable {
        Method metodo = metodo(alvo.getClass(), nome, tipos);

        try {
            return metodo.invoke(alvo, args);
        } catch (InvocationTargetException e) {
            throw e.getCause();
        }
    }

    public static Object invocar(Object alvo, String nome) throws Throwable {
        return invocar(alvo, nome, new Class<?>[0]);
    }

    public static <T> T construir(Class<T> clazz, Class<?>[] tipos, Object... args)
          throws Throwable {
        try {
            return clazz.getConstructor(tipos).newInstance(args);
        } catch (NoSuchMethodException e) {
            throw new AssertionError(
                  String.format("Construtor público não encontrado na classe %s",
                        clazz.getSimpleName()), e);
        } catch (InvocationTargetException e) {
            throw e.getCause();
        }
    }

    public static void assertLanca(Class<?> esperada, Executable executavel) {
        try {
            executavel.execute();
        } catch (Throwable t) {
            Assertions.assertTrue(esperada.isInstance(t),
                  String.format("Esperava %s, mas foi lançada %s", esperada.getSimpleName(),
                        t.getClass().getSimpleName()));
            return;
        }

        Assertions.fail(String.format("Esperava %s, mas nenhuma exceção foi lançada",
              esperada.getSimpleName()));
    }
}
