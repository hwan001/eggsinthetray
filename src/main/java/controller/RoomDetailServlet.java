package controller;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.RoomDAO;
import dto.PasswordRequest;
import dto.SuccessResponse;
import lombok.extern.slf4j.Slf4j;
import service.RoomService;
import util.JsonUtil;

import java.io.IOException;

@Slf4j
@WebServlet("/api/rooms/*")
public class RoomDetailServlet extends HttpServlet {

    private final RoomService roomService = new RoomService(new RoomDAO());

    // 방 비밀번호 입력
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        log.info("[POST] RoomDetailServlet 호출 - 방 비밀번호 입력");
        String roomId = request.getPathInfo().replace("/password", "").substring(1);
        PasswordRequest passwordReq = JsonUtil.readRequest(request, PasswordRequest.class);
        SuccessResponse passwordMatchRes = roomService.checkPassword(roomId, passwordReq);
        JsonUtil.writeResponse(response, passwordMatchRes);
    }

    // 방 삭제
    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        log.info("[DELETE] RoomDetailServlet 호출 - 방 삭제");
        String roomId = request.getPathInfo().substring(1);
        SuccessResponse successResponse = roomService.deleteRoom(roomId);
        JsonUtil.writeResponse(response, successResponse);
    }
}
