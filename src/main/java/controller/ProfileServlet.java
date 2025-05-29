package controller;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import service.MemberService;
import util.JsonUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URI;

import dto.KakaoResponse;
import dto.MemberRequest;
import dto.MemberResponse;
import lombok.extern.slf4j.Slf4j;
import dao.MemberDAO;

@Slf4j
@WebServlet("/profileservlet")
public class ProfileServlet extends HttpServlet {

	private final MemberService memberService = new MemberService(new MemberDAO());

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		log.info("[GET] ProfileServlet 호출");

		HttpSession session = request.getSession(false);
	    if(session == null || session.getAttribute("accessToken") == null) {
	        // 로그인 안 된 상태, 예외 처리 또는 로그인 페이지 이동
	        response.sendRedirect("login.jsp");
	        return;
	    }

	    String accessToken = (String) session.getAttribute("accessToken");
		String reqURL = "https://kapi.kakao.com/v2/user/me";
		
	    URL url = URI.create(reqURL).toURL();
	    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	    conn.setRequestMethod("GET");
	    conn.setRequestProperty("Authorization", "Bearer " + accessToken);
	    conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");

	    // 응답을 문자열로 읽기
	    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
	    String jsonResponse = br.lines().reduce("", String::concat);
	    br.close();

		log.info("카카오 JSON 응답: " + jsonResponse);

	    // JsonUtil을 사용하여 응답 파싱
	    KakaoResponse kakaoResponse = JsonUtil.fromJson(jsonResponse, KakaoResponse.class);
		MemberRequest memberReq = MemberRequest.from(kakaoResponse);
		
	    // DB 처리
	    MemberResponse loginUser = memberService.loginOrJoin(memberReq);

		if (loginUser != null) {
			session.setAttribute("loginUser", loginUser);
			session.setAttribute("memberId", loginUser.getMemberId());
			log.info("loginUser: " + loginUser.getMemberId());
			response.sendRedirect("main.jsp");
		} else {
			response.sendRedirect("login.jsp");
		}
	}
}
