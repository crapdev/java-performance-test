
package model;


public class Usuario {
    private int id;
    private String correo;
    private String contraseña;
    private long isActive;
    private String rol; // 'ADMIN' o 'RECEPCIONISTA'

    public Usuario(int id, String correo, String contraseña, long isActive, String rol) {
        this.id = id;
        this.correo = correo;
        this.contraseña = contraseña;
        this.isActive = isActive;
        this.rol = rol;
    }

 
    
    
    //GETTERS
    public int getId() {
        return id;
    }
    public String getCorreo() {
        return correo;
    }
    public String getContraseña() {
        return contraseña;
    }
    public long getIsActive() {
        return isActive;
    }
    public String getRol() {
        return rol;
    }
    
    
    // SETTERS
    public void setId(int id) {
        this.id = id;
    }
    public void setCorreo(String nombre) {
        this.correo = nombre;
    }
    public void setIsActive(long isActive) {
        this.isActive = isActive;
    }
    public void setRol(String rol) {
        this.rol = rol;
    }
    
}


