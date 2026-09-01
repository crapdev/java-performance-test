/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import dao.impl.RepuestoDAOImpl;
import dao.impl.VehiculoDAOImple;
import exception.ExceptionesNegocio;
import java.util.List;
import model.Cliente;
import model.Repuesto;
import model.Vehiculo;
import model.aux.Categoria;
import model.aux.Proveedor;

/**
 *
 * @author cohorte5
 */
public class ClienteVehiculoService {
    private final VehiculoDAOImple vehiculoDAO = new VehiculoDAOImple();

    public void registrarCliente(Cliente cliente) throws ExceptionesNegocio {

        if (cliente.getNombre() == null || cliente.getNombre().isBlank()) {
            throw new ExceptionesNegocio("El nombre del cliente es obligatorio.");
        }

        if (!vehiculoDAO.registrarCliente(cliente)) {
            throw new ExceptionesNegocio("No se pudo registrar el cliente.");
        }
    }

    public void registrarVehiculo(Vehiculo vehiculo) throws ExceptionesNegocio {

        if (vehiculo.getIdCliente() <= 0) {
            throw new ExceptionesNegocio("Debe seleccionar un cliente válido.");
        }

        if (vehiculo.getMarca() == null || vehiculo.getMarca().isBlank()) {
            throw new ExceptionesNegocio("La marca es obligatoria.");
        }

        if (vehiculo.getModelo() == null || vehiculo.getModelo().isBlank()) {
            throw new ExceptionesNegocio("El modelo es obligatorio.");
        }

        if (vehiculo.getPlaca() == null || vehiculo.getPlaca().isBlank()) {
            throw new ExceptionesNegocio("La placa es obligatoria.");
        }

        if (vehiculoDAO.existePlaca(vehiculo.getPlaca())) {
            throw new ExceptionesNegocio("La placa ya está registrada.");
        }

        if (!vehiculoDAO.registrarVehiculo(vehiculo)) {
            throw new ExceptionesNegocio("No se pudo registrar el vehículo.");
        }
    }

    public List<Cliente> listarClientes() {
        return vehiculoDAO.listarClientes();
    }

    public List<Vehiculo> consultarVehiculosPorCliente(long idCliente) throws ExceptionesNegocio {

        if (idCliente <= 0) {
            throw new ExceptionesNegocio("El ID del cliente no es válido.");
        }

        return vehiculoDAO.consultarVehiculosPorCliente(idCliente);
    }
}
