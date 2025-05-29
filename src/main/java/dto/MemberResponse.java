package dto;

import lombok.Getter;
import model.MemberVO;
import lombok.Builder;

@Getter
@Builder
public class MemberResponse {
    private String memberId;
    private String nickname;
    private String imageUrl;
    private int playCnt;
    private int winCnt;
    private double winRate;
    private int memberLevel;
    private int memberExp;

    public static MemberResponse from(MemberVO memberVO) {
        return MemberResponse.builder()
            .memberId(memberVO.getMemberId())
            .nickname(memberVO.getNickname())
            .imageUrl(memberVO.getImageUrl())
            .playCnt(memberVO.getPlayCnt())
            .winCnt(memberVO.getWinCnt())
            .winRate(memberVO.getPlayCnt() > 0 ? Math.round(((double) memberVO.getWinCnt() / memberVO.getPlayCnt()) * 100 * 100) / 100.0 : 0.0)
            .memberLevel(memberVO.getMemberLevel())
            .memberExp(memberVO.getMemberExp())
            .build();
    }
}
