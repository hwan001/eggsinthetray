package service;

import dao.MemberDAO;
import dto.MemberDTO;

public class MemberService {
	private MemberDAO dao = new MemberDAO();

    public MemberDTO loginOrJoin(MemberDTO member) {
    	//찾아서 있으면 객체 반
        MemberDTO existing = dao.findById(member.getMemberId());
        if (existing != null) {
            return existing; 
        } else {//없으면 새로 insert
            dao.insertMember(member);
            return member;
        }
    }
}
