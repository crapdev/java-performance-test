package dao.impl;

import config.DatabaseConnection;
import dao.OrdenDAO;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.Orden;
import model.aux.OrdenRepuesto;

public class OrdenDAOImpl implements OrdenDAO{
    @Override
    public boolean registrarOrden(Orden orden, List<OrdenRepuesto> repuestosUtilizados) {
        System.out.println("[HTTP POST] -> /api/ordenes | Procesando transacción de nueva orden de servicio.");
        String sqlOrden = "INSERT INTO Ordenes (id_cliente, id_vehiculo, id_mecanico, descripcion_problema, diagnostico, estado) VALUES (?, ?, ?, ?, ?, ?) RETURNING id";
        String sqlDetalle = "INSERT INTO orden_repuestos (id_orden, id_repuestos, cantidad_usada, precio_historico) VALUES (?, ?, ?, ?)";
        String sqlStock = "UPDATE Repuestos SET stock_disponible = stock_disponible - ? WHERE id = ? AND stock_disponible >= ?";

        Connection connection = null;
        try {
            connection = DatabaseConnection.getConnection();
            connection.setAutoCommit(false);
            System.out.println("[TRANSACTION] -> Bloque transaccional ACID iniciado en PostgreSQL.");

            long idOrdenGenerada = 0;
            try (PreparedStatement statementOrden = connection.prepareStatement(sqlOrden)) {
                statementOrden.setLong(1, orden.getIdCliente());
                statementOrden.setInt(2, orden.getIdVehiculo());
                statementOrden.setInt(3, orden.getIdMecanico());
                statementOrden.setString(4, orden.getDescriptionProblema());
                statementOrden.setString(5, orden.getDiagnostico());
                statementOrden.setString(6, orden.getEstado());
                try (ResultSet rs = statementOrden.executeQuery()) {
                    if (rs.next()) idOrdenGenerada = rs.getLong(1);
                }
            }

            if (idOrdenGenerada == 0) throw new SQLException("Error al obtener ID de Orden.");
            System.out.println("[TRANSACTION] -> Cabecera de orden registrada. ID generado: " + idOrdenGenerada);

            try (PreparedStatement statementDetalle = connection.prepareStatement(sqlDetalle);
                 PreparedStatement statementStock = connection.prepareStatement(sqlStock)) {
                for (OrdenRepuesto rep : repuestosUtilizados) {
                    statementStock.setShort(1, rep.getCantidadUsada());
                    statementStock.setLong(2, rep.getIdRepuestos());
                    statementStock.setShort(3, rep.getCantidadUsada());
                    
                    if (statementStock.executeUpdate() == 0) {
                        throw new SQLException("Stock insuficiente para el repuesto ID: " + rep.getIdRepuestos());
                    }
                    System.out.println("[TRANSACTION] -> Stock actualizado para repuesto ID: " + rep.getIdRepuestos());

                    statementDetalle.setLong(1, idOrdenGenerada);
                    statementDetalle.setLong(2, rep.getIdRepuestos());
                    statementDetalle.setShort(3, rep.getCantidadUsada());
                    statementDetalle.setDouble(4, rep.getPrecioHistorico());
                    statementDetalle.executeUpdate();
                    System.out.println("[TRANSACTION] -> Fila de detalle insertada en orden_repuestos.");
                }
            }
            connection.commit();
            System.out.println("[HTTP RESPONSE 201 Created] -> Transacción finalizada. Orden guardada con éxito.");
            return true;
        } catch (SQLException e) {
            if (connection != null) {
                try { 
                    connection.rollback(); 
                    System.out.println("[TRANSACTION ROLLBACK] -> Error detectado. Base de datos restaurada al estado original.");
                } catch (SQLException ex) { 
                    System.out.println(ex.getMessage()); 
                }
            }
            System.out.println("[HTTP RESPONSE 422 Unprocessable Entity] -> " + e.getMessage());
            return false;
        } finally {
            if (connection != null) {
                try { connection.setAutoCommit(true); connection.close(); } catch (SQLException e) { System.out.println(e.getMessage()); }
            }
        }
    }

    @Override
    public boolean actualizarEstado(long idOrden, String nuevoEstado) {
        System.out.println("[HTTP PATCH] -> /api/ordenes/" + idOrden + "/estado | Solicitando cambio de estado a: " + nuevoEstado);
        String sql = "UPDATE Ordenes SET estado = ? WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, nuevoEstado);
            statement.setLong(2, idOrden);
            
            boolean exito = statement.executeUpdate() > 0;
            if (exito) System.out.println("[HTTP RESPONSE 200 OK] -> Estado de la orden modificado correctamente.");
            return exito;
        } catch (SQLException e) {
            System.out.println("[HTTP RESPONSE 400 Bad Request] -> " + e.getMessage());
            return false;
        }
    }

    @Override
    public List<Orden> consultarHistorialPorVehiculo(int idVehiculo) {
        System.out.println("[HTTP GET] -> /api/ordenes?vehiculo=" + idVehiculo + " | Consultando historial clínico automotriz.");
        List<Orden> lista = new ArrayList<>();
        String sql = "SELECT * FROM Ordenes WHERE id_vehiculo = ? ORDER BY fecha_ingreso DESC";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, idVehiculo);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    lista.add(new Orden(
                        resultSet.getLong("id"),
                        resultSet.getLong("id_cliente"),
                        resultSet.getInt("id_vehiculo"),
                        resultSet.getInt("id_mecanico"),
                        resultSet.getTimestamp("fecha_ingreso"),
                        resultSet.getString("descripcion_problema"),
                        resultSet.getString("diagnostico"),
                        resultSet.getString("estado")
                    ));
                }
            }
            System.out.println("[HTTP RESPONSE 200 OK] -> Órdenes recuperadas para el vehículo: " + lista.size());
        } catch (SQLException e) {
            System.out.println("[HTTP RESPONSE 500 Internal Server Error] -> " + e.getMessage());
        }
        return lista;
    }

    @Override
    public double calcularCostoTotalReparacion(long idOrden) {
        System.out.println("[HTTP GET] -> /api/ordenes/" + idOrden + "/costo-total | Solicitando sumatoria de liquidación financiera.");
        String sql = "SELECT COALESCE(SUM(cantidad_usada * precio_historico), 0.0) AS total FROM orden_repuestos WHERE id_orden = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, idOrden);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    double total = resultSet.getDouble("total");
                    System.out.println("[HTTP RESPONSE 200 OK] -> Liquidación calculada: $" + total);
                    return total;
                }
            }
        } catch (SQLException e) {
            System.out.println("[HTTP RESPONSE 500 Internal Server Error] -> " + e.getMessage());
        }
        return 0.0;
    }
}
