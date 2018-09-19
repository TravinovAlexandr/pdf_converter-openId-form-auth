package alex.ru.controller;

import alex.ru.dao.UserDao;
import alex.ru.dto.ChangeAccountDto;
import alex.ru.exceptions.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import javax.validation.Valid;
import java.security.Principal;
import java.util.Locale;

@Controller
public class AccountController {

    private UserDao userDao;

    private MessageSource messageSource;

    @Autowired
    public AccountController(final UserDao userDao,
                             final MessageSource messageSource) {
        this.userDao = userDao;
        this.messageSource = messageSource;
    }

    @GetMapping("/account")
    public String getAccountView(final Model model) {
        model.addAttribute("dto", new ChangeAccountDto());
        return "account";
    }

    @PostMapping("/changeNameLastName")
    public String changeNameAndLastName(@Valid @ModelAttribute("dto") final ChangeAccountDto dto,
                                        final BindingResult bindingResult,
                                        final Principal principal,
                                        final Model optErrors) {
        if(bindingResult.hasErrors()) {
            return "account";
        }
        else {
            try {
                boolean isChanged =
                        userDao.updateNameAndLastName(dto.name, dto.lastName, dto.password, principal.getName());
                if (isChanged) {
                    return "redirect:/";
                } else {
                    optErrors.addAttribute("opt_error", messageSource.getMessage("opt.password", null, Locale.US));
                    return "opt_error";
                }
            } catch (ServiceException e) {
                optErrors.addAttribute("opt_error", messageSource.getMessage("opt.account", null, Locale.US));
                return "opt_error";
            }
        }
    }
}
