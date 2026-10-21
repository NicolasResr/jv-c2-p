package school.sptech.factory;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import school.sptech.hospede.Hospede;
import school.sptech.util.ObjectFieldBuilder;
import school.sptech.util.ObjectFieldMapping;

public class HospedeFactory {

    public static Map<String, Field> campos() throws ReflectiveOperationException {
        Map<String, Field> mapCampos = new HashMap<>();
        ObjectFieldMapping<Hospede> mapping = new ObjectFieldMapping<>(Hospede.class);

        mapCampos.put("nome", mapping.get("nome"));
        mapCampos.put("cpf", mapping.get("cpf"));
        mapCampos.put("limiteReservasAtivas", mapping.get("limiteReservasAtivas"));
        mapCampos.put("reservas", mapping.get("reservas"));

        return mapCampos;
    }

    public static Hospede build(String nome, String cpf, Object... reservas)
          throws ReflectiveOperationException {
        return buildComLimite(nome, cpf, 3, reservas);
    }

    public static Hospede buildComLimite(String nome, String cpf, Integer limiteReservasAtivas,
          Object... reservas) throws ReflectiveOperationException {
        return new ObjectFieldBuilder<>(Hospede.class)
              .with("nome", nome)
              .with("cpf", cpf)
              .with("limiteReservasAtivas", limiteReservasAtivas)
              .with("reservas", new ArrayList<>(Arrays.asList(reservas)))
              .build();
    }

    public static Object cpf(Object hospede) throws ReflectiveOperationException {
        return new ObjectFieldMapping<>(Hospede.class).get("cpf").get(hospede);
    }

    @SuppressWarnings("unchecked")
    public static List<Object> reservas(Object hospede) throws ReflectiveOperationException {
        return (List<Object>) new ObjectFieldMapping<>(hospede.getClass()).get("reservas")
              .get(hospede);
    }
}
