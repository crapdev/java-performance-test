
package controller;

import view.TallerExpressView;

/**
 *
 * @author cohorte5
 */
public class RecepcionistaController {
    private final TallerExpressView view = new TallerExpressView();

    public void iniciarMenu() {

        String[] opciones = {
            "Gestión de Repuestos",
            "Gestión de Clientes y Vehículos",
            "Gestión de Órdenes",
            "Cerrar Sesión"
        };

        int seleccion;

        do {

            seleccion = view.option("RECEPCIONISTA", "Seleccione una opción:", opciones);

            switch (seleccion) {

                case 0 -> new RepuestoController().iniciarMenu();

                case 1 -> new ClienteVehiculoController().iniciarMenu();

                case 2 -> new OrdenController().iniciarMenu();

                case 3 -> view.message("Cerrando sesión.");

                default -> seleccion = 3;
            }

        } while (seleccion != 3);
    }
}
