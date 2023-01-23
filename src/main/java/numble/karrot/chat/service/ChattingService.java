package numble.karrot.chat.service;

import lombok.RequiredArgsConstructor;
import numble.karrot.chat.domain.Chat;
import numble.karrot.chat.domain.ChatRoom;
import numble.karrot.chat.repository.ChattingRepository;
import numble.karrot.member.domain.Member;
import numble.karrot.member.repository.MemberRepository;
import numble.karrot.product.domain.Product;
import numble.karrot.product.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ChattingService{

    private final ChattingRepository repository;
    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;


    public List<ChatRoom> findChatRoomByMember(String email) {
        return repository.findChatRoomByMember(email);
    }

    public ChatRoom findChatRoomByBuyer(Product product, Member buyer) {
        return repository.findChatRoomByBuyer(product.getId(), buyer.getId())
                .orElseGet(()-> new ChatRoom());
    }

    public List<Chat> findChatFromChatRoom(Long roomId) {
        return repository.findChatFromChatRoom(roomId);
    }


    @Transactional
    public Long saveChat(Chat chat) {
        return repository.saveChat(chat);
    }


    public List<ChatRoom> findChatRoomBySeller(Long productId) {
        return repository.findChatRoomBySeller(productId);
    }


    @Transactional
    public ChatRoom findChatRoomByName(Member member, String roomName, Long productId) {
        return repository.findChatRoomByName(roomName).orElseGet(
                () -> repository.createChatRoom(ChatRoom.builder()
                        .roomName(roomName)
                        .buyer(member)
                        .product(productRepository.findProductById(productId)).build())
        );
    }
}
