package alex.ru.utils.pdf.converter;

import alex.ru.exceptions.UtilException;
import org.springframework.lang.NonNull;

public class PdfConverterFactory {

    public enum Factory {
        COMMON
    }

    public PdfConverter pdfConverter(@NonNull final Factory factory) {
        if(factory.equals(Factory.COMMON)) {
            return new PdfCommonConverter();
        }
        throw new UtilException();
    }
}
