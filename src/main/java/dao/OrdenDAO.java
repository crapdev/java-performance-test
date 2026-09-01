/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package dao;

import java.util.List;
import model.Orden;
import model.aux.OrdenRepuesto;

/**
 *
 * @author cohorte5
 */
public interface OrdenDAO {
    // Registra cabecera y detalle de repuestos en una sola transacción
    boolean registrarOrden(Orden orden, List<OrdenRepuesto> repuestosUtilizados);
    boolean actualizarEstado(long idOrden, String nuevoEstado);
    List<Orden> consultarHistorialPorVehiculo(int idVehiculo);
    double calcularCostoTotalReparacion(long idOrden); // Multiplica cant * precio hist

}
