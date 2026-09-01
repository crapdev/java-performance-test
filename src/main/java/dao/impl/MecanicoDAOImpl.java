
package dao.impl;

import config.DatabaseConnection;
import dao.MecanicoDAO;
import java.util.ArrayList;
import java.util.List;
import model.aux.Mecanico;
import java.sql.*;


public class MecanicoDAOImpl implements MecanicoDAO{
    @Override
    public List<Mecanico> listarMecanicos() {
        System.out.println("[HTTP GET] -> /api/mecanicos | Cargando plantilla activa de mecánicos.");
        List<Mecanico> lista = new ArrayList<>();
        String sql = "SELECT * FROM Mecanicos ORDER BY nombre";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                lista.add(new Mecanico(resultSet.getInt("id"), resultSet.getString("nombre")));
            }
            System.out.println("[HTTP RESPONSE 200 OK] -> Operarios activos encontrados: " + lista.size());
        } catch (SQLException e) {
            System.out.println("[HTTP RESPONSE 500 Internal Server Error] -> " + e.getMessage());
        }
        return lista;
    }
}
