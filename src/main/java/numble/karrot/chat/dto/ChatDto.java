package numble.karrot.chat.dto;

import lombok.*;
import numble.karrot.chat.domain.Chat;
import numble.karrot.chat.domain.ChatType;

@Getter
@Setter
@NoArgsConstructor
public class ChatDto {
    private Long roomId;
    private ChatType type;
    private String profile;
    private String nickname;
    private String content;

    public Chat toEntity(){
        return Chat.builder()
                .roomId(this.roomId)
                .type(this.type)
                .profile(this.profile)
                .nickname(this.nickname)
                .content(this.content)
                .build();
    }

    @Override
    public String toString() {
        return "ChatDto{" +
                "roomId=" + roomId +
                ", type=" + type +
                ", profile='" + profile + '\'' +
                ", nickname='" + nickname + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
