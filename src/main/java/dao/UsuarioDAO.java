/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package dao;

import java.util.List;
import model.Usuario;

/**
 *
 * @author cohorte5
 */
public interface UsuarioDAO {
    Usuario login(String nombre, String password);
    boolean crear(Usuario usuario);
    boolean actualizar(Usuario usuario);
    boolean eliminarLogico(int id); // Cambia is_active a 0
    List<Usuario> listarTodos();
}
