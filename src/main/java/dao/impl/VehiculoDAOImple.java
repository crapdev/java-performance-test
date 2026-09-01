
package dao.impl;

import config.DatabaseConnection;
import dao.VehiculoDAO;
import java.sql.*;
import model.Cliente;
import model.Vehiculo;
import java.util.ArrayList;
import java.util.List;


public class VehiculoDAOImple implements VehiculoDAO{
    @Override
    public boolean registrarCliente(Cliente cliente) {
        System.out.println("[HTTP POST] -> /api/clientes | Creando nueva ficha de cliente: " + cliente.getNombre());
        String sql = "INSERT INTO Clientes (nombre) VALUES (?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, cliente.getNombre());
            
            boolean exito = statement.executeUpdate() > 0;
            if (exito) System.out.println("[HTTP RESPONSE 201 Created] -> Cliente registrado exitosamente.");
            return exito;
        } catch (SQLException e) {
            System.out.println("[HTTP RESPONSE 400 Bad Request] -> Error al registrar cliente: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean registrarVehiculo(Vehiculo vehiculo) {
        System.out.println("[HTTP POST] -> /api/vehiculos | Vinculando vehiculo placa [" + vehiculo.getPlaca() + "] al cliente ID: " + vehiculo.getIdCliente());
        String sql = "INSERT INTO Vehiculos (id_cliente, marca, modelo, placa) VALUES (?, ?, ?, ?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, vehiculo.getIdCliente());
            statement.setString(2, vehiculo.getMarca());
            statement.setString(3, vehiculo.getModelo());
            statement.setString(4, vehiculo.getPlaca());
            
            boolean exito = statement.executeUpdate() > 0;
            if (exito) System.out.println("[HTTP RESPONSE 201 Created] -> Vehiculo añadido al cliente.");
            return exito;
        } catch (SQLException e) {
            System.out.println("[HTTP RESPONSE 400 Bad Request] -> Error al registrar vehiculo: " + e.getMessage());
            return false;
        }
    }

    @Override
    public List<Cliente> listarClientes() {
        System.out.println("[HTTP GET] -> /api/clientes | Obteniendo base global de clientes.");
        List<Cliente> lista = new ArrayList<>();
        String sql = "SELECT * FROM Clientes ORDER BY nombre";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                lista.add(new Cliente(resultSet.getLong("id"), resultSet.getString("nombre")));
            }
            System.out.println("[HTTP RESPONSE 200 OK] -> Clientes cargados: " + lista.size());
        } catch (SQLException e) {
            System.out.println("[HTTP RESPONSE 500 Internal Server Error] -> " + e.getMessage());
        }
        return lista;
    }

    @Override
    public List<Vehiculo> consultarVehiculosPorCliente(long idCliente) {
        System.out.println("[HTTP GET] -> /api/clientes/" + idCliente + "/vehiculos | Buscando vehiculos propiedad del cliente.");
        List<Vehiculo> lista = new ArrayList<>();
        String sql = "SELECT * FROM Vehiculos WHERE id_cliente = ? ORDER BY id";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, idCliente);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    lista.add(new Vehiculo(
                        resultSet.getInt("id"),
                        resultSet.getLong("id_cliente"),
                        resultSet.getString("marca"),
                        resultSet.getString("modelo"),
                        resultSet.getString("placa")
                    ));
                }
            }
            System.out.println("[HTTP RESPONSE 200 OK] -> Vehiculos encontrados para el cliente: " + lista.size());
        } catch (SQLException e) {
            System.out.println("[HTTP RESPONSE 500 Internal Server Error] -> " + e.getMessage());
        }
        return lista;
    }

    @Override
    public boolean existePlaca(String placa) {
        System.out.println("[HTTP GET] -> /api/vehiculos/validar-placa?placa=" + placa + " | Validando unicidad de placa.");
        String sql = "SELECT COUNT(*) FROM Vehiculos WHERE placa = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, placa);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    boolean existe = resultSet.getInt(1) > 0;
                    System.out.println("[HTTP RESPONSE 200 OK] -> ¿Placa duplicada?: " + existe);
                    return existe;
                }
            }
        } catch (SQLException e) {
            System.out.println("[HTTP RESPONSE 500 Internal Server Error] -> " + e.getMessage());
        }
        return false;
    }
}
