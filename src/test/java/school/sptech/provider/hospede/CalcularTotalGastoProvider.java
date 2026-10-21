package school.sptech.provider.hospede;

import static org.junit.jupiter.params.provider.Arguments.argumentSet;
import static school.sptech.factory.ReservaFactory.ativa;
import static school.sptech.factory.ReservaFactory.finalizada;

import java.util.stream.Stream;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

public class CalcularTotalGastoProvider implements ArgumentsProvider {

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context)
          throws Exception {
        return Stream.of(
              argumentSet("sem reservas -> 0.0", new Object[]{}, 0.0),
              argumentSet("uma reserva ativa -> 340.0", new Object[]{
                    ativa(101, 3, 100.0)
              }, 340.0),
              argumentSet("reservas ativas e finalizadas -> 940.0", new Object[]{
                    ativa(101, 3, 100.0),
                    finalizada(102, 7, 100.0)
              }, 940.0),
              argumentSet("quatro reservas -> 2300.0", new Object[]{
                    finalizada(201, 1, 100.0),
                    finalizada(202, 6, 100.0),
                    finalizada(203, 5, 200.0),
                    ativa(204, 7, 100.0)
              }, 2300.0)
        );
    }
}
