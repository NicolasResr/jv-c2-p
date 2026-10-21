package school.sptech.provider;

import static org.junit.jupiter.params.provider.Arguments.argumentSet;

import java.util.stream.Stream;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

public class DecimalInvalidoProvider implements ArgumentsProvider {

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
        return Stream.of(
              argumentSet("valor nulo", (Double) null),
              argumentSet("valor igual a zero (0.0)", 0.0),
              argumentSet("valor negativo (-150.0)", -150.0)
        );
    }
}
