package alex.ru.controller;

import alex.ru.dao.UserDao;
import alex.ru.domain.User;
import alex.ru.dto.FormLoginDto;
import alex.ru.exceptions.ServiceException;
import alex.ru.handlers.LogoutHandler;
import alex.ru.utils.viewcache.MapCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;

import static org.springframework.security.web.context.HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY;

@Controller
public class LogController {

    private LogoutHandler logoutHandler;

    private MapCache mapCache;

    private UserDao userDao;

    private AuthenticationManager authenticationManager;

    private Validator validator;

    private MessageSource messageSource;

    @Autowired
    public LogController(final LogoutHandler logoutHandler,
                         final MapCache mapCache,
                         final UserDao userDao,
                         final AuthenticationManager authenticationManager,
                         @Qualifier("formLoginValidator") final Validator validator,
                         final MessageSource messageSource) {

        this.logoutHandler = logoutHandler;
        this.mapCache = mapCache;
        this.userDao = userDao;
        this.authenticationManager = authenticationManager;
        this.validator = validator;
        this.messageSource = messageSource;
    }

    @InitBinder
    public void formLoginInitBinder(final WebDataBinder binder) {
        binder.addValidators(validator);
    }

    @GetMapping("/log")
    public String getLoginView(final Model model) {
        model.addAttribute("formLoginDto", new FormLoginDto());
        return "login";
    }

    @PostMapping("/formLogin")
    public String formLogin(@Validated @ModelAttribute("formLoginDto") final FormLoginDto dto,
                            final BindingResult bindingResult,
                            final HttpServletRequest request,
                            final Model optErrors) {

        if(bindingResult.hasErrors()) {
            return "login";
        }
        else {
            try {
                final User user = userDao.findByNickOrEmailAndPasswordForLoginForm(dto.username, dto.password);

                final List<GrantedAuthority> authorities = new ArrayList<>();

                user.getRoles().forEach(r -> authorities.add(new SimpleGrantedAuthority(r.getName())));

                final UsernamePasswordAuthenticationToken autToken
                        = new UsernamePasswordAuthenticationToken(dto.username, dto.password, authorities);

                final Authentication auth = authenticationManager.authenticate(autToken);

                final SecurityContext securityContext = SecurityContextHolder.getContext();
                securityContext.setAuthentication(auth);

                final HttpSession session = request.getSession(true);
                session.setAttribute(SPRING_SECURITY_CONTEXT_KEY, securityContext);

            } catch (NullPointerException | ServiceException e) {

                if(e.getClass().equals(NullPointerException.class)) {
                    optErrors.addAttribute("opt_error", messageSource.getMessage("opt.account", null, Locale.US));
                }
                return "opt_error";
            }
            return "redirect:/";
        }
    }

    @PostMapping("/logOut")
    public String logOut(final HttpServletRequest request) {

        mapCache.getValue(logoutHandler.logout(request));

        return "redirect:/";
    }
}
