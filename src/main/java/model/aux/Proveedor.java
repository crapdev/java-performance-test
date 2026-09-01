/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.aux;

/**
 *
 * @author cohorte5
 */
public class Proveedor {
    
    private int id;
    private String nombre;
    
    public Proveedor(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }
    
    
    // Getters
    public int getId() { return id; }
    public String getNombre() { return nombre; }
}
