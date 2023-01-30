package numble.karrot.interest.service;

import numble.karrot.exception.DuplicateInterestExistsException;
import numble.karrot.member.domain.Member;
import numble.karrot.member.dto.MemberJoinRequest;
import numble.karrot.member.service.MemberService;
import numble.karrot.product.domain.Product;
import numble.karrot.product.domain.ProductCategory;
import numble.karrot.product.dto.ProductRegisterRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class InterestServiceImplTest {

    @Autowired
    InterestService interestService;

    @Autowired
    MemberService memberService;


    @Autowired
    EntityManager em;

    public Long createMember(){
        Member member = MemberJoinRequest.builder()
                .email(UUID.randomUUID().toString())
                .name("이름")
                .phone("휴대폰")
                .nickname("닉네임")
                .password("비밀번호")
                .build().toMemberEntity();
        return memberService.join(member);
    }

    public Long createProduct(){
        Member seller = memberService.findOne(createMember());
        Product product = ProductRegisterRequest.builder()
                .title("상품명")
                .price(2000)
                .category(ProductCategory.GAME_HOBBIES.getValue())
                .content("상품 정보").build().toProductEntity();
        // 연관관계 편의 메서드 실행
        product.addProduct(seller);
        // 상품 DB 저장
        em.persist(product);
        return product.getId();
    }

    @Test
    @Transactional
    void 하트_누르기_성공() {
        // given
        Product product = em.find(Product.class, createProduct());
        Member member = memberService.findOne(createMember());
        // when
        interestService.addInterestList(member.getEmail(), product.getId());
        // then
        assertThat(product).isIn(member.getProductByInterest());
    }

    @Test
    @Transactional
    void 관심_목록_추가_실패(){
        // given
        Member member = memberService.findOne(createMember());
        Product product = em.find(Product.class, createProduct());
        // when
        interestService.addInterestList(member.getEmail(), product.getId());
        em.flush();
        // then
        assertThrows(DuplicateInterestExistsException.class, ()-> interestService.addInterestList(member.getEmail(), product.getId()));
    }

    @Test
    @Transactional
    void 관심_목록_삭제_성공(){
        // given
        Member member = memberService.findOne(createMember());
        Product product = em.find(Product.class, createProduct());
        int before = member.getInterests().size();
        // when
        interestService.addInterestList(member.getEmail(), product.getId());
        em.flush();
        // when
        interestService.deleteInterestByProductList(member.getEmail(), product.getId());
        int after = em.find(Member.class, member.getId()).getInterests().size();
        // then
        assertThat(after).isEqualTo(before);
    }
}