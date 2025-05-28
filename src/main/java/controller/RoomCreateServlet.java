package controller;

import service.RoomService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import dao.RoomDAO;
import dto.RoomRequest;
import dto.RoomResponse;

import java.io.IOException;
import util.JsonResponseUtil;
import util.JsonRequestUtil;

@WebServlet("/api/rooms")
public class RoomCreateServlet extends HttpServlet {

    private final RoomService roomService = new RoomService(new RoomDAO());

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        System.out.println("RoomCreateServlet doPost 호출");
        
        RoomRequest roomReq = JsonRequestUtil.readRequest(request, response, RoomRequest.class);
        if (roomReq == null) {
            return;
        }

        System.out.println("title: " + roomReq.getTitle());
        System.out.println("isPublic: " + roomReq.getIsPublic());
        System.out.println("password: " + roomReq.getPassword());

        roomReq = RoomRequest.builder()
            .title(roomReq.getTitle())
            .isPublic(roomReq.getIsPublic())
            .password(roomReq.getPassword())
            .build();

        RoomResponse roomRes = roomService.createRoom(roomReq);

        if (roomRes != null) {
            JsonResponseUtil.writeSuccessResponse(response, roomRes, "방 생성에 성공했습니다.");
            System.out.println("방 생성에 성공했습니다.");
        } else {
            JsonResponseUtil.writeErrorResponse(response, "방 생성에 실패했습니다.");
            System.out.println("방 생성에 실패했습니다.");
        }
    }
}
