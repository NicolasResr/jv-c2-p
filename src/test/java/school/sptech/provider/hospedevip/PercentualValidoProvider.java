package school.sptech.provider.hospedevip;

import static org.junit.jupiter.params.provider.Arguments.argumentSet;

import java.util.stream.Stream;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

public class PercentualValidoProvider implements ArgumentsProvider {

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context)
          throws Exception {
        return Stream.of(
              argumentSet("Ana, 0% de desconto", "Ana", "111", 0.0),
              argumentSet("Bruno, 15.5% de desconto", "Bruno", "222", 15.5),
              argumentSet("Carla, 50% de desconto", "Carla", "333", 50.0)
        );
    }
}
