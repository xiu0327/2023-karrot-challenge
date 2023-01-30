package numble.karrot.interest.service;

public interface InterestService {
    Long addInterestList(String email, Long productId);
    void deleteInterestByProductList(String email, Long productId);
    void checkInterestDuplicate(Long memberId, Long productId);
}
