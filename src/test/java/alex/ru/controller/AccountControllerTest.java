package alex.ru.controller;

import alex.ru.dto.ChangeAccountDto;
import conf.ControllerTestConf;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.support.BindingAwareModelMap;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ControllerTestConf.class)
@AutoConfigureMockMvc
public class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AccountController accountController;

    @Test
    public void getAccountView() throws Exception {

        mockMvc.perform(get("/account")).andExpect(status()
                .isOk()).andExpect(content().contentType("text/html;charset=UTF-8"));
    }

    @Test
    public void changeNameAndLastName() throws Exception {

        final ChangeAccountDto changeAccountDto = new ChangeAccountDto();

        final List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("USER_ROLE"));

        final Principal principal = new UsernamePasswordAuthenticationToken("email",
                Base64.getEncoder().encode("password".getBytes()), authorities);

        final BindingResult bindingResult = new BeanPropertyBindingResult(changeAccountDto, changeAccountDto.name);

        final ExtendedModelMap model = new BindingAwareModelMap();

        final String result = accountController.changeNameAndLastName(changeAccountDto, bindingResult, principal, model);

        Assert.assertNotNull(result);
    }

}