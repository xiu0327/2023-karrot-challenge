package numble.karrot.product.service;

import numble.karrot.product.domain.Product;
import numble.karrot.product.domain.ProductStatus;
import numble.karrot.product.dto.ProductRegisterRequest;
import numble.karrot.product_image.domain.ProductImage;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductService {
    Long save(ProductRegisterRequest request, String email);
    Product findOne(Long id);
    List<Product> findAllProducts();
    void updateThumbnail(List<ProductImage> images, Long productId);
    void deleteProduct(Long productId);
    void updateProduct(Long id, Product product);
    void updateProductStatus(Long id, ProductStatus status);
    List<ProductStatus> getChangeableProductStatus(ProductStatus status);
    List<ProductImage> getProductImage(List<MultipartFile> productImages, Product product);
}
