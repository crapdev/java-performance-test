/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package dao;

import java.util.List;
import model.Repuesto;
import model.aux.Categoria;
import model.aux.Proveedor;

/**
 *
 * @author cohorte5
 */
public interface RepuestoDAO {
    
    boolean guardar(Repuesto repuesto);
    boolean editar(Repuesto repuesto);
    List<Repuesto> listarTodos();
    List<Repuesto> filtrarPorCategoria(int idCategoria);
    List<Repuesto> filtrarPorProveedor(int idProveedor);
    boolean existeCodigoReferencia(String codigoReferencia); // Validación obligatoria
    
    // Auxiliares para llenar ComboBoxes o JOptionPanes de filtros
    List<Categoria> listarCategorias();
    List<Proveedor> listarProveedores();

}
