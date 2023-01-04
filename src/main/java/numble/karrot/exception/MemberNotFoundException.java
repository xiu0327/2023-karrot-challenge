package numble.karrot.exception;

public class MemberNotFoundException extends RuntimeException{
    public MemberNotFoundException() {
        super("회원을 찾지 못했습니다.");
    }
}
