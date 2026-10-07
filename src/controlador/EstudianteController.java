package controlador;

import dao.EstudianteDAO;
import dao.UsuarioDAO;
import modelo.Estudiante;
import modelo.Usuario;
import java.util.List;

public class EstudianteController {
    private EstudianteDAO estudianteDAO = new EstudianteDAO();
    private UsuarioDAO usuarioDAO = new UsuarioDAO();

    public List<Estudiante> listarEstudiantes(){
        return estudianteDAO.listar();
    }
    public String agregarEstudiante(String nombre, String rut, String curso, String correo, String contrasena) {
        if(nombre.isEmpty() || rut.isEmpty() || curso.isEmpty() || correo.isEmpty() || contrasena.isEmpty()) {
            return "Todos los campos son obligatorios";
        }
        Estudiante estudiante = new Estudiante(0, nombre, rut, curso, correo);
        if(!estudianteDAO.insertar(estudiante)) {
            return "No se pudo registrar el estudiante (revisa que el RUT no esté repetido)";}

        Usuario usuario = new Usuario(0, nombre, rut, correo, contrasena, "estudiante");
        if(!usuarioDAO.insertar(usuario)) {
            return "Estudiante registrado, pero no se pudo crear su usuario";}
        return "Estudiante y usuario registrados correctamente";
    }
    public String modificarEstudiante(int id, String nombre, String rut, String curso, String correo) {
        if(nombre.isEmpty() || rut.isEmpty() || curso.isEmpty() || correo.isEmpty()) {
            return "Todos los campos son obligatorios";}
        Estudiante estudiante = new Estudiante(id, nombre, rut, curso, correo);
        if(estudianteDAO.actualizar(estudiante)) {return "Estudiante modificado correctamente";}
        return "No se pudo modificar el estudiante";
    }
    public String eliminarEstudiante(int id) {
        if(estudianteDAO.eliminar(id)) {return "Estudiante eliminado correctamente";}
        return "No se pudo eliminar: el estudiante tiene préstamos registrados";}
}
