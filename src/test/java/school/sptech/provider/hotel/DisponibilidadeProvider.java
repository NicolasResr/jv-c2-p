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

public class DisponibilidadeProvider implements ArgumentsProvider {

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context)
          throws Exception {
        return Stream.of(
              argumentSet("quarto vizinho de uma reserva ativa", new Object[]{
                    HospedeFactory.build("Ana", "111", ativa(101, 2, 100.0))
              }, 102, 101),
              argumentSet("reserva ativa de um Vip", new Object[]{
                    HospedeFactory.build("Ana", "111", finalizada(101, 2, 100.0)),
                    HospedeVipFactory.build("Bruno", "222", 10.0, ativa(301, 2, 100.0), ativa(302, 2, 100.0))
              }, 101, 302),
              argumentSet("reserva finalizada nao ocupa o quarto", new Object[]{
                    HospedeFactory.build("Ana", "111", finalizada(101, 2, 100.0), ativa(102, 2, 100.0))
              }, 101, 102),
              argumentSet("ocupado por outro hospede", new Object[]{
                    HospedeFactory.build("Ana", "111"),
                    HospedeFactory.build("Bruno", "222", finalizada(205, 1, 90.0), ativa(206, 1, 90.0))
              }, 205, 206)
        );
    }
}
