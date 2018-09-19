package alex.ru.dao;

import alex.ru.domain.User;
import alex.ru.dto.FormLoginDto;
import alex.ru.dto.PersistDto;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.util.Set;

public interface UserDao {

    void persistNewAccount(PersistDto dto);

    @Nullable
    Set<User> findAll();

    @Nullable
    User findByNick(@NonNull final String nick);

    @Nullable
    User findByEmail(@NonNull final String email);

    boolean updateNameAndLastName(@NonNull final String name, @NonNull final String lastName,
                                  @NonNull final String password, @NonNull final String username);

    @Nullable
    User findByNickOrEmailAndPasswordForLoginForm(@NonNull final String username, @NonNull final String password);

    boolean isExistByNick(@NonNull final String nick);

    boolean isExistByEmail(@NonNull final String email);


}
