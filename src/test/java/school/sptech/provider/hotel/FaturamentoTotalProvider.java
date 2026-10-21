package school.sptech.provider.hotel;

import static org.junit.jupiter.params.provider.Arguments.argumentSet;
import static school.sptech.factory.ReservaFactory.ativa;
import static school.sptech.factory.ReservaFactory.finalizada;

import java.util.stream.Stream;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import school.sptech.factory.HospedeFactory;
import school.sptech.factory.HospedeVipFactory;

public class FaturamentoTotalProvider implements ArgumentsProvider {

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context)
          throws Exception {
        return Stream.of(
              argumentSet("hotel sem hospedes -> 0.0", new Object[]{}, 0.0),
              argumentSet("hospedes sem reservas -> 0.0", new Object[]{
                    HospedeFactory.build("Ana", "111"),
                    HospedeVipFactory.build("Bruno", "222", 10.0)
              }, 0.0),
              argumentSet("um hospede -> 340.0", new Object[]{
                    HospedeFactory.build("Ana", "111", ativa(101, 3, 100.0))
              }, 340.0),
              argumentSet("classicos e Vip com 50% -> 1420.0", new Object[]{
                    HospedeFactory.build("Ana", "111", ativa(101, 3, 100.0), finalizada(102, 7, 100.0)),
                    HospedeVipFactory.build("Bruno", "222", 50.0, finalizada(301, 12, 100.0)),
                    HospedeFactory.build("Carla", "333")
              }, 1420.0),
              argumentSet("apenas Vip com 10% -> 846.0", new Object[]{
                    HospedeVipFactory.build("Bruno", "222", 10.0, ativa(101, 3, 100.0), finalizada(102, 7, 100.0))
              }, 846.0)
        );
    }
}
