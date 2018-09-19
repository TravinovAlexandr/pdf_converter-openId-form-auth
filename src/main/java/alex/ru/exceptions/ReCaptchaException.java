package alex.ru.exceptions;

public class ReCaptchaException extends RuntimeException {

    public ReCaptchaException() {
        super();
    }

    public ReCaptchaException(final String s) {
        super(s);
    }

    public ReCaptchaException(final String s, final Throwable throwable) {
        super(s, throwable);
    }

    public ReCaptchaException(final Throwable throwable) {
        super(throwable);
    }
}
