package alex.ru.utils.img.download;

import alex.ru.exceptions.UtilException;
import org.springframework.lang.NonNull;

public class DownloadImageFactory {

    public enum Factory {
        AWT
    }

    public byte[] getImage(@NonNull final String url, @NonNull final Factory factory) {

        if(factory == Factory.AWT) {

            byte[] image = new DownloadAwtImage().downloadUserAvatar(url);

            if (image != null) return image;
            else
                throw new UtilException("image == null");
        }
        else {
            throw new UtilException("factory == null");
        }

    }
}
