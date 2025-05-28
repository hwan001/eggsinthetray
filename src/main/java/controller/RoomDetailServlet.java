package controller;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.RoomDAO;
import dto.PasswordRequest;
import dto.SuccessResponse;
import service.RoomService;
import util.JsonUtil;

import java.io.IOException;

@WebServlet("/api/rooms/*")
public class RoomDetailServlet extends HttpServlet {

    private final RoomService roomService = new RoomService(new RoomDAO());

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        System.out.println("RoomPasswordServlet 호출");
        String roomId = request.getPathInfo().replace("/password", "").substring(1);
        PasswordRequest passwordReq = JsonUtil.readRequest(request, PasswordRequest.class);
        SuccessResponse passwordMatchRes = roomService.checkPassword(roomId, passwordReq);
        JsonUtil.writeResponse(response, passwordMatchRes);
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        System.out.println("RoomDeleteServlet 호출");
        String roomId = request.getPathInfo().substring(1);
        SuccessResponse successResponse = roomService.deleteRoom(roomId);
        JsonUtil.writeResponse(response, successResponse);
    }
}
