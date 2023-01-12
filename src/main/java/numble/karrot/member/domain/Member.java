package numble.karrot.member.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import numble.karrot.chat.domain.ChatRoom;
import numble.karrot.interest.domain.Interest;
import numble.karrot.product.domain.Product;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "member")
@Getter
@NoArgsConstructor
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "name")
    private String name;

    @Column(name = "nickname")
    private String nickName;

    @Column(name = "phone")
    private String phone;

    @Column(name = "manner")
    private int mannerTemp = 0;

    @Column(name = "role")
    private MemberRole memberRole;

    @Column(name = "profile")
    private String profile;

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY)
    private List<Interest> interestList = new ArrayList<>();

    @OneToMany(mappedBy = "seller", fetch = FetchType.LAZY)
    private List<Product> otherProducts = new ArrayList<>();

    @Builder
    public Member(Long id, String email, String password, String name, String nickName, String phone, MemberRole memberRole) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.name = name;
        this.nickName = nickName;
        this.phone = phone;
        this.memberRole = memberRole;
        this.profile = MemberImageInit.INIT_URL;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }


    /**
     * 회원 비밀번호를 암호화
     * @param passwordEncoder 암호화 인코더 클래스 by 시큐리티
     * @return 변경된 유저 객체
     * */
    public Member encryptPassword(PasswordEncoder passwordEncoder){
        this.password = passwordEncoder.encode(this.password);
        return this;
    }

    public List<Product> toProductList(){
        return this.getInterestList().stream()
                .map((item)-> item.getProduct())
                .collect(Collectors.toList());
    }

}
