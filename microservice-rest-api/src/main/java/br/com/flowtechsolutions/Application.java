package br.com.flowtechsolutions;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = {
    "br.com.flowtechsolutions"
})
@EnableJpaRepositories(basePackages = {
        "br.com.flowtechsolutions.database.repository",
        "br.com.flowtechsolutions.dataproviders.database.repository"
})
@EntityScan(basePackages = {
        "br.com.flowtechsolutions.database.entity",
        "br.com.flowtechsolutions.dataproviders.database.entity"
})
public class Application {
    public static void main(final String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
