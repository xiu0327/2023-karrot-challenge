package numble.karrot.member.repository;

import numble.karrot.member.domain.Member;

import java.util.List;
import java.util.Optional;

public interface MemberRepository {

    Member create(Member member);
    Member findMemberByEmail(String email);
    Member updateMemberByNickName(String email, String nickName);
    Member updateMemberByProfile(String email, String profile);
    void removeMember(Member member);
    List<Member> findAllMember();
}
