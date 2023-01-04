package numble.karrot.interest.service;

import numble.karrot.exception.DuplicateInterestExistsException;
import numble.karrot.interest.domain.Interest;
import numble.karrot.member.domain.Member;
import numble.karrot.member.service.MemberService;
import numble.karrot.product.domain.Product;
import numble.karrot.product.service.ProductService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityManager;
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
    void 관심_목록_추가_성공() {
        // given
        Product product = productService.findAllProducts().get(0);
        Member member = memberService.findMember("test@naver.com");
        // when
        interestService.addInterestList(Interest.builder()
                .member(member)
                .product(product).build());
        // then
        List<Product> interestByMember = interestService.findInterestByMember(member);
        assertThat(product).isIn(interestByMember);
    }

    /**
     * 중복된 관심 상품이 있을 때, 관심 목록 추가 실패
     */
    @Test
    void 관심_목록_추가_실패(){
        // given
        Product product = productService.findAllProducts().get(0);
        Member member = memberService.findMember("test@naver.com");

        Interest interest = Interest.builder()
                .member(member)
                .product(product).build();
        // when
        interestService.addInterestList(interest);
        // then
        assertThrows(DuplicateInterestExistsException.class, ()-> interestService.addInterestList(interest));
    }

    @Test
    void 관심_목록_삭제_성공(){
        // given
        Product product = productService.findAllProducts().get(0);
        Member member = memberService.findMember("test@naver.com");

        Interest interest = Interest.builder()
                .member(member)
                .product(product).build();

        int before = interestService.addInterestList(interest).getProduct().getInterestCount();
        // when
        interestService.deleteInterestByProductList(product, member);
        int after = productService.findProductDetails(product.getId()).getInterestCount();
        // then
        assertThat(after).isNotEqualTo(before);
        assertThat(after).isEqualTo(before-1);
    }

}