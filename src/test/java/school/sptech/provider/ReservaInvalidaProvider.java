package school.sptech.provider;

import static org.junit.jupiter.params.provider.Arguments.argumentSet;

import java.util.stream.Stream;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

public class ReservaInvalidaProvider implements ArgumentsProvider {

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context)
          throws Exception {
        return Stream.of(
              argumentSet("numeroQuarto nulo", null, 4, 150.0),
              argumentSet("numeroQuarto igual a zero", 0, 4, 150.0),
              argumentSet("qtdDiarias igual a zero", 101, 0, 150.0),
              argumentSet("qtdDiarias negativa", 101, -1, 150.0),
              argumentSet("valorDiaria nulo", 101, 4, null),
              argumentSet("valorDiaria negativo", 101, 4, -10.0)
        );
    }
}
