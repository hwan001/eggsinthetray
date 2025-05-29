package model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MemberVO {
    private String memberId;
    private String nickname;
    private String imageUrl;
    private String memberRole;
    private int playCnt;
    private int winCnt;
    private double winRate; 
    private int memberLevel;
    private int memberExp;

    public void updateResult(String result) {
        this.playCnt += 1;
        if (result.equals("win")) {
            this.winCnt += 1;
            this.memberExp += 2;
        } else {
            this.memberExp += 1;
        }
        this.memberLevel = (this.memberExp / 5) + 1;
    }
}
