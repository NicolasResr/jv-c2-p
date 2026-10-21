package school.sptech.provider.hospedevip;

import static org.junit.jupiter.params.provider.Arguments.argumentSet;

import java.util.stream.Stream;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

public class PercentualInvalidoProvider implements ArgumentsProvider {

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
        return Stream.of(
              argumentSet("percentual nulo", (Double) null),
              argumentSet("percentual negativo (-1.0)", -1.0),
              argumentSet("percentual acima de 50 (50.1)", 50.1),
              argumentSet("percentual acima de 50 (80.0)", 80.0)
        );
    }
}
