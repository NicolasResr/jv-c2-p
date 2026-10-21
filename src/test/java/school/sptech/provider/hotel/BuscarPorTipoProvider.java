package school.sptech.provider.hotel;

import static org.junit.jupiter.params.provider.Arguments.argumentSet;

import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import school.sptech.factory.HospedeFactory;
import school.sptech.factory.HospedeVipFactory;

public class BuscarPorTipoProvider implements ArgumentsProvider {

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context)
          throws Exception {
        return Stream.of(
              argumentSet("vip -> apenas Vips", new Object[]{
                    HospedeFactory.build("Ana", "111"),
                    HospedeVipFactory.build("Bruno", "222", 10.0),
                    HospedeFactory.build("Carla", "333"),
                    HospedeVipFactory.build("Diego", "444", 20.0)
              }, "vip", List.of(1, 3)),
              argumentSet("classico -> apenas nao Vips", new Object[]{
                    HospedeFactory.build("Ana", "111"),
                    HospedeVipFactory.build("Bruno", "222", 10.0),
                    HospedeFactory.build("Carla", "333"),
                    HospedeVipFactory.build("Diego", "444", 20.0)
              }, "classico", List.of(0, 2)),
              argumentSet("VIP (maiusculo)", new Object[]{
                    HospedeFactory.build("Ana", "111"),
                    HospedeVipFactory.build("Bruno", "222", 10.0),
                    HospedeFactory.build("Carla", "333"),
                    HospedeVipFactory.build("Diego", "444", 20.0)
              }, "VIP", List.of(1, 3)),
              argumentSet("CLASSICO (maiusculo)", new Object[]{
                    HospedeFactory.build("Ana", "111"),
                    HospedeVipFactory.build("Bruno", "222", 10.0),
                    HospedeFactory.build("Carla", "333"),
                    HospedeVipFactory.build("Diego", "444", 20.0)
              }, "CLASSICO", List.of(0, 2)),
              argumentSet("Classico (misto)", new Object[]{
                    HospedeFactory.build("Ana", "111"),
                    HospedeVipFactory.build("Bruno", "222", 10.0),
                    HospedeFactory.build("Carla", "333"),
                    HospedeVipFactory.build("Diego", "444", 20.0)
              }, "Classico", List.of(0, 2)),
              argumentSet("tipo desconhecido -> lista vazia", new Object[]{
                    HospedeFactory.build("Ana", "111"),
                    HospedeVipFactory.build("Bruno", "222", 10.0),
                    HospedeFactory.build("Carla", "333"),
                    HospedeVipFactory.build("Diego", "444", 20.0)
              }, "premium", List.of()),
              argumentSet("tipo em branco -> lista vazia", new Object[]{
                    HospedeFactory.build("Ana", "111"),
                    HospedeVipFactory.build("Bruno", "222", 10.0),
                    HospedeFactory.build("Carla", "333"),
                    HospedeVipFactory.build("Diego", "444", 20.0)
              }, " ", List.of()),
              argumentSet("vip sem hospedes Vip -> lista vazia", new Object[]{
                    HospedeFactory.build("Ana", "111")
              }, "vip", List.of()),
              argumentSet("classico sem hospedes classicos -> lista vazia", new Object[]{
                    HospedeVipFactory.build("Bruno", "222", 10.0)
              }, "classico", List.of())
        );
    }
}
