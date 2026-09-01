
package model;

/**
 *
 * @author cohorte5
 */
public class Cliente {
    private long id;
    private String nombre;
    
    
    public Cliente(long id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }
    
    
    // Getters
    public long getId() { return id; }
    public String getNombre() { return nombre; }
}
