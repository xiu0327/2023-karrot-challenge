package numble.karrot.product;

import numble.karrot.chat.service.ChattingService;
import numble.karrot.exception.ProductNotFoundException;
import numble.karrot.interest.service.InterestService;
import numble.karrot.member.domain.Member;
import numble.karrot.member.domain.MemberRole;
import numble.karrot.member.service.MemberService;
import numble.karrot.product.domain.Product;
import numble.karrot.product.domain.ProductCategory;
import numble.karrot.product.dto.ProductRegisterRequest;
import numble.karrot.product.dto.ProductUpdateRequest;
import numble.karrot.product.service.ProductService;
import numble.karrot.product_image.domain.ProductImage;
import numble.karrot.product_image.service.ProductImageService;
import org.apache.tomcat.util.http.fileupload.FileItem;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItem;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.transaction.Transactional;

import java.io.*;
import java.nio.file.Files;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class ProductServiceImplTest {

    @Autowired
    MemberService memberService;
    
    @Autowired
    ProductService productService;

    @Test
    void 상품_삭제(){
        // given
        Member member = memberService.join(Member.builder()
                .email("juint@naver.com")
                .password("123")
                .phone("전화번호")
                .nickName("닉네임")
                .name("이름")
                .memberRole(MemberRole.ROLE_USER)
                .build());
        Product product = productService.save(ProductRegisterRequest.builder()
                .title("제목")
                .category(ProductCategory.GAME_HOBBIES.getValue())
                .content("내용")
                .price(2000)
                .build().toProductEntity(member));
        Long productId = product.getId();
        int before = productService.findAllProducts().size();

        // when
        productService.deleteProduct(productId);

        // then
        int after = productService.findAllProducts().size();
        assertThat(before).isNotEqualTo(after);
        assertThat(after).isEqualTo(before-1);
    }

    @Test
    void 상품_등록(){
        // given
        Member member = memberService.join(Member.builder()
                .email("juint@naver.com")
                .password("123")
                .phone("전화번호")
                .nickName("닉네임")
                .name("이름")
                .memberRole(MemberRole.ROLE_USER)
                .build());
        Product product = productService.save(ProductRegisterRequest.builder()
                .title("제목")
                .category(ProductCategory.GAME_HOBBIES.getValue())
                .content("내용")
                .price(2000)
                .build().toProductEntity(member));

        // when
        productService.save(product);

        // then
        assertThat(productService.findProductDetails(product.getId())).isNotNull();
    }

    @Test
    void 상품_수정(){
        // given
        Member member = memberService.join(Member.builder()
                .email("juint@naver.com")
                .password("123")
                .phone("전화번호")
                .nickName("닉네임")
                .name("이름")
                .memberRole(MemberRole.ROLE_USER)
                .build());
        Product product = productService.save(ProductRegisterRequest.builder()
                .title("제목")
                .category(ProductCategory.GAME_HOBBIES.getValue())
                .content("내용")
                .price(2000)
                .build().toProductEntity(member));

        // when
        Product result = productService.updateProduct(product.getId(), ProductUpdateRequest.builder()
                .title("돼끼 팔아용")
                .category(ProductCategory.OTHER_USED.getValue())
                .price(24000)
                .content("외출 X 단순 전시 O 얼른 사가세요")
                .build().toProductEntity());
        productService.findAllProducts(); // flush

        // then
        assertThat(product.getTitle()).isEqualTo(result.getTitle());
    }

}