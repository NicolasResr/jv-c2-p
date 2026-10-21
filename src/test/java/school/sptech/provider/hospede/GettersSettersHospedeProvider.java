package school.sptech.provider.hospede;

import static org.junit.jupiter.params.provider.Arguments.argumentSet;

import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

public class GettersSettersHospedeProvider implements ArgumentsProvider {

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context)
          throws Exception {
        return Stream.of(
              argumentSet("nome (getter e setter)", "nome", String.class, true, true),
              argumentSet("cpf (getter e setter)", "cpf", String.class, true, true),
              argumentSet("limiteReservasAtivas (getter e setter)", "limiteReservasAtivas",
                    Integer.class, true, true),
              argumentSet("reservas (apenas getter)", "reservas", List.class, true, false)
        );
    }
}
