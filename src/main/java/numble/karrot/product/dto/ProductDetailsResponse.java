package numble.karrot.product.dto;

import jdk.jshell.Snippet;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import numble.karrot.member.domain.Member;
import numble.karrot.product.domain.Product;
import numble.karrot.product.domain.ProductCategory;
import numble.karrot.product_image.domain.ProductDefaultImage;

import java.time.*;
import java.util.List;

/**
 * 상품 상세조회 응답 DTO
 * */
@Getter
@Setter
@NoArgsConstructor
public class ProductDetailsResponse {
    private String profile;
    private String nickName;
    private String title;
    private int price;
    private String category;
    private String date;
    private String status;
    private String content;
    private int interestCount;
    private List<String> productImages;
    private List<Product> otherProducts;

    @Builder
    public ProductDetailsResponse(String profile, String status, String nickName, String title, int price, String category, LocalDateTime date, String content, int interestCount, List<String> productImages, List<Product> otherProducts) {
        this.profile = profile;
        this.status = status;
        this.nickName = nickName;
        this.title = title;
        this.price = price;
        this.category = category;
        this.date = replaceProductDate(date);
        this.content = content;
        this.interestCount = interestCount;
        this.productImages = productImages;
        this.otherProducts = otherProducts;
    }

    //테스트용
    @Override
    public String toString() {
        return "ProductDetailsResponse{" +
                "profile='" + profile + '\'' +
                ", nickName='" + nickName + '\'' +
                ", title='" + title + '\'' +
                ", price=" + price +
                ", category='" + category + '\'' +
                ", date='" + date + '\'' +
                ", interestCount=" + interestCount +
                ", productImages=" + productImages +
                ", otherProducts=" + otherProducts +
                '}';
    }

    /**
     * 상품 이미지가 없을 경우, 기본 이미지를 제공
     * */
    public void addProductDefaultImage(){
        productImages.add(ProductDefaultImage.PRODUCT_DEFAULT_IMAGE);
    }

    /**
     * 상품 등록 시간 치환
     * 1시간 전 -> n분전
     * 하루 전 -> n시간 전
     * 한달 전 -> n일 전
    * 1년 전 -> n달 전
     * 1년 후 -> n년 전
     * @param productDate 상품 등록 시간
     * @return 치환된 상품 등록 시간
     */
    public String replaceProductDate(LocalDateTime productDate){
        ZonedDateTime now = LocalDateTime.now().atZone(ZoneId.of("Asia/Seoul"));
        Duration duration = Duration.between(productDate, now);
        Period period = Period.between(productDate.toLocalDate(), now.toLocalDate());

        if(duration.toHours()<1){ return duration.toMinutes() + " 분 전";}
        if(duration.toDays()<1){ return duration.toHours() + " 시간 전";}

        if(period.getMonths()<1){ return period.getDays() + " 일 전";}
        if (period.getYears()<1) { return period.getMonths() + " 달 전";}

        return period.getYears() + " 년 전";
    }
}
