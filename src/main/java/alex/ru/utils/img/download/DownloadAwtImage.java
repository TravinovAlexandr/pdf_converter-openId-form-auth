package alex.ru.utils.img.download;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;

public class DownloadAwtImage {

    public byte[] downloadUserAvatar(final String url) {

        try {
            final BufferedImage image = ImageIO.read(new URL(url));
            final ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(image, "png", baos);
            baos.flush();

            return baos.toByteArray();

        } catch (NullPointerException | IOException  e) {
            return null;
        }

    }
}
