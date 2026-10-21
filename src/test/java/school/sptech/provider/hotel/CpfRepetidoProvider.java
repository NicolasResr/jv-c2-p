package school.sptech.provider.hotel;

import static org.junit.jupiter.params.provider.Arguments.argumentSet;

import java.util.stream.Stream;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import school.sptech.factory.HospedeFactory;
import school.sptech.factory.HospedeVipFactory;

public class CpfRepetidoProvider implements ArgumentsProvider {

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context)
          throws Exception {
        return Stream.of(
              argumentSet("classico com cpf repetido", new Object[]{
                    HospedeFactory.build("Ana", "111")
              }, HospedeFactory.build("Outra Ana", "111"), HospedeFactory.build("Bruno", "222")),
              argumentSet("Vip com cpf de classico", new Object[]{
                    HospedeFactory.build("Ana", "111"),
                    HospedeFactory.build("Bruno", "222")
              }, HospedeVipFactory.build("Ze", "222", 10.0), HospedeFactory.build("Carla", "333")),
              argumentSet("classico com cpf de Vip", new Object[]{
                    HospedeVipFactory.build("Bruno", "222", 10.0)
              }, HospedeFactory.build("Bruno Clone", "222"), HospedeVipFactory.build("Diego", "444", 20.0))
        );
    }
}
