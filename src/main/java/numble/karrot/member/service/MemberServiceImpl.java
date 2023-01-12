package numble.karrot.member.service;

import lombok.RequiredArgsConstructor;
import numble.karrot.aws.S3Uploader;
import numble.karrot.image.ImageStorageFolderName;
import numble.karrot.member.domain.Member;
import numble.karrot.member.repository.MemberRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional
public class MemberServiceImpl implements MemberService{

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public Member join(Member member) {
        member.encryptPassword(passwordEncoder);
        return memberRepository.create(member);
    }

    @Override
    public Member findMember(String email) {
        return memberRepository.findMemberByEmail(email);
    }

    @Override
    public Member updateNickName(String email, String nickName) {
        return memberRepository.updateMemberByNickName(email, nickName);
    }

    @Override
    public Member updateProfile(String email, String profile) {
        return memberRepository.updateMemberByProfile(email, profile);
    }

    @Override
    public void deleteMember(Member member) {
        memberRepository.removeMember(member);
    }

    @Override
    public List<Member> findAllMember() {
        return memberRepository.findAllMember();
    }

}
