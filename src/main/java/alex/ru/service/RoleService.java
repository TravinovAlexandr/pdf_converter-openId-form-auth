package alex.ru.service;

import alex.ru.annotations.Log;
import alex.ru.dao.RoleDao;
import alex.ru.domain.Role;
import alex.ru.handlers.ExceptionHandler;
import alex.ru.handlers.ServiceLayerExceptionHandler;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class RoleService implements RoleDao {

    @Log
    private Logger logger;

    private SessionFactory sessionFactory;

    private ExceptionHandler exceptionHandler;

    @Autowired
    public RoleService(final SessionFactory sessionFactory) {

        this.sessionFactory = sessionFactory;
        exceptionHandler = new ServiceLayerExceptionHandler();
    }

    @Override
    public Role findById(@NonNull final Long id) {
        final Session session = sessionFactory.getCurrentSession();
        try {
            session.beginTransaction();
            final Role role = session.get(Role.class, id);
            session.getTransaction().commit();
            return role;

        } catch (Exception e) {
            throw exceptionHandler.statementHandle(logger, e);
        }
    }

    @Override
    public Set<Role> findAll() {
        final Session session = sessionFactory.openSession();
        try {
            session.beginTransaction();
            final Query query = session.createQuery(String.format("from Role"));
            final Set<Role> roles = Collections.unmodifiableSet(new HashSet<>(query.list()));
            session.getTransaction().commit();
            return roles;

        } catch (Exception e) {
            throw exceptionHandler.statementHandle(logger, e);

        } finally {
            if (session.isOpen()) {
                session.close();
            }
        }
    }
}
