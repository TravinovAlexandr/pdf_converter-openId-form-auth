package alex.ru.utils.pdf.converter;

import alex.ru.dto.MultipartDto;
import alex.ru.exceptions.UtilException;
import alex.ru.utils.pdf.livecycle.LifeCycleController;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class PdfCommonConverter implements PdfConverter {

    @NonNull
    private LifeCycleController pdfLifeCycleController;

    @NonNull
    private String pathToTempDir;

    private final static Logger logger = LoggerFactory.getLogger(PdfCommonConverter.class.getSimpleName());

    @Override @NonNull
    public File convertFileToFile(@NonNull final MultipartDto multipartDto,final int operationId) {
        try {
            final PdfOperation converter = new PdfOperationFactory().getConverter(operationId);

            boolean isWrote = pdfLifeCycleController.write(multipartDto, pathToTempDir);

            if (!isWrote)
                throw new UtilException("notWrote");

            final String name = converter.exec(pathToTempDir, multipartDto.getName());

            if(name == null)
                throw new UtilException("null");

            final Path path = Paths.get(name);

            if (Files.exists(path)) {
                return path.toFile();

            } else
                throw new UtilException("notFounded");

        } catch (RuntimeException e) {
            throw new UtilException();
        }
    }

    @Override @NonNull
    public byte[] convertFileToByteArray(@NonNull final MultipartDto multipartDto, final int operationId) {
        try {
            final File resultFile =  convertFileToFile(multipartDto, operationId);
            return IOUtils.toByteArray(new FileInputStream(resultFile));

        } catch (IOException e) {
            return null;
        }
    }

    public void setPathToTempDir(final String pathToTempDir) {

        final Path path = Paths.get(pathToTempDir);

        if(!Files.exists(path) && Files.isWritable(path.getParent())) {
            try {
                Files.createDirectory(path);
            } catch (IOException e) {
                logger.error("Can not create directory. Parent directory is not writable: " + path.getParent());
                System.exit(1);
                e.printStackTrace();
            }
        }
        this.pathToTempDir = pathToTempDir;
    }

    public void setPdfLifeCycleController(@NonNull final LifeCycleController pdfLifeCycleController) {
        this.pdfLifeCycleController = pdfLifeCycleController;
    }
}
