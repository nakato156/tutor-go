package tutorgo.com.tutorgo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;

@SpringBootApplication
public class TutorGoApplication {
    
    static {
        // Deshabilitar la validación del esquema antes de iniciar la aplicación
        System.setProperty("hibernate.schema_validation.enabled", "false");
    }
    
    public static void main(String[] args) {
        SpringApplication.run(TutorGoApplication.class, args);
    }
}
