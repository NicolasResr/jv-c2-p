package school.sptech.provider;

import static org.junit.jupiter.params.provider.Arguments.argumentSet;

import java.util.stream.Stream;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

public class NullOrBlankProvider implements ArgumentsProvider {

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
        return Stream.of(
              argumentSet("valor nulo", (String) null),
              argumentSet("valor em branco com 1 espaco (\" \")", " "),
              argumentSet("valor em branco com 3 espacos (\"   \")", "   ")
        );
    }
}
