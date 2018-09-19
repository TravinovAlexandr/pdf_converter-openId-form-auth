package alex.ru.handlers;

import alex.ru.exceptions.ServiceException;
import org.hibernate.exception.GenericJDBCException;
import org.slf4j.Logger;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import java.sql.SQLException;

public class ServiceLayerExceptionHandler implements ExceptionHandler {

    @Override
    public RuntimeException statementHandle(final Logger logger, final Exception e) {

        if(logger == null || e == null)
            throw new IllegalArgumentException();

        if(e.getClass().equals(NullPointerException.class)) {
//            e.printStackTrace();
            logger.error("NullPointerException. Argument is null.");
            return new ServiceException("NullPointerException. Argument is null.");
        }
        else if(e.getClass().equals(NoResultException.class)) {
//            e.printStackTrace();
            logger.warn("No result.");
            return new ServiceException("No result");
        }
        else if(e.getClass().equals(NonUniqueResultException.class)) {
//            e.printStackTrace();
            logger.error("No unique result.");
            return new ServiceException("No unique result.");
        }
        else if(e.getClass().equals(IllegalStateException.class)) {
//            e.printStackTrace();
            logger.error("Transaction not successfully started");
            return new ServiceException("Transaction not successfully started");
        }
        else if (e.getClass().equals(GenericJDBCException.class)) {
//            e.printStackTrace();
            logger.error("Unable to acquire JDBC Connection");
            return new ServiceException("Unable to acquire JDBC Connection");
        }
        else if(e instanceof SQLException) {
//            e.printStackTrace();
            logger.error("Db doesn't work");
            return new ServiceException("Db doesn't work");
        }
        else {
//            e.printStackTrace();
            logger.error("Undefined");
            return new ServiceException("Undefined");
        }
    }
}
