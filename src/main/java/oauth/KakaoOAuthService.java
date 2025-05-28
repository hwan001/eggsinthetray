package oauth;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
//인가코드요청 
@WebServlet("/kakaooauth")
public class KakaoOAuthService extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public KakaoOAuthService() {
        super();
        // TODO Auto-generated constructor stub
    }

	

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String clientId = getServletContext().getInitParameter("kakao.client_id");
		
		String kakaoUrl = "https://kauth.kakao.com/oauth/authorize"
				+ "?response_type=code"
				+ "&client_id=" + clientId
				+ "&redirect_uri=http://localhost:8090/eggsinthetray/kakaocallback";
		response.sendRedirect(kakaoUrl);//System.out.println(clientId);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}

