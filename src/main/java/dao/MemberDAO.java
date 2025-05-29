package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import model.MemberVO;
import util.DatabaseUtil;

public class MemberDAO {

    public void insertMember(MemberVO member) {
        String sql = "INSERT INTO member (member_id, nickname, image_url, member_role, play_cnt, win_cnt, member_level, member_exp) " 
        			+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseUtil.getConnection(); 
        	PreparedStatement pstmt = conn.prepareStatement(sql)){
            pstmt.setString(1, member.getMemberId());
            pstmt.setString(2, member.getNickname());
            pstmt.setString(3, member.getImageUrl());
            pstmt.setString(4, member.getMemberRole());
            pstmt.setInt(5, member.getPlayCnt());
            pstmt.setInt(6, member.getWinCnt());
            pstmt.setInt(7, member.getMemberLevel());
            pstmt.setInt(8, member.getMemberExp());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Insert에서 오류 ");
        }
    }

    public MemberVO findByMemberId(String memberId) {
        String selectSql = "SELECT * FROM member WHERE member_id = ?";
        
        return DatabaseUtil.executeQuery(selectSql, 
            pstmt -> pstmt.setString(1, memberId),
            rs -> {
                if (rs.next()) {
                    return MemberVO.builder()
                    .memberId(rs.getString("member_id"))
                    .nickname(rs.getString("nickname"))
                    .imageUrl(rs.getString("image_url"))
                    .memberRole(rs.getString("member_role"))
                    .playCnt(rs.getInt("play_cnt"))
                    .winCnt(rs.getInt("win_cnt"))
                    .memberLevel(rs.getInt("member_level"))
                    .memberExp(rs.getInt("member_exp"))
                    .build();
                }
                return null;
            });
    }

    public void updateMemberResult(MemberVO memberVO) {
        String updateSql = "UPDATE member SET play_cnt = ?, win_cnt = ?, member_level = ?, member_exp = ? WHERE member_id = ?";

        DatabaseUtil.executeUpdate(updateSql, pstmt -> {
            pstmt.setInt(1, memberVO.getPlayCnt());
            pstmt.setInt(2, memberVO.getWinCnt());
            pstmt.setInt(3, memberVO.getMemberLevel());
            pstmt.setInt(4, memberVO.getMemberExp());
            pstmt.setString(5, memberVO.getMemberId());
            pstmt.executeUpdate();
            return null;
        });
    }
}

