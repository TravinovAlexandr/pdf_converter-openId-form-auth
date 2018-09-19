package alex.ru.exceptions;

public class UtilException extends RuntimeException {

    public UtilException() {
        super();
    }

    public UtilException(final String s) {
        super(s);
    }

    public UtilException(final String s,final Throwable throwable) {
        super(s, throwable);
    }

    public UtilException(final Throwable throwable) {
        super(throwable);
    }
}
