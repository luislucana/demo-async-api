package br.com.async;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * https://howtodoinjava.com/spring-restful/multipart-upload-download-example/
 * https://github.com/eugenp/tutorials/tree/master/spring-rest-http
 */
//@EnableConfigurationProperties({
        //FileStorageProperties.class
//})
@EnableAutoConfiguration
@ComponentScan("br.com.async.*")
@EntityScan("br.com.async.model")
@EnableJpaRepositories("br.com.async.repository")
@EnableTransactionManagement
@SpringBootApplication
public class Application extends SpringBootServletInitializer {

    public static void main(final String[] args) {
        SpringApplication.run(Application.class, args);
    }
}