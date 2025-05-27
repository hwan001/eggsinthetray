package controller;

import service.RoomService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import dao.RoomDAO;
import dto.RoomRequest;
import dto.RoomResponse;

import java.io.IOException;
import java.util.UUID;
import util.JsonResponseUtil;

@WebServlet("/api/rooms")
public class RoomCreateServlet extends HttpServlet {

    private final RoomService roomService = new RoomService(new RoomDAO());

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
                
        RoomRequest roomReq = RoomRequest.builder()
            .roomId(UUID.randomUUID().toString().substring(0, 8))
            .title(request.getParameter("title"))
            .isPublic(request.getParameter("isPublic"))
            .password(request.getParameter("password"))
            .build();

        RoomResponse roomRes = roomService.createRoom(roomReq);

        if (roomRes != null) {
            JsonResponseUtil.writeSuccessResponse(response, roomRes, "방 생성에 성공했습니다.");
        } else {
            JsonResponseUtil.writeErrorResponse(response, "방 생성에 실패했습니다.");
        }
    }
}
