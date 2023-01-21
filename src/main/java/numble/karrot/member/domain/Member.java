package numble.karrot.member.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import numble.karrot.chat.domain.ChatRoom;
import numble.karrot.interest.domain.Interest;
import numble.karrot.product.domain.Product;
import numble.karrot.product.domain.ProductStatus;
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

    @OneToMany(mappedBy = "member")
    private List<Interest> interests = new ArrayList<>();

    @OneToMany(mappedBy = "seller")
    private List<Product> products = new ArrayList<>();

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

    /* 비즈니스 로직 */

    // 닉네임 업데이트
    public void updateNickName(String nickName) {
        this.nickName = nickName;
    }

    // 프로필 업데이트
    public void updateProfile(String profile) {
        this.profile = profile;
    }

    // 회원 비밀번호를 암호화
    public Member encryptPassword(PasswordEncoder passwordEncoder){
        this.password = passwordEncoder.encode(this.password);
        return this;
    }

    /* 조회 로직 */

    // 관심 목록에서 상품 정보만 추출
    public List<Product> getProductByInterest(){
        return this.getInterests().stream()
                .map((item)-> item.getProduct())
                .collect(Collectors.toList());
    }

    // 관심 목록 상태별 상품 추출 -> 나중에 동적 쿼리로 해결할 것
    public List<Product> getInterestStatus(ProductStatus status){
        if(status == null) return getProductByInterest();
        return interests.stream()
                .filter((item) -> item.getProduct().getStatus() == status)
                .map((item) -> item.getProduct())
                .collect(Collectors.toList());
    }

    // 상품 상태별 판매 내역 조회
    public List<Product> getProductByStatus(ProductStatus status){
        if(status == null) return products;
        return products.stream()
                .filter((item)->item.getStatus() == status)
                .collect(Collectors.toList());
    }

}
