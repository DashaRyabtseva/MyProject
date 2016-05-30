package by.bsu.up;

import by.bsu.up.help.UsersInitialization;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Dasha on 08.05.2016.
 */
@WebServlet(value = "/login")

public class AuthenticationServlet extends HttpServlet {
    public static final String USER_NAME = "username";
    public static final String PASSWORD = "pass";
    public static final String ID = "id";


    private static ArrayList<Item> users;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        users = new ArrayList<Item>();
        UsersInitialization.loadKeys(getUsersPath(), users);
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
            req.getSession().setAttribute(ID, ourUser.getId());
            req.getSession().setAttribute(USER_NAME, ourUser.getUsername());
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

    private String getProjectPath() {
        String path = getServletContext().getRealPath("/");
        return path.substring(0, path.length() - 18);
    }

    private String getUsersPath() {
        return getProjectPath() + "files/users.txt";
    }

}