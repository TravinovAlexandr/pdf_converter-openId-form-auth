package alex.ru.utils.recaptcha;

import alex.ru.exceptions.ReCaptchaException;

public interface ReCaptchaHandler {

    void processResponse(final String response) throws ReCaptchaException;

    String getReCaptchaSite();

    String getReCaptchaSecret();
}
