package controlador;

import dao.UsuarioDAO;
import modelo.Usuario;

public class LoginController {

    private UsuarioDAO usuarioDAO = new UsuarioDAO();

    public Usuario iniciarSesion(String correo, String contrasena){
        if(correo.isEmpty() || contrasena.isEmpty()){
            return null;
        }return usuarioDAO.login(correo, contrasena);
    }
}
