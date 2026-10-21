package school.sptech.provider.reserva;

import static org.junit.jupiter.params.provider.Arguments.argumentSet;

import java.util.stream.Stream;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

public class AtributosReservaProvider implements ArgumentsProvider {

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context)
          throws Exception {
        return Stream.of(
              argumentSet("numeroQuarto (Integer)", "numeroQuarto", Integer.class),
              argumentSet("qtdDiarias (Integer)", "qtdDiarias", Integer.class),
              argumentSet("valorDiaria (Double)", "valorDiaria", Double.class),
              argumentSet("ativa (Boolean)", "ativa", Boolean.class)
        );
    }
}
