package school.sptech.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.stream.Stream;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.function.Executable;

public class EncapsulationUtil {

    public static void validarGettersSetters(Class<?> clazz) {
        Field[] campos = clazz.getDeclaredFields();

        Stream<Executable> validacoesGetter = Arrays.stream(campos)
              .map((campo) -> () -> {
                  String getName = String.format("get%s",
                        StringUtils.capitalize(campo.getName()));
                  Method getter = clazz.getDeclaredMethod(getName);
                  int getModifier = getter.getModifiers();
                  Assertions.assertTrue(Modifier.isPublic(getModifier));
              });

        Stream<Executable> validacoesSetter = Arrays.stream(campos)
              .map((campo) -> () -> {
                  String setName = String.format("set%s",
                        StringUtils.capitalize(campo.getName()));
                  Method setter = clazz.getDeclaredMethod(setName, campo.getType());
                  int setModifier = setter.getModifiers();
                  Assertions.assertTrue(Modifier.isPublic(setModifier));
              });

        Assertions.assertAll(Stream.concat(validacoesGetter, validacoesSetter));
    }

    public static void validarGettersSettersExcetoSetters(Class<?> clazz, String... fields) {
        Field[] campos = clazz.getDeclaredFields();

        Stream<Executable> validacoesGetter = Arrays.stream(campos)
              .map((campo) -> () -> {
                  String getName = String.format("get%s",
                        StringUtils.capitalize(campo.getName()));
                  Method getter = clazz.getDeclaredMethod(getName);
                  int getModifier = getter.getModifiers();
                  Assertions.assertTrue(Modifier.isPublic(getModifier));
              });

        Stream<Executable> validacoesSetter = Arrays.stream(campos)
              .filter(campo -> Arrays.stream(fields)
                    .noneMatch(field -> field.equals(campo.getName())))
              .map((campo) -> () -> {
                  String setName = String.format("set%s",
                        StringUtils.capitalize(campo.getName()));
                  Method setter = clazz.getDeclaredMethod(setName, campo.getType());
                  int setModifier = setter.getModifiers();
                  Assertions.assertTrue(Modifier.isPublic(setModifier));
              });

        Assertions.assertAll(Stream.concat(validacoesGetter, validacoesSetter));
    }

    public static void validarApenasGetters(Class<?> clazz) {
        Field[] campos = clazz.getDeclaredFields();
        Assertions.assertTrue(campos.length > 0,
              String.format("A classe %s deve possuir atributos", clazz.getSimpleName()));

        Stream<Executable> validacoesGetter = Arrays.stream(campos)
              .map((campo) -> () -> {
                  String getName = String.format("get%s",
                        StringUtils.capitalize(campo.getName()));
                  Assertions.assertDoesNotThrow(() -> clazz.getDeclaredMethod(getName),
                        String.format("%s deve possuir o getter %s", campo.getName(), getName));
                  Method getter = clazz.getDeclaredMethod(getName);
                  Assertions.assertTrue(Modifier.isPublic(getter.getModifiers()),
                        String.format("%s deve ser publico", getName));
              });

        Stream<Executable> validacoesSetter = Arrays.stream(campos)
              .map((campo) -> () -> {
                  String setName = String.format("set%s",
                        StringUtils.capitalize(campo.getName()));
                  Assertions.assertThrows(NoSuchMethodException.class,
                        () -> clazz.getDeclaredMethod(setName, campo.getType()),
                        String.format("%s NAO deve possuir o setter %s", campo.getName(), setName));
              });

        Assertions.assertAll(Stream.concat(validacoesGetter, validacoesSetter));
    }

    public static void validarAtributoExiste(Class<?> clazz, String nome, Class<?> tipo) {
        Field campo = buscarCampo(clazz, nome);
        Assertions.assertEquals(tipo, campo.getType(),
              String.format("%s deve ser do tipo %s", nome, tipo.getSimpleName()));
    }

    public static void validarModificadorAtributo(Class<?> clazz, String nome, boolean protegido) {
        Field campo = buscarCampo(clazz, nome);
        Assertions.assertTrue(protegido
                    ? Modifier.isProtected(campo.getModifiers())
                    : Modifier.isPrivate(campo.getModifiers()),
              String.format("%s deve ser %s", nome, protegido ? "protected" : "private"));
    }

    public static void validarGetterSetter(Class<?> clazz, String nome, Class<?> tipo,
          boolean comGetter, boolean comSetter) {
        String sufixo = StringUtils.capitalize(nome);

        Assertions.assertAll(
              () -> {
                  if (comGetter) {
                      Method getter = Assertions.assertDoesNotThrow(
                            () -> clazz.getDeclaredMethod("get" + sufixo),
                            String.format("%s deve possuir o getter get%s", nome, sufixo));
                      Assertions.assertTrue(Modifier.isPublic(getter.getModifiers()),
                            String.format("get%s deve ser publico", sufixo));
                      Assertions.assertEquals(tipo, getter.getReturnType(),
                            String.format("get%s deve retornar %s", sufixo, tipo.getSimpleName()));
                  } else {
                      Assertions.assertThrows(NoSuchMethodException.class,
                            () -> clazz.getDeclaredMethod("get" + sufixo),
                            String.format("%s NAO deve possuir o getter get%s", nome, sufixo));
                  }
              },
              () -> {
                  if (comSetter) {
                      Method setter = Assertions.assertDoesNotThrow(
                            () -> clazz.getDeclaredMethod("set" + sufixo, tipo),
                            String.format("%s deve possuir o setter set%s", nome, sufixo));
                      Assertions.assertTrue(Modifier.isPublic(setter.getModifiers()),
                            String.format("set%s deve ser publico", sufixo));
                  } else {
                      Assertions.assertThrows(NoSuchMethodException.class,
                            () -> clazz.getDeclaredMethod("set" + sufixo, tipo),
                            String.format("%s NAO deve possuir o setter set%s", nome, sufixo));
                  }
              }
        );
    }

    private static Field buscarCampo(Class<?> clazz, String nome) {
        return Assertions.assertDoesNotThrow(() -> clazz.getDeclaredField(nome),
              String.format("A classe %s deve possuir o atributo %s", clazz.getSimpleName(), nome));
    }

    public static void validarAtributosProtegidos(Class<?> clazz) {
        Field[] campos = clazz.getDeclaredFields();
        Assertions.assertTrue(campos.length > 0,
              String.format("A classe %s deve possuir atributos", clazz.getSimpleName()));

        Assertions.assertAll(Arrays.stream(campos)
              .map(campo -> () -> Assertions.assertTrue(Modifier.isProtected(campo.getModifiers()),
                    String.format("%s deve ser protected", campo.getName()))));
    }

    public static void validarAtributosPrivados(Class<?> clazz) {
        Field[] campos = clazz.getDeclaredFields();
        Assertions.assertTrue(campos.length > 0,
              String.format("A classe %s deve possuir atributos", clazz.getSimpleName()));

        Assertions.assertAll(Arrays.stream(campos)
              .map(campo -> () -> Assertions.assertTrue(Modifier.isPrivate(campo.getModifiers()),
                    String.format("%s deve ser privado", campo.getName()))));
    }
}
