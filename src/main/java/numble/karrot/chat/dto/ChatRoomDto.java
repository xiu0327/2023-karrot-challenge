package numble.karrot.chat.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import numble.karrot.chat.domain.ChatRoom;
import numble.karrot.member.domain.Member;
import numble.karrot.product.domain.Product;
import org.springframework.web.socket.WebSocketSession;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
public class ChatRoomDto {

    private Set<WebSocketSession> sessions = new HashSet<>();

    public ChatRoom toEntity(Product product, Member buyer){
        return ChatRoom.builder()
                .buyer(buyer)
                .product(product)
                .build();
    }
}
