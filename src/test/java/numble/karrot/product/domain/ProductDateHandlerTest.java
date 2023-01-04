package numble.karrot.product.domain;

import numble.karrot.product.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.time.*;

import static java.lang.Math.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ProductDateHandlerTest {

    @Autowired
    ProductService productService;

    /**
     * 상품 등록 시간 변환 핸들러
     * 1시간 전 -> n분전
     * 하루 전 -> n시간 전
     * 한달 전 -> -> n일 전
     * 1년 전 -> 365일전 -> n달 전
     * 1년 후 -> n년 전
     */
    @Test
    void 시간_치환_함수_테스트(){
        Product product = productService.findAllProducts().get(1);
        System.out.println(replaceProductDate(product.getDate().toLocalDateTime()));
    }

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