package alex.ru.controller;

import alex.ru.annotations.Log;
import alex.ru.dao.UserDao;
import alex.ru.dto.PersistDto;
import alex.ru.exceptions.ServiceException;
import alex.ru.exceptions.UtilException;
import alex.ru.utils.cloud.AuthUserInfo;
import alex.ru.utils.cloud.AuthUserInfoFactory;
import alex.ru.utils.cloud.AuthUserInfoFactory.Resource;
import alex.ru.utils.dto_converter.CloudConverter;
import alex.ru.utils.img.download.DownloadImageFactory.Factory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import java.security.Principal;

@Controller
public class RegistrationController {

    @Log
    private Logger logger;

    private UserDao userDao;

    private CloudConverter cloudConverter;

    @Autowired
    public RegistrationController(final UserDao userDao,
                                  final CloudConverter cloudConverter) {
        this.userDao = userDao;
        this.cloudConverter = cloudConverter;

    }

    @GetMapping("/checkStatus")
    public @ResponseBody boolean authCheckStatusForAjaxRequest(final Authentication auth) {
        try {
            return auth.isAuthenticated();
        } catch (NullPointerException e) {
            return false;
        }
    }

    @GetMapping("/registration")
    public String getRegistrationView(final Model model) {
        model.addAttribute("dto", new PersistDto());
        return "registration";
    }

    @GetMapping(value = "/googleRegistration")
    public String getGoogleRegistrationView(final Principal principal, final Model model){
        try {
            final AuthUserInfo info =
                    new AuthUserInfoFactory().getAuthUserInfo(Resource.GOOGLE);

            final PersistDto dto = (PersistDto) cloudConverter
                    .convert(PersistDto.class, info.getUserInformation(principal), Factory.AWT);

            model.addAttribute("dto", dto);

        } catch (NullPointerException | UtilException e) {

            model.addAttribute("dto", new PersistDto());
        }
        return "registration";
    }

    @PostMapping("/registration")
    public String persistNewUserAccount(@Validated @ModelAttribute("dto") final PersistDto dto,
                                      final BindingResult bindingResult, final Model optErrors) {
        if(bindingResult.hasErrors()) {

            return "registration";

        } else {
            try {
                userDao.persistNewAccount(dto);
                return "redirect:/";
            } catch (ServiceException e) {
                logger.warn("New account was not registred.");
                optErrors.addAttribute("opt_error", e.getMessage());
                return "opt_error";
            }
        }
    }
}
