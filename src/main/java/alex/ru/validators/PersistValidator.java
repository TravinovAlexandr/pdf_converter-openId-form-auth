package alex.ru.validators;

import alex.ru.dao.UserDao;
import alex.ru.dto.PersistDto;
import alex.ru.exceptions.ServiceException;
import alex.ru.utils.validation.ValidationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class PersistValidator implements Validator {

    private UserDao userDao;

    private ValidationUtil validationUtil;

    @Autowired
    public PersistValidator(final UserDao userDao, final ValidationUtil validationUtil) {
        this.userDao = userDao;
        this.validationUtil = validationUtil;
    }

    @Override
    public boolean supports(final Class<?> aClass) {
        return PersistDto.class.equals(aClass);
    }

    @Override
    public void validate(@Nullable final Object o,final Errors errors) {
        try {
            final PersistDto persistDto = (PersistDto) o;

            final boolean isCorrectNick = validationUtil.isCorrectNick(persistDto.nick);

            final boolean isCorrectPassword = validationUtil.isCorrectPassword(persistDto.password);

            final boolean isEmailCorrect = validationUtil.isEmail(persistDto.email);

            boolean isUserExistByNick = userDao.isExistByNick(persistDto.nick);

            if (isUserExistByNick)
                errors.rejectValue("nick", "persist.nick.exist");

            else if(!isCorrectNick)
                errors.rejectValue("nick", "persist.nick.incorrect");

            else if(persistDto.nick.length() < 3  || persistDto.nick.length() > 25)
                errors.rejectValue("nick", "persist.nick.size");

            if(!isCorrectPassword)
                errors.rejectValue("password", "persist.password.incorrect");

            else if(persistDto.password.length() < 5  || persistDto.password.length() > 255)
                errors.rejectValue("password", "persist.password.size");

            if(!isEmailCorrect)
                errors.rejectValue("email", "persist.email.incorrect");

            else if(persistDto.email.length() < 5  || persistDto.email.length() > 255)
                errors.rejectValue("email", "persist.email.size");

            if(persistDto.name != null) {
                if( persistDto.name.length() < 3  || persistDto.name.length() > 50)
                    errors.rejectValue("name", "persist.name.size");
            }

            if(persistDto.lastName != null) {
                if( persistDto.lastName.length() < 3  || persistDto.lastName.length() > 50)
                    errors.rejectValue("lastName", "persist.lastName.size");
            }

        } catch (NullPointerException | ServiceException e) {

            ValidationUtils.rejectIfEmptyOrWhitespace(errors,"nick", "persist.nick.empty");
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "persist.password.empty");
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", "persist.email.empty");

        }
    }
}
