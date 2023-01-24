package numble.karrot.interest.service;

import lombok.RequiredArgsConstructor;
import numble.karrot.exception.DuplicateInterestExistsException;
import numble.karrot.exception.InterestNotFoundException;
import numble.karrot.interest.domain.Interest;
import numble.karrot.interest.repository.InterestRepository;
import numble.karrot.member.domain.Member;
import numble.karrot.member.repository.MemberRepository;
import numble.karrot.member.service.MemberService;
import numble.karrot.product.domain.Product;
import numble.karrot.product.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class InterestService{

    private final InterestRepository interestRepository;
    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;

    @Transactional
    public Long addInterestList(String email, Long productId) {
        Member member = memberRepository.findByEmail(email).get();
        checkInterestDuplicate(member.getId(), productId);
        Interest interest = Interest.builder()
                .member(member)
                .product(productRepository.findById(productId).get()).build();
        interestRepository.save(interest);
        return interest.getId();
    }

    @Transactional
    public void deleteInterestByProductList(String email, Long productId) {
        Member member = memberRepository.findByEmail(email).get();
        Interest interest = interestRepository.findByMemberIdAndProductId(member.getId(), productId)
                .orElseThrow(() -> {throw new InterestNotFoundException();});
        interest.reduceProductInterestCount();
        interestRepository.delete(interest);
    }

    public void checkInterestDuplicate(Long memberId, Long productId){
        if(interestRepository.findByMemberIdAndProductId(memberId, productId).isPresent())
            throw new DuplicateInterestExistsException();
    }

}
