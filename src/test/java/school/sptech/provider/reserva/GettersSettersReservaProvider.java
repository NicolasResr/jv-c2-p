package school.sptech.provider.reserva;

import static org.junit.jupiter.params.provider.Arguments.argumentSet;

import java.util.stream.Stream;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

public class GettersSettersReservaProvider implements ArgumentsProvider {

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context)
          throws Exception {
        return Stream.of(
              argumentSet("numeroQuarto (apenas getter)", "numeroQuarto", Integer.class, true,
                    false),
              argumentSet("qtdDiarias (apenas getter)", "qtdDiarias", Integer.class, true, false),
              argumentSet("valorDiaria (apenas getter)", "valorDiaria", Double.class, true, false),
              argumentSet("ativa (apenas getter)", "ativa", Boolean.class, true, false)
        );
    }
}
