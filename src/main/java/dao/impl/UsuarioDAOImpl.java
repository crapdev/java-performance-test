package dao.impl;

import config.DatabaseConnection;
import dao.UsuarioDAO;
import model.Usuario;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class UsuarioDAOImpl implements UsuarioDAO {

    @Override
    public Usuario login(String correo, String contrasena) {
        
        System.out.println("[HTTP POST] -> /api/auth/login | Iniciando autenticación para: " + correo);
        
        String sql = "SELECT * FROM Usuarios WHERE correo = ? AND contrasena = ? AND is_active = 1";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            
            statement.setString(1, correo);
            statement.setString(2, contrasena);
            
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    System.out.println("[HTTP RESPONSE 200 OK] -> Login exitoso.");
                    return new Usuario(
                        resultSet.getInt("id"),
                        resultSet.getString("correo"),
                        resultSet.getString("contrasena"),
                        resultSet.getLong("is_active"),
                        resultSet.getString("rol")
                    );
                }
            }
        } catch (SQLException e) {
            System.out.println("[HTTP RESPONSE 500 Internal Server Error] -> " + e.getMessage());
        }
        System.out.println("[HTTP RESPONSE 401 Unauthorized] -> Credenciales inválidas.");
        return null;
    }

    @Override
    public boolean crear(Usuario usuario) {
        System.out.println("[HTTP POST] -> /api/usuarios | Creando nuevo usuario: " + usuario.getCorreo());
        
        String sql = "INSERT INTO Usuarios (correo, contrasena, is_active, rol) VALUES (?, ?, ?, ?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            
            statement.setString(1, usuario.getCorreo());
            statement.setString(2, usuario.getContraseña());
            statement.setLong(3, usuario.getIsActive());
            statement.setString(4, usuario.getRol());
            
            boolean exito = statement.executeUpdate() > 0;
            if (exito) System.out.println("[HTTP RESPONSE 201 Created] -> Usuario guardado correctamente.");
            return exito;
        } catch (SQLException e) {
            System.out.println("[HTTP RESPONSE 400 Bad Request] -> Error al crear: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean actualizar(Usuario usuario) {
        System.out.println("[HTTP PUT] -> /api/usuarios/" + usuario.getId() + " | Actualizando datos completos del usuario.");
        
        String sql = "UPDATE Usuarios SET correo = ?, contrasena = ?, is_active = ?, rol = ? WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            
            statement.setString(1, usuario.getCorreo());
            statement.setString(2, usuario.getContraseña());
            statement.setLong(3, usuario.getIsActive());
            statement.setString(4, usuario.getRol());
            statement.setInt(5, usuario.getId());
            
            boolean exito = statement.executeUpdate() > 0;
            if (exito) System.out.println("[HTTP RESPONSE 200 OK] -> Usuario actualizado con éxito.");
            return exito;
        } catch (SQLException e) {
            System.out.println("[HTTP RESPONSE 400 Bad Request] -> Error al actualizar: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean eliminarLogico(int id) {
        System.out.println("[HTTP PATCH] -> /api/usuarios/" + id + "/desactivar | Solicitando baja lógica de cuenta.");
        
        String sql = "UPDATE Usuarios SET is_active = 0 WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            
            statement.setInt(1, id);
            boolean exito = statement.executeUpdate() > 0;
            if (exito) System.out.println("[HTTP RESPONSE 200 OK] -> Estado is_active cambiado a 0 (Inactivo).");
            return exito;
        } catch (SQLException e) {
            System.out.println("[HTTP RESPONSE 500 Internal Server Error] -> " + e.getMessage());
            return false;
        }
    }

    @Override
    public List<Usuario> listarTodos() {
        System.out.println("[HTTP GET] -> /api/usuarios | Cargando listado global de usuarios.");
        
        List<Usuario> lista = new ArrayList<>();
        String sql = "SELECT * FROM Usuarios ORDER BY id";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            
            while (resultSet.next()) {
                lista.add(new Usuario(
                    resultSet.getInt("id"),
                    resultSet.getString("correo"),
                    resultSet.getString("contrasena"),
                    resultSet.getLong("is_active"),
                    resultSet.getString("rol")
                ));
            }
            System.out.println("[HTTP RESPONSE 200 OK] -> Registros obtenidos: " + lista.size());
        } catch (SQLException e) {
            System.out.println("[HTTP RESPONSE 500 Internal Server Error] -> " + e.getMessage());
        }
        return lista;
    }
}


