package school.sptech.provider.hospedevip;

import static org.junit.jupiter.params.provider.Arguments.argumentSet;

import java.util.stream.Stream;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

public class LimiteVipProvider implements ArgumentsProvider {

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context)
          throws Exception {
        return Stream.of(
              argumentSet("sem reservas finalizadas", 0),
              argumentSet("com 2 reservas finalizadas", 2),
              argumentSet("com 5 reservas finalizadas", 5)
        );
    }
}
