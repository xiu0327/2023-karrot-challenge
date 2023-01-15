package numble.karrot.interest.repository;

import numble.karrot.interest.domain.Interest;
import numble.karrot.member.domain.Member;
import numble.karrot.product.domain.Product;

import java.util.List;

/**
 * 1. 특정 회원의 관심 목록 조회
 * 2. 관심 목록 추가
 * 3. 관심 목록 삭제
 * 4. 특정 상품의 관심수 증가
 * 5. 특정 상품의 관심수 감소
 */
public interface InterestRepository {
    List<Interest> findInterestsByMember(Member member);
    Interest findInterestByMemberAndProduct(Long productId, Long memberId);
    Interest create(Interest interest);
    void delete(Interest interest);
    void deleteInterestByProductId(Long productId);
}
