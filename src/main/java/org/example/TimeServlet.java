package org.example;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.*;

@WebServlet(value = "/time")
public class TimeServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String timezone = request.getParameter("timezone");
        OffsetDateTime now;
        if (timezone == null || timezone.length() == 3) {
            now = OffsetDateTime.now(ZoneOffset.ofHours(0));
        } else {
            int time = Integer.parseInt(timezone.replace("UTC", "").replace(" ", ""));
            now = OffsetDateTime.now(ZoneOffset.ofHours(time));
        }

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.print("<html><body>");
        out.print("<h3>Hello World, I'm a new Servlet!</h3>");
        out.print("<h1>current time is: " + now + "</h1>");
        out.print("</body></html>");
        out.close();
    }
}