package alex.ru.conf.sub.bpp;

import alex.ru.annotations.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;
import java.lang.reflect.Field;

@Component
public class AnnotationLoggerBeanPostProcessor implements BeanPostProcessor {

    private final static Logger LOG = LoggerFactory.getLogger(AnnotationLoggerBeanPostProcessor.class.getSimpleName());

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {

        for(Field field : bean.getClass().getDeclaredFields()) {
            if (field.isAnnotationPresent(Log.class)){
                field.setAccessible(true);
                try {
                    field.set(bean, LoggerFactory.getLogger(bean.getClass().getSimpleName()));
                } catch (IllegalAccessException e) {
                    LOG.error("Logger wasn't injected.");
                } finally {
                    field.setAccessible(false);
                }
            }
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }
}
