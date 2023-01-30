package numble.karrot.controller;

import lombok.RequiredArgsConstructor;
import numble.karrot.chat.domain.ChatRoom;
import numble.karrot.chat.service.ChattingService;
import numble.karrot.member.domain.Member;
import numble.karrot.member.service.MemberService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/chat")
public class ChatRoomController {

    private final ChattingService chattingService;
    private final MemberService memberService;

    @GetMapping("/room")
    public String enterPage(@AuthenticationPrincipal UserDetails userDetails, @RequestParam("productId") Long productId, @RequestParam("roomName") String roomName, Model model){
        Member member = memberService.findMember(userDetails.getUsername());
        ChatRoom room = chattingService.findChatRoomByName(member, roomName, productId);
        model.addAttribute("product", room.getProduct());
        model.addAttribute("chatList", chattingService.findChatList(room.getId()));
        model.addAttribute("roomId", room.getId());
        model.addAttribute("profile", member.getProfile());
        model.addAttribute("nickname", member.getNickName());
        return "chatting/chat";
    }
}
