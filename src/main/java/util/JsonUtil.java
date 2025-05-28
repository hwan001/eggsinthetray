package util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

public class JsonUtil {
    
    private static final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .disableHtmlEscaping()
            .create();
    
    public static <T> T readRequest(HttpServletRequest request, Class<T> classType) throws IOException {
        request.setCharacterEncoding("UTF-8");
        StringBuilder sb = new StringBuilder();
        String line;
        
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(request.getInputStream(), StandardCharsets.UTF_8))) {
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        }
        
        try {
            return gson.fromJson(sb.toString(), classType);
        } catch (JsonSyntaxException e) {
            throw new IOException("잘못된 JSON 형식입니다.", e);
        }
    }

    public static void writeResponse(HttpServletResponse response, Object data) throws IOException {
        response.setStatus(HttpServletResponse.SC_OK);
        writeJsonResponse(response, data);
    }

    private static void writeJsonResponse(HttpServletResponse response, Object data) throws IOException {    
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        PrintWriter out = response.getWriter();
        String jsonResponse = gson.toJson(data);
        out.write(jsonResponse);
        out.flush();
    }
} 