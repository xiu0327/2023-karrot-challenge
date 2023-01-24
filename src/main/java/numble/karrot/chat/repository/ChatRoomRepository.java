package numble.karrot.chat.repository;

import numble.karrot.chat.domain.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    @Query("select r from ChatRoom r where r.buyer.email= :email or r.product.seller.email= :email")
    List<ChatRoom> findChatRoomByMember(@Param("email") String email);
    Optional<ChatRoom> findByProductIdAndBuyerId(Long productId, Long buyerId);
    Optional<ChatRoom> findByName(String name);
    List<ChatRoom> findByProductId(Long productId);


}
