
package model.aux;

/**
 *
 * @author cohorte5
 */
public class Mecanico {
    private int id;
    private String nombre;
    
    public Mecanico(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }
    
    
    // Getters
    public int getId() { return id; }
    public String getNombre() { return nombre; }
}


