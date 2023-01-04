package numble.karrot.product.repository;

import lombok.RequiredArgsConstructor;
import numble.karrot.product.domain.Product;
import numble.karrot.product.domain.ProductStatus;
import numble.karrot.exception.ProductNotFoundException;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ProductRepositoryImpl implements ProductRepository{

    private final EntityManager em;

    @Override
    public Product save(Product product) {
        em.persist(product);
        return product;
    }

    @Override
    public Product findProductById(Long id) throws ProductNotFoundException {
        return em.find(Product.class, id);
    }

    @Override
    public List<Product> findAllProduct() {
        return em.createQuery("select p from Product p", Product.class)
                .getResultList();
    }

    @Override
    public List<Product> findProductByMember(Long memberId) {
        return em.createQuery("select p from Product p where p.seller.id= :memberId", Product.class)
                .setParameter("memberId", memberId)
                .getResultList();
    }

    @Override
    public List<Product> findProductByStatus(Long memberId, ProductStatus status) {
        return em.createQuery("select p from Product p where p.status= :status", Product.class)
                .setParameter("status", status)
                .getResultList();
    }

    @Override
    public void removeProduct(Product product) {
        em.remove(product);
    }

    @Override
    public Product updateProduct(Long id, Product product) {
        Product findProduct = em.find(Product.class, id);
        findProduct.update(product);
        return findProduct;
    }
}
