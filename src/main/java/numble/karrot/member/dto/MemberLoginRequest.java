package numble.karrot.member.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MemberLoginRequest {

    private String email;
    private String password;

    @Builder
    public MemberLoginRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
