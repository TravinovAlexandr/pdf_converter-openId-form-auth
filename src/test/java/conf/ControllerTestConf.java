package conf;

import alex.ru.controller.AccountController;
import alex.ru.controller.LogController;
import alex.ru.dao.UserDao;
import alex.ru.handlers.LogoutHandler;
import alex.ru.utils.viewcache.MapCache;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.validation.Validator;

@Configuration
@ComponentScan("alex.ru")
public class ControllerTestConf {


    @Bean
    public AccountController accountController(final UserDao userDao,final MessageSource messageSource) {
        return new AccountController(userDao, messageSource);
    }
    @Bean
    public LogController logController(final LogoutHandler logoutHandler, final MapCache mapCache,
                                       final UserDao userDao, final AuthenticationManager authenticationManager,
                                       @Qualifier("formLoginValidator") final Validator validator,
                                       final MessageSource messageSource) {
        return new LogController(logoutHandler, mapCache, userDao, authenticationManager, validator, messageSource);
    }
}
