package numble.karrot.product_image.service;

import numble.karrot.product.domain.Product;
import numble.karrot.product_image.domain.ProductImage;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ProductImageService {
    ProductImage save(ProductImage productImage);
    ProductImage convert(MultipartFile multipartFile, Product product) throws IOException;
}
