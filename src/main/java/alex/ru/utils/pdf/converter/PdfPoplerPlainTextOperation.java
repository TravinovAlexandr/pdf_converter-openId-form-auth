package alex.ru.utils.pdf.converter;

import org.springframework.lang.NonNull;

public class PdfPoplerPlainTextOperation implements PdfOperation {

    @Override
    public String exec(@NonNull final String pathToTmpDir, @NonNull final String name) {

        final String path = pathToTmpDir + "/" + name;
        final ProcessExecutor processExecutor = new ProcessExecutor();
        final boolean isExecuted = processExecutor.exec(String.format("/usr/bin/pdftotext %s", path));

        if(isExecuted)
            return path + ".txt";
        else
            return null;
    }
}
