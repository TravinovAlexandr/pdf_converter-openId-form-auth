package alex.ru.validators;

import alex.ru.dto.MultipartDto;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component("multipartPdfValidator")
public class MultipartDtoValidator implements Validator {

    @Override
    public boolean supports(final Class<?> aClass) {
        return aClass.equals(MultipartDto.class);
    }

    @Override
    public void validate(@Nullable final Object o, final Errors errors) {
        try {
            final MultipartDto dto = (MultipartDto) o;

            if(dto.multipartFile.isEmpty()) {
                errors.rejectValue("multipartFile", "pdf.empty");
            }
            else if(!dto.multipartFile.getContentType().equals("application/pdf")) {
                errors.rejectValue("multipartFile", "pdf.contentType");
            }

        } catch (NullPointerException e) {
            ValidationUtils.rejectIfEmpty(errors,"multipartFile", "pdf.null");
        }
    }
}
