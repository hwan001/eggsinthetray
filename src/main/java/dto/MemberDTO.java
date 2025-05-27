package dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberDTO {
	private String memberId;
	private String nickName;
	private String imageUrl;
	private String memberRole;
	private int playCnt;
	private int winCnt;
	private int memberLevel;
	
}
