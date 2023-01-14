package numble.karrot.product;

import numble.karrot.exception.ProductNotFoundException;
import numble.karrot.member.domain.Member;
import numble.karrot.member.service.MemberService;
import numble.karrot.product.domain.Product;
import numble.karrot.product.domain.ProductCategory;
import numble.karrot.product.dto.ProductRegisterRequest;
import numble.karrot.product.dto.ProductUpdateRequest;
import numble.karrot.product.service.ProductService;
import numble.karrot.product_image.domain.ProductImage;
import numble.karrot.product_image.service.ProductImageService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;

import java.io.File;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class ProductServiceImplTest {
    
    @Autowired
    ProductImageService productImageService;

    @Autowired
    MemberService memberService;
    
    @Autowired
    ProductService productService;

    @Test
    void findProductImages() {
        //given
        Product product = productService.findAllProducts().get(0);
        //when
        List<ProductImage> productImages = productImageService.findProductImages(product);
        product.setThumbnail(productImages);
        //then
        assertThat(product.getJoinProductImages()).isNotNull();
    }

    @Test
    void deleteProductImage(){
        Product product = productService.findProductDetails(1L);
        productImageService.deleteProductImage(product.getJoinProductImages());
        List<ProductImage> productImages = productImageService.findProductImages(product);
        assertThat(productImages.size()).isEqualTo(0);
    }

    @Test
    void registerProduct(){
        // 1. 판매자 정보 SELECT
        Member seller = memberService.findMember("test2@naver.com");
        // 2. 상품 등록 요청 DTO 를 Entity 로 변환
        Product registerProduct = ProductRegisterRequest.builder()
                .title("상품1")
                .price(20000)
                .content("상품 내용")
                .category(ProductCategory.GAME_HOBBIES.getValue())
                .build().toProductEntity(seller);
       productService.save(registerProduct);
    }

    @Test
    void updateProduct(){
        Product request = ProductUpdateRequest.builder()
                .title("돼끼 팔아용")
                .category(ProductCategory.OTHER_USED.getValue())
                .price(24000)
                .content("외출 X 단순 전시 O 얼른 사가세요")
                .build().toProductEntity();
        Product result = productService.updateProduct(1L, request);
        Assertions.assertThat(result.getTitle()).isEqualTo(request.getTitle());
    }

    @Test
    void deleteProduct(){
        int before = productService.findAllProducts().size();
        Product product = productService.findProductDetails(1L);
        productImageService.deleteProductImage(product.getJoinProductImages());
//        List<ProductImage> productImages = productImageService.findProductImages(product);
        productService.deleteProduct(product.getId());
        int after = productService.findAllProducts().size();
        Assertions.assertThat(after).isEqualTo(before-1);
    }

}