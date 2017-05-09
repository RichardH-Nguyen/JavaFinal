import org.json.JSONArray;
import org.json.JSONObject;

import java.util.LinkedList;

/**
 * Created by NinjaHunter on 5/8/17.
 */
public class Comic extends MarvelApi{

    protected Comic(String type){
        this.type = type;
        this.typeUrl = urlMain + type;

    }

    protected LinkedList getInfo(String responseBody, int index){
        JSONObject comic = new JSONObject(responseBody);
        //data gets response returned by the call.
        JSONObject comicData = comic.getJSONObject("data");
        //results is an ARRAY of marvel COMICS returned by the call.
        JSONArray comicResult = comicData.getJSONArray("results");
        JSONObject comicBook = comicResult.getJSONObject(index);
        String title = comicBook.getString("title");
        String description = comicBook.getString("description");

        JSONObject comicThumb = comicBook.getJSONObject("thumbnail");
        String imageURL = comicThumb.getString("path");
        String fullImageUrl = getImageUrl(imageURL);
        LinkedList<String> info = new LinkedList<String>();
        info.add(title);
        info.add(description);
        info.add(fullImageUrl);
        return info;
    }

    protected String titleSearch(String name) {
        String searchUrl = "titleStartsWith=" + name;
        return searchUrl;
    }
}
