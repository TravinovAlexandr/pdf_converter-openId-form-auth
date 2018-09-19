package alex.ru.conf;

import alex.ru.utils.jmx.JmxContainer;
import alex.ru.utils.jmx.JmxInitializer;
import alex.ru.utils.jmx.JmxSimpleContainer;
import alex.ru.utils.jmx.stopserver.StopServerInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JmxConf {

    @Bean
    public JmxContainer jmxContainer() {
        final JmxContainer jmxContainer = new JmxSimpleContainer();
        jmxContainer.put(stopServerInitializer());
        jmxContainer.initialize();
        return jmxContainer;
    }

    public JmxInitializer stopServerInitializer() {
        return new StopServerInitializer();
    }
}
