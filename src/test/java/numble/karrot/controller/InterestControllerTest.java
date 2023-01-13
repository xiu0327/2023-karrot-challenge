package numble.karrot.controller;

import numble.karrot.interest.domain.Interest;
import numble.karrot.interest.service.InterestService;
import numble.karrot.member.domain.Member;
import numble.karrot.member.service.MemberService;
import numble.karrot.product.domain.Product;
import numble.karrot.product.service.ProductService;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(username = "test@naver.com", password = "123", roles = "USER")
@Transactional
class InterestControllerTest {

    @Autowired
    private ProductService productService;

    @Autowired
    private InterestService interestService;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private MemberService memberService;

    @Autowired
    MockMvc mvc;

    @Before
    public void setting(){
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    void 관심목록_저장() throws Exception{
        Product product = productService.findProductDetails(1L);
        System.out.println("InterestControllerTest.관심목록_저장");
        mvc.perform(get("/interests/save?productId=" + product.getId()))
                .andDo(print())
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void 관심목록_삭제() throws Exception{
        // 1. 회원 정보 조회
        Member member = memberService.findMember("test@naver.com");
        Product product = productService.findProductDetails(1L);
        // 2. 관심 목록 저장
        interestService.addInterestList(Interest.builder()
                .member(member)
                .product(product).build());
        System.out.println("InterestControllerTest.관심목록_삭제");
        mvc.perform(get("/interests/delete?productId="+ product.getId()))
                .andDo(print())
                .andExpect(status().is2xxSuccessful());
    }

}