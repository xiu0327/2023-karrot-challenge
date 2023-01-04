package numble.karrot.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import numble.karrot.member.domain.Member;
import numble.karrot.member.dto.MemberLoginRequest;
import numble.karrot.member.service.MemberService;
import numble.karrot.product.domain.Product;
import numble.karrot.product.domain.ProductCategory;
import numble.karrot.product.domain.ProductStatus;
import numble.karrot.product.dto.ProductRegisterRequest;
import numble.karrot.product.service.ProductService;
import numble.karrot.product_image.domain.ProductImage;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.multipart.MultipartFile;

import javax.swing.*;
import javax.transaction.Transactional;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
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

    @Before
    public void setting(){
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    public void 전체_상품_조회_성공() throws Exception{
        // 1. 전체 상품 조회 성공
        mvc.perform(get("/products/list"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void 상품_상세페이지_조회_성공() throws Exception{
        // 1. 조회할 상품 정보
        Product product = productService.findAllProducts().get(1);
        // 2. 상품 상세 페이지 이동 테스트
        mvc.perform(get(String.format("/products/list/%d", product.getId())))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void 상품_상태별_조회_By_판매자_성공() throws Exception{
        // 1. 판매자
        Member member = memberService.findMember("test@naver.com");
        // 2. 테스트
        mvc.perform(get("/products/list/other?memberId="+ member.getId() + "&status=TRADE"))
                .andDo(print())
                .andExpect(status().isOk());
    }
}