package alex.ru.utils.validation;

import alex.ru.exceptions.UtilException;
import org.springframework.lang.NonNull;

import java.util.regex.Pattern;

public class ValidationUtil {

    public boolean isEmail(final String username) {

        if (username == null) throw new UtilException("Parameter is null");

        final Pattern pattern =
                Pattern.compile("^([A-Z0-9_\\.-]+)@([A-Z0-9_\\.-]+)\\.([A-Z\\.]{2,6})$", Pattern.CASE_INSENSITIVE);
        return pattern.matcher(username).matches();
    }

    public boolean isCorrectNick(final String nick) {

        if (nick == null) throw new UtilException("Parameter is null");

        final Pattern pattern = Pattern.compile("^([A-Z0-9.,+#$*/_\\-])$", Pattern.CASE_INSENSITIVE);
        return pattern.matcher(nick).matches();
    }

    public boolean isCorrectPassword(final String password) {

        if (password == null) throw new UtilException("Parameter is null");

        final Pattern pattern = Pattern.compile("^([A-Z0-9.,+#$*/_\\-])$", Pattern.CASE_INSENSITIVE);
        return pattern.matcher(password).matches();
    }
}
