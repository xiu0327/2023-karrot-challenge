package numble.karrot.member.service;

import numble.karrot.member.domain.Member;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface MemberService {

    Member join(Member member);
    Member findMember(String email);
    Member updateNickName(String email, String nickName);
    Member updateProfile(String email, String profile);
    void deleteMember(Member member);
    List<Member> findAllMember();

}
