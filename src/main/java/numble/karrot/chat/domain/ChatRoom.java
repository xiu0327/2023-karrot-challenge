package numble.karrot.chat.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import numble.karrot.member.domain.Member;
import numble.karrot.product.domain.Product;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "chat_room")
@Getter
public class ChatRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "buyer_id")
    private Member buyer;

    @OneToMany(fetch = FetchType.LAZY)
    private List<Chat> chats = new ArrayList<>();

    public ChatRoom() {
        this.name = UUID.randomUUID().toString();
    }

    @Builder
    public ChatRoom(Long roomId, String roomName, Product product, Member buyer) {
        this.id = roomId;
        this.name = roomName;
        this.product = product;
        this.buyer = buyer;
    }

    /* 비즈니스 로직 */
    public Chat getLastChat(){
        return getChats().get(getChats().size()-1);
    }
}
