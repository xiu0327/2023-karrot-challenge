package numble.karrot.chat.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import numble.karrot.member.domain.Member;
import numble.karrot.product.domain.Product;

import javax.persistence.*;
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

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "buyer_id")
    private Member buyer;

    public ChatRoom() {
        this.name = UUID.randomUUID().toString();
    }

    @Builder
    public ChatRoom(String roomName, Product product, Member buyer) {
        this.name = roomName;
        this.product = product;
        this.buyer = buyer;
    }



    public Long checkId(){
        if(this.id == null){return 0L;}
        return this.getId();
    }
}
