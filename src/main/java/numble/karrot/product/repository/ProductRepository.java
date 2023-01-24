package numble.karrot.product.repository;

import numble.karrot.interest.domain.Interest;
import numble.karrot.product.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
