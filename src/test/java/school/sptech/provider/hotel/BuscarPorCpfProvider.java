package school.sptech.provider.hotel;

import static org.junit.jupiter.params.provider.Arguments.argumentSet;

import java.util.stream.Stream;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import school.sptech.factory.HospedeFactory;
import school.sptech.factory.HospedeVipFactory;

public class BuscarPorCpfProvider implements ArgumentsProvider {

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context)
          throws Exception {
        return Stream.of(
              argumentSet("unico hospede", new Object[]{
                    HospedeFactory.build("Ana", "111")
              }, "111", 0),
              argumentSet("hospede Vip", new Object[]{
                    HospedeFactory.build("Ana", "111"),
                    HospedeVipFactory.build("Bruno", "222", 10.0)
              }, "222", 1),
              argumentSet("hospede no meio da lista", new Object[]{
                    HospedeFactory.build("Ana", "111"),
                    HospedeFactory.build("Carla", "333"),
                    HospedeVipFactory.build("Diego", "444", 20.0)
              }, "333", 1),
              argumentSet("hospede depois de um Vip", new Object[]{
                    HospedeVipFactory.build("Bruno", "222", 10.0),
                    HospedeFactory.build("Ana", "111")
              }, "111", 1)
        );
    }
}
