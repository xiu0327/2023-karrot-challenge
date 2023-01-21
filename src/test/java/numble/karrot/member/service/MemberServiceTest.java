//package numble.karrot.member.service;
//
//import numble.karrot.aws.S3Uploader;
//import numble.karrot.image.ImageStorageFolderName;
//import numble.karrot.member.domain.Member;
//import numble.karrot.member.domain.MemberRole;
//import numble.karrot.product.domain.Product;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.core.io.Resource;
//import org.springframework.core.io.ResourceLoader;
//import org.springframework.mock.web.MockMultipartFile;
//import org.springframework.web.multipart.MultipartFile;
//
//import javax.transaction.Transactional;
//
//import java.io.IOException;
//import java.util.List;
//
//import static org.assertj.core.api.Assertions.*;
//
//@SpringBootTest
//@Transactional
//class MemberServiceTest {
//
//    @Autowired
//    MemberService memberService;
//
//    @Autowired
//    S3Uploader s3Uploader;
//
//    @Autowired
//    ResourceLoader resourceLoader;
//
//    @Test
//    void 회원_가입() {
//        //given
//        int before = memberService.findAllMember().size();
//        Member member = Member.builder()
//                .email("test4@naver.com")
//                .name("서창빈")
//                .nickName("돼끼")
//                .password("12345")
//                .phone("010-1234-5678")
//                .memberRole(MemberRole.ROLE_USER)
//                .build();
//        //when
//       memberService.join(member);
//        //then
//        List<Member> afterMembers = memberService.findAllMember();
//        assertThat(afterMembers.size()).isEqualTo(before+1);
//        assertThat(joinMember).isIn(afterMembers);
//    }
//
//    @Test
//    void 닉네임_수정() {
//        //given
//        Member member = Member.builder()
//                .email("test1@naver.com")
//                .name("이용복")
//                .nickName("필릭스")
//                .password("12345")
//                .phone("010-1234-5678")
//                .memberRole(MemberRole.ROLE_USER)
//                .build();
//        String newNickName = "행복이";
//        //when
//        Member joinMember = memberService.join(member);
//        memberService.updateNickName(joinMember.getEmail(), newNickName);
//        //then
//        assertThat(newNickName).isEqualTo(memberService.findMember(joinMember.getEmail()).getNickName());
//    }
//
//    @Test
//    void 프로필_수정() throws IOException {
//        //given
//        Member member = Member.builder()
//                .email("test1@naver.com")
//                .name("이용복")
//                .nickName("필릭스")
//                .password("12345")
//                .phone("010-1234-5678")
//                .memberRole(MemberRole.ROLE_USER)
//                .build();
//
//        String fileName = "test.jpg";
//        Resource res = resourceLoader.getResource("classpath:/static/images/"+fileName);
//        MultipartFile imageFile = new MockMultipartFile("file", fileName, null, res.getInputStream());
//
//        //when
//        Member joinMember = memberService.join(member);
//        String imageUrl = s3Uploader.getImageUrl(imageFile, ImageStorageFolderName.TEST_IMAGE_PATH);
//        Member updateMember = memberService.updateProfile(joinMember.getEmail(), imageUrl);
//        Member checkMember = memberService.findMember(joinMember.getEmail());
//
//        s3Uploader.deleteImageFile(imageUrl);
//
//        //then
//        assertThat(updateMember.getProfile()).isEqualTo(imageUrl);
//        assertThat(updateMember).isEqualTo(checkMember);
//    }
//
//    @Test
//    void 전체_회원_조회(){
//        List<Member> allMember = memberService.findAllMember();
//        Member member = allMember.get(0);
//        List<Product> otherProducts = member.getOtherProducts();
//    }
//
//}