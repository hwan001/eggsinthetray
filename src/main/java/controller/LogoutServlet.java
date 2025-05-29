package controller;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {

    private static final String KAKAO_LOGOUT_URL = "https://kapi.kakao.com/v1/user/logout";

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        System.out.println("[POST] LogoutServlet 호출");

        // 세션에서 액세스 토큰 가져오기
        String accessToken = (String) request.getSession().getAttribute("accessToken");
        System.out.println("LogoutServlet 액세스토큰:" + accessToken);

        if (accessToken != null) {
            // 카카오 로그아웃 요청
            URL url = URI.create(KAKAO_LOGOUT_URL).toURL();
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization", "Bearer " + accessToken);
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
            conn.setDoOutput(true);

            int responseCode = conn.getResponseCode();
            if (responseCode == 200) {
                // 로그아웃 성공
                System.out.println("로그아웃 성공");
                request.getSession().invalidate(); // 세션 무효화
                response.sendRedirect("login.jsp"); // 초기 화면 등으로 리디렉트
            } else {
                // 실패 처리
                System.out.println("로그아웃 실패");
            }
        } else{
            response.sendRedirect("login.jsp");

        }
    }
}

