package numble.karrot.product.service;

import lombok.RequiredArgsConstructor;
import numble.karrot.member.domain.Member;
import numble.karrot.member.domain.MemberImageInit;
import numble.karrot.product.domain.Product;
import numble.karrot.product.domain.ProductStatus;
import numble.karrot.product.repository.ProductRepository;
import numble.karrot.product_image.domain.ProductImage;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductServiceImpl implements ProductService{

    private final ProductRepository productRepository;

    @Override
    public Product save(Product product) {
        return productRepository.save(product);
    }

    @Override
    public Product findProductDetails(Long id) {
        return productRepository.findProductById(id);
    }

    @Override
    public List<Product> findProductsByStatus(Long memberId, ProductStatus status) {
        return productRepository.findProductsByStatus(memberId, status);
    }

    @Override
    public List<Product> findProductsByMember(Long memberId) {
        return productRepository.findProductsByMember(memberId);
    }

    @Override
    public Product updateThumbnail(List<ProductImage> images, Long productId) {
        return productRepository.updateThumbnail(images, productId);
    }

    @Override
    public List<Product> findAllProducts() {
        return productRepository.findAllProduct();
    }

    @Override
    public void deleteProduct(Long productId) {
        productRepository.removeProduct(productId);
    }

    @Override
    public Product updateProduct(Long id, Product product) {
        return productRepository.updateProduct(id, product);
    }

    @Override
    public Product updateProductStatus(Long id, ProductStatus status) {
        return productRepository.updateProductStatus(id, status);
    }
}
