package school.sptech.factory;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import school.sptech.hospede.HospedeVip;
import school.sptech.util.ObjectFieldBuilder;
import school.sptech.util.ObjectFieldMapping;

public class HospedeVipFactory {

    public static Map<String, Field> campos() throws ReflectiveOperationException {
        Map<String, Field> mapCampos = new HashMap<>();
        ObjectFieldMapping<HospedeVip> mapping = new ObjectFieldMapping<>(HospedeVip.class);

        mapCampos.put("nome", mapping.get("nome"));
        mapCampos.put("cpf", mapping.get("cpf"));
        mapCampos.put("limiteReservasAtivas", mapping.get("limiteReservasAtivas"));
        mapCampos.put("reservas", mapping.get("reservas"));
        mapCampos.put("percentualDesconto", mapping.get("percentualDesconto"));

        return mapCampos;
    }

    public static HospedeVip build(String nome, String cpf, Double percentualDesconto,
          Object... reservas) throws ReflectiveOperationException {
        return new ObjectFieldBuilder<>(HospedeVip.class)
              .with("nome", nome)
              .with("cpf", cpf)
              .with("limiteReservasAtivas", 10)
              .with("reservas", new ArrayList<>(Arrays.asList(reservas)))
              .with("percentualDesconto", percentualDesconto)
              .build();
    }
}
