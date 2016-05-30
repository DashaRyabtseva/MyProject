package by.bsu.up.Filters;

import by.bsu.up.help.UsersInitialization;
import by.bsu.up.Item;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Dasha on 23.05.2016.
 */

@WebFilter(urlPatterns = { "/chat", "/pages/ff.html" })

public class AuthenticationFilter implements Filter {

    ArrayList<Item> users;

    public void init(FilterConfig filterConfig) throws ServletException {}

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        users = new ArrayList<Item>();
        String path = servletRequest.getServletContext().getRealPath("/");
        String needPath = path.substring(0, path.length() - 18) + "files/users.txt";
        UsersInitialization.loadKeys(needPath,users);
        String tempId = (String)((HttpServletRequest)servletRequest).getSession().getAttribute("id");
        if (isHere(tempId))
            filterChain.doFilter(servletRequest, servletResponse);
        else if(servletResponse instanceof HttpServletResponse)
            ((HttpServletResponse) servletResponse).sendRedirect("/login.jsp");
    }

    public void destroy() {}

    public boolean isHere (String id) {
        for (int i = 0; i < users.size(); ++i) {
            Item temp = users.get(i);
            if (temp.getId().equals(id))
                return true;
        }
        return false;
    }
}
