package alex.ru.dao;

import alex.ru.domain.Role;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import java.util.Set;

public interface RoleDao {

    Role findById(@NonNull Long id);

    @Nullable
    Set<Role> findAll();
}
