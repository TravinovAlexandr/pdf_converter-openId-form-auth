package alex.ru.utils.pdf.converter;

import alex.ru.dto.MultipartDto;
import org.springframework.lang.NonNull;
import java.io.File;

public interface PdfConverter {

    @NonNull
    File convertFileToFile(@NonNull final MultipartDto multipartDto, final int operationId);

    @NonNull
    byte[] convertFileToByteArray(@NonNull final MultipartDto multipartDto, final int operationId);
}
