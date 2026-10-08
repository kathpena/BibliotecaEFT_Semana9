package vista;

import controlador.PrestamoController;
import modelo.Estudiante;
import modelo.Libro;
import modelo.Prestamo;
import modelo.Usuario;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;

public class PrestamoView extends JFrame{

    private PrestamoController controller = new PrestamoController();
    private Usuario usuario;
    private Estudiante estudianteActual; // solo se usa si entra un estudiante
    private List<Estudiante> estudiantes;
    private List<Libro> libros;
    private JComboBox<Estudiante> cmbEstudiante;
    private JComboBox<Libro> cmbLibro;
    private JButton btnPrestar;
    private JLabel lblEstado;
    private JTable tabla;
    private DefaultTableModel modelo;

    public PrestamoView(Usuario usuario){
        this.usuario = usuario;
        setTitle("Préstamos y devoluciones");
        setSize(760, 540);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(null);

        if(!usuario.esBibliotecario()) {
            estudianteActual = controller.buscarEstudiantePorRut(usuario.getRut());
        }
        JLabel lblEstudiante = new JLabel("Estudiante:");
        lblEstudiante.setBounds(20, 20, 90, 25);
        add(lblEstudiante);

        cmbEstudiante = new JComboBox<>();
        cmbEstudiante.setBounds(110, 20, 320, 25);
        add(cmbEstudiante);

        JLabel lblLibro = new JLabel("Libro:");
        lblLibro.setBounds(20, 55, 90, 25);
        add(lblLibro);

        cmbLibro = new JComboBox<>();
        cmbLibro.setBounds(110, 55, 320, 25);
        add(cmbLibro);

        btnPrestar = new JButton("Registrar préstamo");
        btnPrestar.setBounds(110, 95, 180, 30);
        add(btnPrestar);

        lblEstado = new JLabel(" ");
        lblEstado.setBounds(300, 95, 400, 30);
        add(lblEstado);

        String[] columnas = {"ID", "Estudiante", "Libro", "Fecha préstamo", "Vence", "Estado"};
        modelo = new DefaultTableModel(columnas, 0);
        tabla = new JTable(modelo);
        tabla.setDefaultEditor(Object.class, null);
        tabla.getColumnModel().getColumn(0).setPreferredWidth(30);
        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBounds(20, 140, 710, 300);
        add(scroll);

        JButton btnDevolver = new JButton("Registrar devolución");
        btnDevolver.setBounds(20, 455, 200, 30);
        add(btnDevolver);

        btnPrestar.addActionListener(e -> registrarPrestamo());
        btnDevolver.addActionListener(e -> registrarDevolucion());

        if (!usuario.esBibliotecario() && estudianteActual == null) {
            btnPrestar.setEnabled(false);
            btnDevolver.setEnabled(false);
            lblEstado.setText("No se encontró tu registro de estudiante");
            return;
        }
         cargarDatos();
    }

    private void cargarDatos() {
        estudiantes = controller.listarEstudiantes();
        libros = controller.listarLibros();

        cmbEstudiante.removeAllItems();
        if(usuario.esBibliotecario()) {
            for(Estudiante estudiante : estudiantes) {
                cmbEstudiante.addItem(estudiante);
            }
        }else{
            cmbEstudiante.addItem(estudianteActual);
            cmbEstudiante.setEnabled(false); // el estudiante solo presta para sí mismo
        }

        cmbLibro.removeAllItems();
        for(Libro libro : libros) {
            cmbLibro.addItem(libro);
        }

        List<Prestamo> prestamos;
        if (usuario.esBibliotecario()) {
            prestamos = controller.listarPrestamos();
        } else {
            prestamos = controller.listarPrestamosDeEstudiante(estudianteActual.getId());
        }

        modelo.setRowCount(0);
        for (Prestamo prestamo : prestamos) {
            modelo.addRow(new Object[]{
                    prestamo.getId(),
                    nombreEstudiante(prestamo.getIdEstudiante()),
                    tituloLibro(prestamo.getIdLibro()),
                    prestamo.getFechaPrestamo(),
                    prestamo.getFechaDevolucion(),
                    estado(prestamo)
            });
        }
    }

    private String nombreEstudiante(int idEstudiante) {
        for(Estudiante estudiante : estudiantes) {
            if(estudiante.getId() == idEstudiante) {
                return estudiante.getNombre();
            }
        } return "";
    }

    private String tituloLibro(int idLibro) {
        for(Libro libro : libros) {
            if(libro.getId() == idLibro) {
                return libro.getTitulo();
            }
        }return "";
    }

    private String estado(Prestamo prestamo) {
        if(prestamo.isDevuelto()) {
            return "Devuelto";
        }
        if(prestamo.estaAtrasado()) {
            return "ATRASADO";
        }return "En préstamo";
    }

    private void registrarPrestamo() {
        Estudiante estudiante = (Estudiante) cmbEstudiante.getSelectedItem();
        Libro libro = (Libro) cmbLibro.getSelectedItem();

        btnPrestar.setEnabled(false);
        lblEstado.setText("Procesando préstamo...");

        Thread hilo = new Thread(() -> {
            try{
                Thread.sleep(1500);
            }catch (InterruptedException ex) {
                System.out.println("Hilo interrumpido");
            }
            String mensaje = controller.registrarPrestamo(estudiante, libro);

            SwingUtilities.invokeLater(() -> {
                lblEstado.setText(" ");
                btnPrestar.setEnabled(true);
                JOptionPane.showMessageDialog(this, mensaje);
                cargarDatos();
            });
        });
        hilo.start();
    }

    private void registrarDevolucion() {
        int fila = tabla.getSelectedRow();
        if(fila == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona un préstamo de la tabla");
            return;
        }
        int idPrestamo = (int) modelo.getValueAt(fila, 0);
        String mensaje = controller.registrarDevolucion(idPrestamo);
        JOptionPane.showMessageDialog(this, mensaje);
        cargarDatos();
    }
}

