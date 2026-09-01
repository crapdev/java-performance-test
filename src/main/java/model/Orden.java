/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author cohorte5
 */
public class Orden {
    private long id;
    private long idCliente;
    private int idVehiculo;
    private int idMecanico;
    private java.sql.Timestamp fechaIngreso;
    private String descripcionProblema;
    private String diagnostico;
    private String estado;
    
    public Orden(long id, long idCliente, int idVehiculo, int idMecanico, java.sql.Timestamp fechaIngreso, String descripcionProblema, String diagnostico, String estado) {
        this.id = id;
        this.idCliente = idCliente;
        this.idVehiculo = idVehiculo;
        this.idMecanico = idMecanico;
        this.fechaIngreso = fechaIngreso;
        this.descripcionProblema = descripcionProblema;
        this.diagnostico = diagnostico;
        this.estado = estado;
    }
    // Getters
    public long getId() { return id; }
    public long getIdCliente() { return idCliente; }
    public int getIdVehiculo() { return idVehiculo; }
    public int getIdMecanico() { return idMecanico; }
    public java.sql.Timestamp getFechaIngreso() { return fechaIngreso; }
    public String getDescriptionProblema() { return descripcionProblema; }
    public String getDiagnostico() { return diagnostico; }
    public String getEstado() { return estado; }
}

