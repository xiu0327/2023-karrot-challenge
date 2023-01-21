package numble.karrot.product_image.service;

import lombok.RequiredArgsConstructor;
import numble.karrot.aws.S3Uploader;
import numble.karrot.image.ImageStorageFolderName;
import numble.karrot.product.domain.Product;
import numble.karrot.product_image.domain.ProductImage;
import numble.karrot.product_image.repository.ProductImageRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductImageService{

    private final ProductImageRepository productImageRepository;
    private final S3Uploader s3Uploader;


    public ProductImage save(ProductImage productImage) {
        productImageRepository.create(productImage);
        return productImage;
    }

    @Transactional(readOnly = true)
    public List<ProductImage> findProductImages(Product product) {
        return productImageRepository.findProductImageList(product);
    }

    @Transactional
    public ProductImage updateProductImage(ProductImage oldProductImage, ProductImage newProductImage) {
        productImageRepository.removeProductImage(oldProductImage.getId());
        productImageRepository.create(newProductImage);
        return newProductImage;
    }

    @Transactional
    public void deleteProductImage(List<ProductImage> productImages) {
        for (ProductImage image : productImages) {
            productImageRepository.removeProductImage(image.getId());
        }
    }

    public ProductImage convert(MultipartFile multipartFile, Product product) throws IOException{
        String url = s3Uploader.getImageUrl(multipartFile, ImageStorageFolderName.PRODUCT_IMAGE_PATH);
        return ProductImage.builder()
                .url(url)
                .fileName(s3Uploader.getFileName(url))
                .product(product)
                .build();
    }
}
