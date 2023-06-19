package Swing;
import javax.swing.*;

import Cliente.Usuario;
import Controladores.ControladoraUsuario;
import Empresa.Inmobiliaria;
import Excepciones.Contraseña.MalContraseñaException;
import Excepciones.ControladoraUsuario.DniInvalidoException;
import Excepciones.ControladoraUsuario.EdadInvalidadException;
import Excepciones.ControladoraUsuario.NombreYApellidoIncorrectoException;
import Excepciones.ControladoraUsuario.UsuarioNoEncontradoException;
import Excepciones.Mail.ArrobaException;
import Excepciones.Mail.PuntoComException;

public class MenuInicioGUI extends JFrame {
    private ControladoraUsuario controladoraUsuario = new ControladoraUsuario();

    public MenuInicioGUI(Inmobiliaria inmobiliaria) {
        super("Menú principal");

        JPanel panel = new JPanel();

        JButton loginButton = new JButton("Loguearse");
        loginButton.addActionListener(e -> {
            try {
                Usuario usuario = controladoraUsuario.login(inmobiliaria);
                // código para el caso exitoso del inicio de sesión
            } catch (UsuarioNoEncontradoException ex) {
                JOptionPane.showMessageDialog(this, "El usuario no fue encontrado", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (MalContraseñaException ex) {
                JOptionPane.showMessageDialog(this, "La contraseña es incorrecta", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        JButton registerButton = new JButton("Registrarse");
        registerButton.addActionListener(e -> {
            Usuario usuario = null;
            try {
                usuario = controladoraUsuario.registrarse(inmobiliaria);
            } catch (DniInvalidoException ex) {
                JOptionPane.showMessageDialog(this, "El DNI esta mal", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (NombreYApellidoIncorrectoException ex) {
                JOptionPane.showMessageDialog(this, "El nombre y apellido esta mal", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (EdadInvalidadException ex) {
                JOptionPane.showMessageDialog(this, "La edad es invalida", "Error", JOptionPane.ERROR_MESSAGE);
            }
            inmobiliaria.agregarUsuario(usuario);
            // Realizar acción después de registrar exitosamente
        });

        panel.add(loginButton);
        panel.add(registerButton);

        add(panel);

        setSize(300, 120);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }
}
