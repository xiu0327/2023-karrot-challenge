package numble.karrot.controller;

import numble.karrot.chat.domain.ChatRoom;
import numble.karrot.chat.service.ChattingService;
import numble.karrot.member.domain.Member;
import numble.karrot.member.service.MemberService;
import numble.karrot.product.domain.Product;
import numble.karrot.product.service.ProductService;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@WithMockUser(username = "test2@naver.com", password = "123", roles = "USER")
class ChatRoomControllerTest {

    @Autowired
    ChattingService chattingService;

    @Autowired
    ProductService productService;

    @Autowired
    MemberService memberService;

    @Autowired
    MockMvc mvc;

    @Autowired
    WebApplicationContext context;

    @Before
    public void setting(){
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    void enterPage() throws Exception {
        // given
        Product product = productService.findProductDetails(1L);
        Member buyer = memberService.findMember("test2@naver.com");
        String roomName = ChatRoom.builder().build().getName();
        // when
        mvc.perform(get("/chat/room?roomName=" + roomName + "&productId=" + product.getId()))
                .andDo(print())
                .andExpect(status().is2xxSuccessful());
    }
}