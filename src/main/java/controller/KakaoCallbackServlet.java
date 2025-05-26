package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

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
	    String body = "grant_type=authorization_code"
	                + "&client_id=9842f3b5d39972114e3df176dbd060ad"
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
	    
	    //session에 저장해서 ProfileServlet에 쓰기
	    HttpSession session = request.getSession();
	    session.setAttribute("accessToken", accessToken);
	    
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}

