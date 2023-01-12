package numble.karrot.product_image.service;

import numble.karrot.product.domain.Product;
import numble.karrot.product_image.domain.ProductImage;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ProductImageService {
    /**
     * 1. 상품 이미지 DB 저장
     * 2. 상품 이미지 수정 -> 기존 이미지 제거 후, 새로운 이미지 추가
     * 3. 상품 이미지 삭제 -> 기존 이미지 제거
     * 4. 상품 이미지 조회
     * */
    ProductImage save(ProductImage productImage);
    List<ProductImage> findProductImages(Product product);
    ProductImage updateProductImage(ProductImage oldProductImage, ProductImage newProductImage);
    void deleteProductImage(List<ProductImage> productImages);
    ProductImage convert(MultipartFile multipartFile, Product product) throws IOException;
}
