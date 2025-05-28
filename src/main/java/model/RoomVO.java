package model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RoomVO {
    private String roomId;
    private String title;
    private String isPublic;
    private String password;
    private String createdAt;
}
