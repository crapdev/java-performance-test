
package service;

import dao.impl.UsuarioDAOImpl;
import exception.ExceptionesNegocio;
import model.Usuario;

public class AuthService {
    private final UsuarioDAOImpl usuarioDAO = new UsuarioDAOImpl();

    public Usuario autenticar(String correo, String contrasena) throws ExceptionesNegocio {

        if (correo == null || correo.isBlank()) {
            throw new ExceptionesNegocio("El correo es obligatorio.");
        }

        if (contrasena == null || contrasena.isBlank()) {
            throw new ExceptionesNegocio("La contraseña es obligatoria.");
        }

        Usuario usuario = usuarioDAO.login(correo, contrasena);

        if (usuario == null) {
            throw new ExceptionesNegocio("Credenciales incorrectas o usuario inactivo.");
        }

        return usuario;
    }
}
