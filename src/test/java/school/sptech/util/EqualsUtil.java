package school.sptech.util;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;
import org.junit.jupiter.api.function.Executable;

public class EqualsUtil {

    public static boolean equals(Object obj1, Object obj2) {
        if (obj1 == obj2) {
            return true;
        }
        if (obj1 == null || obj2 == null) {
            return false;
        }
        if (obj1.getClass() != obj2.getClass()) {
            return false;
        }

        Class<?> clazz = obj1.getClass();

        while (clazz != null) {
            Field[] fields = clazz.getDeclaredFields();

            for (Field field : fields) {
                if (Modifier.isStatic(field.getModifiers())) {
                    continue;
                }

                field.trySetAccessible();
                try {
                    Object v1 = field.get(obj1);
                    Object v2 = field.get(obj2);
                    if (!Objects.equals(v1, v2)) {
                        return false;
                    }
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }

            clazz = clazz.getSuperclass();
        }

        return true;
    }

    public static void assertListEquals(List<?> expected, List<?> atual) {
        assertEquals(expected.size(), atual.size(),
              () -> "Tamanhos diferentes - Esperado: " + expected.size() + ", Atual: "
                    + atual.size());

        assertAll(
              IntStream.range(0, expected.size())
                    .mapToObj(i -> (Executable) () -> {
                        Object e = expected.get(i);
                        Object a = atual.get(i);

                        assertTrue(
                              EqualsUtil.equals(e, a),
                              () -> "Diferença no índice " + i + "\nEsperado: " + e + "\nAtual: "
                                    + a
                        );
                    })
        );
    }

    public static void assertListEquals(List<?> expected, List<?> atual, String message) {
        if (expected == null && atual == null) {
            return;
        }
        if (expected == null) {
            fail(message + " - Lista esperada é null, mas a atual não é");
        }
        if (atual == null) {
            fail(message + " - Lista atual é null, mas a esperada não é");
        }

        assertEquals(expected.size(), atual.size(),
              () -> message + " - Tamanhos diferentes - Esperado: " + expected.size() + ", Atual: "
                    + atual.size());
        assertAll(
              IntStream.range(0, expected.size())
                    .mapToObj(i -> (Executable) () -> {
                        Object e = expected.get(i);
                        Object a = atual.get(i);

                        assertTrue(
                              EqualsUtil.equals(e, a),
                              () -> message + " - Diferença no índice " + i + "\nEsperado: " + e
                                    + "\nAtual: " + a
                        );
                    })
        );
    }

    public static void assertListEqualsUnordered(List<?> expected, List<?> atual, String message) {
        if (expected == null && atual == null) {
            return;
        }
        if (expected == null) {
            fail(message + " - Lista esperada é null, mas a atual não é");
        }
        if (atual == null) {
            fail(message + " - Lista atual é null, mas a esperada não é");
        }

        assertEquals(expected.size(), atual.size(),
              () -> "Tamanhos diferentes - Esperado: " + expected.size() + ", Atual: "
                    + atual.size());

        for (Object o : expected) {
            if (atual.stream().noneMatch(a -> EqualsUtil.equals(o, a))) {
                fail(message + " - Elemento esperado não encontrado na lista atual: "
                      + o);
            }
        }
    }

}
