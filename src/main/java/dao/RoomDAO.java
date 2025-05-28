package dao;

import java.util.ArrayList;
import java.util.List;

import model.RoomVO;
import util.DatabaseUtil;

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

    public List<RoomVO> selectAllRoomsByTitle(String title) {
        String selectSql = "SELECT room_id, title, is_public, password, created_at FROM Room WHERE title = ?";

        return DatabaseUtil.executeQuery(selectSql,
            pstmt -> pstmt.setString(1, title),
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

    public List<RoomVO> selectAllRooms() {
        String selectSql = "SELECT room_id, title, is_public, password, created_at FROM Room";

        return DatabaseUtil.executeQuery(selectSql, rs -> {
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

    public void deleteRoom(String roomId) {
        String deleteSql = "DELETE FROM Room WHERE room_id = ?";

        DatabaseUtil.executeUpdate(deleteSql, pstmt -> {
            pstmt.setString(1, roomId);
            pstmt.executeUpdate();
            return null;
        });
    }
}
