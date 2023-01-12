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
    public void removeProduct(Product product) {
        em.remove(product);
    }

    @Override
    public Product updateProduct(Long id, Product product) {
        Product findProduct = em.find(Product.class, id);
        findProduct.update(product);
        return findProduct;
    }

    @Override
    public Product updateProductStatus(Long id, ProductStatus status) {
        Product findProduct = em.find(Product.class, id);
        findProduct.setStatus(status);
        return findProduct;
    }

    @Override
    public List<Product> findProductsByStatus(Long memberId, ProductStatus status) {
        return em.createQuery("select p from Product p where p.seller.id= :memberId and p.status= :status", Product.class)
                .setParameter("memberId", memberId)
                .setParameter("status", status)
                .getResultList();
    }

    @Override
    public List<Product> findProductsByMember(Long memberId) {
        return em.createQuery("select p from Product p where p.seller.id= :memberId", Product.class)
                .setParameter("memberId", memberId)
                .getResultList();
    }
}
