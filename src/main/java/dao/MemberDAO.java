package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import dto.MemberDTO;

public class MemberDAO {
	private Connection conn;

    public MemberDAO() {
        try {
            Class.forName("oracle.jdbc.OracleDriver");
            conn = DriverManager.getConnection(
                "jdbc:oracle:thin:@db.avgmax.in:1521:xe", 
                "EGGSINTHETRAY", 
                "EGGSINTHETRAY1234"
            );
        } catch (Exception e) {
            System.out.println("DB 연결 오류: " + e.getMessage());
        }
    }

    public MemberDTO findById(String memberId) {
        String sql = "SELECT * FROM member WHERE member_id = ?";
        
        try(PreparedStatement pstmt = conn.prepareStatement(sql)){
       
            pstmt.setString(1, memberId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                MemberDTO dto = new MemberDTO();
                dto.setMemberId(rs.getString("member_id"));
                dto.setNickName(rs.getString("nickname"));
                dto.setImageUrl(rs.getString("image_url"));
                dto.setMemberRole(rs.getString("member_role"));
                dto.setPlayCnt(rs.getInt("play_cnt"));
                dto.setWinCnt(rs.getInt("win_cnt"));
                dto.setMemberLevel(rs.getInt("member_level"));
                return dto;
            }
            rs.close();
			pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("findid에서 오류 ");
        }
        return null;
    }

    public void insertMember(MemberDTO member) {
        String sql = "INSERT INTO member (member_id, nickname, image_url, member_role, play_cnt, win_cnt, member_level) " 
        			+ "VALUES (?, ?, ?, 'user', 0, 0, 0)";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)){
            pstmt.setString(1, member.getMemberId());
            pstmt.setString(2, member.getNickName());
            pstmt.setString(3, member.getImageUrl());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Insert에서 오류 ");
        }
    }
}

