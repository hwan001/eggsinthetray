package websocket;

import lombok.Data;

@Data
public class Move {
    int x, y, color;
    String senderId;

    Move(int x, int y, int color, String senderId) {
        this.x = x;
        this.y = y;
        this.color = color;
        this.senderId = senderId;
    }
}