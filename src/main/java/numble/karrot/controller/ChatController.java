package numble.karrot.controller;

import lombok.RequiredArgsConstructor;
import numble.karrot.chat.domain.ChatType;
import numble.karrot.chat.dto.ChatDto;
import numble.karrot.chat.service.ChattingService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

@Controller
@RequiredArgsConstructor
public class ChatController {

    private final ChattingService chattingService;
    private final SimpMessagingTemplate template;

    /**
     * 채팅방 입장
     * @param chatDto
     */
    @MessageMapping(value = "/chat/enter")
    public void enter(ChatDto chatDto){
        chatDto.setContent(chatDto.getNickname() + "님이 채팅방에 참여하셨습니다.");
        chatDto.setType(ChatType.JOIN);
        template.convertAndSend("/sub/chat/room/"+chatDto.getRoomId(), chatDto);
    }

    /**
     * 채팅 메시지 보내기
     * @param chatDto
     */
    @MessageMapping(value = "/chat/send")
    public void send(ChatDto chatDto){
        chattingService.saveChat(chatDto);
        template.convertAndSend("/sub/chat/room/"+chatDto.getRoomId(), chatDto);
    }
}
