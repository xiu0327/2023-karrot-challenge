package numble.karrot.controller;


import lombok.RequiredArgsConstructor;
import numble.karrot.member.dto.MemberJoinRequest;
import numble.karrot.member.dto.MemberLoginRequest;
import numble.karrot.member.service.MemberService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * 모든 사용자가 접근 가능한 HTTP 요청을 다루는 컨트롤러
 * 시작 화면
 * 로그인 화면
 * 회원 가입 화면
 * 오류 화면
 * */
@Controller
@RequiredArgsConstructor
public class CommonController {

    private final MemberService memberService;

    @GetMapping("/")
    public String startPage(){
        return "start";
    }

    @GetMapping("/error")
    public String errorPage(){
        return "error";
    }

    @GetMapping("/join")
    public String joinPage(Model model){
        model.addAttribute("form", new MemberJoinRequest());
        return "members/join";
    }

    @PostMapping("/join")
    public String join(@ModelAttribute MemberJoinRequest form){
        memberService.join(form.toMemberEntity());
        return "redirect:/";
    }

    @GetMapping("/login")
    public String loginPage(Model model){
        model.addAttribute("form", new MemberLoginRequest());
        return "login";
    }

    @GetMapping("/logout")
    public String logoutPage(){
        return "logout";
    }

}
