package alex.ru.conf.sub.auth;

import alex.ru.annotations.Log;
import org.slf4j.Logger;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
public class FormAuthProvider implements AuthenticationProvider {

    @Log
    private Logger logger;

    @Override
    public Authentication authenticate(final Authentication auth) throws AuthenticationException {

        if(auth == null) {
            logger.error("External system authentication failed");
            throw new BadCredentialsException("External system authentication failed");
        }
        return auth;
    }

    @Override
    public boolean supports(Class<?> auth) {
        return auth.equals(UsernamePasswordAuthenticationToken.class);
    }
}