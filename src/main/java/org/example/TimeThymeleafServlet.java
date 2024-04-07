package org.example;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
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
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

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

//        Map<String, String[]> parameterMap = request.getParameterMap();

        Map<String, String> params = new LinkedHashMap<>();
//        for (Map.Entry<String, String[]> keyValue : parameterMap.entrySet()) {
//            params.put(keyValue.getKey(), keyValue.getValue()[0]);
//        }

        String timezone = request.getParameter("timezone");
        OffsetDateTime now;
        if (timezone == null || timezone.length() == 3) {
        now = OffsetDateTime.now(ZoneOffset.ofHours(0));
        } else {
        int time = Integer.parseInt(timezone.replace("UTC", "").replace(" ", ""));
        now = OffsetDateTime.now(ZoneOffset.ofHours(time));
        }

        params.put("Your timezone: ", now.toString());

        Context context = new Context(
                request.getLocale(),
                Map.of("queryParams", params)
        );

        engine.process("index", context, response.getWriter());
        response.getWriter().close();
    }

}
