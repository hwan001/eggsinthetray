package controller;

import service.RoomService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import dao.RoomDAO;
import dto.RoomRequest;
import dto.RoomResponse;

import java.io.IOException;
import util.JsonUtil;

@WebServlet("/api/rooms")
public class RoomCreateServlet extends HttpServlet {

    private final RoomService roomService = new RoomService(new RoomDAO());

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        System.out.println("RoomCreateServlet 호출");
        RoomRequest roomReq = JsonUtil.readRequest(request, RoomRequest.class);
        RoomResponse roomRes = roomService.createRoom(roomReq);
        JsonUtil.writeResponse(response, roomRes);
    }
}
