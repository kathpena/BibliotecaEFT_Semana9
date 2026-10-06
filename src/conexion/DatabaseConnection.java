package conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static DatabaseConnection instancia;
    private Connection conexion;

    /* Datos para conectarse (usar clave propia) */
    private static final String URL = "jdbc:mysql://localhost:3306/biblioteca";
    private static final String USUARIO = "root";
    private static final String CLAVE = "kvpa26123";

    private DatabaseConnection() {

        try {
            conexion = DriverManager.getConnection(URL, USUARIO, CLAVE);
            System.out.println("Conectado a la base de datos");
        } catch (SQLException ex) {
            System.out.println("Error al conectar la base de datos" + ex.getMessage());
        }
    }

    public static synchronized DatabaseConnection getInstance() {
        try {
            if(instancia == null || instancia.conexion == null || instancia.conexion.isClosed()) {
                instancia = new DatabaseConnection();
            }
        }catch (SQLException ex) {
            System.out.println("Error al conectar la base de datos" + ex.getMessage());
        }
        return instancia;
    }

    public Connection getConnection() {
        return conexion;
    }
}
