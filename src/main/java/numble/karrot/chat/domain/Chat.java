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

    @Column(name = "room_id")
    private Long roomId;

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
    public Chat(Long roomId, ChatType type, String profile, String nickname, String content) {
        this.roomId = roomId;
        this.type = type;
        this.profile = profile;
        this.nickname = nickname;
        this.content = content;
    }
}
