package alex.ru.conf;

import org.postgresql.util.PSQLException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@ComponentScan("alex.ru")
@EnableJpaRepositories("alex.ru.dao")
@SpringBootApplication(exclude = HibernateJpaAutoConfiguration.class)
@EnableScheduling
public class AppContext extends SpringBootServletInitializer {

    public static void main(String[] args) {
            SpringApplication.run(AppContext.class, args);
    }

}
