package numble.karrot.product.service;

import lombok.RequiredArgsConstructor;
import numble.karrot.member.domain.Member;
import numble.karrot.member.repository.MemberRepository;
import numble.karrot.product.domain.Product;
import numble.karrot.product.domain.ProductStatus;
import numble.karrot.product.dto.ProductRegisterRequest;
import numble.karrot.product.repository.ProductRepository;
import numble.karrot.product_image.domain.ProductImage;
import numble.karrot.product_image.service.ProductImageService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService{

    private final ProductRepository productRepository;
    private final ProductImageService productImageService;
    private final MemberRepository memberRepository;

    @Transactional
    public Long save(ProductRegisterRequest request, String email) {
        Product product = request.toProductEntity();
        product.addProduct(memberRepository.findMemberByEmail(email));
        productRepository.save(product);
        updateThumbnail(getProductImage(request.getProductImages(), product), product.getId());
        return product.getId();
    }

    public Product findOne(Long id) {
        return productRepository.findProductById(id);
    }

    public List<Product> findAllProducts() {
        return productRepository.findAllProduct();
    }

    @Transactional
    public void updateThumbnail(List<ProductImage> images, Long productId) {
        Product product = productRepository.findProductById(productId);
        product.setThumbnail(images);
    }

    @Transactional
    public void deleteProduct(Long productId) {
        productRepository.removeProduct(productId);
    }

    @Transactional
    public void updateProduct(Long id, Product product) {
        productRepository.findProductById(id).update(product);
    }

    public void updateProductStatus(Long id, ProductStatus status) {
        productRepository.findProductById(id).setStatus(status);
    }

    public List<ProductStatus> getChangeableProductStatus(ProductStatus status){
        return stream(ProductStatus.values())
                .filter((item) -> item != status).collect(Collectors.toList());
    }

    public List<ProductImage> getProductImage(List<MultipartFile> productImages, Product product){
        return productImages.stream()
                .map((image) -> {
                    try {
                        return productImageService.save(productImageService.convert(image, product));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.toList());
    }
}
