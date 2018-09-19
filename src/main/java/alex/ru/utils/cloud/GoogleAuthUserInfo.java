package alex.ru.utils.cloud;

import org.springframework.lang.Nullable;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import java.security.Principal;
import java.util.LinkedHashMap;
import java.util.Map;

public class GoogleAuthUserInfo implements AuthUserInfo {

    @Nullable
    public Map<String, String> getUserInformation(@Nullable final Principal principal) {
        try {
            if(principal.getClass().equals(Principal.class) ||
                principal.getClass().equals(Authentication.class) ||
                principal.getClass().equals(OAuth2Authentication.class)) {

                final OAuth2Authentication auth2Authentication = (OAuth2Authentication) principal;
                final Authentication authentication = auth2Authentication.getUserAuthentication();

                return (LinkedHashMap<String, String>) authentication.getDetails();

            } else
                return null;

        } catch (NullPointerException e) {
                return null;
        }
    }
}
