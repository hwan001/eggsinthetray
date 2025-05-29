package controller;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import service.MemberService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONObject;

import dto.MemberDTO;

@WebServlet("/profileservlet")
public class ProfileServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       

    public ProfileServlet() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(false);
	    if(session == null || session.getAttribute("accessToken") == null) {
	        // 로그인 안 된 상태, 예외 처리 또는 로그인 페이지 이동
	        response.sendRedirect("login.jsp");
	        return;
	    }

	    String accessToken = (String) session.getAttribute("accessToken");
		String reqURL = "https://kapi.kakao.com/v2/user/me";
		
	    URL url = new URL(reqURL);
	    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	    conn.setRequestMethod("GET");
	    conn.setRequestProperty("Authorization", "Bearer " + accessToken);
	    conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");

	    // 응답 받기
	    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
	    StringBuilder sb = new StringBuilder();
	    String line;
	    while ((line = br.readLine()) != null) {
	        sb.append(line);
	    }
	    br.close();
	    
	 // JSON 파싱
	    String jsonResponse = sb.toString();
	    JSONObject jsonObject = new JSONObject(jsonResponse);
	    System.out.println("응답 JSON: " + jsonResponse);

	    long id = jsonObject.getLong("id");
	 // 중첩된 구조 접근
	    JSONObject kakaoAccount = jsonObject.getJSONObject("kakao_account");
	    JSONObject profile = kakaoAccount.getJSONObject("profile");

	    // 필요한 정보 꺼내기
	    String nickname = profile.getString("nickname");
	    String profileImage = profile.getString("profile_image_url");

	    
	    //받아온 정보를 저장해서 MemberDTO 저장 -> MemberService에 넘김 
	   MemberDTO member = new MemberDTO();
	   member.setMemberId(id + "");
	   member.setNickName(nickname);
	   member.setImageUrl(profileImage);
	   member.setMemberRole("user");
	   
	   // DB 처리
       MemberService service = new MemberService();
       MemberDTO loginUser = service.loginOrJoin(member);

       if (loginUser != null) {
           session.setAttribute("loginUser", loginUser);
           session.setAttribute("memberId", loginUser.getMemberId());
		   System.out.println("profileServer에 user존재 "+session.getAttribute("loginUser"));
           response.sendRedirect("main.jsp");
       } else {
           response.sendRedirect("login.jsp");
       }
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
