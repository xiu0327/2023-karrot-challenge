package numble.karrot.controller;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import numble.karrot.chat.domain.ChatRoom;
import numble.karrot.chat.service.ChattingService;
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
import numble.karrot.product.dto.ProductUpdateRequest;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import javax.transaction.Transactional;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Arrays.*;

@Controller
@PropertySource("classpath:aws.properties")
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductImageService productImageService;
    private final ProductService productService;
    private final MemberService memberService;
    private final ChattingService chattingService;

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
        // 3. View 속성값 등록
        model.addAttribute("nickname", member.getNickName());
        model.addAttribute("productList", productList);
        model.addAttribute("interestByMember", member.toProductList());
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
    public String register(@AuthenticationPrincipal UserDetails userDetails, @ModelAttribute ProductRegisterRequest form, RedirectAttributes redirectAttributes) throws IOException {
        // 1. 판매자 정보 SELECT
        Member seller = memberService.findMember(userDetails.getUsername());
        // 2. 상품 등록 요청 DTO 를 Entity 로 변환
        Product registerProduct = form.toProductEntity(seller);
        // 3. 상품 DB에 저장 INSERT
        Product saveProduct = productService.save(registerProduct);
        // 4. 상품 이미지 DB 등록 INSERT
        List<ProductImage> images = getProductImage(form.getProductImages(), saveProduct);
        productService.updateThumbnail(images, saveProduct.getId());
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
        // 1. 회원 정보 조회 SELECT
        Member member = memberService.findMember(userDetails.getUsername());
        // 2. 조회할 판매 상품 SELECT
        Product productDetails = productService.findProductDetails(id);
        // 3. 상품 조회 DTO 변환
        ProductDetailsResponse productDetailsResponse = toProductDetailsResponse(productDetails);
        // 4. 채팅방 가져오기 SELECT
        String roomName = chattingService.findChatRoomByBuyer(productDetails, member).getName();
        // 5. View 속성값 등록
        model.addAttribute("memberId", member.getId());
        model.addAttribute("changeableStatus", stream(ProductStatus.values())
                .filter((item) -> item != productDetails.getStatus()).collect(Collectors.toList()));
        model.addAttribute("product", productDetailsResponse);
        model.addAttribute("checkProduct", productDetails);
        model.addAttribute("interestList", member.toProductList());
        model.addAttribute("status", "/all");
        model.addAttribute("roomName", roomName);
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

    /**
     * 판매자의 다른 상품 보기 페이지
     * @param memberId 판매자 ID
     * @param model
     * @return
     */
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
     * 판매 상품 상태 변경
     * @param productId 변경할 상품의 ID
     * @param status 변경할 판매 상태
     * @param model
     * @return
     */
    @GetMapping("/update/status")
    public String updateProductStatus(@RequestParam("productId") Long productId, @RequestParam("status") ProductStatus status, Model model){
        // 1. 상품 상태 수정 UPDATE
        productService.updateProductStatus(productId, status);
        // 2. View 속성값 등록
        model.addAttribute("state", status.getValue());
        return "product-update-success";
    }

    /**
     * 상품 수정 페이지로 이동
     * @return
     */
    @GetMapping("/update")
    public String updateProductPage(@RequestParam("productId") Long productId ,Model model){
        // 1. 수정할 상품 조회 SELECT
        Product product = productService.findProductDetails(productId);
        // 2. View Data 등록
        model.addAttribute("categoryList", getCategoryList());
        model.addAttribute("product", product);
        model.addAttribute("form", new ProductUpdateRequest());
        return "product-update";
    }

    /**
     * 상품 수정
     * @param form
     * @param productId
     * @param redirectAttributes
     * @return
     */
    @PostMapping("/update")
    @ResponseStatus(HttpStatus.FOUND)
    public String updateProduct(@ModelAttribute ProductUpdateRequest form, @RequestParam("productId") Long productId, RedirectAttributes redirectAttributes){
        // 1. 상품 수정 UPDATE
        productService.updateProduct(productId, form.toProductEntity());
        // 2. 상품 상세 페이지로 리다이렉트
        redirectAttributes.addAttribute("productId", productId);
        return "redirect:/products/list/{productId}";
    }

    /**
     * 상품 삭제 확인 페이지(콘솔)
     * @param productId
     * @param model
     * @return
     */
    @GetMapping("/delete/check")
    public String deleteProductPage(@RequestParam("productId") Long productId, Model model){
        model.addAttribute("productId", productId);
        return "product-delete";
    }

    /**
     * 상품 삭제
     * @param productId 삭제할 상품 ID
     * @return
     */
    @GetMapping("/delete")
    public String deleteProduct(@RequestParam("productId") Long productId){
        // 1. 삭제할 상품 조회 SELECT
        Product product = productService.findProductDetails(productId);
        // 2. 상품 이미지 삭제
        productImageService.deleteProductImage(product.getJoinProductImages());
        // 2. 상품 삭제
        productService.deleteProduct(productId);
        // 3. 상품 상세 페이지로 리다이렉트
        return "redirect:/products/list";
    }

    @GetMapping("/list/{productId}/chat")
    public String productChatListPage(@PathVariable("productId") Long productId, Model model){
        // 1. 채팅 목록 조회 SELECT
        List<ChatRoom> chatList = chattingService.findChatRoomBySeller(productId);
        // 2. View 속성값 등록
        model.addAttribute("productId", productId);
        model.addAttribute("chatList", chatList);
        return "product-chat";
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

    private List<ProductImage> getProductImage(List<MultipartFile> productImages, Product product){
        return productImages.stream()
                .map((image) -> {
                    try {
                        return productImageService.save(productImageService.convert(image, product));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.toList());
    }

    /**
     * 상품 상세조회 DTO 변환
     * @param product 상품 객체
     * @return상품 상세 정보 DTO
     */
    private ProductDetailsResponse toProductDetailsResponse(Product product){
        Member seller = product.getSeller();
        return ProductDetailsResponse.builder()
                .sellerId(seller.getId())
                .profile(seller.getProfile())
                .nickName(seller.getNickName())
                .title(product.getTitle())
                .status(product.getStatus().getValue())
                .price(product.getPrice())
                .category(product.getCategory())
                .date(product.getDate().toLocalDateTime())
                .otherProducts(seller.getOtherProducts())
                .content(product.getContent())
                .interestCount(product.getInterestCount())
                .productImages(product.getJoinProductImages().stream()
                        .map(productImage -> productImage.getUrl())
                        .collect(Collectors.toList())
                )
                .build();
    }

}
