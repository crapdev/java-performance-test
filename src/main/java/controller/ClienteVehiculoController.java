
package controller;

import exception.ExceptionesNegocio;
import java.util.List;
import model.Cliente;
import model.Vehiculo;
import service.ClienteVehiculoService;
import view.TallerExpressView;

public class ClienteVehiculoController {

    private final TallerExpressView view = new TallerExpressView();
    private final ClienteVehiculoService clienteVehiculoService = new ClienteVehiculoService();

    public void iniciarMenu() {

        String[] opciones = {
            "Registrar Cliente",
            "Registrar Vehículo",
            "Consultar Vehículos por Cliente",
            "Volver"
        };

        int seleccion;

        do {

            seleccion = view.option("CLIENTES Y VEHÍCULOS", "Seleccione una opción:", opciones);

            switch (seleccion) {

                case 0 -> registrarCliente();

                case 1 -> registrarVehiculo();

                case 2 -> consultarVehiculosPorCliente();

                case 3 -> {
                }

                default -> seleccion = 3;
            }

        } while (seleccion != 3);
    }

    private void registrarCliente() {

        try {

            String nombre = view.input("Nombre del cliente:");
            if (nombre == null) return;

            nombre = nombre.trim();

            Cliente cliente = new Cliente(0, nombre);

            clienteVehiculoService.registrarCliente(cliente);

            view.message("Cliente registrado correctamente.");

        } catch (ExceptionesNegocio e) {

            view.error(e.getMessage());
        }
    }

    private void registrarVehiculo() {

        try {

            mostrarClientes();

            String clienteTexto = view.input("ID del cliente:");
            if (clienteTexto == null) return;

            long idCliente = Long.parseLong(clienteTexto.trim());

            String marca = view.input("Marca:");
            if (marca == null) return;

            String modelo = view.input("Modelo:");
            if (modelo == null) return;

            String placa = view.input("Placa:");
            if (placa == null) return;

            marca = marca.trim();
            modelo = modelo.trim();
            placa = placa.trim().toUpperCase();

            Vehiculo vehiculo = new Vehiculo(
                    0,
                    idCliente,
                    marca,
                    modelo,
                    placa
            );

            clienteVehiculoService.registrarVehiculo(vehiculo);

            view.message("Vehículo registrado correctamente.");

        } catch (NumberFormatException e) {

            view.error("El ID del cliente debe ser numérico.");

        } catch (ExceptionesNegocio e) {

            view.error(e.getMessage());
        }
    }

    private void consultarVehiculosPorCliente() {

        try {

            mostrarClientes();

            String texto = view.input("ID del cliente:");
            if (texto == null) return;

            long idCliente = Long.parseLong(texto.trim());

            List<Vehiculo> vehiculos = clienteVehiculoService.consultarVehiculosPorCliente(idCliente);

            mostrarVehiculos(vehiculos);

        } catch (NumberFormatException e) {

            view.error("El ID debe ser numérico.");

        } catch (ExceptionesNegocio e) {

            view.error(e.getMessage());
        }
    }

    private void mostrarClientes() {

        List<Cliente> clientes = clienteVehiculoService.listarClientes();

        if (clientes.isEmpty()) {
            view.message("No existen clientes registrados.");
            return;
        }

        StringBuilder texto = new StringBuilder("CLIENTES\n\n");

        for (Cliente cliente : clientes) {

            texto.append("ID: ")
                    .append(cliente.getId())
                    .append(" | ")
                    .append(cliente.getNombre())
                    .append("\n");
        }

        view.message(texto.toString());
    }

    private void mostrarVehiculos(List<Vehiculo> vehiculos) {

        if (vehiculos.isEmpty()) {
            view.message("El cliente no tiene vehículos registrados.");
            return;
        }

        StringBuilder texto = new StringBuilder("VEHÍCULOS\n\n");

        for (Vehiculo vehiculo : vehiculos) {

            texto.append("ID: ").append(vehiculo.getId())
                    .append("\nMarca: ").append(vehiculo.getMarca())
                    .append("\nModelo: ").append(vehiculo.getModelo())
                    .append("\nPlaca: ").append(vehiculo.getPlaca())
                    .append("\n--------------------------\n");
        }

        view.message(texto.toString());
    }
}