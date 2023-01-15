package numble.karrot.chat.repository;

import numble.karrot.chat.domain.Chat;
import numble.karrot.chat.domain.ChatRoom;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

public interface ChattingRepository {

    // 1. 전체 채팅방 조회
    List<ChatRoom> findAllChatRoom();
    // 2. 특정 회원이 참여중인 채팅방 조회
    List<ChatRoom> findChatRoomByMember(String email);
    // 3. 특정 상품의 채팅 가져오기 - 주체 : 구매자
    Optional<ChatRoom> findChatRoomByBuyer(Long productId, Long buyerId);
    // 4. 특정 채팅방 조회 By Id
    ChatRoom findChatRoomById(Long id);
    // 5. 특정 채팅방의 채팅 내역 가져오기
    List<Chat> findChatFromChatRoom(Long roomId);
    // 6. 채팅방 생성
    ChatRoom createChatRoom(ChatRoom chatRoom);
    // 7. 채팅 내용 저장
    Long saveChat(Chat chat);
    // 8. 특정 상품의 채팅 가져오기 - 주체 : 판매자
    List<ChatRoom> findChatRoomBySeller(Long productId);
    Optional<ChatRoom> findChatRoomByName(String name);
    void deleteChatRoomByProductId(Long productId);
}
