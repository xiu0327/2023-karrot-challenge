package numble.karrot.controller;

import numble.karrot.member.domain.Member;
import numble.karrot.member.dto.MemberUpdateRequest;
import numble.karrot.member.service.MemberService;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.transaction.Transactional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(username = "test2@naver.com", password = "123", roles = "USER")
@Transactional
class MyPageControllerTest {

    @Autowired
    MockMvc mvc;

    @Autowired
    WebApplicationContext context;

    @Autowired
    MemberService memberService;

    @Before
    public void setting(){
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    void 마이_페이지() throws Exception{
        mvc.perform(get("/my-page"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void 프로필_수정_페이지() throws Exception{
        mvc.perform(get("/my-page/profile"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void 판매내역_페이지() throws Exception{
        mvc.perform(get("/my-page/product?status=TRADE"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void 전체_관심목록_조회() throws Exception{
        mvc.perform(get("/my-page/interest/all"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void 상태별_관심목록_조회() throws Exception{
        mvc.perform(get("/my-page/interest?status=TRADE"))
                .andDo(print())
                .andExpect(status().isOk());

        mvc.perform(get("/my-page/interest?status=COMPLETED"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void 채팅목록_조회() throws Exception{
        System.out.println("MyPageControllerTest.채팅목록_조회");
        mvc.perform(get("/my-page/chat"))
                .andDo(print())
                .andExpect(status().isOk());
    }
}