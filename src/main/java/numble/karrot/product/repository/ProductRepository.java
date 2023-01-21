package numble.karrot.product.repository;

import lombok.RequiredArgsConstructor;
import numble.karrot.product.domain.Product;
import numble.karrot.product.domain.ProductStatus;
import numble.karrot.exception.ProductNotFoundException;
import numble.karrot.product_image.domain.ProductImage;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ProductRepository{

    private final EntityManager em;

    public Long save(Product product) {
        em.persist(product);
        return product.getId();
    }

    public Product findProductById(Long id) throws ProductNotFoundException {
        return em.find(Product.class, id);
    }

    public List<Product> findAllProduct() {
        return em.createQuery("select p from Product p", Product.class)
                .getResultList();
    }

    public void removeProduct(Long productId) {
        em.remove(em.find(Product.class, productId));
    }

}
