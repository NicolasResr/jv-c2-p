package school.sptech.provider.reserva;

import static org.junit.jupiter.params.provider.Arguments.argumentSet;

import java.util.stream.Stream;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

public class CalcularValorProvider implements ArgumentsProvider {

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
        return Stream.of(
              argumentSet("3 diarias de 100.0 -> 340.0", 3, 100.0, 340.0),
              argumentSet("5 diarias de 200.0 -> 1040.0", 5, 200.0, 1040.0),
              argumentSet("6 diarias de 100.0 -> 520.0", 6, 100.0, 520.0),
              argumentSet("7 diarias de 100.0 -> 600.0", 7, 100.0, 600.0),
              argumentSet("8 diarias de 50.0 -> 360.0", 8, 50.0, 360.0),
              argumentSet("10 diarias de 200.0 -> 1640.0", 10, 200.0, 1640.0),
              argumentSet("11 diarias de 50.0 -> 440.0 (sem taxa de limpeza)", 11, 50.0, 440.0),
              argumentSet("12 diarias de 100.0 -> 960.0 (sem taxa de limpeza)", 12, 100.0, 960.0),
              argumentSet("15 diarias de 80.0 -> 960.0 (sem taxa de limpeza)", 15, 80.0, 960.0)
        );
    }
}
