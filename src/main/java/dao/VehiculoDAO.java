
package dao;

import java.util.List;
import model.Cliente;
import model.Vehiculo;

/**
 *
 * @author cohorte5
 */
public interface VehiculoDAO {
    
    boolean registrarCliente(Cliente cliente);
    boolean registrarVehiculo(Vehiculo vehiculo);
    List<Cliente> listarClientes();
    List<Vehiculo> consultarVehiculosPorCliente(long idCliente);
    boolean existePlaca(String placa); // Validación obligatoria

}
