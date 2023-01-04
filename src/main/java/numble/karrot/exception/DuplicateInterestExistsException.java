package numble.karrot.exception;

public class DuplicateInterestExistsException extends RuntimeException{
    public DuplicateInterestExistsException() {
        super("이미 관심 목록에 존재하는 상품입니다.");
    }
}
