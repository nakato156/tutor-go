package tutorgo.com.tutorgo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.sql.DataSource;
import org.springframework.boot.CommandLineRunner;

@Configuration
public class WebAppConfig implements WebMvcConfigurer {
    
    @Autowired
    private DataSource dataSource;
    
    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        // Configuración simplificada para que Spring maneje correctamente las rutas API
        // Removemos el método deprecado y usamos un enfoque más directo
        configurer.setUseSuffixPatternMatch(false);
    }
    
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // Configuración única de CORS para todas las rutas
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .maxAge(3600);
    }
    
    /**
     * Bean para crear/reiniciar secuencias en la base de datos al iniciar la aplicación
     */
    @Bean
    public CommandLineRunner initSequence() {
        return args -> {
            JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
            
            try {
                // Verificar el ID máximo existente
                Integer maxId = jdbcTemplate.queryForObject(
                    "SELECT COALESCE(MAX(id), 0) FROM sesiones", 
                    Integer.class
                );
                
                // Usar un valor seguro que sea mayor que cualquier ID existente
                Integer nuevoValorSecuencia = (maxId != null ? maxId : 0) + 50;
                
                System.out.println("ID máximo en tabla sesiones: " + maxId);
                System.out.println("Configurando secuencia para iniciar desde: " + nuevoValorSecuencia);
                
                // Intentar crear la secuencia si no existe
                try {
                    jdbcTemplate.execute("CREATE SEQUENCE IF NOT EXISTS sesion_id_seq START WITH " + nuevoValorSecuencia);
                    System.out.println("Secuencia creada o ya existente");
                } catch (Exception e) {
                    System.out.println("Nota al crear secuencia: " + e.getMessage());
                }
                
                // Actualizar el valor de la secuencia existente
                try {
                    jdbcTemplate.execute("ALTER SEQUENCE sesion_id_seq RESTART WITH " + nuevoValorSecuencia);
                    System.out.println("Secuencia actualizada correctamente");
                } catch (Exception e) {
                    System.out.println("Error al actualizar secuencia (puede ignorarse si se creó correctamente): " + e.getMessage());
                }
                
                // Verificación adicional
                try {
                    Integer valorActual = jdbcTemplate.queryForObject(
                        "SELECT last_value FROM sesion_id_seq", 
                        Integer.class
                    );
                    System.out.println("Valor actual de la secuencia después de actualizarla: " + valorActual);
                } catch (Exception e) {
                    System.out.println("No se pudo verificar el valor de la secuencia: " + e.getMessage());
                }
                
            } catch (Exception e) {
                System.out.println("Error durante la inicialización de secuencias: " + e.getMessage());
                e.printStackTrace();
            }
            
            System.out.println("Proceso de inicialización de secuencias completado");
        };
    }
}
