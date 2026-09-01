
package model;

import java.time.LocalDateTime;


public class Repuesto {
    private long id;
    private String codigoReferencia;
    private String nombre;
    private int idCategoria;
    private int idProveedor;
    private long stockTotal;
    private long stockDisponible;
    private double precioUnitario;
    private boolean isActivo;

    public Repuesto(long id, String codigoReferencia, String nombre, int idCategoria, int idProveedor, long stockTotal, long stockDisponible, double precioUnitario, boolean isActivo) {
        this.id = id;
        this.codigoReferencia = codigoReferencia;
        this.nombre = nombre;
        this.idCategoria = idCategoria;
        this.idProveedor = idProveedor;
        this.stockTotal = stockTotal;
        this.stockDisponible = stockDisponible;
        this.precioUnitario = precioUnitario;
        this.isActivo = isActivo;
    }
    

    // Getters
    public long getId() { return id; }
    public String getCodigoReferencia() { return codigoReferencia; }
    public String getNombre() { return nombre; }
    public int getIdCategoria() { return idCategoria; }
    public int getIdProveedor() { return idProveedor; }
    public long getStockTotal() { return stockTotal; }
    public long getStockDisponible() { return stockDisponible; }
    public double getPrecioUnitario() { return precioUnitario; }
    public boolean isActivo() { return isActivo; }
    
    
 
    
    
}