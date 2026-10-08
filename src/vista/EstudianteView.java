package vista;

import controlador.EstudianteController;
import modelo.Estudiante;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class EstudianteView extends JFrame {

    private EstudianteController controller = new EstudianteController();

    private JTextField txtNombre;
    private JTextField txtRut;
    private JTextField txtCurso;
    private JTextField txtCorreo;
    private JPasswordField txtContrasena;

    private JTable tabla;
    private DefaultTableModel modelo;
    private int idSeleccionado = -1;

    public EstudianteView() {
        setTitle("Gestión de estudiantes");
        setSize(820, 480);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(null);

        // Formulario
        txtNombre = agregarCampo("Nombre:", 20);
        txtRut = agregarCampo("RUT:", 55);
        txtCurso = agregarCampo("Curso:", 90);
        txtCorreo = agregarCampo("Correo:", 125);

        JLabel lblContrasena = new JLabel("Contraseña:");
        lblContrasena.setBounds(20, 160, 80, 25);
        add(lblContrasena);

        txtContrasena = new JPasswordField();
        txtContrasena.setBounds(100, 160, 200, 25);
        add(txtContrasena);

        JLabel lblNota = new JLabel("(solo para agregar)");
        lblNota.setBounds(100, 185, 200, 20);
        add(lblNota);

        JButton btnAgregar = new JButton("Agregar");
        btnAgregar.setBounds(20, 230, 135, 30);
        add(btnAgregar);

        JButton btnModificar = new JButton("Modificar");
        btnModificar.setBounds(165, 230, 135, 30);
        add(btnModificar);

        JButton btnEliminar = new JButton("Eliminar");
        btnEliminar.setBounds(20, 270, 135, 30);
        add(btnEliminar);

        JButton btnLimpiar = new JButton("Limpiar");
        btnLimpiar.setBounds(165, 270, 135, 30);
        add(btnLimpiar);


        String[] columnas = {"ID", "Nombre", "RUT", "Curso", "Correo"};
        modelo = new DefaultTableModel(columnas, 0);
        tabla = new JTable(modelo);
        tabla.setDefaultEditor(Object.class, null);
        tabla.getColumnModel().getColumn(0).setPreferredWidth(30);
        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBounds(320, 20, 470, 400);
        add(scroll);

        btnAgregar.addActionListener(e -> agregar());
        btnModificar.addActionListener(e -> modificar());
        btnEliminar.addActionListener(e -> eliminar());
        btnLimpiar.addActionListener(e -> limpiar());
        tabla.getSelectionModel().addListSelectionListener(e -> cargarSeleccion());

        cargarTabla();
    }

    private JTextField agregarCampo(String texto, int y) {
        JLabel etiqueta = new JLabel(texto);
        etiqueta.setBounds(20, y, 80, 25);
        add(etiqueta);

        JTextField campo = new JTextField();
        campo.setBounds(100, y, 200, 25);
        add(campo);
        return campo;
    }

    private void cargarTabla() {
        modelo.setRowCount(0);
        for (Estudiante estudiante : controller.listarEstudiantes()) {
            modelo.addRow(new Object[]{
                    estudiante.getId(),
                    estudiante.getNombre(),
                    estudiante.getRut(),
                    estudiante.getCurso(),
                    estudiante.getCorreo()
            });
        }
    }

    private void cargarSeleccion() {
        int fila = tabla.getSelectedRow();
        if(fila == -1) {
            return;
        }
        idSeleccionado = (int) modelo.getValueAt(fila, 0);
        txtNombre.setText(modelo.getValueAt(fila, 1).toString());
        txtRut.setText(modelo.getValueAt(fila, 2).toString());
        txtCurso.setText(modelo.getValueAt(fila, 3).toString());
        txtCorreo.setText(modelo.getValueAt(fila, 4).toString());
        txtContrasena.setText("");
    }

    private void agregar() {
        String mensaje = controller.agregarEstudiante(
                txtNombre.getText().trim(),
                txtRut.getText().trim(),
                txtCurso.getText().trim(),
                txtCorreo.getText().trim(),
                new String(txtContrasena.getPassword())
        );
        mostrarResultado(mensaje);
    }

    private void modificar() {
        if(idSeleccionado == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona un estudiante de la tabla");
            return;
        }
        String mensaje = controller.modificarEstudiante(
                idSeleccionado,
                txtNombre.getText().trim(),
                txtRut.getText().trim(),
                txtCurso.getText().trim(),
                txtCorreo.getText().trim()
        );
        mostrarResultado(mensaje);
    }

    private void eliminar() {
        if(idSeleccionado == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona un estudiante de la tabla");
            return;
        }
        int respuesta = JOptionPane.showConfirmDialog(this, "¿Eliminar este estudiante?",
                "Confirmar", JOptionPane.YES_NO_OPTION);
        if(respuesta == JOptionPane.YES_OPTION) {
            mostrarResultado(controller.eliminarEstudiante(idSeleccionado));
        }
    }

    private void mostrarResultado(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje);
        if(mensaje.contains("correctamente")) {
            cargarTabla();
            limpiar();
        }
    }

    private void limpiar() {
        txtNombre.setText("");
        txtRut.setText("");
        txtCurso.setText("");
        txtCorreo.setText("");
        txtContrasena.setText("");
        idSeleccionado = -1;
        tabla.clearSelection();
    }
}
