package tutorgo.com.tutorgo.service;

import jakarta.transaction.Transactional;

public interface UsuarioService {
    @Transactional
    void eliminarPerfilUsuario(Integer usuarioId);
}
