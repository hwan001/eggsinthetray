package util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class JsonResponseUtil {
    
    private static final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .create();

    public static void writeSuccessResponse(HttpServletResponse response, Object data, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_OK);
        writeJsonResponse(response, data, message);
    }

    public static void writeErrorResponse(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        writeJsonResponse(response, null, message);
    }

    private static void writeJsonResponse(HttpServletResponse response, Object data, String message) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        PrintWriter out = response.getWriter();
        String jsonResponse = gson.toJson(data);
        out.write(jsonResponse);
        out.flush();
    }
} 