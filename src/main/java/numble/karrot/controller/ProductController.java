package numble.karrot.controller;

import lombok.RequiredArgsConstructor;
import numble.karrot.chat.service.ChattingService;
import numble.karrot.member.domain.Member;
import numble.karrot.member.service.MemberService;
import numble.karrot.product.domain.Product;
import numble.karrot.product.domain.ProductCategory;
import numble.karrot.product.domain.ProductStatus;
import numble.karrot.product.dto.ProductRegisterRequest;
import numble.karrot.product.dto.ProductUpdateRequest;
import numble.karrot.product.service.ProductServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Controller
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductServiceImpl productService;
    private final MemberService memberService;
    private final ChattingService chattingService;
    private final List<String> categoryList = getCategoryList();


    /**
     * 전체 상품 리스트 페이지
     * */
    @GetMapping("/list")
    public String productsPage(@AuthenticationPrincipal UserDetails userDetails, Model model){
        Member member = memberService.findMember(userDetails.getUsername());
        model.addAttribute("nickname", member.getNickName());
        model.addAttribute("productList", productService.findAllProducts());
        model.addAttribute("interestByMember", member.getProductByInterest());
        return "products/productList";
    }

    /**
     * 상품 등록 페이지로 이동
     * */
    @GetMapping("/register")
    public String registerPage(Model model){
        model.addAttribute("form", new ProductRegisterRequest());
        model.addAttribute("categoryList", categoryList);
        return "products/register";
    }

    /**
     * 상품등록 요청을 처리 후 302 상태 코드 반환
     * @param form: 등록 할 상품 정보
     * */
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.FOUND)
    public String register(@AuthenticationPrincipal UserDetails userDetails, @ModelAttribute ProductRegisterRequest form, RedirectAttributes redirectAttributes) throws IOException {
        // 1. 상품 DB 저장
        Long productId = productService.save(form, userDetails.getUsername());
        // 2. 상품 상세 페이지로 redirect
        redirectAttributes.addAttribute("productId", productId);
        return "redirect:/products/list/{productId}";
    }

    /**
     * 상품 상세 정보 페이지 이동
     */
    @GetMapping("/list/{productId}")
    public String productDetailPage(@AuthenticationPrincipal UserDetails userDetails, @PathVariable("productId") Long productId, Model model){
        // 1. 회원 정보 조회 SELECT
        Member member = memberService.findMember(userDetails.getUsername());
        // 2. 조회할 판매 상품 SELECT
        Product product = productService.findOne(productId);
        // 3. 채팅방 가져오기 SELECT
        String roomName = chattingService.findChatRoomByBuyer(productId, member.getId()).getName();
        // 4. View 속성값 등록
        model.addAttribute("memberId", member.getId());
        model.addAttribute("changeableStatus", productService.getChangeableProductStatus(product.getStatus()));
        model.addAttribute("productDetail", product.toProductDetail());
        model.addAttribute("product", product);
        model.addAttribute("interestList", member.getProductByInterest());
        model.addAttribute("roomName", roomName);
        return "products/detail";
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
            @Nullable @RequestParam("status") ProductStatus status,
            @RequestParam("memberId") Long memberId,
            Model model){
        model.addAttribute("otherProducts", memberService.findOne(memberId).getProductByStatus(status));
        model.addAttribute("memberId", memberId);
        return "products/other";
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
        return "products/update-success";
    }

    /**
     * 상품 수정 페이지로 이동
     * @return
     */
    @GetMapping("/update")
    public String updateProductPage(@RequestParam("productId") Long productId ,Model model){
        // 1. 수정할 상품 조회 SELECT
        Product product = productService.findOne(productId);
        // 2. View Data 등록
        model.addAttribute("categoryList", getCategoryList());
        model.addAttribute("product", product);
        model.addAttribute("form", ProductUpdateRequest.builder()
                .title(product.getTitle())
                .category(product.getCategory())
                .content(product.getContent())
                .price(product.getPrice())
                .build());
        return "products/update";
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
        return "products/delete";
    }

    /**
     * 상품 삭제
     * @param productId 삭제할 상품 ID
     * @return
     */
    @GetMapping("/delete")
    public String deleteProduct(@RequestParam("productId") Long productId){
        productService.deleteProduct(productId);
        return "redirect:/products/list";
    }

    @GetMapping("/list/{productId}/chat")
    public String productChatListPage(@PathVariable("productId") Long productId, Model model){
        model.addAttribute("productId", productId);
        model.addAttribute("chatList", chattingService.findByProductId(productId));
        return "products/chat";
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


}
