import java.util.List;
import com.google.gson.Gson;

public class JSON_Converter {
    /**
     * Main author: Michal Becmer
     **/
    public static String listToJsonString(List<DS_Weapons> list) {
        //Creates a new Gson object, then uses it to convert the list to a JSON string
        return new Gson().toJson(list);
    }
}
