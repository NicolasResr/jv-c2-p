package school.sptech.provider.hotel;

import static org.junit.jupiter.params.provider.Arguments.argumentSet;
import static school.sptech.factory.ReservaFactory.finalizada;

import java.util.stream.Stream;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import school.sptech.factory.HospedeFactory;
import school.sptech.factory.HospedeVipFactory;

public class HospedeQueMaisGastouProvider implements ArgumentsProvider {

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context)
          throws Exception {
        return Stream.of(
              argumentSet("hotel sem hospedes -> null", new Object[]{}, null),
              argumentSet("unico hospede", new Object[]{
                    HospedeFactory.build("Ana", "111", finalizada(101, 3, 100.0))
              }, 0),
              argumentSet("considera o desconto do Vip (530.0 x 700.0)", new Object[]{
                    HospedeVipFactory.build("Bruno", "222", 50.0, finalizada(301, 12, 100.0)),
                    HospedeFactory.build("Ana", "111", finalizada(101, 7, 100.0))
              }, 1),
              argumentSet("Vip que gastou mais mesmo com desconto (954.0 x 700.0)", new Object[]{
                    HospedeFactory.build("Ana", "111", finalizada(101, 7, 100.0)),
                    HospedeVipFactory.build("Diego", "444", 10.0, finalizada(301, 12, 100.0))
              }, 1)
        );
    }
}
