import java.util.List;
import com.google.gson.Gson;
import java.lang.reflect.Type;
import com.google.gson.reflect.TypeToken;

public class JSON_Converter {
    /**
     * Main author: Michal Becmer
     **/
    public static String listToJsonString(List<DS_Weapons> list) {
        //Creates a new Gson object, then uses it to convert the list to a JSON string
        return new Gson().toJson(list);
    }

    /**
     * Main author: Stephen Carragher Kelly
     **/
    public static String EntitytoJsonString(DS_Weapons weapon)
    {
        return new Gson().toJson(weapon);
    }

    public static DS_Weapons JsonStringtoEntity(String JsonWeapon, Class<DS_Weapons> weaponClass)
    {
        return new Gson().fromJson(JsonWeapon, weaponClass);
    }

    public static <T>List<T> jsonStringtoList(String jsonString, Class<T> typeClass)
    {
        Gson gson = new Gson();
        Type listType = TypeToken.getParameterized(List.class, typeClass).getType();
        return gson.fromJson(jsonString, listType);
    }

    //converts an array of strings to a json string
    public static String arrayToJsonString(String[] array) {
        return new Gson().toJson(array);
    }

}