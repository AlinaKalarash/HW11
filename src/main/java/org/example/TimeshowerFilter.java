package org.example;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebFilter(value = "/time")
public class TimeshowerFilter extends HttpFilter {

    @Override
    protected void doFilter(HttpServletRequest request,
                            HttpServletResponse response,
                            FilterChain chain) throws IOException, ServletException {

        String givenTimezone = request.getParameter("timezone");

        if (givenTimezone == null || givenTimezone.length() < 6) {
            chain.doFilter(request, response);

        } else {
            response.setStatus(400);

            response.getWriter().write("<h1>Invalid timezone</h1>");
            response.getWriter().close();
        }
    }
}
