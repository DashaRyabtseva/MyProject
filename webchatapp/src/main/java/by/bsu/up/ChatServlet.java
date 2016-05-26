package by.bsu.up;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.*;
import org.json.simple.parser.ParseException;
import storage.*;
import utils.*;

import java.io.IOException;
import java.util.List;

/**
 * Created by Dasha on 22.05.2016.
 */

@WebServlet(value = "/chat")
public class ChatServlet extends HttpServlet {
    private MessageStorage messageStorage = new InMemoryMessageStorage();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String token = req.getParameter("token");
        System.out.println("hi");
        System.out.println(token);
        int index = MessageHelper.parseToken(token); // из токена вида TN11EN уберем буквы, а число декодируем
        Portion portion = new Portion(index);
        List<Message> messages = messageStorage.getPortion(portion); // сообщения от названного токена до последнего
        String responseBody = MessageHelper.buildServerResponseBody(messages, messageStorage.size());
        resp.getWriter().println(responseBody);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            Message message = MessageHelper.getClientMessage(req.getInputStream());
            messageStorage.addMessage(message);
        } catch (ParseException e) {}
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            Message message = MessageHelper.getClientMessage(req.getInputStream());
            messageStorage.addMessage(message);
        } catch (ParseException e) {}
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            Message message = MessageHelper.getClientMessage(req.getInputStream());
            messageStorage.addMessage(message);
        } catch (ParseException e) {}
    }


}
