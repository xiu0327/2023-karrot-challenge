package numble.karrot.product.dto;

import lombok.*;
import numble.karrot.product.domain.Product;
import numble.karrot.product.domain.ProductStatus;

import java.time.*;
import java.util.List;

/**
 * 상품 상세조회 응답 DTO
 * */
@Getter
@Setter
@Builder
@AllArgsConstructor
public class ProductDetailsResponse {
    private String nickName;
    private String profile;
    private List<String> productImages;
    private String title;
    private int price;
    private String category;
    private String date;
    private String content;
    private int interestCount;
    private List<Product> otherProducts;
    private String status;
}
