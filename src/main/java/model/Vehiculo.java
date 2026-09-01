
package model;

public class Vehiculo {
    
    private int id;
    private long idCliente;
    private String marca;
    private String modelo;
    private String placa;
    
    
    public Vehiculo(int id, long idCliente, String marca, String modelo, String placa) {
        this.id = id;
        this.idCliente = idCliente;
        this.marca = marca;
        this.modelo = modelo;
        this.placa = placa;
    }
    
    
    // Getters
    public int getId() { return id; }
    public long getIdCliente() { return idCliente; }
    public String getMarca() { return marca; }
    public String getModelo() { return modelo; }
    public String getPlaca() { return placa; }

}
