package school.sptech.provider.reserva;

import static org.junit.jupiter.params.provider.Arguments.argumentSet;

import java.util.stream.Stream;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

public class ReservaValidaProvider implements ArgumentsProvider {

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context)
          throws Exception {
        return Stream.of(
              argumentSet("quarto 101, 3 diarias de 250.0", 101, 3, 250.0),
              argumentSet("quarto 7, 1 diaria de 89.9", 7, 1, 89.9),
              argumentSet("quarto 1203, 15 diarias de 1000.0", 1203, 15, 1000.0)
        );
    }
}
