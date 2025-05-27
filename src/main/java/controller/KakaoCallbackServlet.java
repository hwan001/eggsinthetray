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

import org.json.JSONObject;;

@WebServlet("/kakaocallback")
public class KakaoCallbackServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    public KakaoCallbackServlet() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String code = request.getParameter("code");
		String tokenUrl = "https://kauth.kakao.com/oauth/token";
		
		URL url = new URL(tokenUrl);
	    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	    
	    
	    conn.setRequestMethod("POST");
	    conn.setDoOutput(true);
	    conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");

	    // body 파라미터 만들기
	    String clientId = getServletContext().getInitParameter("kakao.client_id");
	    String body = "grant_type=authorization_code"
	                + "&client_id=" + clientId
	                + "&redirect_uri=http://localhost:8090/eggsinthetray/kakaocallback"
	                + "&code=" + code;
	    
	    OutputStream os = conn.getOutputStream();
	    os.write(body.getBytes());
	    os.flush();
	    os.close();
	    
	    // 응답 읽기
	    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
	    StringBuilder sb = new StringBuilder();
	    String line;
	    while ((line = br.readLine()) != null) {
	        sb.append(line);
	    }
	    br.close();
	    //토큰 결과 전체
	    System.out.println("Token Response: " + sb.toString());
	    
	    //access 토큰만 뽑기 
	    JSONObject json = new JSONObject(sb.toString());
	    String accessToken = json.getString("access_token");
	    
	    System.out.println("access token: " + accessToken);
	    
	    //accessToken session에 저장해서ProfileServlet에 쓰기
	    HttpSession session = request.getSession();
	    session.setAttribute("accessToken", accessToken);
	    response.sendRedirect("profileservlet");
	    
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}

