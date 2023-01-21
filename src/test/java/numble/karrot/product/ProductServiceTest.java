//package numble.karrot.product;
//
//import numble.karrot.member.domain.Member;
//import numble.karrot.member.dto.MemberJoinRequest;
//import numble.karrot.member.repository.MemberRepository;
//import numble.karrot.product.domain.Product;
//import numble.karrot.product.domain.ProductCategory;
//import numble.karrot.product.dto.ProductRegisterRequest;
//import numble.karrot.product.dto.ProductUpdateRequest;
//import numble.karrot.product.repository.ProductRepository;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.transaction.annotation.Transactional;
//
//import javax.persistence.EntityManager;
//
//import static org.assertj.core.api.Assertions.*;
//
//@SpringBootTest
//@Transactional(readOnly = true)
//class ProductServiceTest {
//
//    @Autowired
//    EntityManager em;
//    @Autowired
//    MemberRepository memberRepository;
//    @Autowired
//    ProductRepository productRepository;
//
//    @Test
//    @Transactional
//    void 상품_삭제(){
//        // given
//        Long productId = createProduct();
//        int before = productRepository.findAllProduct().size();
//
//        // when
//        productRepository.removeProduct(productId);
//
//        // then
//        int after = productRepository.findAllProduct().size();
//        assertThat(before).isNotEqualTo(after);
//        assertThat(after).isEqualTo(before-1);
//    }
//
//    @Test
//    @Transactional
//    void 상품_수정(){
//        // given
//        Long productId = createProduct();
//        // when
//        Product result = productRepository.updateProduct(productId, ProductUpdateRequest.builder()
//                .title("돼끼 팔아용")
//                .category(ProductCategory.OTHER_USED.getValue())
//                .price(24000)
//                .content("외출 X 단순 전시 O 얼른 사가세요")
//                .build().toProductEntity());
//        // then
//        assertThat(productRepository.findProductById(productId).getTitle()).isEqualTo(result.getTitle());
//    }
//
//    @Test
//    Long createProduct(){
//        // 회원 생성
//        Member seller = em.find(Member.class, createMember());
//        // DTO -> Entity 변환
//        Product product = ProductRegisterRequest.builder()
//                .title("상품명")
//                .price(2000)
//                .category(ProductCategory.GAME_HOBBIES.getValue())
//                .content("상품 정보").build().toProductEntity();
//        // 연관관계 편의 메서드 실행
//        product.addProduct(seller);
//        // 상품 DB 저장
//        Long productId = productRepository.save(product);
//        // 테스트
//        Product findProduct = productRepository.findProductById(productId);
//        assertThat(findProduct).isEqualTo(product);
//        assertThat(findProduct.getSeller()).isEqualTo(seller);
//
//        return productId;
//    }
//
//    Long createMember(){
//        Member member = MemberJoinRequest.builder()
//                .email("test10@naver.com")
//                .password("123")
//                .nickname("닉네임")
//                .phone("휴대폰")
//                .name("이름")
//                .build().toMemberEntity();
//
//        return memberRepository.create(member).getId();
//    }
//
//}