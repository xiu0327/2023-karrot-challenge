package numble.karrot.member.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
public class MemberUpdateRequest {
    private MultipartFile rowProfile;
    private String nickName;

    @Builder
    public MemberUpdateRequest(MultipartFile rowProfile, String nickName) {
        this.rowProfile = rowProfile;
        this.nickName = nickName;
    }

}
