package alex.ru.utils.jmx;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

public class JmxSimpleContainer implements JmxContainer {

    final private Map<String, JmxInitializer> initializerMap = new HashMap<>();

    public void initialize() {
        initializerMap.forEach((i,j) -> j.init());
    }

    public void put(@NonNull final JmxInitializer jmxInitializer) {
        initializerMap.put(jmxInitializer.getMBeanName(), jmxInitializer);
    }

    public void put(@NonNull final String mBeanName,@NonNull final JmxInitializer jmxInitializer) {
        initializerMap.put(mBeanName, jmxInitializer);
    }

    @Nullable
    public Object get(@NonNull final Class mBeanClazz) {
        try {
            return initializerMap.values().stream()
                    .filter(e -> e.getMBean().getClass().equals(mBeanClazz)).findFirst().get().getMBean();

        } catch (NoSuchElementException e) {
            return null;
        }
    }

    @Nullable
    public Object get(@NonNull final String mBeanName) {
        return initializerMap.get(mBeanName).getMBean();
    }
}
