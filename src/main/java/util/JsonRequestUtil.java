package util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;

public class JsonRequestUtil {
    
    private static final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .create();
    
    public static <T> T readRequest(HttpServletRequest request, HttpServletResponse response, Class<T> classType) {
        try {
            StringBuilder sb = new StringBuilder();
            String line;
            
            try (BufferedReader reader = request.getReader()) {
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
            }
            
            return gson.fromJson(sb.toString(), classType);
        } catch (JsonSyntaxException e) {
            try {
                JsonResponseUtil.writeErrorResponse(response, "잘못된 JSON 형식입니다.");
            } catch (IOException ignored) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }
            return null;
        } catch (IOException e) {
            try {
                JsonResponseUtil.writeErrorResponse(response, "요청을 처리하는 중 오류가 발생했습니다.");
            } catch (IOException ignored) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
            return null;
        } catch (Exception e) {
            try {
                JsonResponseUtil.writeErrorResponse(response, "알 수 없는 오류가 발생했습니다.");
            } catch (IOException ignored) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
            return null;
        }
    }
} 