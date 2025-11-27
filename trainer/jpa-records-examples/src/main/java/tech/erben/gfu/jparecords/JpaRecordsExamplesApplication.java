package tech.erben.gfu.jparecords;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

@SpringBootApplication
public class JpaRecordsExamplesApplication {

    public static void main(String[] args) {
        SpringApplication.run(JpaRecordsExamplesApplication.class, args);
    }

    @Bean
    @Profile("!test")
    @ConditionalOnBean(RecordDemoService.class)
    CommandLineRunner demoRunner(RecordDemoService demoService) {
        return args -> demoService.logExamples();
    }
}
