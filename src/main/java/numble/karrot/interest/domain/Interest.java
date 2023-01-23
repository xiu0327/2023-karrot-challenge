package numble.karrot.interest.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import numble.karrot.member.domain.Member;
import numble.karrot.product.domain.Product;

import javax.persistence.*;

@Entity
@Table(name = "interest")
@Getter
@NoArgsConstructor
public class Interest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @Builder
    public Interest(Member member, Product product) {
        this.member = member;
        this.product = product;
        product.addInterestCount();
        member.getInterests().add(this);
    }

    public void reduceProductInterestCount(){
        product.reduceInterestCount();
        member.getInterests().remove(this);
    }
}
