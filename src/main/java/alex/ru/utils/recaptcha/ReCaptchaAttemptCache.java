package alex.ru.utils.recaptcha;

import alex.ru.exceptions.UtilException;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Component
@PropertySource("classpath:recaptcha.properties")
public class ReCaptchaAttemptCache {

    @Value("${recaptcha.cashSize}")
    private int MAX_ATTEMPT;

    private LoadingCache<String, Integer> attemptsCache;

    private final ReadWriteLock lock = new ReentrantReadWriteLock();

    public ReCaptchaAttemptCache() {
        attemptsCache = CacheBuilder.newBuilder().expireAfterWrite(4, TimeUnit.HOURS).build(new CacheLoader<String, Integer>() {
            @Override
            public Integer load(final String key) {
                return 0;
            }
        });
    }

    public void reCaptchaSucceeded(final String key) {
        lock.writeLock().lock();

        if (key == null) throw new UtilException();

        try {
            attemptsCache.invalidate(key);
        } finally {
            lock.writeLock().unlock();
        }
    }

    public void reCaptchaFailed(final String key) {
        lock.writeLock().lock();

        if (key == null) throw new UtilException();

        try {
            int attempts = attemptsCache.getUnchecked(key);
            attempts++;
            attemptsCache.put(key, attempts);
        } finally {
            lock.writeLock().unlock();
        }
    }

    public boolean isBlocked(final String key) {
        lock.readLock().lock();

        if (key == null) throw new UtilException();

        try {
            return attemptsCache.getUnchecked(key) >= MAX_ATTEMPT;
        } finally {
            lock.readLock().unlock();
        }
    }
}
