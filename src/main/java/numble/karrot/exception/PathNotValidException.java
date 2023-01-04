package numble.karrot.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PathNotValidException extends RuntimeException{
    public PathNotValidException() {
        super("URL 경로가 잘못되었습니다.");
        log.info("This path is not valid");
    }
}
