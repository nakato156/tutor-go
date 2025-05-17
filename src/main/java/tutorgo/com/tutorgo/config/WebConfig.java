//package tutorgo.com.tutorgo.config; // Asegúrate que el paquete sea correcto
//
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.CorsRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
//@Configuration
//public class WebConfig implements WebMvcConfigurer {
//
//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/**") // Aplica a todas las rutas de tu API bajo /api/v1 (o lo que tengas)
//                // Lista los orígenes que quieres permitir.
//                // Para desarrollo con Postman, "*" es permisivo.
//                // Para un frontend en localhost:3000, usa "http://localhost:3000".
//                .allowedOrigins("http://localhost:3000", "http://localhost:8080", "*") // "*" permite cualquier origen (¡cuidado en producción!)
//                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH", "HEAD") // Métodos HTTP permitidos
//                .allowedHeaders("*") // Permite todos los headers en la solicitud
//                .allowCredentials(false) // Generalmente false si usas tokens en headers y no cookies de sesión complejas. Si es true, allowedOrigins no puede ser "*".
//                .maxAge(3600); // Tiempo en segundos que el resultado de una pre-flight request puede ser cacheado.
//    }
//}