package controller;

import java.io.IOException;

import util.JsonUtil;

import dao.MemberDAO;
import dto.MemberResponse;
import dto.ResultRequest;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import service.MemberService;

@Slf4j
@WebServlet("/api/members/*")
public class MainProfiileServlet extends HttpServlet {

    private final MemberService memberService = new MemberService(new MemberDAO());

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
