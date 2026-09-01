package controller;

import exception.ExceptionesNegocio;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import model.Cliente;
import model.Orden;
import model.Repuesto;
import model.Vehiculo;
import model.aux.Mecanico;
import model.aux.OrdenRepuesto;
import service.ClienteVehiculoService;
import service.OrdenService;
import service.RepuestoService;
import view.TallerExpressView;

public class OrdenController {

    private final TallerExpressView view = new TallerExpressView();
    private final OrdenService ordenService = new OrdenService();
    private final RepuestoService repuestoService = new RepuestoService();
    private final ClienteVehiculoService clienteVehiculoService = new ClienteVehiculoService();

    public void iniciarMenu() {

        String[] opciones = {
            "Registrar Orden",
            "Actualizar Estado",
            "Historial por Vehículo",
            "Calcular Costo Total",
            "Volver"
        };

        int seleccion;

        do {

            seleccion = view.option("GESTIÓN DE ÓRDENES", "Seleccione una opción:", opciones);

            switch (seleccion) {

                case 0 -> registrarOrden();

                case 1 -> actualizarEstado();

                case 2 -> consultarHistorial();

                case 3 -> calcularCostoTotal();

                case 4 -> {
                }

                default -> seleccion = 4;
            }

        } while (seleccion != 4);
    }

    private void registrarOrden() {

        try {

            mostrarClientes();

            String clienteTexto = view.input("ID del cliente:");
            if (clienteTexto == null) return;

            long idCliente = Long.parseLong(clienteTexto.trim());

            List<Vehiculo> vehiculos = clienteVehiculoService.consultarVehiculosPorCliente(idCliente);

            if (vehiculos.isEmpty()) {
                view.error("El cliente no tiene vehículos registrados.");
                return;
            }

            mostrarVehiculos(vehiculos);

            String vehiculoTexto = view.input("ID del vehículo:");
            if (vehiculoTexto == null) return;

            int idVehiculo = Integer.parseInt(vehiculoTexto.trim());

            mostrarMecanicos();

            String mecanicoTexto = view.input("ID del mecánico:");
            if (mecanicoTexto == null) return;

            int idMecanico = Integer.parseInt(mecanicoTexto.trim());

            String descripcion = view.input("Descripción del problema:");
            if (descripcion == null) return;

            String diagnostico = view.input("Diagnóstico:");
            if (diagnostico == null) return;

            String[] estados = {
                "PENDIENTE",
                "REPARANDO",
                "TERMINADO"
            };

            int opcionEstado = view.option("ESTADO", "Seleccione el estado:", estados);

            if (opcionEstado == -1) return;

            List<OrdenRepuesto> repuestosUtilizados = new ArrayList<>();

            while (view.confirm("¿Desea agregar un repuesto a la orden?")) {

                mostrarRepuestos();

                String repuestoTexto = view.input("ID del repuesto:");
                if (repuestoTexto == null) return;

                long idRepuesto = Long.parseLong(repuestoTexto.trim());

                Repuesto repuesto = repuestoService.buscarPorId(idRepuesto);

                String cantidadTexto = view.input("Cantidad utilizada:");
                if (cantidadTexto == null) return;

                short cantidad = Short.parseShort(cantidadTexto.trim());

                OrdenRepuesto detalle = new OrdenRepuesto(
                        0,
                        idRepuesto,
                        cantidad,
                        repuesto.getPrecioUnitario()
                );

                repuestosUtilizados.add(detalle);
            }

            Orden orden = new Orden(
                    0,
                    idCliente,
                    idVehiculo,
                    idMecanico,
                    new Timestamp(System.currentTimeMillis()),
                    descripcion.trim(),
                    diagnostico.trim(),
                    estados[opcionEstado]
            );

            ordenService.registrarOrden(orden, repuestosUtilizados);

            view.message("Orden registrada correctamente.");

        } catch (NumberFormatException e) {

            view.error("Alguno de los valores numéricos no es válido.");

        } catch (ExceptionesNegocio e) {

            view.error(e.getMessage());
        }
    }

    private void actualizarEstado() {

        try {

            String idTexto = view.input("ID de la orden:");
            if (idTexto == null) return;

            long idOrden = Long.parseLong(idTexto.trim());

            String[] estados = {
                "PENDIENTE",
                "REPARANDO",
                "TERMINADO"
            };

            int seleccion = view.option("ESTADO", "Seleccione el nuevo estado:", estados);

            if (seleccion == -1) return;

            ordenService.actualizarEstado(idOrden, estados[seleccion]);

            view.message("Estado actualizado correctamente.");

        } catch (NumberFormatException e) {

            view.error("El ID debe ser numérico.");

        } catch (ExceptionesNegocio e) {

            view.error(e.getMessage());
        }
    }

    private void consultarHistorial() {

        try {

            String idTexto = view.input("ID del vehículo:");
            if (idTexto == null) return;

            int idVehiculo = Integer.parseInt(idTexto.trim());

            List<Orden> ordenes = ordenService.consultarHistorialPorVehiculo(idVehiculo);

            if (ordenes.isEmpty()) {
                view.message("El vehículo no tiene órdenes registradas.");
                return;
            }

            StringBuilder texto = new StringBuilder("HISTORIAL DE SERVICIOS\n\n");

            for (Orden orden : ordenes) {

                texto.append("Orden: ").append(orden.getId())
                        .append("\nFecha: ").append(orden.getFechaIngreso())
                        .append("\nProblema: ").append(orden.getDescriptionProblema())
                        .append("\nDiagnóstico: ").append(orden.getDiagnostico())
                        .append("\nEstado: ").append(orden.getEstado())
                        .append("\n--------------------------\n");
            }

            view.message(texto.toString());

        } catch (NumberFormatException e) {

            view.error("El ID debe ser numérico.");

        } catch (ExceptionesNegocio e) {

            view.error(e.getMessage());
        }
    }

    private void calcularCostoTotal() {

        try {

            String idTexto = view.input("ID de la orden:");
            if (idTexto == null) return;

            long idOrden = Long.parseLong(idTexto.trim());

            double total = ordenService.calcularCostoTotal(idOrden);

            view.message("Costo total de la reparación: $" + total);

        } catch (NumberFormatException e) {

            view.error("El ID debe ser numérico.");

        } catch (ExceptionesNegocio e) {

            view.error(e.getMessage());
        }
    }

    private void mostrarClientes() {

        List<Cliente> clientes = clienteVehiculoService.listarClientes();

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

        StringBuilder texto = new StringBuilder("VEHÍCULOS\n\n");

        for (Vehiculo vehiculo : vehiculos) {

            texto.append("ID: ").append(vehiculo.getId())
                    .append(" | ")
                    .append(vehiculo.getMarca())
                    .append(" ")
                    .append(vehiculo.getModelo())
                    .append(" | ")
                    .append(vehiculo.getPlaca())
                    .append("\n");
        }

        view.message(texto.toString());
    }

    private void mostrarMecanicos() {

        List<Mecanico> mecanicos = ordenService.listarMecanicos();

        StringBuilder texto = new StringBuilder("MECÁNICOS\n\n");

        for (Mecanico mecanico : mecanicos) {

            texto.append("ID: ")
                    .append(mecanico.getId())
                    .append(" | ")
                    .append(mecanico.getNombre())
                    .append("\n");
        }

        view.message(texto.toString());
    }

    private void mostrarRepuestos() {

        List<Repuesto> repuestos = repuestoService.listarRepuestos();

        StringBuilder texto = new StringBuilder("REPUESTOS DISPONIBLES\n\n");

        for (Repuesto repuesto : repuestos) {

            texto.append("ID: ").append(repuesto.getId())
                    .append(" | ")
                    .append(repuesto.getNombre())
                    .append(" | Stock: ")
                    .append(repuesto.getStockDisponible())
                    .append(" | Precio: $")
                    .append(repuesto.getPrecioUnitario())
                    .append("\n");
        }

        view.message(texto.toString());
    }
}
