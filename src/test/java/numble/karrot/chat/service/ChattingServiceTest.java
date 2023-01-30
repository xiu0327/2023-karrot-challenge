package numble.karrot.chat.service;

import numble.karrot.chat.domain.Chat;
import numble.karrot.chat.domain.ChatRoom;
import numble.karrot.chat.domain.ChatType;
import numble.karrot.chat.dto.ChatDto;
import numble.karrot.member.domain.Member;
import numble.karrot.member.dto.MemberJoinRequest;
import numble.karrot.member.service.MemberService;
import numble.karrot.product.domain.Product;
import numble.karrot.product.domain.ProductCategory;
import numble.karrot.product.dto.ProductRegisterRequest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ChattingServiceTest {

    @Autowired MemberService memberService;
    @Autowired ChattingService chattingService;
    @Autowired EntityManager em;


    @Test
    @Transactional
    void 채팅_생성(){
        Member member = em.find(Member.class, createMember());
        ChatRoom room = chattingService.findChatRoomByName(member, "", createProduct());
        Long chatId = chattingService.saveChat(ChatDto.builder()
                .roomId(room.getId())
                .nickname(member.getNickName())
                .profile(member.getProfile())
                .type(ChatType.MESSAGE)
                .content("메시지").build());
        Chat newChat = em.find(Chat.class, chatId);
        newChat.getChatRoom().getChats().add(newChat);
        List<ChatRoom> chatList = chattingService.findChatRoomByMember(member.getEmail());
        List<Chat> chats = chatList.stream().map(i -> i.getLastChat()).collect(Collectors.toList());
        Assertions.assertThat(room.getChats().size()).isEqualTo(1);
        Assertions.assertThat(room.getLastChat().getContent()).isEqualTo("메시지");
    }

    @Test
    @Transactional
    void findChatRoomByMember(){
        List<ChatRoom> chatRoomByMember = chattingService.findChatRoomByMember("test@naver.com");
        chatRoomByMember.get(0).getProduct().getId();
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

}