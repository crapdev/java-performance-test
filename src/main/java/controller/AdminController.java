/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import exception.ExceptionesNegocio;
import java.util.List;
import javax.swing.JOptionPane;
import model.Usuario;
import service.AdminService;
import view.TallerExpressView;

/**
 *
 * @author cohorte5
 */
public class AdminController {
    
    private final AdminService adminService = new AdminService();
    private final TallerExpressView view = new TallerExpressView();

    public void iniciarMenu() {

        String[] opciones = {
            "Crear Usuario",
            "Modificar Usuario",
            "Desactivar Usuario",
            "Listar Usuarios",
            "Cerrar Sesión"
        };

        int seleccion;

        do {

            seleccion = view.option("ADMINISTRADOR", "Seleccione una opción:", opciones);

            switch (seleccion) {

                case 0 -> crearUsuario();

                case 1 -> modificarUsuario();

                case 2 -> desactivarUsuario();

                case 3 -> listarUsuarios();

                case 4 -> view.message("Cerrando sesión.");

                default -> seleccion = 4;
            }

        } while (seleccion != 4);
    }

    private void crearUsuario() {

        try {

            String correo = view.input("Correo:");
            if (correo == null) return;

            String contrasena = view.input("Contraseña:");
            if (contrasena == null) return;

            String[] roles = {"ADMIN", "RECEPCIONISTA"};

            int opcionRol = view.option("ROL", "Seleccione el rol:", roles);
            if (opcionRol == -1) return;

            correo = correo.trim();
            contrasena = contrasena.trim();

            Usuario usuario = new Usuario(0, correo, contrasena, 1, roles[opcionRol]);

            adminService.crearUsuario(usuario);

            view.message("Usuario creado correctamente.");

        } catch (ExceptionesNegocio e) {

            view.error(e.getMessage());
        }
    }

    private void modificarUsuario() {

        try {

            String idTexto = view.input("ID del usuario:");
            if (idTexto == null) return;

            int id = Integer.parseInt(idTexto.trim());

            String correo = view.input("Nuevo correo:");
            if (correo == null) return;

            String contrasena = view.input("Nueva contraseña:");
            if (contrasena == null) return;

            String[] roles = {"ADMIN", "RECEPCIONISTA"};

            int opcionRol = view.option("ROL", "Seleccione el nuevo rol:", roles);
            if (opcionRol == -1) return;

            Usuario usuario = new Usuario(id, correo.trim(), contrasena.trim(), 1, roles[opcionRol]);

            adminService.modificarUsuario(usuario);

            view.message("Usuario actualizado correctamente.");

        } catch (NumberFormatException e) {

            view.error("El ID debe ser numérico.");

        } catch (ExceptionesNegocio e) {

            view.error(e.getMessage());
        }
    }

    private void desactivarUsuario() {

        try {

            String idTexto = view.input("ID del usuario:");
            if (idTexto == null) return;

            int id = Integer.parseInt(idTexto.trim());

            adminService.darDeBajaUsuario(id);

            view.message("Usuario desactivado correctamente.");

        } catch (NumberFormatException e) {

            view.error("El ID debe ser numérico.");

        } catch (ExceptionesNegocio e) {

            view.error(e.getMessage());
        }
    }

    private void listarUsuarios() {

        List<Usuario> usuarios = adminService.listarUsuarios();

        if (usuarios.isEmpty()) {
            view.message("No existen usuarios registrados.");
            return;
        }

        StringBuilder texto = new StringBuilder("USUARIOS\n\n");

        for (Usuario usuario : usuarios) {

            texto.append("ID: ").append(usuario.getId())
                 .append("\nCorreo: ").append(usuario.getCorreo())
                 .append("\nRol: ").append(usuario.getRol())
                 .append("\nEstado: ").append(usuario.getIsActive() == 1 ? "Activo" : "Inactivo")
                 .append("\n------------------------\n");
        }

        view.message(texto.toString());
    }

    
}
