package alex.ru.utils.pdf.converter;

import org.springframework.lang.NonNull;
import java.io.IOException;

public class ProcessExecutor {

    public boolean exec(@NonNull final String command) {

        try {
            final Process process = Runtime.getRuntime().exec(command);
            process.waitFor();
            return process.exitValue() == 0;

        } catch (NullPointerException | InterruptedException | IOException e) {
            return false;
        }
    }
}
