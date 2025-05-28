package controller;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {
    public LogoutServlet(){
        super();
    }
    private static final String KAKAO_LOGOUT_URL = "https://kapi.kakao.com/v1/user/logout";
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // 세션에서 액세스 토큰 가져오기
        String accessToken = (String) request.getSession().getAttribute("accessToken");

        if (accessToken != null) {
            // 카카오 로그아웃 요청
            URL url = new URL(KAKAO_LOGOUT_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization", "Bearer " + accessToken);
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
            conn.setDoOutput(true);

            int responseCode = conn.getResponseCode();
            if (responseCode == 200) {
                // 로그아웃 성공
                request.getSession().invalidate(); // 세션 무효화
                response.sendRedirect("login.jsp"); // 초기 화면 등으로 리디렉트
            } else {
                // 실패 처리
                response.getWriter().write("카카오 로그아웃 실패: " + responseCode);
            }
        } else {
            response.sendRedirect("login.jsp");
        }
    }
}

