package kr.co.mootravle;

public class DataNotFoundException extends RuntimeException {
    private static final long serialVersionUID=1L;
    public DataNotFoundException(String message) {
        super(message);
    }
}
