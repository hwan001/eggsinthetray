package service;

import dao.MemberDAO;
import dto.MemberDTO;
import dto.MemberResponse;
import dto.ResultRequest;
import lombok.RequiredArgsConstructor;
import model.MemberVO;

@RequiredArgsConstructor
public class MemberService {
	private final MemberDAO memberDAO;

    public MemberDTO loginOrJoin(MemberDTO member) {
    	//찾아서 있으면 객체 반
        MemberDTO existing = memberDAO.findById(member.getMemberId());
        if (existing != null) {
            return existing; 
        } else {//없으면 새로 insert
            memberDAO.insertMember(member);
            return member;
        }
    }

    public MemberResponse getMember(String memberId) {
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
