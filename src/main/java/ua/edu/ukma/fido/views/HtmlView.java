package ua.edu.ukma.fido.views;

import com.sun.net.httpserver.HttpExchange;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import ua.edu.ukma.fido.dto.Response;

import java.io.IOException;
import java.io.OutputStream;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

public class HtmlView implements View {
    @Override
    public void view(Response response) {
        String responseBody = "<b>error</b>";
        Integer statusCode = 500;

        HttpExchange httpExchange = response.getHttpExchange();
        OutputStream outputStream = httpExchange.getResponseBody();

        try {
            Object data = response.getData();

            Map root = new HashMap();
            // boolshit!!!
            root.put("data", data == null ? new Object() : data);

            String templateName = response.getTemplate();

            Configuration configuration = getConfiguration();
            Template template = configuration.getTemplate(templateName + ".flth");

            StringWriter stringWriter = new StringWriter();
            template.process(root, stringWriter);

            responseBody = stringWriter.toString();
            statusCode = response.getStatusCode();
        } catch (IOException | TemplateException e) {
            e.printStackTrace();
        }
        try {
            httpExchange.sendResponseHeaders(statusCode, responseBody.length());

            outputStream.write(responseBody.getBytes());
            outputStream.close();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    private Configuration getConfiguration() {
        Configuration configuration = new Configuration(Configuration.VERSION_2_3_30);

        configuration.setClassForTemplateLoading(this.getClass(), "/templates/");

        configuration.setDefaultEncoding("UTF-8");

        return configuration;
    }
}
