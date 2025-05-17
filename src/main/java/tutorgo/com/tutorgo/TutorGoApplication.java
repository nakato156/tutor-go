package tutorgo.com.tutorgo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class TutorGoApplication {

    public static void main(String[] args) {
        SpringApplication.run(TutorGoApplication.class, args);
    }
}
