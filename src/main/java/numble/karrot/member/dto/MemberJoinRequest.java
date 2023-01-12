package numble.karrot.member.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import numble.karrot.member.domain.Member;
import numble.karrot.member.domain.MemberRole;

@Getter
@Setter
@NoArgsConstructor
public class MemberJoinRequest {


    private String email;
    private String password;
    private String name;
    private String phone;
    private String nickname;

    @Builder
    public MemberJoinRequest(String email, String password, String name, String phone, String nickname) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.phone = phone;
        this.nickname = nickname;
    }

    /*
    * 회원 엔티티로 변환
    * */
    public Member toMemberEntity(){
        return Member.builder()
                .email(this.email)
                .password(this.password)
                .nickName(this.nickname)
                .name(this.name)
                .phone(this.phone)
                .memberRole(MemberRole.ROLE_USER)

                .build();
    }

}
