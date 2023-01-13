package numble.karrot.interest.service;

import numble.karrot.exception.DuplicateInterestExistsException;
import numble.karrot.interest.domain.Interest;
import numble.karrot.member.domain.Member;
import numble.karrot.member.dto.MemberJoinRequest;
import numble.karrot.member.service.MemberService;
import numble.karrot.product.domain.Product;
import numble.karrot.product.domain.ProductCategory;
import numble.karrot.product.dto.ProductRegisterRequest;
import numble.karrot.product.service.ProductService;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.JpaEntityInformationSupport;
import org.springframework.test.annotation.Commit;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
/**
 * 1. 특정 회원의 관심 목록 조회
 * 2. 관심 목록 추가
 * 3. 관심 목록 삭제
 * 4. 특정 상품의 관심수 증가
 * 5. 특정 상품의 관심수 감소
 */
@SpringBootTest
@Transactional
class InterestServiceImplTest {

    @Autowired
    InterestService interestService;

    @Autowired
    MemberService memberService;

    @Autowired
    ProductService productService;

    @Autowired
    EntityManager em;

    @Test
    void 하트_누르기_성공() {
        // given
        Member member = memberService.join(MemberJoinRequest.builder()
                .email("test5@naver.com")
                .name("서창빈")
                .nickname("돼끼")
                .password("12345")
                .phone("010-1234-5678")
                .build().toMemberEntity());
        Member seller = memberService.findMember("test@naver.com");
        Product product = productService.save(ProductRegisterRequest.builder()
                .title("제목")
                .category(ProductCategory.BEAUTY.getValue())
                .price(100)
                .content("내용")
                .build().toProductEntity(seller));
        // when
        interestService.addInterestList(Interest.builder()
                .member(member)
                .product(product).build());
        Product productDetails = productService.findProductDetails(product.getId());
        // then
        List<Product> interestByMember = interestService.findInterestByMember(member);
        assertThat(productDetails).isIn(interestByMember);
    }

    /**
     * 중복된 관심 상품이 있을 때, 관심 목록 추가 실패
     */
    @Test
    void 관심_목록_추가_실패(){
        // given
        Member member = memberService.join(MemberJoinRequest.builder()
                .email("test5@naver.com")
                .name("서창빈")
                .nickname("돼끼")
                .password("12345")
                .phone("010-1234-5678")
                .build().toMemberEntity());
        Member seller = memberService.findMember("test@naver.com");
        Product product = productService.save(ProductRegisterRequest.builder()
                .title("제목")
                .category(ProductCategory.BEAUTY.getValue())
                .price(100)
                .content("내용")
                .build().toProductEntity(seller));
        // when
        Interest interest = interestService.addInterestList(Interest.builder()
                .member(member)
                .product(product).build());
        // then
        assertThrows(DuplicateInterestExistsException.class, ()-> interestService.addInterestList(interest));
    }

    @Test
    void 관심_목록_삭제_성공(){
        // given
        Member member = memberService.join(MemberJoinRequest.builder()
                .email("test5@naver.com")
                .name("서창빈")
                .nickname("돼끼")
                .password("12345")
                .phone("010-1234-5678")
                .build().toMemberEntity());
        Member seller = memberService.findMember("test@naver.com");
        Product product = productService.save(ProductRegisterRequest.builder()
                .title("제목")
                .category(ProductCategory.BEAUTY.getValue())
                .price(100)
                .content("내용")
                .build().toProductEntity(seller));
        interestService.addInterestList(Interest.builder()
                .member(member)
                .product(product).build());
        int before = interestService.findInterestByMember(member).size();
        // when
        interestService.deleteInterestByProductList(product.getId(), member.getId());
        int after = interestService.findInterestByMember(member).size();
        // then
        assertThat(after).isNotEqualTo(before);
        assertThat(after).isEqualTo(before-1);
    }
}