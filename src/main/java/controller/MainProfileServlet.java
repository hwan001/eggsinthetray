package controller;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import dao.MemberDAO;
import dto.MemberResponse;
import dto.ResultRequest;
import lombok.extern.slf4j.Slf4j;
import service.MemberService;
import util.JsonUtil;

@Slf4j
@WebServlet("/api/members/*")
public class MainProfileServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private final MemberService memberService = new MemberService(new MemberDAO());

    // 회원 정보 조회
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.info("[GET] MainProfileServlet 호출 - 회원 정보 조회");
        String memberId = request.getPathInfo().substring(1);
        MemberResponse member = memberService.getMemberById(memberId);
        JsonUtil.writeResponse(response, member);
    }

    // 전적 수정
    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.info("[PUT] MainProfiileServlet 호출 - 전적 수정");
        String memberId = request.getPathInfo().replace("/result", "").substring(1);
        ResultRequest resultReq = JsonUtil.readRequest(request, ResultRequest.class);
        MemberResponse memberRes = memberService.updateMemberResult(memberId, resultReq);
        JsonUtil.writeResponse(response, memberRes);
    }
}
