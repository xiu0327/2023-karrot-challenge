package numble.karrot.interest.service;

import lombok.RequiredArgsConstructor;
import numble.karrot.interest.domain.Interest;
import numble.karrot.interest.repository.InterestRepository;
import numble.karrot.member.domain.Member;
import numble.karrot.product.domain.Product;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class InterestServiceImpl implements InterestService{

    private final InterestRepository interestRepository;


    @Override
    public List<Product> findInterestByMember(Member member) {
        return interestRepository.findInterestsByMember(member)
                .stream()
                .map((interest)-> interest.getProduct())
                .collect(Collectors.toList());
    }

    @Override
    public Interest addInterestList(Interest interest) {
        return interestRepository.create(interest);
    }

    /**
     * 특정 회원 관심 목록에서 하트 취소를 누르는 경우
     * @param interest 하트 취소 대상
     */
    @Override
    public void deleteInterestByMember(Interest interest) {
        interestRepository.delete(interest);
    }

    /**
     * 전체 상품 목록(그외 등등)에서 하트 취소를 누르는 경우
     * @param product 하트 취소를 누른 상품 정보 (상품의 하트수를 감소하기 위해)
     * @param member 하트 취소를 누른 행위자 (회원 관심 목록에서 제거하기 위해)
     */
    @Override
    public void deleteInterestByProductList(Product product, Member member) {
        Interest interest = interestRepository.findInterestByMemberAndProduct(product, member);
        interestRepository.delete(interest);
    }


}
