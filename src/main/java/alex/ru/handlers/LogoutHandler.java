package alex.ru.handlers;

import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

@Component
public class LogoutHandler {

    public String logout(@Nullable final HttpServletRequest request) {
        try {

            request.logout();
            request.getSession().invalidate();

            if(request.getUserPrincipal() == null)
                return getRedirectUrl(false, request);
            return getRedirectUrl(true, request);

        } catch (NullPointerException | ServletException e ) {
            return getRedirectUrl(false, request);
        }
    }

    private String getRedirectUrl(boolean isAuthenticated, final HttpServletRequest request) {

        if(request == null || isAuthenticated)
            return "index";

        else {
            String [] refererUrl = request.getHeader("referer").split("/");
            return refererUrl[refererUrl.length - 1];
        }
    }
}
