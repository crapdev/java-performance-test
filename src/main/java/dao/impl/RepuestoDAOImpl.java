
package dao.impl;

import config.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import dao.RepuestoDAO;
import model.Repuesto;
import model.aux.Categoria;
import model.aux.Proveedor;

public class RepuestoDAOImpl implements RepuestoDAO {

     @Override
    public boolean guardar(Repuesto repuesto) {
        System.out.println("[HTTP POST] -> /api/repuestos | Registrando nuevo repuesto: Ref " + repuesto.getCodigoReferencia());
        String sql = """
            INSERT INTO Repuestos (codigo_referencia, nombre, categoria, proveedor, stock_total, stock_disponible, precio_unitario, is_activo) 
            VALUES (?, ?, ?, ?, ?, ?, ?, ?)
            """;
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, repuesto.getCodigoReferencia());
            statement.setString(2, repuesto.getNombre());
            statement.setInt(3, repuesto.getIdCategoria());
            statement.setInt(4, repuesto.getIdProveedor());
            statement.setLong(5, repuesto.getStockTotal());
            statement.setLong(6, repuesto.getStockDisponible());
            statement.setDouble(7, repuesto.getPrecioUnitario());
            statement.setBoolean(8, repuesto.isActivo());
            
            boolean exito = statement.executeUpdate() > 0;
            if (exito) System.out.println("[HTTP RESPONSE 201 Created] -> Repuesto guardado exitosamente.");
            return exito;
        } catch (SQLException e) {
            System.out.println("[HTTP RESPONSE 400 Bad Request] -> Error al guardar repuesto: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean editar(Repuesto repuesto) {
        System.out.println("[HTTP PUT] -> /api/repuestos/" + repuesto.getId() + " | Reemplazando informacion del repuesto.");
        String sql = """
            UPDATE Repuestos SET codigo_referencia = ?, nombre = ?, categoria = ?, proveedor = ?, 
                                 stock_total = ?, stock_disponible = ?, precio_unitario = ?, is_activo = ? 
            WHERE id = ?
            """;
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, repuesto.getCodigoReferencia());
            statement.setString(2, repuesto.getNombre());
            statement.setInt(3, repuesto.getIdCategoria());
            statement.setInt(4, repuesto.getIdProveedor());
            statement.setLong(5, repuesto.getStockTotal());
            statement.setLong(6, repuesto.getStockDisponible());
            statement.setDouble(7, repuesto.getPrecioUnitario());
            statement.setBoolean(8, repuesto.isActivo());
            statement.setLong(9, repuesto.getId());
            
            boolean exito = statement.executeUpdate() > 0;
            if (exito) System.out.println("[HTTP RESPONSE 200 OK] -> Repuesto actualizado con exito.");
            return exito;
        } catch (SQLException e) {
            System.out.println("[HTTP RESPONSE 400 Bad Request] -> Error al editar repuesto: " + e.getMessage());
            return false;
        }
    }

    @Override
    public List<Repuesto> listarTodos() {
        System.out.println("[HTTP GET] -> /api/repuestos | Recuperando catalogo de repuestos global.");
        List<Repuesto> lista = new ArrayList<>();
        String sql = "SELECT * FROM Repuestos ORDER BY id";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                lista.add(new Repuesto(
                    resultSet.getLong("id"),
                    resultSet.getString("codigo_referencia"),
                    resultSet.getString("nombre"),
                    resultSet.getInt("categoria"),
                    resultSet.getInt("proveedor"),
                    resultSet.getLong("stock_total"),
                    resultSet.getLong("stock_disponible"),
                    resultSet.getDouble("precio_unitario"),
                    resultSet.getBoolean("is_activo")
                ));
            }
            System.out.println("[HTTP RESPONSE 200 OK] -> Repuestos cargados: " + lista.size());
        } catch (SQLException e) {
            System.out.println("[HTTP RESPONSE 500 Internal Server Error] -> " + e.getMessage());
        }
        return lista;
    }

    @Override
    public List<Repuesto> filtrarPorCategoria(int idCategoria) {
        System.out.println("[HTTP GET] -> /api/repuestos?categoria=" + idCategoria + " | Filtrando por categoria.");
        List<Repuesto> lista = new ArrayList<>();
        String sql = "SELECT * FROM Repuestos WHERE categoria = ? ORDER BY id";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, idCategoria);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    lista.add(new Repuesto(
                        resultSet.getLong("id"),
                        resultSet.getString("codigo_referencia"),
                        resultSet.getString("nombre"),
                        resultSet.getInt("categoria"),
                        resultSet.getInt("proveedor"),
                        resultSet.getLong("stock_total"),
                        resultSet.getLong("stock_disponible"),
                        resultSet.getDouble("precio_unitario"),
                        resultSet.getBoolean("is_activo")
                    ));
                }
            }
            System.out.println("[HTTP RESPONSE 200 OK] -> Coincidencias por categoria encontradas: " + lista.size());
        } catch (SQLException e) {
            System.out.println("[HTTP RESPONSE 500 Internal Server Error] -> " + e.getMessage());
        }
        return lista;
    }

    @Override
    public List<Repuesto> filtrarPorProveedor(int idProveedor) {
        System.out.println("[HTTP GET] -> /api/repuestos?proveedor=" + idProveedor + " | Filtrando por proveedor.");
        List<Repuesto> lista = new ArrayList<>();
        String sql = "SELECT * FROM Repuestos WHERE proveedor = ? ORDER BY id";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, idProveedor);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    lista.add(new Repuesto(
                        resultSet.getLong("id"),
                        resultSet.getString("codigo_referencia"),
                        resultSet.getString("nombre"),
                        resultSet.getInt("categoria"),
                        resultSet.getInt("proveedor"),
                        resultSet.getLong("stock_total"),
                        resultSet.getLong("stock_disponible"),
                        resultSet.getDouble("precio_unitario"),
                        resultSet.getBoolean("is_activo")
                    ));
                }
            }
            System.out.println("[HTTP RESPONSE 200 OK] -> Coincidencias por proveedor encontradas: " + lista.size());
        } catch (SQLException e) {
            System.out.println("[HTTP RESPONSE 500 Internal Server Error] -> " + e.getMessage());
        }
        return lista;
    }

    @Override
    public boolean existeCodigoReferencia(String codigoReferencia) {
        System.out.println("[HTTP GET] -> /api/repuestos/validar-codigo?ref=" + codigoReferencia + " | Comprobando disponibilidad de codigo.");
        String sql = "SELECT COUNT(*) FROM Repuestos WHERE codigo_referencia = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, codigoReferencia);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    boolean existe = resultSet.getInt(1) > 0;
                    System.out.println("[HTTP RESPONSE 200 OK] -> ¿Codigo ya registrado?: " + existe);
                    return existe;
                }
            }
        } catch (SQLException e) {
            System.out.println("[HTTP RESPONSE 500 Internal Server Error] -> " + e.getMessage());
        }
        return false;
    }

    @Override
    public List<Categoria> listarCategorias() {
        System.out.println("[HTTP GET] -> /api/categorias | Listando categorias maestras.");
        List<Categoria> lista = new ArrayList<>();
        String sql = "SELECT * FROM Categorias ORDER BY nombre";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                lista.add(new Categoria(resultSet.getInt("id"), resultSet.getString("nombre")));
            }
        } catch (SQLException e) {
            System.out.println("[HTTP RESPONSE 500 Internal Server Error] -> " + e.getMessage());
        }
        return lista;
    }

     @Override
    public List<Proveedor> listarProveedores() {
        System.out.println("[HTTP GET] -> /api/proveedores | Listando proveedores maestros.");
        
        List<Proveedor> lista = new ArrayList<>();
        String sql = "SELECT * FROM Proveedores ORDER BY nombre";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                lista.add(new Proveedor(resultSet.getInt("id"), resultSet.getString("nombre")));
            }
        } catch (SQLException e) {
            System.out.println("[HTTP RESPONSE 500 Internal Server Error] -> " + e.getMessage());
        }
        return lista;
    }
}

