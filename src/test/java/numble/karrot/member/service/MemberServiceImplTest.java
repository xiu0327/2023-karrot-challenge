package numble.karrot.member.service;

import numble.karrot.aws.S3Uploader;
import numble.karrot.image.ImageStorageFolderName;
import numble.karrot.member.domain.Member;
import numble.karrot.member.domain.MemberImageInit;
import numble.karrot.member.domain.MemberRole;
import numble.karrot.member.repository.MemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.annotation.Commit;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;

import java.io.IOException;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberServiceImplTest {

    @Autowired
    MemberService memberService;

    @Autowired
    S3Uploader s3Uploader;

    @Autowired
    ResourceLoader resourceLoader;

    @Test
    void 회원_가입() {
        //given
        Member member = Member.builder()
                .email("test2@naver.com")
                .name("서창빈")
                .nickName("돼끼")
                .password("12345")
                .phone("010-1234-5678")
                .memberRole(MemberRole.ROLE_USER)
                .build();
        //when
        memberService.join(member);
        Member checkMember = memberService.findMember(member.getEmail());
        //then
        assertThat(member).isEqualTo(checkMember);
        assertThat(checkMember.getProfile()).isEqualTo(MemberImageInit.INIT_URL);
    }

    @Test
    void 닉네임_수정() {
        //given
        Member member = Member.builder()
                .email("test1@naver.com")
                .name("이용복")
                .nickName("필릭스")
                .password("12345")
                .phone("010-1234-5678")
                .memberRole(MemberRole.ROLE_USER)
                .build();
        String newNickName = "행복이";
        //when
        Member joinMember = memberService.join(member);
        memberService.updateNickName(joinMember.getEmail(), newNickName);
        //then
        assertThat(newNickName).isEqualTo(memberService.findMember(joinMember.getEmail()).getNickName());
    }

    @Test
    void 프로필_수정() throws IOException {
        //given
        Member member = Member.builder()
                .email("test1@naver.com")
                .name("이용복")
                .nickName("필릭스")
                .password("12345")
                .phone("010-1234-5678")
                .memberRole(MemberRole.ROLE_USER)
                .build();

        String fileName = "test.jpg";
        Resource res = resourceLoader.getResource("classpath:/static/images/"+fileName);
        MultipartFile imageFile = new MockMultipartFile("file", fileName, null, res.getInputStream());

        //when
        Member joinMember = memberService.join(member);
        String imageUrl = s3Uploader.getImageUrl(imageFile, ImageStorageFolderName.TEST_IMAGE_PATH);
        Member updateMember = memberService.updateProfile(joinMember.getEmail(), imageUrl);
        Member checkMember = memberService.findMember(joinMember.getEmail());

        s3Uploader.deleteImageFile(imageUrl);

        //then
        assertThat(updateMember.getProfile()).isEqualTo(imageUrl);
        assertThat(updateMember).isEqualTo(checkMember);
    }
}