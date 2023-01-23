package numble.karrot.interest.repository;

import lombok.RequiredArgsConstructor;
import numble.karrot.chat.domain.ChatRoom;
import numble.karrot.exception.DuplicateInterestExistsException;
import numble.karrot.exception.InterestNotFoundException;
import numble.karrot.exception.ProductNotFoundException;
import numble.karrot.interest.domain.Interest;
import numble.karrot.member.domain.Member;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

/**
 * 1. 특정 회원의 관심 목록 조회
 * 2. 관심 목록 추가
 * 3. 관심 목록 삭제
 * 4. 특정 상품의 관심수 증가
 * 5. 특정 상품의 관심수 감소
 */
@Repository
@RequiredArgsConstructor
public class InterestRepository{

    private final EntityManager em;

    public List<Interest> findInterestsByMember(Long memberId) {
        return em.createQuery("select i from Interest i where i.member.id= :memberId", Interest.class)
                .setParameter("memberId", memberId)
                .getResultList();
    }

    public Interest findInterestByMemberAndProduct(Long memberId, Long productId) {
        return em.createQuery("select i from Interest i where i.member.id= :memberId and i.product.id= :productId", Interest.class)
                .setParameter("memberId", memberId)
                .setParameter("productId", productId)
                .getResultList().stream().findAny()
                .orElseThrow(() -> new InterestNotFoundException());
    }

    public Long create(Interest interest) {
        checkDuplicate(interest);
        em.persist(interest);
        return interest.getId();
    }

    public void delete(Interest interest) {
        interest.reduceProductInterestCount();
        em.remove(interest);
    }


    private void checkDuplicate(Interest interest){
        em.createQuery("select i from Interest i where i.member.id= :memberId and i.product.id= :productId", Interest.class)
                .setParameter("memberId", interest.getMember().getId())
                .setParameter("productId", interest.getProduct().getId())
                .getResultList().stream().findAny()
                .ifPresent(result -> {
                    throw new DuplicateInterestExistsException();
                });
    }
}
