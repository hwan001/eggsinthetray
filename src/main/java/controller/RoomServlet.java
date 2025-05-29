package controller;

import service.RoomService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import dao.RoomDAO;
import dto.RoomRequest;
import dto.RoomResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.List;

import util.JsonUtil;

@Slf4j
@WebServlet("/api/rooms")
public class RoomServlet extends HttpServlet {

    private final RoomService roomService = new RoomService(new RoomDAO());

    // 방 목록 조회
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        log.info("[GET] RoomServlet 호출 - 방 목록 조회");
        List<RoomResponse> roomResList = roomService.getAllRooms();
        JsonUtil.writeResponse(response, roomResList);
    }

    // 방 생성
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        log.info("[POST] RoomServlet 호출 - 방 생성");
        RoomRequest roomReq = JsonUtil.readRequest(request, RoomRequest.class);
        RoomResponse roomRes = roomService.createRoom(roomReq);
        JsonUtil.writeResponse(response, roomRes);
    }
}
