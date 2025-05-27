package oauth;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
//인가코드요청 
@WebServlet("/kakaooauth")
public class KakaoOAuthService extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public KakaoOAuthService() {
        super();
        // TODO Auto-generated constructor stub
    }

	String clientId = getServletContext().getInitParameter("kakao.client_id");
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String kakaoUrl = "https://kauth.kakao.com/oauth/authorize"
				+ "?response_type=code"
				+ "&client_id=" + clientId;
				+ "&redirect_uri=http://localhost:8090/eggsinthetray/kakaocallback";
		response.sendRedirect(kakaoUrl);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}

