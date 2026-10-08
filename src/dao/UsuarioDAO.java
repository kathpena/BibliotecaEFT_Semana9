package dao;

import conexion.DatabaseConnection;
import modelo.Usuario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO implements CrudDAO<Usuario>{

    private Connection getConexion(){
        return DatabaseConnection.getInstance().getConnection();
    }
    public Usuario login(String correo, String contrasena) {
        String sql = "SELECT * FROM usuarios WHERE correo = ? AND `contraseña` = ?";
        try(PreparedStatement ps = getConexion().prepareStatement(sql)){
            ps.setString(1, correo);
            ps.setString(2, contrasena);
            try(ResultSet rs = ps.executeQuery()) {
                if(rs.next()) {
                    return crearUsuario(rs);
                }
            }
        }catch (SQLException e) {
            System.out.println("Error en login: " + e.getMessage());
        }
        return null;
    }
    @Override
    public boolean insertar(Usuario usuario){
        String sql = "INSERT INTO usuarios (nombre, rut, correo, `contraseña`, rol) VALUES (?, ?, ?, ?, ?)";
        try(PreparedStatement ps = getConexion().prepareStatement(sql)) {
            ps.setString(1, usuario.getNombre());
            ps.setString(2, usuario.getRut());
            ps.setString(3, usuario.getCorreo());
            ps.setString(4, usuario.getContrasena());
            ps.setString(5, usuario.getRol());
            return ps.executeUpdate() > 0;
        }catch (SQLException e) {
            System.out.println("Error al insertar usuario: " + e.getMessage());
            return false;
        }
    }
    @Override
    public List<Usuario> listar(){
        List<Usuario> lista = new ArrayList<>();
        String sql = "SELECT * FROM usuarios";
        try(PreparedStatement ps = getConexion().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()){
            while (rs.next()) {
                lista.add(crearUsuario(rs));
            }
        }catch (SQLException e) {
            System.out.println("Error al listar usuarios: " + e.getMessage());
        }
        return lista;
    }
    @Override
    public Usuario buscarPorId(int id) {
        String sql = "SELECT * FROM usuarios WHERE id = ?";
        try(PreparedStatement ps = getConexion().prepareStatement(sql)){
            ps.setInt(1, id);
            try(ResultSet rs = ps.executeQuery()){
                if(rs.next()){
                    return crearUsuario(rs);
                }
            }
        }catch (SQLException e) {
            System.out.println("Error al buscar usuario: " + e.getMessage());
        }
        return null;
    }
    @Override
    public boolean actualizar(Usuario usuario) {
        String sql = "UPDATE usuarios SET nombre = ?, rut = ?, correo = ?, `contraseña` = ?, rol = ? WHERE id = ?";
        try(PreparedStatement ps = getConexion().prepareStatement(sql)){
            ps.setString(1, usuario.getNombre());
            ps.setString(2, usuario.getRut());
            ps.setString(3, usuario.getCorreo());
            ps.setString(4, usuario.getContrasena());
            ps.setString(5, usuario.getRol());
            ps.setInt(6, usuario.getId());
            return ps.executeUpdate() > 0;
        }catch(SQLException e){
            System.out.println("Error al actualizar usuario: " + e.getMessage());
            return false;
        }
    }
    @Override
    public boolean eliminar(int id){
        String sql = "DELETE FROM usuarios WHERE id = ?";
        try(PreparedStatement ps = getConexion().prepareStatement(sql)){
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }catch(SQLException e) {
            System.out.println("Error al eliminar usuario: " + e.getMessage());
            return false;
        }
    }

    /* Elimina el usuario que tenga ese rut */
    public boolean eliminarPorRut(String rut) {
        String sql = "DELETE FROM usuarios WHERE rut = ?";
        try (PreparedStatement ps = getConexion().prepareStatement(sql)) {
            ps.setString(1, rut);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al eliminar usuario por rut: " + e.getMessage());
            return false;
        }
    }

    private Usuario crearUsuario(ResultSet rs) throws SQLException{
        return new Usuario(
                rs.getInt("id"),
                rs.getString("nombre"),
                rs.getString("rut"),
                rs.getString("correo"),
                rs.getString("contraseña"),
                rs.getString("rol"));
    }
}
