package numble.karrot.controller;

import numble.karrot.chat.domain.ChatRoom;
import numble.karrot.chat.service.ChattingService;
import numble.karrot.member.domain.Member;
import numble.karrot.member.dto.MemberJoinRequest;
import numble.karrot.member.service.MemberService;
import numble.karrot.product.domain.Product;
import numble.karrot.product.domain.ProductCategory;
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

@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@WithMockUser(username = "test2@naver.com", password = "123", roles = "USER")
class ChatRoomControllerTest {

    @Autowired
    ChattingService chattingService;

    @Autowired
    ProductServiceImpl productService;

    @Autowired
    MemberService memberService;

    @Autowired EntityManager em;

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
    @Transactional
    void enterPage() throws Exception {
        // given
        Long productId = createProduct();
        String roomName = new ChatRoom().getName();
        em.flush();
        // when
        mvc.perform(get("/chat/room?roomName=" + roomName + "&productId=" + productId))
                .andDo(print())
                .andExpect(status().is2xxSuccessful());
    }
}