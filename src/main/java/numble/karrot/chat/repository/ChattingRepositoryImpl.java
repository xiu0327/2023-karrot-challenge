package numble.karrot.chat.repository;


import lombok.RequiredArgsConstructor;
import numble.karrot.chat.domain.Chat;
import numble.karrot.chat.domain.ChatRoom;
import numble.karrot.exception.ProductNotFoundException;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ChattingRepositoryImpl implements ChattingRepository{

    private final EntityManager em;

    @Override
    public List<ChatRoom> findAllChatRoom() {
        return em.createQuery("select r from ChatRoom r", ChatRoom.class)
                .getResultList();
    }

    @Override
    public List<ChatRoom> findChatRoomByMember(String email) {
        return em.createQuery("select r from ChatRoom r where r.buyer.email= :email or r.product.seller.email= :email", ChatRoom.class)
                .setParameter("email", email)
                .getResultList();
    }

    @Override
    public Optional<ChatRoom> findChatRoomByBuyer(Long productId, Long buyerId) {
        return em.createQuery("select r from ChatRoom r where r.product.id= :productId and r.buyer.id= :buyerId", ChatRoom.class)
                .setParameter("productId", productId)
                .setParameter("buyerId", buyerId)
                .getResultList().stream().findAny();
    }

    @Override
    public ChatRoom findChatRoomById(Long id) {
        return em.find(ChatRoom.class, id);
    }

    @Override
    public List<Chat> findChatFromChatRoom(Long roomId) {
        return em.createQuery("select c from Chat c where c.roomId= :roomId", Chat.class)
                .setParameter("roomId", roomId)
                .getResultList();
    }

    @Override
    public ChatRoom createChatRoom(ChatRoom chatRoom) {
        em.persist(chatRoom);
        return chatRoom;
    }

    @Override
    public Long saveChat(Chat chat) {
        em.persist(chat);
        return chat.getId();
    }

    @Override
    public List<ChatRoom> findChatRoomBySeller(Long productId) {
        return em.createQuery("select r from ChatRoom r where r.product.id= :productId", ChatRoom.class)
                .setParameter("productId", productId)
                .getResultList();
    }

    @Override
    public Optional<ChatRoom> findChatRoomByName(String name) {
        return em.createQuery("select r from ChatRoom r where r.name= : name", ChatRoom.class)
                .setParameter("name", name)
                .getResultList().stream().findAny();
    }

    @Override
    public void deleteChatRoomByProductId(Long productId) {
        ChatRoom target = em.createQuery("select r from ChatRoom r where r.product.id= :productId", ChatRoom.class)
                .setParameter("productId", productId)
                .getResultList().stream().findAny().orElseThrow(() -> {
                    throw new ProductNotFoundException();
                });
        em.remove(target);
    }
}
