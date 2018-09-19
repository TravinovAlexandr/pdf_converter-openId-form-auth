package alex.ru.dto;

import lombok.*;
import javax.validation.constraints.Size;

@ToString @Data
@NoArgsConstructor
public class ChangeAccountDto {

    @Size(min = 3, max = 50)
    public String name;

    @Size(min = 3, max = 50)
    public String lastName;

    @Size(min=5, max=255)
    public String password;
}
