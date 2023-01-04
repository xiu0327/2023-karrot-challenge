package numble.karrot.product_image.service;

import numble.karrot.product.domain.Product;
import numble.karrot.product.service.ProductService;
import numble.karrot.product_image.domain.ProductImage;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ProductImageServiceImplTest {
    
    @Autowired
    ProductImageService productImageService;
    
    @Autowired
    ProductService productService;

    @Test
    void findProductImages() {
        //given
        Product product = productService.findAllProducts().get(0);
        //when
        List<ProductImage> productImages = productImageService.findProductImages(product);
        product.setProductImages(productImages);
        //then
        assertThat(product.getProductImages()).isNotNull();
    }
}