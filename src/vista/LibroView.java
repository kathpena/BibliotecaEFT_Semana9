package vista;

import controlador.LibroController;
import modelo.Categoria;
import modelo.Libro;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;

public class LibroView extends JFrame {

    private LibroController controller = new LibroController();
    private List<Categoria> categorias;

    private JTextField txtTitulo;
    private JTextField txtAutor;
    private JTextField txtIsbn;
    private JTextField txtEditorial;
    private JTextField txtStock;
    private JComboBox<Categoria> cmbCategoria;

    private JTable tabla;
    private DefaultTableModel modelo;
    private int idSeleccionado = -1; // -1 significa que no hay libro seleccionado

    public LibroView(boolean soloConsulta) {
        if(soloConsulta) {
            setTitle("Consultar libros");
        }else {
            setTitle("Gestión de libros");
        }
        setSize(820, 480);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(null);

        categorias = controller.listarCategorias();

        // Tabla
        String[] columnas = {"ID", "Título", "Autor", "ISBN", "Editorial", "Stock", "Categoría"};
        modelo = new DefaultTableModel(columnas, 0);
        tabla = new JTable(modelo);
        tabla.setDefaultEditor(Object.class, null); // la tabla no se puede editar escribiendo en ella
        JScrollPane scroll = new JScrollPane(tabla);
        add(scroll);

        if(soloConsulta) {
            scroll.setBounds(20, 20, 770, 400);
        }else {
            scroll.setBounds(320, 20, 470, 400);
            crearFormulario();
        }
        cargarTabla();
    }

    private void crearFormulario() {
        txtTitulo = agregarCampo("Título:", 20);
        txtAutor = agregarCampo("Autor:", 55);
        txtIsbn = agregarCampo("ISBN:", 90);
        txtEditorial = agregarCampo("Editorial:", 125);
        txtStock = agregarCampo("Stock:", 160);

        JLabel lblCategoria = new JLabel("Categoría:");
        lblCategoria.setBounds(20, 195, 80, 25);
        add(lblCategoria);

        cmbCategoria = new JComboBox<>();
        for(Categoria categoria : categorias) {
            cmbCategoria.addItem(categoria);
        }
        cmbCategoria.setBounds(100, 195, 200, 25);
        add(cmbCategoria);

        JButton btnAgregar = new JButton("Agregar");
        btnAgregar.setBounds(20, 245, 135, 30);
        add(btnAgregar);

        JButton btnModificar = new JButton("Modificar");
        btnModificar.setBounds(165, 245, 135, 30);
        add(btnModificar);

        JButton btnEliminar = new JButton("Eliminar");
        btnEliminar.setBounds(20, 285, 135, 30);
        add(btnEliminar);

        JButton btnLimpiar = new JButton("Limpiar");
        btnLimpiar.setBounds(165, 285, 135, 30);
        add(btnLimpiar);

        btnAgregar.addActionListener(e -> agregar());
        btnModificar.addActionListener(e -> modificar());
        btnEliminar.addActionListener(e -> eliminar());
        btnLimpiar.addActionListener(e -> limpiar());

        tabla.getSelectionModel().addListSelectionListener(e -> cargarSeleccion());
    }

    private JTextField agregarCampo(String texto, int y){
        JLabel etiqueta = new JLabel(texto);
        etiqueta.setBounds(20, y, 80, 25);
        add(etiqueta);

        JTextField campo = new JTextField();
        campo.setBounds(100, y, 200, 25);
        add(campo);
        return campo;
    }

    private void cargarTabla(){
        modelo.setRowCount(0); // borra las filas anteriores
        for(Libro libro : controller.listarLibros()) {
            modelo.addRow(new Object[]{
                    libro.getId(),
                    libro.getTitulo(),
                    libro.getAutor(),
                    libro.getIsbn(),
                    libro.getEditorial(),
                    libro.getStock(),
                    nombreCategoria(libro.getIdCategoria())
            });
        }
    }

    private String nombreCategoria(int idCategoria) {
        for(Categoria categoria : categorias) {
            if(categoria.getId() == idCategoria) {
                return categoria.getNombre();
            }
        }
        return "";
    }

    private void cargarSeleccion() {
        int fila = tabla.getSelectedRow();
        if (fila == -1) {
            return;
        }
        idSeleccionado = (int) modelo.getValueAt(fila, 0);
        txtTitulo.setText(modelo.getValueAt(fila, 1).toString());
        txtAutor.setText(modelo.getValueAt(fila, 2).toString());
        txtIsbn.setText(modelo.getValueAt(fila, 3).toString());
        txtEditorial.setText(modelo.getValueAt(fila, 4).toString());
        txtStock.setText(modelo.getValueAt(fila, 5).toString());

        String nombreCategoria = modelo.getValueAt(fila, 6).toString();
        for(Categoria categoria : categorias) {
            if (categoria.getNombre().equals(nombreCategoria)) {
                cmbCategoria.setSelectedItem(categoria);
            }
        }
    }

    private void agregar() {
        String mensaje = controller.agregarLibro(
                txtTitulo.getText().trim(),
                txtAutor.getText().trim(),
                txtIsbn.getText().trim(),
                txtEditorial.getText().trim(),
                txtStock.getText().trim(),
                (Categoria) cmbCategoria.getSelectedItem()
        );
        mostrarResultado(mensaje);
    }

    private void modificar() {
        if(idSeleccionado == -1){
            JOptionPane.showMessageDialog(this, "Selecciona un libro de la tabla");
            return;
        }
        String mensaje = controller.modificarLibro(
                idSeleccionado,
                txtTitulo.getText().trim(),
                txtAutor.getText().trim(),
                txtIsbn.getText().trim(),
                txtEditorial.getText().trim(),
                txtStock.getText().trim(),
                (Categoria) cmbCategoria.getSelectedItem()
        );
        mostrarResultado(mensaje);
    }

    private void eliminar() {
        if(idSeleccionado == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona un libro de la tabla");
            return;
        }
        int respuesta = JOptionPane.showConfirmDialog(this, "¿Eliminar este libro?",
                "Confirmar", JOptionPane.YES_NO_OPTION);
        if(respuesta == JOptionPane.YES_OPTION) {
            mostrarResultado(controller.eliminarLibro(idSeleccionado));
        }
    }

    private void mostrarResultado(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje);
        if(mensaje.contains("correctamente")) {
            cargarTabla();
            limpiar();
        }
    }
    private void limpiar(){
        txtTitulo.setText("");
        txtAutor.setText("");
        txtIsbn.setText("");
        txtEditorial.setText("");
        txtStock.setText("");
        idSeleccionado = -1;
        tabla.clearSelection();
    }
}
