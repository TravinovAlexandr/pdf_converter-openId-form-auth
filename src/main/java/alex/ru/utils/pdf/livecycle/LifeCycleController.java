package alex.ru.utils.pdf.livecycle;

import alex.ru.dto.MultipartDto;
import org.springframework.lang.NonNull;

public interface LifeCycleController {

    boolean write(@NonNull final MultipartDto dto, @NonNull final String pathToTempDir);

    void remove();
}
