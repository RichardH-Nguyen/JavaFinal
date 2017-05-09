import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.math.BigInteger;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

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

    protected void getKey(){
        try {
            BufferedReader keyReader = new BufferedReader(new FileReader("urlKey.txt"));
            String publicKey = keyReader.readLine();
            String privateKey = keyReader.readLine();

            Random rnd = new Random();
            int ts = rnd.nextInt(10000000) + 10000000;
            System.out.println(ts);

            String hashMe = ts + privateKey + publicKey;

            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] keyBytes = hashMe.getBytes();
            byte[] hashBytes = md.digest(keyBytes);

            String hash = String.format("%032X", new BigInteger(1, hashBytes)); //http://stackoverflow.com/questions/5470219/get-md5-string-from-message-digest
            System.out.println(hash.toLowerCase());

            String baseurl = "&ts=%s&apikey=%s&hash=%s";
            key = String.format(baseurl, ts, publicKey, hash.toLowerCase());

            System.out.println(key);

            keyReader.close();

        }catch (IOException e){
            String error = "Cannot find key file";

        }catch (NoSuchAlgorithmException nsa){
            String error = "error";

        }
    }

    protected String getFullUrl(String searchURL){
        String fullUrl = typeUrl + searchURL + key;
        return fullUrl;

    }



    protected String getImage(String url){
        //get image to display.
        try {
            URL image = new URL(url);

            InputStream stream = image.openStream();

            String filename = "temp.jpg";   // TODO use unique filenames if you need to store many files
            Path filePath = new File(filename).toPath();

            // ... delete the file, if it exists, ignore otherwise ...
            try {
                Files.delete(filePath);
            } catch (NoSuchFileException nsfe) {
                //ignore!
                System.out.println("There was no temp file to delete. Probably not a problem.");
            }

            // .. and copy the URL stream to the file.
            Files.copy(stream, filePath);

            // ... close stream.
            stream.close();

            return filename;

        }catch (Exception e){
            String error = "error";
            return error;
        }

    }

    protected String getImageUrl(String url){
        String imageUrl = url + "/portrait_xlarge.jpg";
        return imageUrl;

    }

}
