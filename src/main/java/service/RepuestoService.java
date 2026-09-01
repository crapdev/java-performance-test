
package service;

import dao.impl.RepuestoDAOImpl;
import exception.ExceptionesNegocio;
import java.util.List;
import model.Repuesto;
import model.aux.Categoria;
import model.aux.Proveedor;


public class RepuestoService {
    private final RepuestoDAOImpl repuestoDAO = new RepuestoDAOImpl();
    
    //acciones en la opcion 1
    public void registrarRepuesto(Repuesto repuesto) throws ExceptionesNegocio {

        validarRepuesto(repuesto);

        if (repuestoDAO.existeCodigoReferencia(repuesto.getCodigoReferencia())) {
            throw new ExceptionesNegocio("El código de referencia ya está registrado.");
        }

        if (!repuestoDAO.guardar(repuesto)) {
            throw new ExceptionesNegocio("No se pudo registrar el repuesto.");
        }
    }

    
    public void actualizarRepuesto(Repuesto repuesto) throws ExceptionesNegocio {

        if (repuesto.getId() <= 0) {
            throw new ExceptionesNegocio("El ID del repuesto debe ser válido.");
        }

        validarRepuesto(repuesto);

        List<Repuesto> repuestos = repuestoDAO.listarTodos();

        for (Repuesto existente : repuestos) {
            if (existente.getCodigoReferencia().equalsIgnoreCase(repuesto.getCodigoReferencia())
                    && existente.getId() != repuesto.getId()) {

                throw new ExceptionesNegocio("El código de referencia pertenece a otro repuesto.");
            }
        }

        if (!repuestoDAO.editar(repuesto)) {
            throw new ExceptionesNegocio("No se encontró el repuesto o no pudo actualizarse.");
        }
    }

    
    public List<Repuesto> listarRepuestos() {
        return repuestoDAO.listarTodos();
    }

    public List<Repuesto> filtrarPorCategoria(int idCategoria) throws ExceptionesNegocio {

        if (idCategoria <= 0) {
            throw new ExceptionesNegocio("La categoría seleccionada no es válida.");
        }

        return repuestoDAO.filtrarPorCategoria(idCategoria);
    }
    
    
    public List<Repuesto> filtrarPorProveedor(int idProveedor) throws ExceptionesNegocio {

        if (idProveedor <= 0) {
            throw new ExceptionesNegocio("El proveedor seleccionado no es válido.");
        }

        return repuestoDAO.filtrarPorProveedor(idProveedor);
    }

    public List<Categoria> obtenerCategorias() {
        return repuestoDAO.listarCategorias();
    }

    public List<Proveedor> obtenerProveedores() {
        return repuestoDAO.listarProveedores();
    }

    public Repuesto buscarPorId(long id) throws ExceptionesNegocio {

        if (id <= 0) {
            throw new ExceptionesNegocio("El ID del repuesto no es válido.");
        }

        for (Repuesto repuesto : repuestoDAO.listarTodos()) {
            if (repuesto.getId() == id) {
                return repuesto;
            }
        }

        throw new ExceptionesNegocio("No existe un repuesto con ese ID.");
    }

    
    
    private void validarRepuesto(Repuesto repuesto) throws ExceptionesNegocio {

        if (repuesto.getCodigoReferencia() == null || repuesto.getCodigoReferencia().isBlank()) {
            throw new ExceptionesNegocio("El código de referencia es obligatorio.");
        }

        if (repuesto.getNombre() == null || repuesto.getNombre().isBlank()) {
            throw new ExceptionesNegocio("El nombre del repuesto es obligatorio.");
        }

        if (repuesto.getIdCategoria() <= 0) {
            throw new ExceptionesNegocio("La categoría no es válida.");
        }

        if (repuesto.getIdProveedor() <= 0) {
            throw new ExceptionesNegocio("El proveedor no es válido.");
        }

        if (repuesto.getStockTotal() < 0 || repuesto.getStockDisponible() < 0) {
            throw new ExceptionesNegocio("El stock no puede ser negativo.");
        }

        if (repuesto.getStockDisponible() > repuesto.getStockTotal()) {
            throw new ExceptionesNegocio("El stock disponible no puede superar el stock total.");
        }

        if (repuesto.getPrecioUnitario() <= 0) {
            throw new ExceptionesNegocio("El precio debe ser mayor que cero.");
        }
    }
    
}
