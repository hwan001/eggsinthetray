package controller;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONObject;
import dao.MemberDAO;
import dto.MemberDTO;

@WebServlet("/api/members/*")
public class MainProfileServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 1. URL에서 memberId 추출
        String pathInfo = request.getPathInfo(); // 예: "/123"
        
        if (pathInfo == null || pathInfo.equals("/")) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "memberId required");
            return;
        }
        String memberId = pathInfo.substring(1); // "123"

        // 2. DB에서 memberId로 회원 정보 조회
        MemberDAO dao = new MemberDAO();
        MemberDTO member = dao.findById(memberId);

        if (member == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Member not found");
            return;
        }

        // 3. JSON 응답 생성
        JSONObject json = new JSONObject();
        json.put("memberId", member.getMemberId());
        json.put("nickname", member.getNickName());
        json.put("profileImage", member.getImageUrl());
        json.put("level", member.getMemberLevel());      
        json.put("playCount", member.getPlayCnt());     
        json.put("winCount", member.getWinCnt());       

        // 승률 계산 로직이 DTO에 없으니 여기서 직접 계산:
        double winRate = 0;
        if (member.getPlayCnt() > 0) {
            winRate = (double) member.getWinCnt() / member.getPlayCnt() * 100;
        }
        json.put("winRate", winRate);


        // 4. JSON으로 응답
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(json.toString());
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
