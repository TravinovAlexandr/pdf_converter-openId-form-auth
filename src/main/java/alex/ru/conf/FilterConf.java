package alex.ru.conf;

import alex.ru.utils.rendering_adapter.ViewRender;
import alex.ru.filters.StopServerFilter;
import alex.ru.utils.jmx.JmxContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;

@Configuration
public class FilterConf {

    private ViewRender viewRender;

    private JmxContainer jmxContainer;

    @Value("${jmx.stop_server.error_massage}")
    private String errorMassage;

    @Autowired
    public FilterConf(final ViewRender viewRender,
                      final JmxContainer jmxContainer) {
        this.jmxContainer = jmxContainer;
        this.viewRender = viewRender;
    }

    @Bean
    public FilterRegistrationBean stopServerFilterRegistrationBean() {
        final FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setFilter(stopServerFilter());
        filterRegistrationBean.addUrlPatterns("/*");
        filterRegistrationBean.setOrder(1);
        return filterRegistrationBean;
    }

    public Filter stopServerFilter() {
        final StopServerFilter stopServerFilter = new StopServerFilter();
        stopServerFilter.setViewRender(viewRender);
        stopServerFilter.setJmxContainer(jmxContainer);
        stopServerFilter.setErrorMassage(errorMassage);
        return stopServerFilter;
    }
}
