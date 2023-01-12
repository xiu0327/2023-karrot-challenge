package numble.karrot.product.repository;

import numble.karrot.product.domain.Product;
import numble.karrot.product.domain.ProductStatus;

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
public interface ProductRepository {
    Product save(Product product);
    Product findProductById(Long id);
    List<Product> findAllProduct();

    void removeProduct(Product product);
    Product updateProduct(Long id, Product product);
    Product updateProductStatus(Long id, ProductStatus status);
    List<Product> findProductsByStatus(Long memberId, ProductStatus status);
    List<Product> findProductsByMember(Long memberId);
}
