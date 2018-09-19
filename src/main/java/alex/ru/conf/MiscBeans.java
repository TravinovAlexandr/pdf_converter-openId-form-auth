package alex.ru.conf;

import alex.ru.utils.pdf.livecycle.LifeCycleController;
import alex.ru.utils.pdf.converter.PdfCommonConverter;
import alex.ru.utils.pdf.converter.PdfConverter;
import alex.ru.utils.pdf.livecycle.PdfLiveCycleController;
import alex.ru.utils.pdf.queue.PdfDynamicQueue;
import alex.ru.utils.pdf.queue.PdfQueue;
import alex.ru.utils.viewcache.MapCache;
import alex.ru.utils.viewcache.MapUrlToViewCache;
import alex.ru.utils.validation.ValidationUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.web.context.request.RequestContextListener;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

@Configuration
@PropertySource("classpath:pdf.properties")
public class MiscBeans {

    @Value("${pdf.pathToTmpDir}")
    private String pathToTempDir;

    @Value("${pdf.maxUploadSize}")
    private String maxUploadSize;

    @Bean
    public RequestContextListener requestContextListener() {
        return new RequestContextListener();
    }

    @Bean
    public MapCache urlCash() {
        final MapCache mapCache = new MapUrlToViewCache();
        mapCache.addUrl("localhost:8081","index");
        mapCache.addUrl("registration", "registration");
        return mapCache;
    }

    @Bean
    public MessageSource messageSource() {
        final ReloadableResourceBundleMessageSource source = new ReloadableResourceBundleMessageSource();
        source.setBasenames("classpath:/validation",
                "classpath:/pdf",
                "classpath:/recaptcha");
        source.setFallbackToSystemLocale(false);
        source.setCacheSeconds(60 * 30);
        return source;
    }

    @Bean("multipartPdfResolver")
    public MultipartResolver multipartPdfResolver() {
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
        multipartResolver.setMaxUploadSize(Long.valueOf(maxUploadSize));
        return multipartResolver;
    }

    @Bean
    public PdfConverter pdfCommonConverter() {
        final  PdfCommonConverter pdfConverter = new PdfCommonConverter();
        pdfConverter.setPathToTempDir(pathToTempDir);
        pdfConverter.setPdfLifeCycleController(lifeCycleController());
        return pdfConverter;
    }

    @Bean
    public PdfQueue<String> pdfQueue() {
        return new PdfDynamicQueue<>();
    }

    @Bean
    public LifeCycleController lifeCycleController() {
        PdfLiveCycleController liveCycleController = new PdfLiveCycleController();
        liveCycleController.setPathToTempDir(pathToTempDir);
        return liveCycleController;
    }

    @Bean
    public ValidationUtil validationUtil() {
        return new ValidationUtil();
    }

    @Bean
    public ThreadPoolTaskScheduler threadPoolTaskScheduler(){
        ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
        threadPoolTaskScheduler.setPoolSize(1);
        threadPoolTaskScheduler.setThreadNamePrefix(
                "--ThreadPoolTaskScheduler");
        return threadPoolTaskScheduler;
    }
}
