package numble.karrot.member.service;

import lombok.RequiredArgsConstructor;
import numble.karrot.aws.S3Uploader;
import numble.karrot.exception.MemberEmailDuplicateException;
import numble.karrot.exception.MemberNotFoundException;
import numble.karrot.image.ImageStorageFolderName;
import numble.karrot.member.domain.Member;
import numble.karrot.member.dto.MemberUpdateRequest;
import numble.karrot.member.repository.MemberRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService{

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final S3Uploader s3Uploader;

    @Transactional
    public Long join(Member member) {
        existMemberCheck(member.getEmail());
        member.encryptPassword(passwordEncoder);
        return memberRepository.save(member).getId();
    }

    @Transactional
    public void update(Long memberId, MemberUpdateRequest request) throws IOException {
        Member member = findOne(memberId);
        member.updateNickName(request.getNickName());
        if(!request.getProfile().isEmpty()){
            String imageUrl = s3Uploader.getImageUrl(request.getProfile(), ImageStorageFolderName.MEMBER_IMAGE_PATH);
            member.updateProfile(imageUrl);
        }
    }

    public Member findOne(Long memberId){
        return memberRepository.findById(memberId).orElseThrow(() -> {throw new MemberNotFoundException();});
    }

    public Member findMember(String email) {
        return memberRepository.findByEmail(email).orElseThrow(()->{throw new MemberNotFoundException();});
    }

    public void existMemberCheck(String email){
        if(memberRepository.findByEmail(email).isPresent()) throw new MemberEmailDuplicateException();
    }

}
