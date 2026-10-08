package controlador;

import dao.EstudianteDAO;
import dao.LibroDAO;
import dao.PrestamoDAO;
import modelo.Estudiante;
import modelo.Libro;
import modelo.Prestamo;
import java.time.LocalDate;
import java.util.List;

public class PrestamoController {
    private static final int DIAS_PRESTAMO = 7;
    private PrestamoDAO prestamoDAO = new PrestamoDAO();
    private LibroDAO libroDAO = new LibroDAO();
    private EstudianteDAO estudianteDAO = new EstudianteDAO();
    public List<Estudiante> listarEstudiantes() {return estudianteDAO.listar();}
    public List<Libro> listarLibros() {return libroDAO.listar();}
    public Estudiante buscarEstudiantePorRut(String rut) {return estudianteDAO.buscarPorRut(rut);}
    public List<Prestamo> listarPrestamos() {return prestamoDAO.listar();}
    public List<Prestamo> listarPrestamosDeEstudiante(int idEstudiante) {return prestamoDAO.listarPorEstudiante(idEstudiante);}

    /* Registra prestamo - descuenta stock y guarda prestamo */
    public String registrarPrestamo(Estudiante estudiante, Libro libro) {
        if(estudiante == null || libro == null) {return "Debes seleccionar un estudiante y un libro";}

        /* Descontar stock */
        if (!LibroDAO.descontarStock(libro.getId())) {
            return "No hay stock disponible de \"" + libro.getTitulo() + "\"";
        }
        /* Calcular fechas */
        LocalDate hoy = LocalDate.now();
        LocalDate vencimiento = hoy.plusDays(DIAS_PRESTAMO);
        Prestamo prestamo = new Prestamo(0, estudiante.getId(), libro.getId(), hoy, vencimiento, false);

        /* Guardar el prestamo */
        if(prestamoDAO.insertar(prestamo)) {
            return "Préstamo registrado correctamente. Vence el " + vencimiento;
        }

        LibroDAO.aumentarStock(libro.getId());
        return "No se pudo registrar el préstamo";
    }
    public String registrarDevolucion(int idPrestamo) {
        Prestamo prestamo = prestamoDAO.buscarPorId(idPrestamo);
        if(prestamo == null) {
            return "No se encontró el préstamo";
        }
        if(prestamo.isDevuelto()) {return "Este préstamo ya fue devuelto";}
        boolean atrasado = prestamo.estaAtrasado();

        if(!prestamoDAO.marcarDevuelto(idPrestamo)) {return "No se pudo registrar la devolución";}
        LibroDAO.aumentarStock(prestamo.getIdLibro());

        if(atrasado) {return "Devolución registrada correctamente. ATENCIÓN: devuelto con atraso (vencía el "
                    + prestamo.getFechaDevolucion() + ")";}
        return "Devolución registrada correctamente";
    }
}
