package numble.karrot.product.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import numble.karrot.TimeService;
import numble.karrot.chat.domain.ChatRoom;
import numble.karrot.interest.domain.Interest;
import numble.karrot.member.domain.Member;
import numble.karrot.product.dto.ProductDetailsResponse;
import numble.karrot.product_image.domain.ProductImage;

import javax.persistence.*;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;

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

    @OneToMany(mappedBy = "product", cascade = {CascadeType.REMOVE, CascadeType.PERSIST})
    List<ProductImage> productImages = new ArrayList<>();

    @OneToMany(mappedBy = "product", cascade = {CascadeType.REMOVE, CascadeType.PERSIST})
    List<ChatRoom> chatRooms = new ArrayList<>();

    @OneToMany(mappedBy = "product", cascade = {CascadeType.REMOVE, CascadeType.PERSIST})
    List<Interest> interests = new ArrayList<>();

    @Builder
    public Product(String title, String category, int price, String content) {
        this.title = title;
        this.category = category;
        this.content = content;
        this.price = price;
        this.status = ProductStatus.TRADE;
        this.interestCount = 0;
        this.chattingCount = 0;
        this.place = "서울";
        this.date = TimeService.getPresentTime();
    }

    // 연관관계 편의 메소드
    public void addProduct(Member seller){
        this.seller = seller;
        seller.getProducts().add(this);
    }


    /* 비즈니스 로직 */

    // 상품 상태 변경
    public void setStatus(ProductStatus status) {
        this.status = status;
    }

    // 썸네일 등록
    public void setThumbnail(List<ProductImage> productImages) {
        this.thumbnail = productImages.get(0).getUrl();
        this.productImages = productImages;
    }

    //상품 정보 변경
    public Product update(Product product){
        title = product.getTitle();
        price = product.getPrice();
        content = product.getContent();
        category = product.getCategory();
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

    /* 조회 로직 */

    public List<String> getProductImageUrl(){
        return productImages.stream().map(i -> i.getUrl()).collect(Collectors.toList());
    }

    public ProductDetailsResponse toProductDetail(){
        return ProductDetailsResponse.builder()
                .productImages(getProductImageUrl())
                .profile(seller.getProfile())
                .otherProducts(seller.getProducts())
                .category(category)
                .content(content)
                .date(TimeService.replaceProductDate(date.toLocalDateTime()))
                .nickName(seller.getNickName())
                .price(price)
                .interestCount(interestCount)
                .title(title)
                .status(status.getValue()).build();
    }

}
