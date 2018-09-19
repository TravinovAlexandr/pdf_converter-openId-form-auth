package alex.ru.handlers;

import org.slf4j.Logger;

public interface ExceptionHandler {

    RuntimeException statementHandle(final Logger logger, final Exception e);
}
