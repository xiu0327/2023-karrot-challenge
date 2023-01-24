package numble.karrot.interest.repository;

import numble.karrot.interest.domain.Interest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface InterestRepository extends JpaRepository<Interest, Long> {
    Optional<Interest> findByMemberIdAndProductId(Long memberId, Long productId);
}
