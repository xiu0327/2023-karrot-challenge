package numble.karrot.product_image.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import numble.karrot.image.ImageStorageFolderName;
import numble.karrot.product.domain.Product;

import javax.persistence.*;

@Entity
@Table(name = "product_image")
@Getter
@NoArgsConstructor
public class ProductImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "url")
    private String url;

    @Column(name = "filename")
    private String fileName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @Builder
    public ProductImage(String url, String fileName, Product product) {
        this.url = url;
        this.fileName = fileName;
        this.product = product;
        product.getProductImages().add(this);
    }

    /* 연관관계 편의 메서드 */
    public void remove(){
        product.getProductImages().remove(this);
    }
}
