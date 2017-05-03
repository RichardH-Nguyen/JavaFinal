import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by NinjaHunter on 4/26/17.
 */
public class ComicCharacter extends MarvelApi {


    protected ComicCharacter(String type){
        this.type = type;
        this.typeUrl = urlMain + type;
    }

    protected String getSingleCharacterName(String responseBody,int index){
        JSONObject hero = new JSONObject(responseBody);
        //data gets response returned by the call.
        JSONObject heroData = hero.getJSONObject("data");
        //results is an ARRAY of marvel CHARACTERS returned by the call.
        JSONArray heroResult = heroData.getJSONArray("results");
        //grabbing the character from the heroResult array.
        JSONObject character = heroResult.getJSONObject(index);
        String name = character.getString("name");
        return name;
    }

    protected LinkedList getInfo(String responseBody, int index){
        String fullImageURL;
        JSONObject hero = new JSONObject(responseBody);
        //data gets response returned by the call.
        JSONObject heroData = hero.getJSONObject("data");
        //results is an ARRAY of marvel CHARACTERS returned by the call.
        JSONArray heroResult = heroData.getJSONArray("results");
        //grabbing the character from the heroResult array.
        JSONObject character = heroResult.getJSONObject(index);
        String name = character.getString("name");

        String description = character.getString("description");
        JSONObject charThumb = character.getJSONObject("thumbnail");
        String imageURL = charThumb.getString("path");
        String imageExtension = charThumb.getString("extension");
        fullImageURL = String.format(imageURL + "/portrait_xlarge." + imageExtension);
        LinkedList<String> info = new LinkedList<String>();

        info.add(name);
        info.add(description);
        info.add(fullImageURL);
        return info;

    }
}
