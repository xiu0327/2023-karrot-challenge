package numble.karrot.controller;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import numble.karrot.exception.PathNotValidException;
import numble.karrot.interest.domain.Interest;
import numble.karrot.interest.service.InterestService;
import numble.karrot.member.domain.Member;
import numble.karrot.member.domain.MemberImageInit;
import numble.karrot.member.service.MemberService;
import numble.karrot.product.domain.Product;
import numble.karrot.product.domain.ProductCategory;
import numble.karrot.product.domain.ProductStatus;
import numble.karrot.product.dto.ProductDetailsResponse;
import numble.karrot.product.dto.ProductRegisterRequest;
import numble.karrot.product.service.ProductService;
import numble.karrot.product_image.domain.ProductImage;
import numble.karrot.product_image.service.ProductImageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Controller
@PropertySource("classpath:aws.properties")
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductImageService productImageService;
    private final ProductService productService;
    private final MemberService memberService;
    private final InterestService interestService;
    @Value("${removeUrl}")
    private String removeUrl;
    private final List<String> categoryList = getCategoryList();


    /**
     * 전체 상품 리스트 페이지
     * */
    @GetMapping("/list")
    public String productsPage(@AuthenticationPrincipal UserDetails userDetails, Model model){
        // 1. 회원 정보 SELECT
        Member member = memberService.findMember(userDetails.getUsername());
        // 2. 전체 상품 조회 SELECT
        List<Product> productList = productService.findAllProducts();
        // 3. 회원의 관심 목록 조회 SELECT
        List<Product> interestByMember = interestService.findInterestByMember(member);
        // 4. View 속성값 등록
        model.addAttribute("nickname", member.getNickName());
        model.addAttribute(productList);
        model.addAttribute("interestByMember", interestByMember);
        return "product-list";
    }

    /**
     * 상품 등록 페이지로 이동
     * */
    @GetMapping("/register")
    public String registerPage(Model model){
        model.addAttribute("form", new ProductRegisterRequest());
        model.addAttribute("categoryList", categoryList);
        return "register-product";
    }

    /**
     * 상품등록 요청을 처리 후 302 상태 코드 반환
     * @param form: 등록 할 상품 정보
     * */
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.FOUND)
    @Transactional
    public String register(@AuthenticationPrincipal UserDetails userDetails, @ModelAttribute ProductRegisterRequest form, RedirectAttributes redirectAttributes) throws IOException {
        // 1. 판매자 정보 SELECT
        Member seller = memberService.findMember(userDetails.getUsername());
        // 2. 상품 등록 요청 DTO 를 Entity 로 변환
        Product registerProduct = form.toProductEntity(seller);
        // 3. 상품 이미지 DB 등록 CREATE
        List<ProductImage> productImages = form.getProductImages().stream()
                .map((image) -> {
                    try {
                        return productImageService.save(productImageService.convert(image, registerProduct));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.toList());
        registerProduct.setProductImages(productImages);
        // 4. 상품 DB에 저장 CREATE
        Product saveProduct = productService.save(registerProduct);
        // 5. 상품 상세 페이지로 리다이렉트
        redirectAttributes.addAttribute("productId", saveProduct.getId());
        return "redirect:/products/list/{productId}";
    }

    /**
     * 상품 상세 정보 페이지 이동
     * @param id 상품 ID
     * @param model
     * @return 상품 상세 정보 페이지
     */
    @GetMapping("/list/{productId}")
    public String productDetailPage(@AuthenticationPrincipal UserDetails userDetails, @PathVariable("productId") Long id, Model model){
        // 1. 판매자 정보 SELECT
        Member seller = memberService.findMember(userDetails.getUsername());
        // 2. 프로필이 등록되어 있지 않으면 기본 이미지로 초기화
        if(seller.getProfile().isEmpty()){seller.setProfile(MemberImageInit.INIT_URL);}
        // 3. 조회할 판매 상품 SELECT
        Product productDetails = productService.findProductDetails(id);
        // 4. 조회할 판매 상품 이미지 SELECT
        productDetails.setProductImages(productImageService.findProductImages(productDetails));
        // 5. 해당 상품의 판매자가 판매하는 다른 상품들 SELECT
        List<Product> otherProducts = productService.findProductsByMember(seller.getId());
        // 6. 상품 조회 DTO 변환
        ProductDetailsResponse productDetailsResponse = toProductDetailsResponse(seller, productDetails, otherProducts);
        // 7. View 속성값 등록
        model.addAttribute("productDetail", productDetailsResponse);
        model.addAttribute("otherProducts", otherProducts);
        model.addAttribute("memberId", seller.getId());
        model.addAttribute("status", "/all");
        return "product-detail";
    }

    /**
     *  판매자의 판매 상태별 상품 조회 페이지
     * @param memberId 판매자의 ID
     * @param status 판매상품 조회 구분 기준
     * @param model
     * @return
     */
    @GetMapping("/list/other")
    public String otherProductPage(
            @RequestParam("status") ProductStatus status,
            @RequestParam("memberId") Long memberId,
            Model model){
        // 1. 판매 상태에 따른 상품 목록 조회 SELECT
        List<Product> otherProducts = productService.findProductsByStatus(memberId, status);
        // 2. View 속성값 등록
        model.addAttribute("otherProducts", otherProducts);
        model.addAttribute("memberId", memberId);
        return "product-other";
    }

    @GetMapping("/list/other/all")
    public String otherAllProductPage(@RequestParam("memberId") Long memberId, Model model){
        // 1. 판매 상태에 따른 상품 목록 전체 조회 SELECT
        List<Product> otherProducts = productService.findProductsByMember(memberId);
        // 2. View 속성값 등록
        model.addAttribute("otherProducts", otherProducts);
        model.addAttribute("memberId", memberId);
        return "product-other";
    }

    /**
     * 카테고리 리스트를 가져옴
     * @return 카테고리 목록
     */
    private List<String> getCategoryList(){
        return Stream.of(ProductCategory.values())
                .map(m -> m.getValue())
                .collect(Collectors.toList());
    }

    /**
     * 상품 상세조회 DTO 변환
     * @param product 상품 객체
     * @param otherProducts 그 외 판매 상품
     * @return상품 상세 정보 DTO
     */
    private ProductDetailsResponse toProductDetailsResponse(Member seller, Product product, List<Product> otherProducts){
        return ProductDetailsResponse.builder()
                .profile(seller.getProfile())
                .nickName(seller.getNickName())
                .title(product.getTitle())
                .status(product.getStatus().getValue())
                .price(product.getPrice())
                .category(product.getCategory())
                .date(product.getDate().toLocalDateTime())
                .content(product.getContent())
                .interestCount(product.getInterestCount())
                .productImages(product.getProductImages().stream()
                        .map(productImage -> productImage.getUrl())
                        .collect(Collectors.toList())
                )
                .otherProducts(otherProducts)
                .build();
    }

}
