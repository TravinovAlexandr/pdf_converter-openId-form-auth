package alex.ru.utils.viewcache;

import org.springframework.lang.Nullable;

public interface MapCache {

    @Nullable
    String getValue(final String key);

    boolean isKeyExists(final String key);

    boolean addUrl(final String key, final String value);

    boolean removeUrl(final String key);
}
