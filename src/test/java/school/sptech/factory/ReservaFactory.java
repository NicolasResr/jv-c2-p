package school.sptech.factory;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import school.sptech.hospede.Reserva;
import school.sptech.util.ObjectFieldBuilder;
import school.sptech.util.ObjectFieldMapping;

public class ReservaFactory {

    public static Map<String, Field> campos() throws ReflectiveOperationException {
        Map<String, Field> mapCampos = new HashMap<>();
        ObjectFieldMapping<Reserva> mapping = new ObjectFieldMapping<>(Reserva.class);

        mapCampos.put("numeroQuarto", mapping.get("numeroQuarto"));
        mapCampos.put("qtdDiarias", mapping.get("qtdDiarias"));
        mapCampos.put("valorDiaria", mapping.get("valorDiaria"));
        mapCampos.put("ativa", mapping.get("ativa"));

        return mapCampos;
    }

    public static Reserva build(Integer numeroQuarto, Integer qtdDiarias, Double valorDiaria,
          Boolean ativa) throws ReflectiveOperationException {
        return new ObjectFieldBuilder<>(Reserva.class)
              .with("numeroQuarto", numeroQuarto)
              .with("qtdDiarias", qtdDiarias)
              .with("valorDiaria", valorDiaria)
              .with("ativa", ativa)
              .build();
    }

    public static Reserva ativa(Integer numeroQuarto, Integer qtdDiarias, Double valorDiaria)
          throws ReflectiveOperationException {
        return build(numeroQuarto, qtdDiarias, valorDiaria, true);
    }

    public static Reserva finalizada(Integer numeroQuarto, Integer qtdDiarias, Double valorDiaria)
          throws ReflectiveOperationException {
        return build(numeroQuarto, qtdDiarias, valorDiaria, false);
    }
}
