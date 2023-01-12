package numble.karrot.product_image.service;

import lombok.RequiredArgsConstructor;
import numble.karrot.aws.S3Uploader;
import numble.karrot.image.ImageStorageFolderName;
import numble.karrot.product.domain.Product;
import numble.karrot.product.service.ProductService;
import numble.karrot.product_image.domain.ProductImage;
import numble.karrot.product_image.repository.ProductImageRepository;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductImageServiceImpl implements ProductImageService{

    private final ProductImageRepository productImageRepository;
    private final S3Uploader s3Uploader;


    @Override
    public ProductImage save(ProductImage productImage) {
        productImageRepository.create(productImage);
        return productImage;
    }

    @Override
    public List<ProductImage> findProductImages(Product product) {
        return productImageRepository.findProductImageList(product);
    }

    /**
     * DB에 있는 기존 이미지 삭제 (S3에는 이미 삭제된 상태) -> DB에 새로운 이미지 추가 (S3에는 이미 저장된 상태)
     * */
    @Override
    public ProductImage updateProductImage(ProductImage oldProductImage, ProductImage newProductImage) {
        productImageRepository.removeProductImage(oldProductImage.getId());
        productImageRepository.create(newProductImage);
        return newProductImage;
    }

    @Override
    public void deleteProductImage(List<ProductImage> productImages) {
        for (ProductImage image : productImages) {
            productImageRepository.removeProductImage(image.getId());
        }
    }


    @Override
    public ProductImage convert(MultipartFile multipartFile, Product product) throws IOException{
        String url = s3Uploader.getImageUrl(multipartFile, ImageStorageFolderName.PRODUCT_IMAGE_PATH);
        return ProductImage.builder()
                .url(url)
                .fileName(s3Uploader.getFileName(url))
                .product(product)
                .build();
    }
}
