package numble.karrot.product.service;


import numble.karrot.product.domain.Product;
import numble.karrot.product.domain.ProductStatus;
import numble.karrot.product_image.domain.ProductImage;

import java.util.List;

/**
 * 1. 상품 등록
 * 2. 상품 상세 정보 조회
 * 3. 전체 상품 목록 조회
 * 4. 특정 회원이 올린 상품 전체 조회
 * 5. 특정 회원이 올린 상품 판매 상태별 조회
 * 6. 상품 삭제
 * 7. 상품 수정
 * */
public interface ProductService {
    Product save(Product product);
    Product findProductDetails(Long id);
    List<Product> findAllProducts();
    void deleteProduct(Long productId);
    Product updateProduct(Long id, Product product);
    Product updateProductStatus(Long id, ProductStatus status);
    List<Product> findProductsByStatus(Long memberId, ProductStatus status);
    List<Product> findProductsByMember(Long memberId);
    Product updateThumbnail(List<ProductImage> images, Long productId);
}
