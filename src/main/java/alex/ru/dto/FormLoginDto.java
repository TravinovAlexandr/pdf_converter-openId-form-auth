package alex.ru.dto;

import lombok.*;
import javax.validation.constraints.Size;

@ToString @Data
@NoArgsConstructor
public class FormLoginDto {

    @Size(min=5, max=50)
    public String username;

    @Size(min=5, max=255)
    public String password;
}
