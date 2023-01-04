package numble.karrot.exception;

public class ProductImageNotFound extends RuntimeException {
    public ProductImageNotFound() {
        super("상품 이미지를 반드시 한개 이상 추가해주세요.");
    }
}
