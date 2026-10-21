package school.sptech.provider.hospede;

import static org.junit.jupiter.params.provider.Arguments.argumentSet;
import static school.sptech.factory.ReservaFactory.ativa;
import static school.sptech.factory.ReservaFactory.finalizada;

import java.util.stream.Stream;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

public class AdicionarReservaProvider implements ArgumentsProvider {

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context)
          throws Exception {
        return Stream.of(
              argumentSet("sem reservas anteriores", new Object[]{}, 101, 3, 250.0),
              argumentSet("com uma reserva finalizada", new Object[]{
                    finalizada(101, 2, 100.0)
              }, 102, 4, 150.0),
              argumentSet("com uma ativa e duas finalizadas", new Object[]{
                    finalizada(101, 2, 100.0),
                    ativa(102, 1, 80.0),
                    finalizada(103, 5, 120.0)
              }, 104, 12, 90.5),
              argumentSet("com duas ativas (atinge o limite de 3)", new Object[]{
                    ativa(101, 2, 100.0),
                    ativa(102, 2, 100.0)
              }, 103, 1, 1000.0)
        );
    }
}
