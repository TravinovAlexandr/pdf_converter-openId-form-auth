package alex.ru.utils.jmx;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

public interface JmxContainer {

    void initialize();

    void put(@NonNull final JmxInitializer jmxInitializer);

    void put(@NonNull final String mBeanName, @NonNull final JmxInitializer jmxInitializer);

    @Nullable
    Object get(@NonNull final Class mBeanClazz);

    @Nullable
    Object get(@NonNull final String mBeanName);
}
