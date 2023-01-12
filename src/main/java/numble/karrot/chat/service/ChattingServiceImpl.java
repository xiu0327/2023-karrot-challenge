package numble.karrot.chat.service;

import lombok.RequiredArgsConstructor;
import numble.karrot.chat.domain.Chat;
import numble.karrot.chat.domain.ChatRoom;
import numble.karrot.chat.repository.ChattingRepository;
import numble.karrot.member.domain.Member;
import numble.karrot.product.domain.Product;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class ChattingServiceImpl implements ChattingService{

    private final ChattingRepository repository;

    @Override
    public List<ChatRoom> findAllChatRoom() {
        return repository.findAllChatRoom();
    }

    @Override
    public List<ChatRoom> findChatRoomByMember(String email) {
        return repository.findChatRoomByMember(email);
    }

    @Override
    public ChatRoom findChatRoomByBuyer(Product product, Member buyer) {
        return repository.findChatRoomByBuyer(product.getId(), buyer.getId())
                .orElseGet(()-> new ChatRoom());
    }

    @Override
    public ChatRoom findChatRoomById(Long id) {
        return repository.findChatRoomById(id);
    }

    @Override
    public List<Chat> findChatFromChatRoom(Long roomId) {
        return repository.findChatFromChatRoom(roomId);
    }

    @Override
    public Long saveChat(Chat chat) {
        return repository.saveChat(chat);
    }

    @Override
    public List<ChatRoom> findChatRoomBySeller(Long productId) {
        return repository.findChatRoomBySeller(productId);
    }

    @Override
    public Optional<ChatRoom> findChatRoomByName(String name) {
        return repository.findChatRoomByName(name);
    }

    @Override
    public ChatRoom createChatRoom(ChatRoom chatRoom) {
        return repository.createChatRoom(chatRoom);
    }
}
