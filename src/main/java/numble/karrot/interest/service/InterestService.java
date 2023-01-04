package numble.karrot.interest.service;

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
public interface InterestService {
    List<Product> findInterestByMember(Member member);
    Interest addInterestList(Interest interest);
    void deleteInterestByMember(Interest interest);
    void deleteInterestByProductList(Product product, Member member);
}
