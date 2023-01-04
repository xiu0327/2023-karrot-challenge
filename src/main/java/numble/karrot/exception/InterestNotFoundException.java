package numble.karrot.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class InterestNotFoundException extends RuntimeException{
    public InterestNotFoundException() {
        super("찾으시는 관심 정보가 없습니다.");
        log.info("Failed to cancel the heart.");
    }
}
