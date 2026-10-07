package dao;

import conexion.DatabaseConnection;
import modelo.Estudiante;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EstudianteDAO implements CrudDAO<Estudiante>{

    private Connection getConexion() {return DatabaseConnection.getInstance().getConnection();}

    @Override
    public boolean insertar(Estudiante estudiante) {
        String sql = "INSERT INTO estudiantes (nombre, rut, curso, correo) VALUES (?, ?, ?, ?)";
        try(PreparedStatement ps = getConexion().prepareStatement(sql)) {
            ps.setString(1, estudiante.getNombre());
            ps.setString(2, estudiante.getRut());
            ps.setString(3, estudiante.getCurso());
            ps.setString(4, estudiante.getCorreo());
            return ps.executeUpdate() > 0;
        }catch (SQLException e) {
            System.out.println("Error al insertar estudiante: " + e.getMessage());
            return false;
        }
    }
    @Override
    public List<Estudiante> listar() {
        List<Estudiante> lista = new ArrayList<>();
        String sql = "SELECT * FROM estudiantes";
        try(PreparedStatement ps = getConexion().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while(rs.next()) {
                lista.add(crearEstudiante(rs));
            }
        }catch (SQLException e) {
            System.out.println("Error al listar estudiantes: " + e.getMessage());
        }
        return lista;
    }

    @Override
    public Estudiante buscarPorId(int id) {
        String sql = "SELECT * FROM estudiantes WHERE id = ?";
        try(PreparedStatement ps = getConexion().prepareStatement(sql)){
            ps.setInt(1, id);
            try(ResultSet rs = ps.executeQuery()){
                if(rs.next()) {
                    return crearEstudiante(rs);
                }
            }
        }catch (SQLException e) {
            System.out.println("Error al buscar estudiante: " + e.getMessage());
        }
        return null;
    }
    public Estudiante buscarPorRut(String rut) {
        String sql = "SELECT * FROM estudiantes WHERE rut = ?";
        try(PreparedStatement ps = getConexion().prepareStatement(sql)){
            ps.setString(1, rut);
            try(ResultSet rs = ps.executeQuery()) {
                if(rs.next()) {
                    return crearEstudiante(rs);
                }
            }
        }catch (SQLException e) {
            System.out.println("Error al buscar estudiante por rut: " + e.getMessage());
        }
        return null;
    }
    @Override
    public boolean actualizar(Estudiante estudiante) {
        String sql = "UPDATE estudiantes SET nombre = ?, rut = ?, curso = ?, correo = ? WHERE id = ?";
        try (PreparedStatement ps = getConexion().prepareStatement(sql)) {
            ps.setString(1, estudiante.getNombre());
            ps.setString(2, estudiante.getRut());
            ps.setString(3, estudiante.getCurso());
            ps.setString(4, estudiante.getCorreo());
            ps.setInt(5, estudiante.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al actualizar estudiante: " + e.getMessage());
            return false;
        }
    }
    @Override
    public boolean eliminar(int id) {
        String sql = "DELETE FROM estudiantes WHERE id = ?";
        try(PreparedStatement ps = getConexion().prepareStatement(sql)){
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }catch (SQLException e){
            System.out.println("Error al eliminar estudiante: " + e.getMessage());
            return false;
        }
    }
    private Estudiante crearEstudiante(ResultSet rs) throws SQLException{
        return new Estudiante(
                rs.getInt("id"),
                rs.getString("nombre"),
                rs.getString("rut"),
                rs.getString("curso"),
                rs.getString("correo"));
    }
}
