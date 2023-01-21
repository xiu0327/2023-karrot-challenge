package numble.karrot.member.service;

import lombok.RequiredArgsConstructor;
import numble.karrot.aws.S3Uploader;
import numble.karrot.image.ImageStorageFolderName;
import numble.karrot.member.domain.Member;
import numble.karrot.member.dto.MemberUpdateRequest;
import numble.karrot.member.repository.MemberRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService{

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    private final S3Uploader s3Uploader;

    @Transactional
    public Long join(Member member) {
        member.encryptPassword(passwordEncoder);
        return memberRepository.create(member);
    }

    @Transactional
    public void update(Long memberId, MemberUpdateRequest request) throws IOException {
        memberRepository.updateNickName(memberId, request.getNickName());
        if(!request.getProfile().isEmpty()){
            String imageUrl = s3Uploader.getImageUrl(request.getProfile(), ImageStorageFolderName.MEMBER_IMAGE_PATH);
            memberRepository.updateProfile(memberId,imageUrl);
        }
    }

    public Member findOne(Long memberId){ return memberRepository.findOne(memberId); }

    public List<Member> findAllMember() {
        return memberRepository.findAllMember();
    }

    public Member findMember(String email) {
        return memberRepository.findMemberByEmail(email);
    }

}
