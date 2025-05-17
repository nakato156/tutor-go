package tutorgo.com.tutorgo.config;

import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class JPAConfig {

    /**
     * Personaliza las propiedades de Hibernate para desactivar validaciones innecesarias.
     */
    @Bean
    public HibernatePropertiesCustomizer hibernatePropertiesCustomizer() {
        return hibernateProperties -> {
            hibernateProperties.put("hibernate.schema_validation.enabled", "false");
            hibernateProperties.put("hibernate.check_nullability", "false");
        };
    }
}
