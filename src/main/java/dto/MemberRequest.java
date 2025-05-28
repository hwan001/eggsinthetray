package dto;

import lombok.Getter;
import lombok.Builder;
import util.JsonUtil;

@Getter
@Builder
public class MemberRequest {
    private String memberId;
    private String nickname;
    private String imageUrl;

    public static MemberRequest from(String jsonResponse) {
        KakaoResponse kakaoResponse = JsonUtil.fromJson(jsonResponse, KakaoResponse.class);
        return MemberRequest.builder()
            .memberId(String.valueOf(kakaoResponse.getId()))
            .nickname(kakaoResponse.getKakao_account().getProfile().getNickname())
            .imageUrl(kakaoResponse.getKakao_account().getProfile().getProfile_image_url())
            .build();
    }
}
