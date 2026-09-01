package controller;

import exception.ExceptionesNegocio;
import model.Usuario;
import service.AuthService;
import view.TallerExpressView;


public class MainController {
    private final AuthService authService = new AuthService();
    private final TallerExpressView view = new TallerExpressView();

    public void iniciar() {

        String[] opciones = {"Iniciar Sesión", "Salir"};

        int seleccion;

        do {

            seleccion = view.option("MENÚ PRINCIPAL", "Bienvenido a TallerExpress", opciones);

            switch (seleccion) {

                case 0 -> iniciarSesion();

                case 1 -> view.message("Cerrando el sistema.");

                default -> seleccion = 1;
            }

        } while (seleccion != 1);
    }

    private void iniciarSesion() {

        try {

            String correo = view.input("Ingrese su correo:");
            if (correo == null) return;

            String contrasena = view.input("Ingrese su contraseña:");
            if (contrasena == null) return;

            correo = correo.trim();
            contrasena = contrasena.trim();

            Usuario usuario = authService.autenticar(correo, contrasena);

            view.message("Inicio de sesión exitoso.");

            if (usuario.getRol().equalsIgnoreCase("ADMIN")) {
                new AdminController().iniciarMenu();
            } else if (usuario.getRol().equalsIgnoreCase("RECEPCIONISTA")) {
                new RecepcionistaController().iniciarMenu();
            }

        } catch (ExceptionesNegocio e) {

            view.error(e.getMessage());
        }
    }
    
}
