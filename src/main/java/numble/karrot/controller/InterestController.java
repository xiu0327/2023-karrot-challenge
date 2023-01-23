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

@Controller
@RequiredArgsConstructor
@RequestMapping("/interests")
public class InterestController {

    private final InterestService interestService;

    @GetMapping("/save")
    @ResponseStatus(HttpStatus.CREATED)
    public String save(@AuthenticationPrincipal UserDetails userDetails, @RequestParam("productId") Long productId, Model model){
        interestService.addInterestList(userDetails.getUsername(), productId);
        model.addAttribute("state", "추가");
        return "interest-save-and-delete";
    }

    @GetMapping("/delete")
    public String delete(@AuthenticationPrincipal UserDetails userDetails, @RequestParam("productId") Long productId, Model model){
        interestService.deleteInterestByProductList(userDetails.getUsername(), productId);
        model.addAttribute("state", "삭제");
        return "interest-save-and-delete";
    }

}
