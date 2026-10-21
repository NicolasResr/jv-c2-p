package school.sptech.factory;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import school.sptech.Hotel;
import school.sptech.util.ObjectFieldBuilder;
import school.sptech.util.ObjectFieldMapping;

public class HotelFactory {

    public static Map<String, Field> campos() throws ReflectiveOperationException {
        Map<String, Field> mapCampos = new HashMap<>();
        ObjectFieldMapping<Hotel> mapping = new ObjectFieldMapping<>(Hotel.class);

        mapCampos.put("nome", mapping.get("nome"));
        mapCampos.put("hospedes", mapping.get("hospedes"));

        return mapCampos;
    }

    public static Hotel build(String nome, Object... hospedes)
          throws ReflectiveOperationException {
        return new ObjectFieldBuilder<>(Hotel.class)
              .with("nome", nome)
              .with("hospedes", new ArrayList<>(Arrays.asList(hospedes)))
              .build();
    }

    @SuppressWarnings("unchecked")
    public static List<Object> hospedes(Hotel hotel) throws ReflectiveOperationException {
        return (List<Object>) campos().get("hospedes").get(hotel);
    }
}
