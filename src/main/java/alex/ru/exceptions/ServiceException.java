package alex.ru.exceptions;

public class ServiceException extends RuntimeException {

    public ServiceException() {
        super();
    }

    public ServiceException(final String s) {
        super(s);
    }

    public ServiceException(final String s,final Throwable throwable) {
        super(s, throwable);
    }

    public ServiceException(final Throwable throwable) {
        super(throwable);
    }
}
