package numble.karrot.product.domain;

import numble.karrot.member.domain.Member;
import numble.karrot.member.dto.MemberJoinRequest;
import numble.karrot.member.repository.MemberRepository;
import numble.karrot.member.service.MemberService;
import numble.karrot.product.dto.ProductRegisterRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
public class ProductDomainTest {

    @Autowired EntityManager em;
    @Autowired MemberRepository memberRepository;

    @Test
    void 상품_생성(){
        // 회원 생성
        Member seller = em.find(Member.class, createMember());
        // DTO -> Entity 변환
        Product product = ProductRegisterRequest.builder()
                .title("상품명")
                .price(2000)
                .category(ProductCategory.GAME_HOBBIES.getValue())
                .content("상품 정보").build().toProductEntity();
        // 연관관계 편의 메서드 실행
        product.addProduct(seller);
        em.persist(product);
        // 상품 DB 저장
        Long productId = product.getId();
        // 테스트
        Product findProduct = em.find(Product.class, productId);
        assertThat(findProduct).isEqualTo(product);
        assertThat(findProduct.getSeller()).isEqualTo(seller);
    }

    @Test
    void 판매자_상품_조회(){
        Member member = memberRepository.findByEmail("test@naver.com").get();
        System.out.println(member.getProducts());
    }

    Long createMember(){
        Member member = MemberJoinRequest.builder()
                .email("test10@naver.com")
                .password("123")
                .nickname("닉네임")
                .phone("휴대폰")
                .name("이름")
                .build().toMemberEntity();
        em.persist(member);
        return member.getId();
    }
}
