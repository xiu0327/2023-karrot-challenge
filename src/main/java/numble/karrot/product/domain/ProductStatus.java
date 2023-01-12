package numble.karrot.product.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;

@Getter
@AllArgsConstructor
public enum ProductStatus {
    TRADE("판매중"),
    COMPLETED("거래완료"),
    RESERVATION("예약중");

    private String value;

}
