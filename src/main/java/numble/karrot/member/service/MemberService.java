package numble.karrot.member.service;

import numble.karrot.member.domain.Member;
import numble.karrot.member.dto.MemberUpdateRequest;

import java.io.IOException;

public interface MemberService {
    Long join(Member member);
    void update(Long memberId, MemberUpdateRequest request) throws IOException;
    Member findOne(Long memberId);
    Member findMember(String email);
    void existMemberCheck(String email);
}
