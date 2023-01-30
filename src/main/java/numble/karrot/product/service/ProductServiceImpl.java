package numble.karrot.product.service;

import lombok.RequiredArgsConstructor;
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
public class ProductServiceImpl implements ProductService{

    private final ProductRepository productRepository;
    private final ProductImageService productImageService;
    private final MemberRepository memberRepository;

    @Override
    @Transactional
    public Long save(ProductRegisterRequest request, String email) {
        Product product = request.toProductEntity();
        product.addProduct(memberRepository.findByEmail(email).get());
        productRepository.save(product);
        updateThumbnail(getProductImage(request.getProductImages(), product), product.getId());
        return product.getId();
    }

    @Override
    public Product findOne(Long id) {
        return productRepository.findById(id).get();
    }

    @Override
    public List<Product> findAllProducts() {
        return productRepository.findAll();
    }

    @Override
    @Transactional
    public void updateThumbnail(List<ProductImage> images, Long productId) {
        Product product = productRepository.findById(productId).get();
        product.setThumbnail(images);
    }

    @Override
    @Transactional
    public void deleteProduct(Long productId) {
        productRepository.delete(productRepository.findById(productId).get());
    }

    @Override
    @Transactional
    public void updateProduct(Long id, Product product) {
        productRepository.findById(id).get().update(product);
    }

    @Override
    public void updateProductStatus(Long id, ProductStatus status) {
        productRepository.findById(id).get().setStatus(status);
    }

    @Override
    public List<ProductStatus> getChangeableProductStatus(ProductStatus status){
        return stream(ProductStatus.values())
                .filter((item) -> item != status).collect(Collectors.toList());
    }

    @Override
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
