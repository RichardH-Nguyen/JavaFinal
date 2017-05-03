import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by NinjaHunter on 4/26/17.
 */
public class MarvelApi {
    protected String type;
    protected final String urlMain = "https://gateway.marvel.com:443/v1/public/";
    protected String idNum;
    protected String typeUrl;
    protected String key;

    protected String getDescription(int index, String responseBody){
        JSONObject hero = new JSONObject(responseBody);
        //data gets response returned by the call.
        JSONObject heroData = hero.getJSONObject("data");
        //results is an ARRAY of marvel CHARACTERS returned by the call.
        JSONArray heroResult = heroData.getJSONArray("results");
        //grabbing the character from the heroResult array.
        JSONObject character = heroResult.getJSONObject(index);
        //Getting the description from the JSON data.
        String descipt = character.getString("description");
        return descipt;
    }

    protected String getIdNum(int index, String responseBody){
        JSONObject hero = new JSONObject(responseBody);
        //data gets response returned by the call.
        JSONObject heroData = hero.getJSONObject("data");
        //results is an ARRAY of marvel CHARACTERS returned by the call.
        JSONArray heroResult = heroData.getJSONArray("results");
        //grabbing the character from the heroResult array.
        JSONObject character = heroResult.getJSONObject(index);
        //Getting the description from the JSON data.
        String id = character.getString("id");
        return id;

    }

    protected String getKey(){
        try {
            BufferedReader keyReader = new BufferedReader(new FileReader("urlKey.txt"));
            key = keyReader.readLine();
            return key;
        }catch (IOException e){
            String error = "Cannot find key file";
            return error;
        }
    }

    protected String getFullUrl(String name){
        String fullUrl = typeUrl + nameSearch(name) + key;
        return fullUrl;

    }

    protected String nameSearch(String name){
        String searchUrl = "nameStartsWith=" + name;
        return searchUrl;
    }

}
