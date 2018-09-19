package alex.ru.utils.jmx;

public interface JmxInitializer {

    void init();

    Object getMBean();

    String getMBeanName();
}
