package school.sptech.provider.hotel;

import static org.junit.jupiter.params.provider.Arguments.argumentSet;

import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import school.sptech.factory.HospedeFactory;
import school.sptech.factory.HospedeVipFactory;

public class HospedesVipProvider implements ArgumentsProvider {

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context)
          throws Exception {
        return Stream.of(
              argumentSet("hotel sem hospedes", new Object[]{}, List.of()),
              argumentSet("apenas classicos", new Object[]{
                    HospedeFactory.build("Ana", "111"),
                    HospedeFactory.build("Carla", "333")
              }, List.of()),
              argumentSet("um Vip depois de um classico", new Object[]{
                    HospedeFactory.build("Ana", "111"),
                    HospedeVipFactory.build("Bruno", "222", 10.0)
              }, List.of(1)),
              argumentSet("dois Vips intercalados", new Object[]{
                    HospedeVipFactory.build("Bruno", "222", 10.0),
                    HospedeFactory.build("Ana", "111"),
                    HospedeVipFactory.build("Diego", "444", 20.0)
              }, List.of(0, 2))
        );
    }
}
