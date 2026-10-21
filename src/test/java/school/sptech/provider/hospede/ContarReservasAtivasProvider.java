package school.sptech.provider.hospede;

import static org.junit.jupiter.params.provider.Arguments.argumentSet;
import static school.sptech.factory.ReservaFactory.ativa;
import static school.sptech.factory.ReservaFactory.finalizada;

import java.util.stream.Stream;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

public class ContarReservasAtivasProvider implements ArgumentsProvider {

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context)
          throws Exception {
        return Stream.of(
              argumentSet("sem reservas -> 0", new Object[]{}, 0),
              argumentSet("apenas finalizadas -> 0", new Object[]{
                    finalizada(101, 2, 100.0),
                    finalizada(102, 2, 100.0)
              }, 0),
              argumentSet("uma ativa e uma finalizada -> 1", new Object[]{
                    finalizada(101, 2, 100.0),
                    ativa(102, 2, 100.0)
              }, 1),
              argumentSet("duas ativas -> 2", new Object[]{
                    ativa(101, 2, 100.0),
                    ativa(102, 2, 100.0)
              }, 2),
              argumentSet("tres ativas e uma finalizada -> 3", new Object[]{
                    ativa(101, 2, 100.0),
                    finalizada(102, 2, 100.0),
                    ativa(103, 2, 100.0),
                    ativa(104, 2, 100.0)
              }, 3)
        );
    }
}
