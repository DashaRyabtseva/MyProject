package by.bsu.up.servlets;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Dasha on 30.05.2016.
 */
@WebServlet(value = "/logoff")

public class LogoffServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getSession().removeAttribute("id");
        req.getSession().removeAttribute("username");

        resp.sendRedirect("/login.jsp");
    }
}
