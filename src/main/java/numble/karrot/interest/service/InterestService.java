package numble.karrot.interest.service;

import lombok.RequiredArgsConstructor;
import numble.karrot.interest.domain.Interest;
import numble.karrot.interest.repository.InterestRepository;
import numble.karrot.member.domain.Member;
import numble.karrot.member.repository.MemberRepository;
import numble.karrot.product.domain.Product;
import numble.karrot.product.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InterestService{

    private final InterestRepository interestRepository;
    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;

    @Transactional
    public Long addInterestList(String email, Long productId) {
        Member member = memberRepository.findMemberByEmail(email);
        Product product = productRepository.findProductById(productId);
        return interestRepository.create(Interest.builder()
                .member(member)
                .product(product).build());
    }

    @Transactional
    public void deleteInterestByProductList(String email, Long productId) {
        Member member = memberRepository.findMemberByEmail(email);
        interestRepository.delete(interestRepository.findInterestByMemberAndProduct(member.getId(), productId));
    }



}
