package controller;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
//인가코드요청 
@WebServlet("/kakaooauth")
public class KakaoOAuthServlet extends HttpServlet {

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String clientId = getServletContext().getInitParameter("kakao.client_id");
		String redirectCallback = getServletContext().getInitParameter("kakao.redirect_uri_callback");
		
		String kakaoUrl = "https://kauth.kakao.com/oauth/authorize"
				+ "?response_type=code"
				+ "&client_id=" + clientId
				+ "&redirect_uri=" + redirectCallback;
		response.sendRedirect(kakaoUrl);
	}

}

