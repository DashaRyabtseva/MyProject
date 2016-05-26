package by.bsu.up.Filters;


import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;

/**
 * Created by Dasha on 23.05.2016.
 */

@WebFilter(value = "/chat")

public class AuthenticationFilter implements Filter {

    public void init(FilterConfig filterConfig) throws ServletException {}

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        /*boolean valid = checkRequest(ServletRequest);

        if (valid) {

            filterChain.doFilter(ServletRequest, ServletResponse);

        } else {

            ServletResponse.getOutputStream().println("403, Forbidden");

        }*/
    }

    public void destroy() {}
}
