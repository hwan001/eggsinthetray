package model;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class RoomVO {
    private String roomId;
    private String title;
    private String isPublic;
    private String password;
    private String createdAt;
} 