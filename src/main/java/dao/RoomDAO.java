package dao;

import model.RoomVO;
import util.DatabaseUtil;
import java.util.List;
import java.util.ArrayList;

public class RoomDAO {

    public void insertRoom(RoomVO roomVO) {
        String insertSql = "INSERT INTO Room (room_id, title, is_public, password) VALUES (?, ?, ?, ?)";

        DatabaseUtil.executeUpdate(insertSql, pstmt -> {
            pstmt.setString(1, roomVO.getRoomId());
            pstmt.setString(2, roomVO.getTitle());
            pstmt.setString(3, roomVO.getIsPublic());
            pstmt.setString(4, roomVO.getPassword());
            pstmt.executeUpdate();
            return null;
        });
    }
    
    public RoomVO selectRoomById(String roomId) {
        String selectSql = "SELECT room_id, title, is_public, password, created_at FROM Room WHERE room_id = ?";
        
        return DatabaseUtil.executeQuery(selectSql, 
            pstmt -> pstmt.setString(1, roomId),
            rs -> {
                if (rs.next()) {
                    return RoomVO.builder()
                        .roomId(rs.getString("room_id"))
                        .title(rs.getString("title"))
                        .isPublic(rs.getString("is_public"))
                        .password(rs.getString("password"))
                        .createdAt(rs.getString("created_at"))
                        .build();
                }
                return null;
            });
    }

    public void deleteRoom(String roomId) {
        String deleteSql = "DELETE FROM Room WHERE room_id = ?";

        DatabaseUtil.executeUpdate(deleteSql, pstmt -> {
            pstmt.setString(1, roomId);
            pstmt.executeUpdate();
            return null;
        });
    }

    public List<RoomVO> getAllRooms() {
        String selectSql = "SELECT room_id, title, is_public, password, created_at FROM Room ORDER BY created_at DESC";
        
        return DatabaseUtil.executeQuery(selectSql,
            pstmt -> {},
            rs -> {
                List<RoomVO> rooms = new ArrayList<>();
                while (rs.next()) {
                    rooms.add(RoomVO.builder()
                        .roomId(rs.getString("room_id"))
                        .title(rs.getString("title"))
                        .isPublic(rs.getString("is_public"))
                        .password(rs.getString("password"))
                        .createdAt(rs.getString("created_at"))
                        .build());
                }
                return rooms;
            });
    }
}
