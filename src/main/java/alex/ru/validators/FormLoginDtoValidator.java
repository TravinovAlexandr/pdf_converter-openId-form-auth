package alex.ru.validators;

import alex.ru.annotations.Log;
import alex.ru.dto.FormLoginDto;
import alex.ru.utils.validation.ValidationUtil;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import java.util.regex.Pattern;

@Component("formLoginValidator")
public class FormLoginDtoValidator implements Validator {

    @Log
    private Logger logger;

    private ValidationUtil validationUtil;

    @Override
    public boolean supports(final Class<?> aClass) {
        return aClass.equals(FormLoginDto.class);
    }

    @Override
    public void validate(final Object o, final Errors errors) {

        try {

            final FormLoginDto dto = (FormLoginDto) o;

            dto.username = dto.username.trim();
            dto.password = dto.password.trim();

            if (dto.username.contains("@")) {

                if (validationUtil.isEmail(dto.username))
                    errors.rejectValue("username", "formLoginDto.email.incorrect");

                else if (dto.username.length() < 5)
                    errors.rejectValue("username", "formLoginDto.email.less");

                else if (dto.username.length() > 255)
                    errors.rejectValue("username", "formLoginDto.email.more");

            } else if (!dto.username.equals("")) {

                if (dto.username.length() < 3)
                    errors.rejectValue("username", "formLoginDto.nick.less");

                else if (dto.username.length() > 25)
                    errors.rejectValue("username", "formLoginDto.nick.more");

            } else
                ValidationUtils.rejectIfEmpty(errors, "username", "formLoginDto.username.empty");

            if(dto.password.equals(""))
                ValidationUtils.rejectIfEmpty(errors, "password", "formLoginDto.password.empty");

            else if (dto.password.length() < 3)
                errors.rejectValue("password", "formLoginDto.password.less");

            else if (dto.password.length() > 255)
                errors.rejectValue("password", "formLoginDto.password.more");


        } catch (NullPointerException e) {

            logger.error("FormLoginDto or some field(s) are null.");
            ValidationUtils.rejectIfEmpty(errors, "username", "formLoginDto.username.empty");
            ValidationUtils.rejectIfEmpty(errors, "password", "formLoginDto.password.empty");
        }
    }

    @Autowired
    public void setValidationUtil(ValidationUtil validationUtil) {
        this.validationUtil = validationUtil;
    }
}
