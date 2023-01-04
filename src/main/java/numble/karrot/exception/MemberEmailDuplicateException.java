package numble.karrot.exception;

public class MemberEmailDuplicateException extends RuntimeException{
    public MemberEmailDuplicateException() {
        super("중복된 이메일입니다. 다른 이메일을 사용해주세요.");
    }
}
