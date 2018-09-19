package alex.ru.utils.pdf.converter;

import org.springframework.lang.NonNull;

public interface PdfOperation {

    String exec(@NonNull final String pathToTmpDir, @NonNull final String fileName);
}
