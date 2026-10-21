package school.sptech.provider.exceptions;

import static org.junit.jupiter.params.provider.Arguments.argumentSet;

import java.util.stream.Stream;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import school.sptech.exceptions.DadoInvalidoException;
import school.sptech.exceptions.HospedeNaoEncontradoException;
import school.sptech.exceptions.LimiteReservasExcedidoException;
import school.sptech.exceptions.QuartoIndisponivelException;

public class ExceptionsProvider implements ArgumentsProvider {

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context)
          throws Exception {
        return Stream.of(
              argumentSet("DadoInvalidoException deve ser unchecked", DadoInvalidoException.class,
                    RuntimeException.class),
              argumentSet("HospedeNaoEncontradoException deve ser unchecked",
                    HospedeNaoEncontradoException.class, RuntimeException.class),
              argumentSet("LimiteReservasExcedidoException deve ser unchecked",
                    LimiteReservasExcedidoException.class, RuntimeException.class),
              argumentSet("QuartoIndisponivelException deve ser checked", QuartoIndisponivelException.class,
                    Exception.class)
        );
    }
}
