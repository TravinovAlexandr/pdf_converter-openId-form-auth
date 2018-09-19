package alex.ru.utils.dto_converter;

import alex.ru.annotations.Log;
import alex.ru.dto.PersistDto;
import alex.ru.exceptions.UtilException;
import alex.ru.utils.img.download.DownloadImageFactory;
import alex.ru.utils.img.size.SizeConverterFactory;
import alex.ru.utils.img.size.SizeConverterFactory.Factory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.Map;

//rewrite
@Component @PropertySource("classpath:google_api.properties")
public class DtoCloudConverter implements CloudConverter {

    @Log
    private Logger logger;

    @Value("${google.family_name}")
    private String googleFamilyName;

    @Value("${google.given_name}")
    private String googleGivenName;

    @Value("${google.email}")
    private String googleEmail;

    @Value("${google.avatar}")
    private String googleAvatar;

    public DtoCloudConverter() {}

    public Object convert(@NonNull final Class clazz, @NonNull final Map info,
                          @NonNull final DownloadImageFactory.Factory factory) {

        try {

            DownloadImageFactory imageFactory = new DownloadImageFactory();
            SizeConverterFactory sizeFactory = new SizeConverterFactory();

            if (PersistDto.class == clazz) {

                PersistDto persistDto = new PersistDto();

                persistDto.lastName = (String) info.get(googleFamilyName);
                persistDto.name = (String) info.get(googleGivenName);
                persistDto.email = (String) info.get(googleEmail);

                try {
                    persistDto.avatar =
                            sizeFactory.getResizeImage(imageFactory.getImage((String)
                                    info.get(googleAvatar), factory), 100, Factory.SCARL);

                } catch (UtilException e) {
                    //net connection problem
                    persistDto = null;
                }
                return persistDto;
            }

        } catch(NullPointerException e) {
            logger.error("1 of arguments == null.");
            throw new UtilException("NullPointerException");

        }
        return null;
    }
}
