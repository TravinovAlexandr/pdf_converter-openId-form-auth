package alex.ru.service;

import alex.ru.annotations.Log;
import alex.ru.dao.RoleDao;
import alex.ru.dao.UserDao;
import alex.ru.domain.Role;
import alex.ru.domain.User;
import alex.ru.dto.PersistDto;
import alex.ru.exceptions.ServiceException;
import alex.ru.handlers.ServiceLayerExceptionHandler;
import alex.ru.handlers.ExceptionHandler;
import alex.ru.utils.validation.ValidationUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Service
public class UserService implements UserDao {

    @Log
    private Logger logger;

    private SessionFactory sessionFactory;

    private RoleDao roleDao;

    private PasswordEncoder passwordEncoder;

    private ValidationUtil validationUtil;

    private ExceptionHandler exceptionHandler;

    @Autowired
    public UserService(final SessionFactory sessionFactory,
                       final RoleDao roleDao,
                       final PasswordEncoder passwordEncoder,
                       final ValidationUtil validationUtil) {
        this.sessionFactory = sessionFactory;
        this.roleDao = roleDao;
        this.passwordEncoder = passwordEncoder;
        this.validationUtil = validationUtil;
        exceptionHandler = new ServiceLayerExceptionHandler();
    }

    @Override
    public void persistNewAccount(@NonNull final PersistDto dto) {

        final Session session = sessionFactory.openSession();

        try {
            session.beginTransaction();
            final User user = new User(dto.nick, dto.email, passwordEncoder.encode(dto.password), LocalDate.now(), LocalTime.now());
            final Role role = roleDao.findById(2L);

            if (role == null) {
                logger.error("NullPointerException. ROLE_P_USER i s not found.");
                throw new ServiceException("NullPointerException. ROLE_P_USER is not found.");
            }
            user.setRoles(Collections.singleton(role));
            session.save(user);
            session.getTransaction().commit();

        } catch (Exception e) {
            throw exceptionHandler.statementHandle(logger , e);

        } finally {
            if (session.isOpen()) {
                session.close();
            }
        }
    }

    @Override
    public Set<User> findAll() {
        try {
            final Session session = sessionFactory.openSession();
            session.beginTransaction();
            final Query query = session.createQuery(String.format("from User"));
            final Set<User> set = Collections.unmodifiableSet(new HashSet<>(query.getResultList()));
            return set;

        } catch (Exception e) {
            throw exceptionHandler.statementHandle(logger, e);
        }
    }

    @Override @Nullable
    public User findByNick(@NonNull final String nick) {
        return findUserWithOneArgument("from User u where u.nick = :nick", "nick", nick);
    }

    @Override @Nullable
    public User findByEmail(@NonNull final String email) {
        return findUserWithOneArgument("from User u where u.email = :email","email", email);
    }

    @Override
    public boolean updateNameAndLastName(@NonNull final String name, @NonNull final String lastName,
                                         @NonNull final String password, @NonNull final String username) {

        final boolean isEmail = validationUtil.isEmail(username);

        final String nickOrEmail = isEmail ? "email" : "nick";

        final Session session = sessionFactory.openSession();
        try {
            //PSQLException
            session.beginTransaction();

            final String statement = String.format("from User u join fetch u.roles where u.%s = :%s", nickOrEmail, nickOrEmail);

            final Query<User> query = session.createQuery(statement);
            query.setParameter(nickOrEmail, username, org.hibernate.type.StringType.INSTANCE);

            final User user = query.getSingleResult();

            final boolean isPasswordMatches = user != null
                    && passwordEncoder.matches(password, user.getPassword());

            if (isPasswordMatches) {
                user.setName(name);
                user.setLastName(lastName);
            }
            session.getTransaction().commit();

            return isPasswordMatches;

        } catch (Exception e) {

            throw exceptionHandler.statementHandle(logger, e);

        } finally {
            session.getTransaction().commit();
            if (session.isOpen()) {
                session.close();
            }
        }
    }

    @Override @Nullable
    public User findByNickOrEmailAndPasswordForLoginForm(@NonNull final String username, @NonNull final String password) {

        final String nickOrEmail = (username.contains("@")) ? "email" : "nick";

        final String statement = String.format("from User u join fetch u.roles where u.%s = :%s",
                nickOrEmail, nickOrEmail);

        final User user = findUserWithOneArgument(statement, nickOrEmail, username);

        return (user != null) ?
                (passwordEncoder.matches(password, user.getPassword()) ? user : null)
                : null;
    }

    @Override
    public boolean isExistByNick(@NonNull final String nick) {
        return findByNick(nick) == null;
    }

    @Override
    public boolean isExistByEmail(@NonNull final String email) {
        return findByEmail(email) == null;
    }

    private User findUserWithOneArgument(@NonNull final String _query,
                                         @NonNull final String column,
                                         @NonNull final String parameter) {

        final Session session = sessionFactory.openSession();

        try {
            session.beginTransaction();
            final Query<User> query = session.createQuery(_query);
            query.setParameter(column, parameter, org.hibernate.type.StringType.INSTANCE);
            final User user = query.getSingleResult();
            session.getTransaction().commit();
            return user;

        } catch (Exception e) {
            exceptionHandler.statementHandle(logger, e);

        } finally {
            if (session.isOpen()) {
                session.close();
            }
        }
        //necessary return null
        return null;
    }
}
