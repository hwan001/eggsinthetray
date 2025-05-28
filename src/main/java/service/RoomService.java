package service;

import java.util.UUID;

import dao.RoomDAO;
import dto.RoomRequest;
import dto.RoomResponse;
import dto.PasswordMatchResponse;
import dto.PasswordRequest;
import lombok.RequiredArgsConstructor;
import model.RoomVO;

@RequiredArgsConstructor
public class RoomService {

    private final RoomDAO roomDAO;

    public RoomResponse createRoom(RoomRequest roomReq) {
        RoomVO roomVO = RoomVO.builder()
            .roomId(UUID.randomUUID().toString().substring(0, 10))
            .title(roomReq.getTitle())
            .isPublic(roomReq.getIsPublic())
            .password(roomReq.getPassword())
            .build();
        roomDAO.insertRoom(roomVO);
        return RoomResponse.from(roomDAO.selectRoomById(roomVO.getRoomId()));
    }

    public PasswordMatchResponse checkPassword(String roomId, PasswordRequest passwordReq) {
        RoomVO roomVO = roomDAO.selectRoomById(roomId);
        boolean isMatch = roomVO.getPassword().equals(passwordReq.getPassword());
        return PasswordMatchResponse.of(isMatch);
    }
}
