package by.bsu.up.help;

import by.bsu.up.Item;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by Dasha on 27.05.2016.
 */
public class UsersInitialization {

    private static final String FIELD_USERNAME = "username";
    private static final String FIELD_PASSWORD = "hashPassword";
    private static final String FIELD_ID = "id";


    private static final JSONParser jsonParser = new JSONParser();

    public static JSONParser getJsonParser() { return jsonParser;}

    public static void loadKeys (String path, ArrayList<Item> users) {
        File file = new File(path);
        Scanner sc = null;
        try {
            sc = new Scanner(new FileInputStream(file));
        }
        catch (IOException e) {}
        if (sc.hasNextLine()) {
            String jsonArrayString = sc.nextLine();
            try {
                JSONArray jsonArray = (JSONArray) getJsonParser().parse(jsonArrayString);
                for (Object aJsonArray : jsonArray) {
                    Item item = new Item();
                    JSONObject jsonObject = (JSONObject) aJsonArray;
                    item.setUsername((String) jsonObject.get(FIELD_USERNAME));
                    item.setHashPassword((String) jsonObject.get(FIELD_PASSWORD));
                    item.setId((String) jsonObject.get(FIELD_ID));
                    users.add(item);
                }
            }
            catch (ParseException e) {}
        }
    }
}
