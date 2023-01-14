package numble.karrot.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import numble.karrot.exception.ProductNotFoundException;
import numble.karrot.member.domain.Member;
import numble.karrot.member.domain.MemberRole;
import numble.karrot.member.service.MemberService;
import numble.karrot.product.domain.Product;
import numble.karrot.product.domain.ProductCategory;
import numble.karrot.product.domain.ProductStatus;
import numble.karrot.product.dto.ProductUpdateRequest;
import numble.karrot.product.service.ProductService;
import numble.karrot.product_image.service.ProductImageService;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ResourceLoader;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import javax.transaction.Transactional;

import static org.assertj.core.api.Assertions.*;
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
    private ProductService productService;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    MockMvc mvc;

    @Autowired
    ResourceLoader resourceLoader;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ProductImageService productImageService;

    @Before
    public void setting(){
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    public void 전체_상품_조회() throws Exception{
        // 1. 전체 상품 조회 성공
        mvc.perform(get("/products/list"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void 상품_상세페이지_조회() throws Exception{
        // 1. 조회할 상품 정보
        Long productId = 1L;
        // 2. 상품 상세 페이지 이동 테스트
        System.out.println("ProductControllerTest.상품_상세페이지_조회");
        mvc.perform(get(String.format("/products/list/%d", productId)))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void 판매자의_다른_상품_전체() throws Exception{
        // 2. 새로운 회원이 선택한 판매자의 상품 목록 조회
        Long sellerId = 1L;
        mvc.perform(get("/products/list/other/all?memberId=" + sellerId))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void 상품_상태별_조회_By_판매자() throws Exception{
        Long sellerId = 1L;
        mvc.perform(get("/products/list/other?memberId="+ sellerId + "&status=TRADE"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void 상품_상태_수정() throws Exception{
        Product product = productService.findAllProducts().get(0);

        mvc.perform(get("/products/update/status?productId=" + product.getId() + "&status=" + ProductStatus.COMPLETED))
                .andDo(print())
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    public void 상품_수정_페이지() throws Exception{
        mvc.perform(get("/products/update?productId=" + 1L))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void 상품_삭제_페이지() throws Exception{
        mvc.perform(get("/products/delete/check?productId="+1L))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void 상품_삭제() throws Exception{
        mvc.perform(get("/products/delete?productId="+1L))
                .andDo(print())
                .andExpect(status().is3xxRedirection());
    }

    @Test
    public void 채팅_조회() throws Exception{
        mvc.perform(get("/products/list/" + 1L + "/chat"))
                .andDo(print())
                .andExpect(status().isOk());
    }
}