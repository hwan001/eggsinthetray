package dto;

import lombok.Builder;
import lombok.Getter;
import model.RoomVO;

@Getter
@Builder
public class RoomResponse {
    private String roomId;
    private String title;
    private String isPublic;

    public static RoomResponse from(RoomVO roomVO) {
        return RoomResponse.builder()
            .roomId(roomVO.getRoomId())
            .title(roomVO.getTitle())
            .isPublic(roomVO.getIsPublic())
            .build();
    }
}
