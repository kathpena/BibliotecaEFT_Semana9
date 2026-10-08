package vista;

import modelo.Usuario;
import javax.swing.*;

public class MenuView extends JFrame {

    private Usuario usuario;

    public MenuView(Usuario usuario){
        this.usuario = usuario;

        setTitle("Biblioteca Escolar - Menú principal");
        setSize(400, 360);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        JLabel lblBienvenida = new JLabel(usuario.mostrarInformacion());
        lblBienvenida.setBounds(30, 15, 340, 25);
        add(lblBienvenida);

        JButton btnLibros = new JButton("Gestión de libros");
        btnLibros.setBounds(80, 60, 230, 35);
        add(btnLibros);

        JButton btnEstudiantes = new JButton("Gestión de estudiantes");
        btnEstudiantes.setBounds(80, 105, 230, 35);
        add(btnEstudiantes);

        JButton btnPrestamos = new JButton("Préstamos y devoluciones");
        btnPrestamos.setBounds(80, 150, 230, 35);
        add(btnPrestamos);

        JButton btnReportes = new JButton("Reportes");
        btnReportes.setBounds(80, 195, 230, 35);
        add(btnReportes);

        JButton btnCerrarSesion = new JButton("Cerrar sesión");
        btnCerrarSesion.setBounds(80, 260, 230, 35);
        add(btnCerrarSesion);

        if(!usuario.esBibliotecario()) {
            btnLibros.setText("Consultar libros");
            btnEstudiantes.setVisible(false);
            btnReportes.setVisible(false);
            btnPrestamos.setBounds(80, 105, 230, 35);
            btnCerrarSesion.setBounds(80, 165, 230, 35);
            setSize(400, 260);
            setLocationRelativeTo(null);
        }

        btnLibros.addActionListener(e -> new LibroView(!usuario.esBibliotecario()).setVisible(true));
        btnEstudiantes.addActionListener(e -> new EstudianteView().setVisible(true));
        btnPrestamos.addActionListener(e -> new PrestamoView(usuario).setVisible(true));
        btnReportes.addActionListener(e -> pantallaPendiente());

        btnCerrarSesion.addActionListener(e -> {
            new LoginView().setVisible(true);
            dispose();});
    }

    private void pantallaPendiente(){
        JOptionPane.showMessageDialog(this, "Pantalla en construcción");}
}
