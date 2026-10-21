package school.sptech.provider.hospedevip;

import static org.junit.jupiter.params.provider.Arguments.argumentSet;
import static school.sptech.factory.ReservaFactory.ativa;
import static school.sptech.factory.ReservaFactory.finalizada;

import java.util.stream.Stream;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

public class CalcularTotalGastoVipProvider implements ArgumentsProvider {

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context)
          throws Exception {
        return Stream.of(
              argumentSet("sem reservas -> 0.0", 10.0, new Object[]{}, 0.0),
              argumentSet("10% de desconto em 940.0 -> 846.0", 10.0, new Object[]{
                    ativa(101, 3, 100.0),
                    finalizada(102, 7, 100.0)
              }, 846.0),
              argumentSet("20% de desconto em 940.0 -> 752.0", 20.0, new Object[]{
                    ativa(101, 3, 100.0),
                    finalizada(102, 7, 100.0)
              }, 752.0),
              argumentSet("50% de desconto em 960.0 -> 480.0", 50.0, new Object[]{
                    finalizada(301, 12, 100.0)
              }, 480.0),
              argumentSet("25% de desconto em 2300.0 -> 1725.0", 25.0, new Object[]{
                    finalizada(201, 1, 100.0),
                    ativa(202, 6, 100.0),
                    ativa(203, 5, 200.0),
                    finalizada(204, 7, 100.0)
              }, 1725.0)
        );
    }
}
