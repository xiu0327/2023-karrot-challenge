package numble.karrot.product.domain;

import numble.karrot.member.domain.Member;
import numble.karrot.member.dto.MemberJoinRequest;
import numble.karrot.member.service.MemberService;
import numble.karrot.product.dto.ProductRegisterRequest;
import numble.karrot.product.service.ProductServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.time.*;
import java.util.UUID;

@SpringBootTest
@Transactional
class ProductDateHandlerTest {

    @Autowired
    ProductServiceImpl productService;

    @Autowired
    MemberService memberService;

    @Autowired
    EntityManager em;



    /**
     * 상품 등록 시간 변환 핸들러
     * 1시간 전 -> n분전
     * 하루 전 -> n시간 전
     * 한달 전 -> -> n일 전
     * 1년 전 -> 365일전 -> n달 전
     * 1년 후 -> n년 전
     */

    public Long createMember(){
        Member member = MemberJoinRequest.builder()
                .email(UUID.randomUUID().toString())
                .name("이름")
                .phone("휴대폰")
                .nickname("닉네임")
                .password("비밀번호")
                .build().toMemberEntity();
        return memberService.join(member);
    }

    public Long createProduct(){
        Member seller = memberService.findOne(createMember());
        Product product = ProductRegisterRequest.builder()
                .title("상품명")
                .price(2000)
                .category(ProductCategory.GAME_HOBBIES.getValue())
                .content("상품 정보").build().toProductEntity();
        // 연관관계 편의 메서드 실행
        product.addProduct(seller);
        // 상품 DB 저장
        em.persist(product);
        return product.getId();
    }
    @Test
    void 시간_치환_함수_테스트(){
        Product product = em.find(Product.class, createProduct());
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