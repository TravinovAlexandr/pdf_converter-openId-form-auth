package alex.ru.utils.recaptcha;

import alex.ru.annotations.Log;
import alex.ru.exceptions.ReCaptchaException;
import alex.ru.exceptions.UtilException;
import alex.ru.utils.ip.ClientIp;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestOperations;
import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.regex.Pattern;

@Component("reCaptchaHandler")
public class ReCaptchaHandlerImpl implements ReCaptchaHandler {

    @Log
    private static Logger logger;

    private static final Pattern RESPONSE_PATTERN = Pattern.compile("[A-Za-z0-9_-]+");

    private HttpServletRequest request;

    private ReCaptchaSettings reCaptchaSettings;

    private ReCaptchaAttemptCache reCaptchaAttemptCache;

    private RestOperations restTemplate;

    private ClientIp clientIp;

    @Autowired
    public ReCaptchaHandlerImpl(final HttpServletRequest request,
                                final ReCaptchaSettings reCaptchaSettings,
                                final ReCaptchaAttemptCache reCaptchaAttemptCache,
                                final RestOperations restTemplate,
                                final ClientIp clientIp) {

        this.request = request;
        this.reCaptchaSettings = reCaptchaSettings;
        this.reCaptchaAttemptCache = reCaptchaAttemptCache;
        this.restTemplate = restTemplate;
        this.clientIp = clientIp;
    }

    @Override
    public void processResponse(final String response) {

        final String ip = clientIp.getClientIP(request);

        try {
            if (reCaptchaAttemptCache.isBlocked(ip)) {
                throw new ReCaptchaException("bloc");
            }

            if (!responseCheck(response)) {
                throw new ReCaptchaException("mark");
            }

            final URI verifyUri = URI.create(String.format("https://www.google.com/recaptcha/api/siteverify?secret=%s&response=%s&remoteip=%s",
                getReCaptchaSecret(), response, ip));

            final GoogleResponse googleResponse = restTemplate.getForObject(verifyUri, GoogleResponse.class);

            if (!googleResponse.isSuccess()) {
                if (googleResponse.hasClientError()) {
                    reCaptchaAttemptCache.reCaptchaFailed(ip);
                }
                throw new ReCaptchaException("exception");
            }

        } catch (NullPointerException | RestClientException | UtilException rce) {
            throw new ReCaptchaException("exception");
        }
        reCaptchaAttemptCache.reCaptchaSucceeded(ip);
    }

    private boolean responseCheck(final String response) {
        return StringUtils.hasLength(response) && RESPONSE_PATTERN.matcher(response).matches();
    }

    @Override
    public String getReCaptchaSite() {
        return reCaptchaSettings.getSite();
    }

    @Override
    public String getReCaptchaSecret() {
        return reCaptchaSettings.getSecret();
    }

}