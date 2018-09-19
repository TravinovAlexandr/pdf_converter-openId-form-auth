package alex.ru.dto;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@ToString @Data
@NoArgsConstructor
public class MultipartDto {

    public MultipartFile multipartFile;

    public String checkBox;

    public String getName() {
        return String.valueOf(multipartFile.hashCode()).substring(1);
    }

}