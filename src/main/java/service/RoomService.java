package service;

import java.util.List;
import java.util.UUID;

import dao.RoomDAO;
import dto.RoomRequest;
import dto.RoomResponse;
import dto.SuccessResponse;
import dto.PasswordRequest;
import lombok.RequiredArgsConstructor;
import model.RoomVO;

@RequiredArgsConstructor
public class RoomService {

    private final RoomDAO roomDAO;

    public List<RoomResponse> getAllRooms() {
        List<RoomVO> roomList = roomDAO.getAllRooms();
        return roomList.stream()
            .map(RoomResponse::from)
            .toList();
    }

    public RoomResponse getRoomById(String roomId) {
        RoomVO roomVO = roomDAO.selectRoomById(roomId);
        return RoomResponse.from(roomVO);
    }

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

    public SuccessResponse checkPassword(String roomId, PasswordRequest passwordReq) {
        RoomVO roomVO = roomDAO.selectRoomById(roomId);
        boolean isMatch = roomVO.getPassword().equals(passwordReq.getPassword());
        return SuccessResponse.of(isMatch);
    }

    public SuccessResponse deleteRoom(String roomId) {
        roomDAO.deleteRoom(roomId);
        return SuccessResponse.of(true);
    }
}
