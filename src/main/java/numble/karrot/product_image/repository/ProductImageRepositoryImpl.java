package numble.karrot.product_image.repository;

import lombok.RequiredArgsConstructor;
import numble.karrot.product.domain.Product;
import numble.karrot.product_image.domain.ProductImage;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ProductImageRepositoryImpl implements ProductImageRepository{

    private final EntityManager em;

    @Override
    public ProductImage create(ProductImage productImage) {
        em.persist(productImage);
        return productImage;
    }

    @Override
    public List<ProductImage> findProductImageList(Product product) {
        return em.createQuery("select pi from ProductImage pi where pi.product= :product", ProductImage.class)
                .setParameter("product", product)
                .getResultList();
    }

    @Override
    public void removeProductImage(Long id) {
        ProductImage productImage = em.find(ProductImage.class, id);
        em.remove(productImage);
    }
}
