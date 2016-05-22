package by.bsu.up;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by Dasha on 08.05.2016.
 */
@WebServlet(value = "/login"/*, initParams = {
        @WebInitParam(name = "hzhzhzzh"/*AuthenticationServlet.USER_NAME*//*, value = "hz chto eto")
}*/)
public class AuthenticationServlet extends HttpServlet {
    public static final String USER_NAME = "username";
    public static final String PASSWORD = "pass";

    private static final String FILE_FOR_USERS = "users.txt";
    private static final String FIELD_USERNAME = "username";
    private static final String FIELD_PASSWORD = "hashPassword";

    private static ArrayList<Item> users;

    private static final JSONParser jsonParser = new JSONParser();

    public static JSONParser getJsonParser() {
        return jsonParser;
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        users = new ArrayList<Item>();
        loadKeys();
        String tempUsername = req.getParameter(USER_NAME);
        String tempPassword = req.getParameter(PASSWORD);
        if (tempUsername == null || tempUsername.trim().isEmpty()) {
            resp.sendError(400, "Bad request");
            resp.getOutputStream().println(String.format("paramater %s is required", USER_NAME));
            return;
        }
        Item ourUser = getItemFromName(tempUsername);
        if (ourUser == null) {
            req.setAttribute("errorMsg", "Unsupporeted user. Try again:");
            getServletContext().getRequestDispatcher("/login.jsp").forward(req, resp);
        }
        if(ourUser.getHashPassword().equals(Hashcode.encryptPassword(tempPassword))) {
            resp.sendRedirect("pages/ff.html");
        }
        else {
            req.setAttribute("errorMsg", "Wrong username/password. Try again:");
            getServletContext().getRequestDispatcher("/login.jsp").forward(req, resp);
        }
    }

    public static Item getItemFromName (String username) {
        for (int i = 0; i < users.size(); ++i) {
            Item temp = users.get(i);
            if (temp.getUsername().equals(username))
                return temp;
        }
        return null;
    }

    /*@Override
    public void init() throws ServletException {
        super.init();
        users = new ArrayList<Item>();
        //users.add(new Item("dasha", "b40981aab75932c5b2f555f50769d878e44913d7"));
        loadKeys();
    }*/
    private String getProjectPath() {
        String path = getServletContext().getRealPath("/");
        return path.substring(0, path.length() - 18);
    }

    private String getUsersPath() {
        return getProjectPath() + "files/users.txt";
    }
    public void loadKeys () {
        File file = new File(getUsersPath());
        System.out.println(file.toString());
        Scanner sc = null;
        try {
            sc = new Scanner(new FileInputStream(file));
        }
        catch (IOException e) {}
        if (/*sc!= null && */sc.hasNextLine()) {
            String jsonArrayString = sc.nextLine();
            try {
                JSONArray jsonArray = (JSONArray) getJsonParser().parse(jsonArrayString);
                for (int i = 0; i < jsonArray.size(); i++) {
                    Item item = new Item();
                    JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                    item.setUsername((String) jsonObject.get(FIELD_USERNAME));
                    item.setHashPassword((String) jsonObject.get(FIELD_PASSWORD));
                    users.add(item);
                }
            }
            catch (ParseException e) {}
        }
    }
}