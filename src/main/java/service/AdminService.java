
package service;

import dao.impl.UsuarioDAOImpl;
import exception.ExceptionesNegocio;
import java.util.List;
import model.Usuario;

/**
 *
 * @author cohorte5
 */
public class AdminService {
    private final UsuarioDAOImpl usuarioDAO = new UsuarioDAOImpl();

    public void crearUsuario(Usuario usuario) throws ExceptionesNegocio {
        validarUsuario(usuario);

        if (!usuarioDAO.crear(usuario)) {
            throw new ExceptionesNegocio("No se pudo crear el usuario.");
        }
    }

    public void modificarUsuario(Usuario usuario) throws ExceptionesNegocio {

        if (usuario.getId() <= 0) {
            throw new ExceptionesNegocio("El ID del usuario debe ser válido.");
        }

        validarUsuario(usuario);

        if (!usuarioDAO.actualizar(usuario)) {
            throw new ExceptionesNegocio("No se encontró el usuario o no pudo actualizarse.");
        }
    }

    public void darDeBajaUsuario(int id) throws ExceptionesNegocio {

        if (id <= 0) {
            throw new ExceptionesNegocio("El ID debe ser mayor que cero.");
        }

        if (!usuarioDAO.eliminarLogico(id)) {
            throw new ExceptionesNegocio("No se pudo desactivar el usuario.");
        }
    }

    public List<Usuario> listarUsuarios() {
        return usuarioDAO.listarTodos();
    }

    private void validarUsuario(Usuario usuario) throws ExceptionesNegocio {

        if (usuario.getCorreo() == null || usuario.getCorreo().isBlank()) {
            throw new ExceptionesNegocio("El correo es obligatorio.");
        }

        if (usuario.getContraseña() == null || usuario.getContraseña().isBlank()) {
            throw new ExceptionesNegocio("La contraseña es obligatoria.");
        }

        if (!usuario.getRol().equalsIgnoreCase("ADMIN") && !usuario.getRol().equalsIgnoreCase("RECEPCIONISTA")) {
            throw new ExceptionesNegocio("El rol seleccionado no es válido.");
        }
    }
}
