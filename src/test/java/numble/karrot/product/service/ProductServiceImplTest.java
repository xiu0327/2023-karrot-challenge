package numble.karrot.product.service;

import numble.karrot.member.domain.Member;
import numble.karrot.member.dto.MemberJoinRequest;
import numble.karrot.member.service.MemberService;
import numble.karrot.product.domain.Product;
import numble.karrot.product.domain.ProductCategory;
import numble.karrot.product.domain.ProductStatus;
import numble.karrot.product.dto.ProductDetailsResponse;
import numble.karrot.product.dto.ProductRegisterRequest;
import numble.karrot.product.dto.ProductUpdateRequest;
import numble.karrot.product_image.domain.ProductImage;
import numble.karrot.product_image.service.ProductImageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.*;

/**
 * 1. 상품 등록
 * 2. 상품 상세 정보 조회
 * 3. 전체 상품 목록 조회
 * 4. 특정 회원이 올린 상품 전체 조회
 * 5. 특정 회원이 올린 상품 판매 상태별 조회
 * 6. 상품 삭제
 * 7. 상품 수정
 * */

@SpringBootTest
@Transactional
class ProductServiceImplTest{

    @Autowired
    MemberService memberService;

    @Autowired
    ProductService productService;

    @Autowired
    ProductImageService productImageService;

    @Autowired
    ResourceLoader resourceLoader;

    Member seller;

    @BeforeEach
    void before(){
        Member joinMember = MemberJoinRequest.builder()
                .email("test2@naver.com")
                .name("서창빈")
                .nickname("돼끼")
                .password("12345")
                .phone("010-1234-5678")
                .build().toMemberEntity();
        memberService.join(joinMember);
        seller = memberService.findMember(joinMember.getEmail());
    }

    /**
     * 상품 등록 테스트
     * 판매자(회원)과 상품이 mapping 되어 DB에 저장되는지 확인
     * */
    @Test
    void 상품_등록() throws IOException{
        //given
        List<MultipartFile> productImages = new ArrayList<>();
        String fileName = "test.jpg";
        Resource res = resourceLoader.getResource("classpath:/static/images/"+fileName);
        MultipartFile imageFile = new MockMultipartFile("file", fileName, null, res.getInputStream());
        productImages.add(imageFile);

        Product product = ProductRegisterRequest.builder()
                .title("제목")
                .category(ProductCategory.BEAUTY.getValue())
                .price(100)
                .content("내용")
                .productImages(productImages)
                .build().toProductEntity(seller);
        //when
        Product saveProduct = productService.save(product);
        //then
        Product findProduct = productService.findProductDetails(saveProduct.getId());
        assertThat(findProduct.getSeller()).isEqualTo(seller);
    }


    /**
     * 상품 등록 테스트
     * 상품과 상품 이미지가 mapping 되어 저장되는지 확인
     * */
    @Test
    void 상품_등록_with_상품이미지() throws IOException{
        //given
        List<MultipartFile> productImages = new ArrayList<>();
        String fileName = "test.jpg";
        Resource res = resourceLoader.getResource("classpath:/static/images/"+fileName);
        MultipartFile imageFile = new MockMultipartFile("file", fileName, null, res.getInputStream());
        productImages.add(imageFile);

        Product product = ProductRegisterRequest.builder()
                .title("제목")
                .category(ProductCategory.BEAUTY.getValue())
                .price(100)
                .content("내용")
                .productImages(productImages)
                .build().toProductEntity(seller);
        //when
        List<ProductImage> convertProductImage = productImages.stream()
                .map((image) -> {
                    try {
                        return productImageService.save(productImageService.convert(image, product));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.toList());

        product.setProductImages(convertProductImage);
        System.out.println("product = " + product.getThumbnail());
        productService.save(product);
        //then
        Product findProduct = productService.findProductDetails(product.getId());
        assertThat(findProduct).isEqualTo(product);
    }

    @Test
    void 상품_등록_객체_비교(){
        Product instance1 = productService.findProductDetails(1L);
        Product instance2 = productService.findProductDetails(1L);

        instance1.setStatus(ProductStatus.COMPLETED);
        productService.updateProduct(1L, instance1);

        assertThat(instance1).isEqualTo(instance2);
        assertThat(instance1.getStatus()).isEqualTo(instance2.getStatus());
    }

    /**
     * 상품 전체 조회
     * */
    @Test
    void 상품_전체_조회() throws IOException{
        //given
        상품_등록();
        //when
        List<Product> allProducts = productService.findAllProducts();
        //then
        assertThat(allProducts.size()).isNotEqualTo(0);
    }

    @Test
    void 상품_상세_조회() throws IOException{
        //given
        Product product = productService.findAllProducts().get(0);
        Member seller = product.getSeller();
        List<String> productImages = productImageService.findProductImages(product).stream()
                .map((image) -> image.getUrl())
                .collect(Collectors.toList());
        List<Product> otherProducts = productService.findProductsByMember(product.getSeller().getId());
        // when
        ProductDetailsResponse response = ProductDetailsResponse.builder()
                .profile(seller.getProfile())
                .nickName(seller.getNickName())
                .date(product.getDate().toLocalDateTime())
                .interestCount(0)
                .otherProducts(otherProducts)
                .productImages(productImages)
                .price(product.getPrice())
                .status(product.getStatus().getValue())
                .content(product.getContent())
                .title(product.getTitle()).build();
        //then
        System.out.println("result.toString() = " + response.toString());
    }

    @Test
    void 판매자_상품_상태별_목록_조회() throws IOException{
        //given
        상품_등록_with_상품이미지();
        //when
        List<Product> productsByStatus = productService.findProductsByStatus(seller.getId(), ProductStatus.TRADE);
        //then
        assertThat(productsByStatus.isEmpty()).isEqualTo(false);
    }

    @Test
    void 상품_수정() throws IOException{
        /**
         * given
         * 상품 등록 -> 새로운 상품 이미지 업로드
         * when
         * 수정할 판매 상품의 기본 정보 조회 -> 수정 요청을 Product Entity로 변환 -> 새로운 상품 이미지 S3 등록 및 DB 저장 -> update
         * */
        상품_등록_with_상품이미지();

        List<MultipartFile> updateProductImage = new ArrayList<>();
        String fileName = "update.jpeg";
        Resource res = resourceLoader.getResource("classpath:/static/images/"+fileName);
        MultipartFile imageFile = new MockMultipartFile("file", fileName, null, res.getInputStream());
        updateProductImage.add(imageFile);

        //when
        ProductUpdateRequest request = ProductUpdateRequest.builder()
                .title("수정 제목")
                .content("수정 내용")
                .price(2000)
                .productImages(updateProductImage)
                .category(ProductCategory.DIGITAL.getValue())
                .build();

        Product updateProduct = request.toProductEntity();
        Product targetProduct = productService.findAllProducts().get(0);

        List<ProductImage> convertProductImage = updateProductImage.stream()
                .map((image) -> {
                    try {
                        return productImageService.save(productImageService.convert(image, targetProduct));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.toList());

        updateProduct.setProductImages(convertProductImage);

        Product result = productService.updateProduct(targetProduct.getId(), updateProduct);
        //then
        assertThat(result.getTitle()).isEqualTo(request.getTitle());
        assertThat(result.getProductImages()).isEqualTo(updateProduct.getProductImages());
    }
}