package service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import dao.RoomDAO;
import dto.RoomDTO;

import lombok.RequiredArgsConstructor;
import model.RoomVO;

@RequiredArgsConstructor
public class RoomService {

    private final RoomDAO roomDAO;

    public boolean createRoom(RoomDTO roomDTO) {
        RoomVO roomVO = RoomVO.builder()
            .roomId(roomDTO.getRoomId())
            .title(roomDTO.getTitle())
            .isPublic(roomDTO.getIsPublic())
            .password(roomDTO.getPassword())
            .createdAt(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
            .build();
        return roomDAO.insertRoom(roomVO);
    }
}
