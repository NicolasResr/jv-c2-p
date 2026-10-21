package school.sptech.provider.hotel;

import static org.junit.jupiter.params.provider.Arguments.argumentSet;

import java.util.stream.Stream;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import school.sptech.factory.HospedeFactory;
import school.sptech.factory.HospedeVipFactory;

public class CadastrarHospedeProvider implements ArgumentsProvider {

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context)
          throws Exception {
        return Stream.of(
              argumentSet("hotel vazio", new Object[]{}, HospedeFactory.build("Ana", "111")),
              argumentSet("hospede Vip", new Object[]{
                    HospedeFactory.build("Ana", "111")
              }, HospedeVipFactory.build("Bruno", "222", 10.0)),
              argumentSet("hotel com classico e Vip", new Object[]{
                    HospedeFactory.build("Ana", "111"),
                    HospedeVipFactory.build("Bruno", "222", 10.0)
              }, HospedeFactory.build("Carla", "333"))
        );
    }
}
