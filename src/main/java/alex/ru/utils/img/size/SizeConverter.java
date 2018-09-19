package alex.ru.utils.img.size;

import org.springframework.lang.NonNull;

public interface SizeConverter {

    byte[] convert(@NonNull final byte[] _image, final int size);
}
