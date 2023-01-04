package numble.karrot.controller;

import lombok.RequiredArgsConstructor;
import numble.karrot.member.domain.Member;
import numble.karrot.member.dto.MemberJoinRequest;
import numble.karrot.member.dto.MemberLoginRequest;
import numble.karrot.member.service.MemberService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("members")
public class MemberController {

    private final MemberService memberService;


}
