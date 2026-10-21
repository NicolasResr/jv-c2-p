package school.sptech.provider.hotel;

import static org.junit.jupiter.params.provider.Arguments.argumentSet;
import static school.sptech.factory.ReservaFactory.ativa;

import java.util.stream.Stream;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import school.sptech.factory.HospedeFactory;
import school.sptech.factory.HospedeVipFactory;

public class CpfInexistenteProvider implements ArgumentsProvider {

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context)
          throws Exception {
        return Stream.of(
              argumentSet("cpf nao cadastrado", new Object[]{
                    HospedeFactory.build("Ana", "111", ativa(101, 2, 100.0))
              }, "999"),
              argumentSet("hotel sem hospedes", new Object[]{}, "111"),
              argumentSet("cpf nulo", new Object[]{
                    HospedeFactory.build("Ana", "111", ativa(101, 2, 100.0)),
                    HospedeVipFactory.build("Bruno", "222", 10.0)
              }, null),
              argumentSet("cpf em branco", new Object[]{
                    HospedeFactory.build("Ana", "111", ativa(101, 2, 100.0))
              }, " "),
              argumentSet("cpf parcialmente igual", new Object[]{
                    HospedeFactory.build("Ana", "111", ativa(101, 2, 100.0))
              }, "11")
        );
    }
}
