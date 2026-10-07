package microbank.util;

public class BusinessException extends RuntimeException {
    public BusinessException(String message) {
        super(message);
    }
}