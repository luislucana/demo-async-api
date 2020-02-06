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
 * https://www.thetechnojournals.com/2019/10/asynchronous-rest-service.html
 * https://www.baeldung.com/async-http-client
 * https://howtodoinjava.com/spring-boot2/rest/enableasync-async-controller/
 * https://spring.io/blog/2012/05/16/spring-mvc-3-2-preview-chat-sample/
 * https://howtodoinjava.com/spring-restful/multipart-upload-download-example/
 * https://github.com/eugenp/tutorials/tree/master/spring-rest-http
 * https://github.com/emmanuelneri-blog/spring-boot-redis
 * https://emmanuelneri.com.br/2019/04/30/cache-distribuido-com-redis-no-spring-boot/
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
public class Application {

    public static void main(final String[] args) {
        SpringApplication.run(Application.class, args);
    }
}