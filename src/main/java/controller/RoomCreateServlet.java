package controller;

import service.RoomService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import dto.RoomDTO;
import dao.RoomDAO;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import util.JsonResponseUtil;

@WebServlet("/room")
public class RoomCreateServlet extends HttpServlet {

    private final RoomService roomService = new RoomService(new RoomDAO());

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        RoomDTO roomDTO = RoomDTO.builder()
            .roomId(UUID.randomUUID().toString().substring(0, 8))
            .title(request.getParameter("title"))
            .isPublic(request.getParameter("isPublic"))
            .password(request.getParameter("password"))
            .createdAt(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
            .build();

        boolean success = roomService.createRoom(roomDTO);

        if (success) {
            JsonResponseUtil.writeSuccessResponse(response, roomDTO, "방 생성에 성공했습니다.");
        } else {
            JsonResponseUtil.writeErrorResponse(response, "방 생성에 실패했습니다.");
        }
    }
}
