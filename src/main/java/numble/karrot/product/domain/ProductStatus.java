package numble.karrot.product.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ProductStatus {
    TRADE("거래중"),
    COMPLETED("거래완료"),
    RESERVATION("예약");

    private String value;
}
