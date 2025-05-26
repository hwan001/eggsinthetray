package dao;

import java.sql.*;
import model.RoomVO;
import util.DatabaseUtil;

public class RoomDAO {

    public boolean insertRoom(RoomVO roomVO) {
        String sql = "INSERT INTO Room (room_id, title, is_public, password, created_at) VALUES (?, ?, ?, ?, ?)";

        try (Connection con = DatabaseUtil.getConnection();
            PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setString(1, roomVO.getRoomId());
            pstmt.setString(2, roomVO.getTitle());
            pstmt.setString(3, roomVO.getIsPublic());
            pstmt.setString(4, roomVO.getPassword());
            pstmt.setString(5, roomVO.getCreatedAt());
            pstmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
