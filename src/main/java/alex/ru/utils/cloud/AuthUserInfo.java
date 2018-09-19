package alex.ru.utils.cloud;

import org.springframework.lang.Nullable;

import java.security.Principal;
import java.util.Map;

public interface AuthUserInfo {

    @Nullable
    Map<String, String> getUserInformation(@Nullable final Principal principal);
}
