package numble.karrot.chat.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import numble.karrot.member.domain.Member;

import javax.persistence.*;

@Entity
@Table(name = "chat")
@Getter
@NoArgsConstructor
public class Chat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST})
    @JoinColumn(name = "room_id")
    private ChatRoom chatRoom;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private ChatType type;

    @Column(name = "profile")
    private String profile;

    @Column(name = "nickname")
    private String nickname;

    @Column(name = "content")
    private String content;

    @Builder
    public Chat(ChatType type, String profile, String nickname, String content) {
        this.type = type;
        this.profile = profile;
        this.nickname = nickname;
        this.content = content;
    }

    /* 연관관계 편의 메서드 */
    public void addChat(ChatRoom chatRoom){
        this.chatRoom = chatRoom;
//        chatRoom.getChats().add(this);
    }
}
