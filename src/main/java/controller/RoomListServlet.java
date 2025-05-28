package controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.RoomDAO;
import model.RoomVO;
import util.JsonResponseUtil;

@WebServlet("/roomList")
public class RoomListServlet extends HttpServlet {
    private final RoomDAO roomDAO = new RoomDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        System.out.println("RoomListServlet doGet 호출");
        List<RoomVO> roomList = roomDAO.getAllRooms();
        JsonResponseUtil.writeSuccessResponse(response, roomList, "방 목록 조회에 성공했습니다.");
    }
}