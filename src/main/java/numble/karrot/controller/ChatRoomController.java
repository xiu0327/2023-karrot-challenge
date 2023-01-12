package numble.karrot.controller;

import lombok.RequiredArgsConstructor;
import numble.karrot.chat.domain.Chat;
import numble.karrot.chat.domain.ChatRoom;
import numble.karrot.chat.service.ChattingService;
import numble.karrot.member.domain.Member;
import numble.karrot.member.service.MemberService;
import numble.karrot.product.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/chat")
public class ChatRoomController {

    private final ChattingService chattingService;
    private final MemberService memberService;
    private final ProductService productService;
    /**
     * 채팅방 페이지 ('채팅하기'를 눌렀을 때)
     */
    @GetMapping("/room")
    public String enterPage(@AuthenticationPrincipal UserDetails userDetails, @RequestParam("productId") Long productId, @RequestParam("roomName") String roomName, Model model){
        // 1. 회원 정보 조회 SELECT
        Member member = memberService.findMember(userDetails.getUsername());
        // 2. 채팅방 상세 정보 조회 SELECT OR INSERT
        ChatRoom room = chattingService.findChatRoomByName(roomName).orElseGet(
                () -> chattingService.createChatRoom(ChatRoom.builder()
                        .roomName(roomName)
                        .buyer(member)
                        .product(productService.findProductDetails(productId)).build())
        );
        // 3. 채팅 내역 조회 SELECT
        List<Chat> chatList = chattingService.findChatFromChatRoom(room.getId());
        // 4. View 속성값 등록
        model.addAttribute("product", room.getProduct());
        model.addAttribute("chatList", chatList);
        model.addAttribute("roomId", room.getId());
        model.addAttribute("profile", member.getProfile());
        model.addAttribute("nickname", member.getNickName());
        return "chat";
    }
}
