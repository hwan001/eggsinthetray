package dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RoomRequest {
    private String roomId;
    private String title;
    private String isPublic;
    private String password;
}
