package numble.karrot.product.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import numble.karrot.chat.domain.ChatRoom;
import numble.karrot.interest.domain.Interest;
import numble.karrot.member.domain.Member;
import numble.karrot.product_image.domain.ProductImage;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Getter
@Table(name = "product")
@NoArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "category")
    private String category;

    @Column(name = "place")
    private String place;

    @Column(name = "price")
    private int price;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private ProductStatus status;

    @Column(name = "interest_count")
    private int interestCount;

    @Column(name = "chatting_count")
    private int chattingCount;

    @Column(name = "date")
    private ZonedDateTime date;

    @Column(name = "content")
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member seller;

    @Column(name = "thumbnail")
    private String thumbnail;

    @OneToMany(mappedBy = "product", cascade = {CascadeType.REMOVE, CascadeType.DETACH})
    List<ProductImage> joinProductImages = new ArrayList<>();

    @OneToMany(mappedBy = "product", cascade = {CascadeType.REMOVE, CascadeType.DETACH})
    List<ChatRoom> roomList = new ArrayList<>();

    @OneToMany(mappedBy = "product", cascade = {CascadeType.REMOVE, CascadeType.DETACH})
    List<Interest> interests = new ArrayList<>();

    @Builder
    public Product(String title, String category, String place, int price, ProductStatus status, int interestCount, int chattingCount, ZonedDateTime date, String content, Member seller) {
        this.title = title;
        this.category = category;
        this.place = place;
        this.price = price;
        this.status = status;
        this.interestCount = interestCount;
        this.chattingCount = chattingCount;
        this.date = date;
        this.content = content;
        this.seller = seller;
        if(seller != null) seller.getOtherProducts().add(this);
    }

    // 상품 상태 변경
    public void setStatus(ProductStatus status) {
        this.status = status;
    }

    // 썸네일 등록
    public void setThumbnail(List<ProductImage> productImages) {
        this.thumbnail = productImages.get(0).getUrl();
        this.joinProductImages = productImages;
    }

    // 연관관계 편의 메소드
    public void addProduct(Member seller){
        this.seller = seller;
        seller.getOtherProducts().add(this);
    }


    //상품 정보 변경
    public Product update(Product product){
        this.title = product.getTitle();
        this.price = product.getPrice();
        this.content = product.getContent();
        this.category = product.getCategory();
        return this;
    }

    //관심 갯수 증가
    public void addInterestCount(){
        this.interestCount++;
    }

    //관심 갯수 감소
    public void reduceInterestCount(){
        this.interestCount--;
    }

    //채팅 갯수 증가
    public void addChattingCount(){
        this.chattingCount++;
    }

    //채팅 갯수 감소
    public void reduceChattingCount(){
        this.chattingCount--;
    }

}
