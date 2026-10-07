package vista;

import controlador.LoginController;
import modelo.Usuario;
import javax.swing.*;

public class LoginView extends JFrame {

    private JTextField txtCorreo;
    private JPasswordField txtContrasena;
    private LoginController controller = new LoginController();

    public LoginView() {
        setTitle("Biblioteca Escolar - Iniciar sesión");
        setSize(350, 230);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        JLabel lblTitulo = new JLabel("Sistema de Biblioteca");
        lblTitulo.setBounds(105, 15, 200, 25);
        add(lblTitulo);

        JLabel lblCorreo = new JLabel("Correo:");
        lblCorreo.setBounds(30, 60, 90, 25);
        add(lblCorreo);

        txtCorreo = new JTextField();
        txtCorreo.setBounds(120, 60, 180, 25);
        add(txtCorreo);

        JLabel lblContrasena = new JLabel("Contraseña:");
        lblContrasena.setBounds(30, 100, 90, 25);
        add(lblContrasena);

        txtContrasena = new JPasswordField();
        txtContrasena.setBounds(120, 100, 180, 25);
        add(txtContrasena);

        JButton btnIngresar = new JButton("Ingresar");
        btnIngresar.setBounds(120, 145, 110, 30);
        add(btnIngresar);

        btnIngresar.addActionListener(e -> ingresar());
    }

    private void ingresar() {
        String correo = txtCorreo.getText().trim();
        String contrasena = new String(txtContrasena.getPassword());

        Usuario usuario = controller.iniciarSesion(correo, contrasena);

        if(usuario == null){
            JOptionPane.showMessageDialog(this, "Correo o contraseña incorrectos",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }else {
            new MenuView(usuario).setVisible(true);
            dispose();
        }
    }
}
