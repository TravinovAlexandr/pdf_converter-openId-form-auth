package alex.ru.utils.referer;

import javax.servlet.http.HttpServletRequest;

public class RefererMapping {

    private String getReferer(boolean isAuthenticated, final HttpServletRequest request) {

        if(request == null || isAuthenticated)
            return "index";

        else {
            String [] refererUrl = request.getHeader("referer").split("/");
            return refererUrl[refererUrl.length - 1];
        }
    }
}
