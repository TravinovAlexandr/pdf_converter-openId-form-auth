package alex.ru.utils.pdf.converter;

import alex.ru.exceptions.UtilException;

public class PdfOperationFactory {

    public final static int POPLER_PDF_TO_TEXT = 0;

    public final static int POPLER_PDF_TO_HTML = 1;

    public PdfOperation getConverter(final int operationId) {

        if(operationId == POPLER_PDF_TO_TEXT)
            return new PdfPoplerPlainTextOperation();

        else if(operationId == POPLER_PDF_TO_HTML)
            return new PdfPoplerHtmlOperation();

        else
            throw new UtilException();
    }
}
