package service;

import dao.MemberDAO;
import dto.MemberRequest;
import dto.MemberResponse;
import dto.ResultRequest;
import lombok.RequiredArgsConstructor;
import model.MemberVO;

@RequiredArgsConstructor
public class MemberService {

	private final MemberDAO memberDAO;

    public MemberResponse loginOrJoin(MemberRequest memberReq) {
        MemberVO memberVO = memberDAO.findByMemberId(memberReq.getMemberId());
        if (memberVO != null) {
            return MemberResponse.from(memberVO);
        }
        memberVO = MemberVO.builder()
            .memberId(memberReq.getMemberId())
            .nickname(memberReq.getNickname())
            .imageUrl(memberReq.getImageUrl())
            .memberRole("USER")
            .playCnt(0)
            .winCnt(0)
            .memberLevel(1)
            .memberExp(0)
            .build();
        memberDAO.insertMember(memberVO);
        return MemberResponse.from(memberVO);
    }

    public MemberResponse getMemberById(String memberId) {
        MemberVO memberVO = memberDAO.findByMemberId(memberId);
        return MemberResponse.from(memberVO);
    }

    public MemberResponse updateMemberResult(String memberId, ResultRequest resultReq) {
        MemberVO memberVO = memberDAO.findByMemberId(memberId);
        memberVO.updateResult(resultReq.getResult());
        memberDAO.updateMemberResult(memberVO);
        return MemberResponse.from(memberVO);
    }
}
