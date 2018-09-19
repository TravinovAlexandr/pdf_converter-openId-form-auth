package alex.ru.utils.img.size;

import alex.ru.exceptions.UtilException;
import org.springframework.lang.NonNull;

public class SizeConverterFactory {

    public enum Factory {
        SCARL
    }

    public byte[] getResizeImage(@NonNull final byte[] image, int preferSize, @NonNull final Factory converter) {

        if(converter == Factory.SCARL) {
            try {

                return new SizeScarlConverter().convert(image, preferSize);

            } catch (UtilException e) {
                throw e;
            }
        }
        else
            throw new UtilException("NullPointerException");
    }
}
