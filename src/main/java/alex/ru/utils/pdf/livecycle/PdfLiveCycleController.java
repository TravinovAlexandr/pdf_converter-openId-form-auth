package alex.ru.utils.pdf.livecycle;

import alex.ru.annotations.Log;
import alex.ru.dto.MultipartDto;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.springframework.lang.NonNull;
import org.springframework.scheduling.annotation.Scheduled;
import java.io.*;
import java.time.LocalTime;

public class PdfLiveCycleController implements LifeCycleController {

    @Log
    private Logger logger;

    private String pathToTempDir;

    @Override
    public boolean write(@NonNull final MultipartDto dto, @NonNull final String pathToTempDir) {

        try(BufferedOutputStream outputStream = new BufferedOutputStream(
                new FileOutputStream(
                        new File(pathToTempDir, dto.getName())))) {

            outputStream.write(dto.getMultipartFile().getBytes());
            outputStream.flush();
            return true;

        } catch (NullPointerException | IOException e) {

            if(e.equals(NullPointerException.class)) {
                logger.error("Argument(s) == null.");
            }
            else
                logger.error("Has no permissions.");

            return false;
        }
    }

    @Override
    @Scheduled(fixedRate = 1000 * 60 * 60 * 4)
    //server start  (24 <= start < 1)
    public void remove() {

        final LocalTime localTime = LocalTime.now();

        if(localTime.getHour() >= 4 && localTime.getHour() <= 5) {

            final File temp = new File(pathToTempDir);

            try {
                final File[] allTempFiles = temp.listFiles();
                for (File file : allTempFiles) {
                    if (file.isDirectory()) {
                        FileUtils.deleteDirectory(file);
                    }
                    file.delete();
                }
            } catch (NullPointerException | IOException e) {
                if(e.getClass().equals(NullPointerException.class)) {
                    logger.error("Parent dirrectory was not founded.");
                }
                else
                    logger.error("Has no permissions to delite.");
            }
        }
    }

    public void setPathToTempDir(final String pathToTempDir) {
        this.pathToTempDir = pathToTempDir;
    }
}
