package school.sptech.provider.hotel;

import static org.junit.jupiter.params.provider.Arguments.argumentSet;

import java.util.stream.Stream;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

public class NomeHotelProvider implements ArgumentsProvider {

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context)
          throws Exception {
        return Stream.of(
              argumentSet("Hotel SPTech", "Hotel SPTech"),
              argumentSet("Pousada do Mar", "Pousada do Mar"),
              argumentSet("H", "H")
        );
    }
}
