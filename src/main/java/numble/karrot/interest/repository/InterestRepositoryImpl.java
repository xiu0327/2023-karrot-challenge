package numble.karrot.interest.repository;

import lombok.RequiredArgsConstructor;
import numble.karrot.chat.domain.ChatRoom;
import numble.karrot.exception.DuplicateInterestExistsException;
import numble.karrot.exception.InterestNotFoundException;
import numble.karrot.exception.ProductNotFoundException;
import numble.karrot.interest.domain.Interest;
import numble.karrot.member.domain.Member;
import numble.karrot.product.domain.Product;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

/**
 * 1. 특정 회원의 관심 목록 조회
 * 2. 관심 목록 추가
 * 3. 관심 목록 삭제
 * 4. 특정 상품의 관심수 증가
 * 5. 특정 상품의 관심수 감소
 */
@Repository
@RequiredArgsConstructor
public class InterestRepositoryImpl implements InterestRepository{

    private final EntityManager em;

    @Override
    public List<Interest> findInterestsByMember(Member member) {
        return em.createQuery("select i from Interest i where i.member.id= :memberId", Interest.class)
                .setParameter("memberId", member.getId())
                .getResultList();
    }

    @Override
    public Interest findInterestByMemberAndProduct(Long productId, Long memberId) {
        return em.createQuery("select i from Interest i where i.member.id= :memberId and i.product.id= :productId", Interest.class)
                .setParameter("memberId", memberId)
                .setParameter("productId", productId)
                .getResultList().stream().findAny()
                .orElseThrow(() -> new InterestNotFoundException());
    }

    @Override
    public Interest create(Interest interest) {
        checkDuplicate(interest);
        em.persist(interest);
        interest.increaseProductInterestCount();
        return interest;
    }

    @Override
    public void delete(Interest interest) {
        em.remove(interest);
    }

    @Override
    public void deleteInterestByProductId(Long productId) {
        em.remove(
                em.createQuery("select r from ChatRoom r where r.product.id= :productId", ChatRoom.class)
                        .setParameter("productId", productId)
                        .getResultList().stream().findAny().orElseThrow(
                                () -> {throw new ProductNotFoundException();
                                }
                        )
        );
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
