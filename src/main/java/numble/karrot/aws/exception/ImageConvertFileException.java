package numble.karrot.aws.exception;

public class ImageConvertFileException extends RuntimeException{
    public ImageConvertFileException() {
        super("파일 전환 실패");
    }
}
