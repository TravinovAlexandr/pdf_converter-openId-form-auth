package alex.ru.utils.cloud;

import alex.ru.exceptions.UtilException;
import org.springframework.lang.NonNull;

public class AuthUserInfoFactory {

    public enum Resource {
        GOOGLE
    }

    public AuthUserInfo getAuthUserInfo(@NonNull final Resource resource) {
        try {
            if (resource == Resource.GOOGLE) {
                return new GoogleAuthUserInfo();
            }
        } catch (NullPointerException e) {
            throw new UtilException("Resource == null");
        }
        return null;
    }
}
