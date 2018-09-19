package alex.ru.utils.recaptcha;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:recaptcha.properties")
public class ReCaptchaSettings {

    @Value("${recaptcha.site_key}")
    private String site;

    @Value("${recaptcha.secret_key}")
    private String secret;

    public String getSite() {
        return site;
    }

    public void setSite(final String site) {
        this.site = site;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(final String secret) {
        this.secret = secret;
    }
}
