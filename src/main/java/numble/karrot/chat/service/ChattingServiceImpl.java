package numble.karrot.chat.service;

import lombok.RequiredArgsConstructor;
import numble.karrot.chat.domain.Chat;
import numble.karrot.chat.domain.ChatRoom;
import numble.karrot.chat.dto.ChatDto;
import numble.karrot.chat.repository.ChatRepository;
import numble.karrot.chat.repository.ChatRoomRepository;
import numble.karrot.member.domain.Member;
import numble.karrot.product.domain.Product;
import numble.karrot.product.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ChattingServiceImpl implements ChattingService{

    private final ChatRoomRepository chatRoomRepository;
    private final ChatRepository chatRepository;
    private final ProductRepository productRepository;

    @Override
    public List<ChatRoom> findChatRoomByMember(String email) {
        return chatRoomRepository.findChatRoomByMember(email);
    }

    @Override
    public ChatRoom findChatRoomByBuyer(Long productId, Long buyerId) {
        return chatRoomRepository.findByProductIdAndBuyerId(productId, buyerId)
                .orElseGet(()-> new ChatRoom());
    }

    @Override
    @Transactional
    public Long saveChat(ChatDto chatDto) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatDto.getRoomId()).get();
        Chat chat = chatDto.toEntity(chatRoom);
        chatRepository.save(chat);
        return chat.getId();
    }

    @Override
    public List<ChatRoom> findByProductId(Long productId){
        return chatRoomRepository.findByProductId(productId);
    }

    @Override
    public List<Chat> findChatList(Long roomId){
        return chatRepository.findByChatRoomId(roomId);
    }


    @Override
    @Transactional
    public ChatRoom findChatRoomByName(Member member, String roomName, Long productId) {
        Optional<ChatRoom> findChatRoom = chatRoomRepository.findByName(roomName);
        Product product = productRepository.findById(productId).get();
        if(!findChatRoom.isPresent()){
            ChatRoom newChatRoom = ChatRoom.builder()
                    .roomName(roomName)
                    .buyer(member)
                    .product(product).build();
            chatRoomRepository.save(newChatRoom);
            return newChatRoom;
        }
        return findChatRoom.get();
    }
}
