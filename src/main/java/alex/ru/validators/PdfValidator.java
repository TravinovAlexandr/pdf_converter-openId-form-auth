package alex.ru.validators;

import alex.ru.exceptions.ReCaptchaException;
import alex.ru.exceptions.UtilException;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

@Component
public class PdfValidator {

    public void validate(final Exception e, final Errors errors) {

        if(e.getClass().equals(ReCaptchaException.class)) {

            if (e.getMessage().equals("block")) {
                errors.rejectValue("multipartFile", "recaptcha.block");

            } else if (e.getMessage().equals("mark")) {
                errors.rejectValue("multipartFile", "recaptcha.mark");

            } else if (e.getMessage().equals("exception")) {
                errors.rejectValue("multipartFile", "recaptcha.exception");
            }

        } else if(e.getClass().equals(UtilException.class)) {
            errors.rejectValue("multipartFile", "pdf.error");
        }
    }
}
