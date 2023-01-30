package numble.karrot.chat.service;

import numble.karrot.chat.domain.Chat;
import numble.karrot.chat.domain.ChatRoom;
import numble.karrot.chat.dto.ChatDto;
import numble.karrot.member.domain.Member;

import java.util.List;

public interface ChattingService {
    List<ChatRoom> findChatRoomByMember(String email);
    ChatRoom findChatRoomByBuyer(Long productId, Long buyerId);
    Long saveChat(ChatDto chatDto);
    List<ChatRoom> findByProductId(Long productId);
    List<Chat> findChatList(Long roomId);
    ChatRoom findChatRoomByName(Member member, String roomName, Long productId);

}
