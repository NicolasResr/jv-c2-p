package school.sptech.provider.hospede;

import static org.junit.jupiter.params.provider.Arguments.argumentSet;

import java.util.stream.Stream;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

public class LimiteReservasProvider implements ArgumentsProvider {

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context)
          throws Exception {
        return Stream.of(
              argumentSet("limite 3, sem reservas finalizadas", 3, 0),
              argumentSet("limite 3, com 2 reservas finalizadas", 3, 2),
              argumentSet("limite 1, sem reservas finalizadas", 1, 0),
              argumentSet("limite 5, com 1 reserva finalizada", 5, 1)
        );
    }
}
