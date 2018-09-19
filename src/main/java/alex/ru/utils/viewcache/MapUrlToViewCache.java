package alex.ru.utils.viewcache;

import org.springframework.lang.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class MapUrlToViewCache implements MapCache {

    private final Map<String, String> urlCash = new HashMap<>();

    private final ReadWriteLock lock = new ReentrantReadWriteLock();

    @Override @Nullable
    public String getValue(final String key) {
        lock.readLock().lock();
        try {
            return urlCash.get(key);

        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public boolean isKeyExists(final String key) {
        lock.readLock().lock();
        try {
            return urlCash.containsKey(key);

        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public boolean addUrl(final String key, final String value) {
        lock.writeLock().lock();
        try {
            return urlCash.put(key, value) == null;

        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public boolean removeUrl(final String key) {
        lock.writeLock().lock();
        try {
            return urlCash.remove(key) != null;

        } finally {
            lock.writeLock().unlock();
        }
    }
}
