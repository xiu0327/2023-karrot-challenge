package numble.karrot.member.service;

import numble.karrot.member.dto.MemberLoginRequest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.transaction.Transactional;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.logout;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(roles = "USER")
public class SecurityControllerTest {


    @Autowired
    private WebApplicationContext context;

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
    @Transactional
    public void 로그인_테스트_성공() throws Exception{
        //given
        MemberLoginRequest request = MemberLoginRequest.builder()
                .email("test@naver.com")
                .password("123")
                .build();
        //when
        mvc.perform(formLogin()
                        .userParameter("email")
                        .user(request.getEmail())
                        .password(request.getPassword()))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/products/list"));
    }

    @Test
    @Transactional
    public void 로그인_테스트_실패() throws Exception{
        //given
        MemberLoginRequest request = MemberLoginRequest.builder()
                .email("test@naver.com")
                .password("12346")
                .build();
        //when
        mvc.perform(formLogin()
                        .userParameter("email")
                        .user(request.getEmail()).password(request.getPassword()))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login?error"));
    }

    @WithMockUser
    @Test
    @Transactional
    public void 로그아웃_테스트_성공() throws Exception{
        mvc.perform(post("/logout")
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"))
                .andExpect(unauthenticated());
    }

}
