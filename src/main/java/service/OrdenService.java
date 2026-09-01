
package service;

import dao.impl.MecanicoDAOImpl;
import dao.impl.OrdenDAOImpl;
import dao.impl.VehiculoDAOImple;
import exception.ExceptionesNegocio;
import java.util.List;
import model.Orden;
import model.Vehiculo;
import model.aux.Mecanico;
import model.aux.OrdenRepuesto;


public class OrdenService {
    private final OrdenDAOImpl ordenDAO = new OrdenDAOImpl();
    private final VehiculoDAOImple vehiculoDAO = new VehiculoDAOImple();
    private final MecanicoDAOImpl mecanicoDAO = new MecanicoDAOImpl();

    public void registrarOrden(Orden orden, List<OrdenRepuesto> repuestos) throws ExceptionesNegocio {

        if (orden.getIdCliente() <= 0) {
            throw new ExceptionesNegocio("El cliente seleccionado no es válido.");
        }

        if (orden.getIdVehiculo() <= 0) {
            throw new ExceptionesNegocio("El vehículo seleccionado no es válido.");
        }

        if (orden.getIdMecanico() <= 0) {
            throw new ExceptionesNegocio("El mecánico seleccionado no es válido.");
        }

        if (orden.getDescriptionProblema() == null || orden.getDescriptionProblema().isBlank()) {
            throw new ExceptionesNegocio("La descripción del problema es obligatoria.");
        }

        validarEstado(orden.getEstado());

        boolean vehiculoPerteneceCliente = false;

        for (Vehiculo vehiculo : vehiculoDAO.consultarVehiculosPorCliente(orden.getIdCliente())) {
            if (vehiculo.getId() == orden.getIdVehiculo()) {
                vehiculoPerteneceCliente = true;
                break;
            }
        }

        if (!vehiculoPerteneceCliente) {
            throw new ExceptionesNegocio("El vehículo seleccionado no pertenece al cliente.");
        }

        boolean mecanicoExiste = false;

        for (Mecanico mecanico : mecanicoDAO.listarMecanicos()) {
            if (mecanico.getId() == orden.getIdMecanico()) {
                mecanicoExiste = true;
                break;
            }
        }

        if (!mecanicoExiste) {
            throw new ExceptionesNegocio("El mecánico seleccionado no existe.");
        }

        for (OrdenRepuesto repuesto : repuestos) {

            if (repuesto.getCantidadUsada() <= 0) {
                throw new ExceptionesNegocio("La cantidad utilizada debe ser mayor que cero.");
            }

            if (repuesto.getPrecioHistorico() <= 0) {
                throw new ExceptionesNegocio("El precio histórico del repuesto no es válido.");
            }
        }

        if (!ordenDAO.registrarOrden(orden, repuestos)) {
            throw new ExceptionesNegocio("No se pudo registrar la orden. Verifique el stock de los repuestos.");
        }
    }

    public void actualizarEstado(long idOrden, String estado) throws ExceptionesNegocio {

        if (idOrden <= 0) {
            throw new ExceptionesNegocio("El ID de la orden no es válido.");
        }

        validarEstado(estado);

        if (!ordenDAO.actualizarEstado(idOrden, estado)) {
            throw new ExceptionesNegocio("No se encontró la orden.");
        }
    }

    public List<Orden> consultarHistorialPorVehiculo(int idVehiculo) throws ExceptionesNegocio {

        if (idVehiculo <= 0) {
            throw new ExceptionesNegocio("El vehículo seleccionado no es válido.");
        }

        return ordenDAO.consultarHistorialPorVehiculo(idVehiculo);
    }

    public double calcularCostoTotal(long idOrden) throws ExceptionesNegocio {

        if (idOrden <= 0) {
            throw new ExceptionesNegocio("El ID de la orden no es válido.");
        }

        return ordenDAO.calcularCostoTotalReparacion(idOrden);
    }

    public List<Mecanico> listarMecanicos() {
        return mecanicoDAO.listarMecanicos();
    }

    private void validarEstado(String estado) throws ExceptionesNegocio {

        if (estado == null || estado.isBlank()) {
            throw new ExceptionesNegocio("El estado es obligatorio.");
        }

        if (!estado.equalsIgnoreCase("PENDIENTE")
                && !estado.equalsIgnoreCase("REPARANDO")
                && !estado.equalsIgnoreCase("TERMINADO")) {

            throw new ExceptionesNegocio("El estado de la orden no es válido.");
        }
    }
}
