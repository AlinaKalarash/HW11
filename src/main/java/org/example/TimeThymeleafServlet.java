package org.example;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templateresolver.WebApplicationTemplateResolver;
import org.thymeleaf.web.servlet.JakartaServletWebApplication;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.*;

@WebServlet("/timezone")
public class TimeThymeleafServlet extends HttpServlet {
    private TemplateEngine engine;

    @Override
    public void init() throws ServletException {
        engine = new TemplateEngine();

        JakartaServletWebApplication webApplication = JakartaServletWebApplication.buildApplication(this.getServletContext());
        WebApplicationTemplateResolver resolver = new WebApplicationTemplateResolver(webApplication);

        resolver.setPrefix("/WEB-INF/temp/");
        resolver.setSuffix(".html");
        resolver.setTemplateMode("HTML5");
        resolver.setOrder(engine.getTemplateResolvers().size());
        resolver.setCacheable(false);
        engine.addTemplateResolver(resolver);

    }


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");

        Map<String, String> params = new LinkedHashMap<>();

        params.put("Your timezone: ", timezone(request, response));

        Context context = new Context(
                request.getLocale(),
                Map.of("queryParams", params)
        );

        engine.process("index", context, response.getWriter());
        response.getWriter().close();
    }

    private String timezone(HttpServletRequest request, HttpServletResponse response) {
        String timezone = request.getParameter("timezone");
        OffsetDateTime now;
        if (timezone == null || timezone.length() == 3) {
            if(getCookies(request) != null) {
                return getCookies(request);
            }
            now = OffsetDateTime.now(ZoneOffset.ofHours(0));

        } else {
            int time = Integer.parseInt(timezone.replace("UTC", "").replace(" ", ""));
            now = OffsetDateTime.now(ZoneOffset.ofHours(time));
            response.addCookie(new Cookie("lastTimezone", now.toString()));
        }
        return now.toString();
    }

    private String getCookies(HttpServletRequest req) {
        String cookies = req.getHeader("Cookie");

        if (cookies == null) {
            return null;
        }

        Map<String, String> result = new HashMap<>();

        String[] separateCookies = cookies.split(";");
        for (String pair : separateCookies) {
            String[] keyValue = pair.split("=");

            result.put(keyValue[0], keyValue[1]);
        }

        return result.get("lastTimezone");
    }


}
