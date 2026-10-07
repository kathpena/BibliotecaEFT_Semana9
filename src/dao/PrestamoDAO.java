package dao;

import conexion.DatabaseConnection;
import modelo.Prestamo;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PrestamoDAO implements CrudDAO<Prestamo>{

    private Connection getConexion() {return DatabaseConnection.getInstance().getConnection();}

    @Override
    public boolean insertar(Prestamo prestamo){
        String sql = "INSERT INTO prestamos(id_estudiante, id_libro, fecha_prestamo, fecha_devolucion, devuelto) VALUES (?, ?, ?, ?, ?)";
        try(PreparedStatement ps = getConexion().prepareStatement(sql)) {
            ps.setInt(1, prestamo.getIdEstudiante());
            ps.setInt(2, prestamo.getIdLibro());
            ps.setDate(3, Date.valueOf(prestamo.getFechaPrestamo()));
            ps.setDate(4, Date.valueOf(prestamo.getFechaDevolucion()));
            ps.setBoolean(5, prestamo.isDevuelto());
            return ps.executeUpdate() > 0;
        }catch (SQLException e) {
            System.out.println("Error al insertar préstamo: " + e.getMessage());
            return false;
        }
    }
    @Override
    public List<Prestamo> listar(){
        return listarConSql("SELECT * FROM prestamos");
    }

    public List<Prestamo> listarNoDevueltos(){return listarConSql("SELECT * FROM prestamos WHERE devuelto = FALSE");}

    public List<Prestamo> listarPorEstudiante(int idEstudiante) {
        List<Prestamo> lista = new ArrayList<>();
        String sql = "SELECT * FROM prestamos WHERE id_estudiante = ?";
        try(PreparedStatement ps = getConexion().prepareStatement(sql)) {
            ps.setInt(1, idEstudiante);
            try(ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(crearPrestamo(rs));
                }
            }
        }catch (SQLException e) {
            System.out.println("Error al listar préstamos del estudiante: " + e.getMessage());
        }
        return lista;
    }
    @Override
    public Prestamo buscarPorId(int id) {
        String sql = "SELECT * FROM prestamos WHERE id = ?";
        try(PreparedStatement ps = getConexion().prepareStatement(sql)) {
            ps.setInt(1, id);
            try(ResultSet rs = ps.executeQuery()) {
                if(rs.next()) {
                    return crearPrestamo(rs);
                }
            }
        }catch(SQLException e) {
            System.out.println("Error al buscar préstamo: " + e.getMessage());
        }
        return null;
    }
    @Override
    public boolean actualizar(Prestamo prestamo){
        String sql = "UPDATE prestamos SET id_estudiante = ?, id_libro = ?, fecha_prestamo = ?, fecha_devolucion = ?, devuelto = ? WHERE id = ?";
        try(PreparedStatement ps = getConexion().prepareStatement(sql)) {
            ps.setInt(1, prestamo.getIdEstudiante());
            ps.setInt(2, prestamo.getIdLibro());
            ps.setDate(3, Date.valueOf(prestamo.getFechaPrestamo()));
            ps.setDate(4, Date.valueOf(prestamo.getFechaDevolucion()));
            ps.setBoolean(5, prestamo.isDevuelto());
            ps.setInt(6, prestamo.getId());
            return ps.executeUpdate() > 0;
        }catch(SQLException e) {
            System.out.println("Error al actualizar préstamo: " + e.getMessage());
            return false;
        }
    }

    public boolean marcarDevuelto(int id){
        String sql = "UPDATE prestamos SET devuelto = TRUE WHERE id = ?";
        try(PreparedStatement ps = getConexion().prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }catch(SQLException e) {
            System.out.println("Error al marcar devolución: " + e.getMessage());
            return false;
        }
    }
    @Override
    public boolean eliminar(int id) {
        String sql = "DELETE FROM prestamos WHERE id = ?";
        try(PreparedStatement ps = getConexion().prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }catch (SQLException e){
            System.out.println("Error al eliminar préstamo: " + e.getMessage());
            return false;
        }
    }

    private List<Prestamo> listarConSql(String sql){
        List<Prestamo> lista = new ArrayList<>();
        try(PreparedStatement ps = getConexion().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()){
            while(rs.next()) {
                lista.add(crearPrestamo(rs));
            }
        }catch(SQLException e) {
            System.out.println("Error al listar préstamos: " + e.getMessage());
        }
        return lista;
    }
    private Prestamo crearPrestamo(ResultSet rs) throws SQLException {
        return new Prestamo(
                rs.getInt("id"),
                rs.getInt("id_estudiante"),
                rs.getInt("id_libro"),
                rs.getDate("fecha_prestamo").toLocalDate(),
                rs.getDate("fecha_devolucion").toLocalDate(),
                rs.getBoolean("devuelto")
        );
    }
}
