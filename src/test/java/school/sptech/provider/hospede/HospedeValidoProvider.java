package school.sptech.provider.hospede;

import static org.junit.jupiter.params.provider.Arguments.argumentSet;

import java.util.stream.Stream;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

public class HospedeValidoProvider implements ArgumentsProvider {

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context)
          throws Exception {
        return Stream.of(
              argumentSet("Ana, cpf 11122233344", "Ana", "11122233344"),
              argumentSet("Bruno Souza, cpf 98765432100", "Bruno Souza", "98765432100"),
              argumentSet("C, cpf 1", "C", "1")
        );
    }
}
