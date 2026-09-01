/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.aux;

/**
 *
 * @author cohorte5
 */
public class OrdenRepuesto {
    private long idOrden;
    private long idRepuestos;
    private short cantidadUsada;
    private double precioHistorico;
    
    public OrdenRepuesto(long idOrden, long idRepuestos, short cantidadUsada, double precioHistorico) {
        this.idOrden = idOrden;
        this.idRepuestos = idRepuestos;
        this.cantidadUsada = cantidadUsada;
        this.precioHistorico = precioHistorico;
    }
    
    
    // Getters
    public long getIdOrden() { return idOrden; }
    public long getIdRepuestos() { return idRepuestos; }
    public short getCantidadUsada() { return cantidadUsada; }
    public double getPrecioHistorico() { return precioHistorico; }
}
