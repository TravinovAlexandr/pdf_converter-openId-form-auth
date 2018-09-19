package alex.ru.utils.dto_converter;

import alex.ru.utils.img.download.DownloadImageFactory.Factory;
import org.springframework.lang.NonNull;

import java.util.Map;

public interface CloudConverter {

    Object convert(@NonNull final Class clazz, @NonNull final Map info, @NonNull final Factory factory);
}
