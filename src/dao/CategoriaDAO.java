package dao;

import conexion.DatabaseConnection;
import modelo.Categoria;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CategoriaDAO {

    private Connection getConexion(){return DatabaseConnection.getInstance().getConnection();}

    public List<Categoria> listar(){
        List<Categoria> lista = new ArrayList<>();
        String sql = "SELECT * FROM categorias";
        try(PreparedStatement ps = getConexion().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while(rs.next()) {
                lista.add(new Categoria(rs.getInt("id"), rs.getString("nombre")));
            }
        }catch (SQLException e) {
            System.out.println("Error al listar categorías: " + e.getMessage());
        }
        return lista;
    }
}
