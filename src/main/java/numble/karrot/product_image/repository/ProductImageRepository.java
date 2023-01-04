package numble.karrot.product_image.repository;

import numble.karrot.product.domain.Product;
import numble.karrot.product_image.domain.ProductImage;

import java.util.List;

/**
 * 1. 상품 이미지 DB 등록
 * 2. 상품 이미지 조회
 * 3. 상품 이미지 삭제
 * */


public interface ProductImageRepository {
    ProductImage create(ProductImage productImage);
    List<ProductImage> findProductImageList(Product product);
    void removeProductImage(Long id);
}
