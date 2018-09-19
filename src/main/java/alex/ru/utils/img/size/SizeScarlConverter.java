package alex.ru.utils.img.size;

import alex.ru.exceptions.UtilException;
import org.imgscalr.Scalr;
import org.springframework.lang.NonNull;
import org.springframework.lang.NonNullApi;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class SizeScarlConverter implements SizeConverter{

    @Override
    public byte[] convert(@NonNull final byte[] _image, final int size) {

        try {

            final InputStream in = new ByteArrayInputStream(_image);
            final BufferedImage image = ImageIO.read(in);

            final BufferedImage scaledImg = Scalr.resize(image,
                    Scalr.Method.ULTRA_QUALITY, Scalr.Mode.AUTOMATIC, size, Scalr.OP_ANTIALIAS);

            final ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(scaledImg, "png", baos);

            byte[] newImage = baos.toByteArray();
            baos.flush();
            baos.close();

            return newImage;

        } catch (NullPointerException | IOException e) {
            throw new UtilException("_image==null");
        }
    }
}
