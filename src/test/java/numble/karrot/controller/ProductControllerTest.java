package numble.karrot.controller;

import numble.karrot.member.domain.Member;
import numble.karrot.member.dto.MemberJoinRequest;
import numble.karrot.member.service.MemberService;
import numble.karrot.product.domain.Product;
import numble.karrot.product.domain.ProductCategory;
import numble.karrot.product.domain.ProductStatus;
import numble.karrot.product.dto.ProductRegisterRequest;
import numble.karrot.product.service.ProductServiceImpl;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import javax.persistence.EntityManager;

import java.util.UUID;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(username = "test@naver.com", password = "123", roles = "USER")
@Transactional
class ProductControllerTest {

    @Autowired
    private MemberService memberService;
    @Autowired
    private ProductServiceImpl productService;
    @Autowired
    private WebApplicationContext context;
    @Autowired
    MockMvc mvc;
    @Autowired EntityManager em;


    @Before
    public void setting(){
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

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
    public void 전체_상품_조회() throws Exception{
        mvc.perform(get("/products/list"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void 상품_상세페이지_조회() throws Exception{
        System.out.println("ProductControllerTest.상품_상세페이지_조회");
        mvc.perform(get(String.format("/products/list/%d", createProduct())))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void 판매자의_다른_상품_전체() throws Exception{
        // 2. 새로운 회원이 선택한 판매자의 상품 목록 조회
        Long sellerId = em.find(Product.class, createProduct()).getSeller().getId();
        mvc.perform(get("/products/list/other?memberId=" + sellerId + "&status="))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void 상품_상태별_조회_By_판매자() throws Exception{
        Long sellerId = em.find(Product.class, createProduct()).getSeller().getId();
        mvc.perform(get("/products/list/other?memberId="+ sellerId + "&status=TRADE"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void 상품_상태_수정() throws Exception{
        mvc.perform(get("/products/update/status?productId=" + createProduct() + "&status=" + ProductStatus.COMPLETED))
                .andDo(print())
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    public void 상품_수정_페이지() throws Exception{
        mvc.perform(get("/products/update?productId=" + createProduct()))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void 상품_삭제_페이지() throws Exception{
        mvc.perform(get("/products/delete/check?productId="+createProduct()))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void 상품_삭제() throws Exception{
        mvc.perform(get("/products/delete?productId="+createProduct()))
                .andDo(print())
                .andExpect(status().is3xxRedirection());
    }

    @Test
    public void 채팅_조회() throws Exception{
        mvc.perform(get("/products/list/" + createProduct() + "/chat"))
                .andDo(print())
                .andExpect(status().isOk());
    }
}