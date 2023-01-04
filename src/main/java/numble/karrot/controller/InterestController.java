package numble.karrot.controller;

import lombok.RequiredArgsConstructor;
import numble.karrot.interest.domain.Interest;
import numble.karrot.interest.service.InterestService;
import numble.karrot.member.domain.Member;
import numble.karrot.member.service.MemberService;
import numble.karrot.product.domain.Product;
import numble.karrot.product.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/interests")
public class InterestController {

    private final InterestService interestService;
    private final MemberService memberService;
    private final ProductService productService;

    /**
     * 특정 상품을 관심 목록에 추가
     * @param userDetails
     * @param productId
     * @return
     */
    @GetMapping("/save")
    @ResponseStatus(HttpStatus.CREATED)
    public String save(@AuthenticationPrincipal UserDetails userDetails, @RequestParam("productId") Long productId, Model model){
        // 1. 회원 정보 SELECT
        Member member = memberService.findMember(userDetails.getUsername());
        // 2. 관심 목록에 추가할 판매 상품 정보 조회 SELECT
        Product product = productService.findProductDetails(productId);
        // 3. 관심 목록에 저장 CREATE
        interestService.addInterestList(Interest.builder()
                .member(member)
                .product(product).build());
        // 4. View 속성값 등록
        model.addAttribute("state", "추가");
        return "interest-save-and-delete";
    }

    @GetMapping("/delete")
    public String delete(@AuthenticationPrincipal UserDetails userDetails, @RequestParam("productId") Long productId, Model model){
        // 1. 회원 정보 SELECT
        Member member = memberService.findMember((userDetails.getUsername()));
        // 2. 관심 목록에서 삭제할 판매 상품 정보 조회 SELECT
        Product product = productService.findProductDetails(productId);
        // 3. 관심 목록에서 삭제 DELETE
        interestService.deleteInterestByProductList(product, member);
        // 4. View 속성값 등록
        model.addAttribute("state", "삭제");
        return "interest-save-and-delete";
    }

}
