package alex.ru.dto;

import lombok.*;
import javax.validation.constraints.Size;

@ToString @Data
@NoArgsConstructor
public class PersistDto {

    @Size(min=3, max=25)
    public String nick;

    @Size(min=5, max=255)
    public String email;

    @Size(min=5, max=255)
    public String password;

    @Size(min = 3, max = 50)
    public String name;

    @Size(min = 3, max = 50)
    public String lastName;

    public byte[] avatar;

}
