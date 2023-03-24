package hau.kute.dojo;

import hau.kute.dojo.config.ApplicationProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({ApplicationProperties.class})
public class DojoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DojoApplication.class, args);
    }

}
