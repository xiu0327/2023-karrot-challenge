package numble.karrot.product.dto;

import lombok.*;
import numble.karrot.member.domain.Member;
import numble.karrot.product.domain.Product;
import numble.karrot.product.domain.ProductStatus;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ProductRegisterRequest {

    private String title;
    private String category;
    private int price;
    private String content;
    private List<MultipartFile> productImages;

    @Builder
    public ProductRegisterRequest(String title, String category, int price, String content, List<MultipartFile> productImages) {
        this.title = title;
        this.category = category;
        this.price = price;
        this.content = content;
        this.productImages = productImages;
    }

    /**
     * 상품 엔티티로 변환
     * 상품 이미지가 비어있다면 예외 발생 및 로그 기록
     * @param seller 판매자(회원) 객체
     * @return Product Entity
     * */
    public Product toProductEntity(Member seller){
        return Product.builder()
                .title(this.getTitle())
                .category(this.getCategory())
                .date(getPresentTime())
                .content(this.getContent())
                .chattingCount(0)
                .interestCount(0)
                .status(ProductStatus.TRADE)
                .place("서울")
                .price(this.getPrice())
                .seller(seller).build();
    }

    private ZonedDateTime getPresentTime(){
        LocalDateTime now = LocalDateTime.now();
        return now.atZone(ZoneId.of("Asia/Seoul"));
    }
}
