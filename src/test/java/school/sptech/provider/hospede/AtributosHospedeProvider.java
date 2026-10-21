package school.sptech.provider.hospede;

import static org.junit.jupiter.params.provider.Arguments.argumentSet;

import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

public class AtributosHospedeProvider implements ArgumentsProvider {

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context)
          throws Exception {
        return Stream.of(
              argumentSet("nome (String)", "nome", String.class),
              argumentSet("cpf (String)", "cpf", String.class),
              argumentSet("limiteReservasAtivas (Integer)", "limiteReservasAtivas", Integer.class),
              argumentSet("reservas (List)", "reservas", List.class)
        );
    }
}
