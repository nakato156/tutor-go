package tutorgo.com.tutorgo.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tutorgo.com.tutorgo.dto.request.RegisterRequest;
import tutorgo.com.tutorgo.dto.response.AccountResponse;
import tutorgo.com.tutorgo.model.entity.Rol;
import tutorgo.com.tutorgo.model.entity.Usuario;
import tutorgo.com.tutorgo.repository.RolRepository;

import java.sql.Timestamp;
import java.time.Instant;
import java.lang.reflect.Field;

@Component
public class AccountMapper {

    private final RolRepository rolRepository;

    @Autowired
    public AccountMapper(RolRepository rolRepository) {
        this.rolRepository = rolRepository;
    }

    public AccountResponse toAccountResponse(Usuario usuario) {
        if (usuario == null) {
            return null;
        }

        try {
            Field idField = Usuario.class.getDeclaredField("id");
            Field nombreField = Usuario.class.getDeclaredField("nombre");
            Field emailField = Usuario.class.getDeclaredField("email");
            Field rolField = Usuario.class.getDeclaredField("rol");
            Field horaCreacionField = Usuario.class.getDeclaredField("horaCreacion");

            idField.setAccessible(true);
            nombreField.setAccessible(true);
            emailField.setAccessible(true);
            rolField.setAccessible(true);
            horaCreacionField.setAccessible(true);

            Integer id = (Integer) idField.get(usuario);
            String nombre = (String) nombreField.get(usuario);
            String email = (String) emailField.get(usuario);
            Rol rol = (Rol) rolField.get(usuario);
            Timestamp horaCreacion = (Timestamp) horaCreacionField.get(usuario);

            String rolNombre = null;
            if (rol != null) {
                Field rolNombreField = Rol.class.getDeclaredField("nombre");
                rolNombreField.setAccessible(true);
                rolNombre = (String) rolNombreField.get(rol);
            }

            return new AccountResponse(
                    id,
                    nombre,
                    email,
                    rolNombre,
                    horaCreacion
            );
        } catch (Exception e) {
            // Si hay algún error con la reflexión, manejamos el error
            e.printStackTrace();
            return null;
        }
    }

    public Usuario toAccountEntity(RegisterRequest request) {
        if (request == null) {
            return null;
        }

        try {
            Usuario usuario = new Usuario();

            Field nombreField = Usuario.class.getDeclaredField("nombre");
            Field emailField = Usuario.class.getDeclaredField("email");
            Field passwordHashField = Usuario.class.getDeclaredField("passwordHash");
            Field horaCreacionField = Usuario.class.getDeclaredField("horaCreacion");

            nombreField.setAccessible(true);
            emailField.setAccessible(true);
            passwordHashField.setAccessible(true);
            horaCreacionField.setAccessible(true);

            nombreField.set(usuario, request.nombre());
            emailField.set(usuario, request.email());
            passwordHashField.set(usuario, request.password());
            horaCreacionField.set(usuario, Timestamp.from(Instant.now()));

            // Asignar rol si se proporciona
            if (request.rolId() != null) {
                rolRepository.findById(request.rolId())
                        .ifPresent(rol -> {
                            try {
                                Field rolField = Usuario.class.getDeclaredField("rol");
                                rolField.setAccessible(true);
                                rolField.set(usuario, rol);
                            } catch (Exception e) {
                                // Error handling
                                e.printStackTrace();
                            }
                        });
            }
    
            return usuario;
        } catch (Exception e) {
            // Si hay algún error con la reflexión, manejamos el error
            e.printStackTrace();
            return null;
        }
    }
}