package Swing;

import javax.swing.*;

import Cliente.Contraseña;
import Cliente.Mail;
import Cliente.TiposMail;
import Cliente.Usuario;
import Controladores.ControladoraUsuario;
import Empresa.Inmobiliaria;
import Excepciones.CancelarException;
import Excepciones.Contraseña.CantMayusException;
import Excepciones.Contraseña.CantNumException;
import Excepciones.Contraseña.MalContraseñaException;
import Excepciones.Contraseña.TotalDigitosException;
import Excepciones.ControladoraUsuario.DniInvalidoException;
import Excepciones.ControladoraUsuario.EdadInvalidadException;
import Excepciones.ControladoraUsuario.NombreYApellidoIncorrectoException;
import Excepciones.ControladoraUsuario.UsuarioNoEncontradoException;
import Excepciones.EleccionIncorrectaException;
import Excepciones.Inmuebles.DireccionInvalidaException;
import Excepciones.LugarExistenteException;
import Excepciones.Mail.ArrobaException;
import Excepciones.Mail.MailNoIngresadoException;
import Excepciones.Mail.PuntoComException;
import Excepciones.NoDisponibleException;
import Interfaces.JsonUtiles;
import Lugares.*;
import org.json.JSONException;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.util.Objects;

public class MenuInicioGUI extends JFrame {

    public MenuInicioGUI(Inmobiliaria inmobiliaria) {
        super("Menú principal");

        JPanel panel = new JPanel();

        JButton loginButton = new JButton("Loguearse");
        loginButton.addActionListener(e -> {
            setVisible(false);
            try {
                Usuario usuario = loginGUI(inmobiliaria);
                if (Usuario.comprobarAdmin(usuario)) {
                    menuAdminGUI(inmobiliaria);
                    setVisible(true);
                } else {
                    menuUsuarioGUI(inmobiliaria, usuario);
                    setVisible(true);
                }

            } catch (UsuarioNoEncontradoException | MalContraseñaException | EleccionIncorrectaException |
                     CancelarException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                setVisible(true);
            }
        });

        JButton registerButton = new JButton("Registrarse");

        registerButton.addActionListener(e -> {
            setVisible(false);
            Usuario usuario = new Usuario();
            try {
                usuario = registrarseGUI();
            } catch (DniInvalidoException | NombreYApellidoIncorrectoException | EdadInvalidadException |
                     PuntoComException | ArrobaException | MailNoIngresadoException | CancelarException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                setVisible(true);
            }
            inmobiliaria.agregarUsuario(usuario);
            setVisible(true);
        });

        JButton salirButton = new JButton("Salir");
        salirButton.addActionListener(e -> {
            try {
                JsonUtiles.grabar(inmobiliaria.toJsonObj(), "inmobiliaria");
                dispose(); // cierra la ventana actual
            } catch (JSONException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        panel.add(loginButton);
        panel.add(registerButton);
        panel.add(salirButton);

        add(panel);

        setSize(300, 120);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    public static void menuAdminGUI(Inmobiliaria inmobiliaria) throws EleccionIncorrectaException, UsuarioNoEncontradoException {
        int opcion = 0;
        while (true) {
            Object[] options = {"Agregar inmueble", "Remover inmueble", "Modificar inmueble",
                    "Listar inmueble", "Mostrar inmueble", "Dar de baja usuario",
                    "Mostrar usuario"};

            opcion = JOptionPane.showOptionDialog(null, "¿Qué le gustaría realizar?", "Opciones",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
                    null, options, options[0]);

            switch (opcion) {
                case 0:
                    agregarInmueblesGUI(inmobiliaria);
                    break;
                case 1:
                    darDeBajaInmuebleGUI(inmobiliaria);
                    break;
                case 2:
                    modificarInmuebleGUI(inmobiliaria);
                    break;
                case 3:
                    listarInmuebleGUI(inmobiliaria);
                    break;
                case 4:
                    buscarInmuebleGUI(inmobiliaria);
                    break;
                case 5:
                    darDeBajaUsuarioGUI(inmobiliaria);
                    break;
                case 6:
                    mostrarUsuarioGUI(inmobiliaria);
                    break;
                default:
                    JOptionPane.showMessageDialog(null, "Valor ingresado no valido");
                    break;
            }
            int respuesta = JOptionPane.showConfirmDialog(null, "Desea continuar?");
            if (respuesta != JOptionPane.YES_OPTION) {
                break;
            }
        }
    }

    public Usuario loginGUI(Inmobiliaria inmobiliaria) throws UsuarioNoEncontradoException, MalContraseñaException, CancelarException {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 2));

        JLabel usuarioLabel = new JLabel("Usuario:");
        JTextField usuarioField = new JTextField(20);
        panel.add(usuarioLabel);
        panel.add(usuarioField);

        JLabel contraseñaLabel = new JLabel("Contraseña:");
        JPasswordField contraseñaField = new JPasswordField(20);
        panel.add(contraseñaLabel);
        panel.add(contraseñaField);

        int opcion = JOptionPane.showConfirmDialog(null, panel, "Ingrese su información de inicio de sesión", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (opcion == JOptionPane.CANCEL_OPTION || opcion == JOptionPane.CLOSED_OPTION) {
            throw new CancelarException("Inicio de sesión cancelado por el usuario");
        }

        String mail = usuarioField.getText();
        Usuario usuario = inmobiliaria.buscarUsuario(mail);
        if (usuario == null) {
            throw new UsuarioNoEncontradoException("Usuario no encontrado");
        }

        String contraseña = new String(contraseñaField.getPassword());
        if (!usuario.getContraseña().equals(contraseña)) {
            throw new MalContraseñaException("Contraseña incorrecta");
        }

        return usuario;
    }

    public Usuario registrarseGUI() throws DniInvalidoException, NombreYApellidoIncorrectoException, EdadInvalidadException, PuntoComException, ArrobaException, MailNoIngresadoException, CancelarException {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(7, 2));

        JLabel nombreLabel = new JLabel("Nombre y apellido:");
        JTextField nombreField = new JTextField(20);
        panel.add(nombreLabel);
        panel.add(nombreField);

        JLabel contraseñaLabel = new JLabel("Contraseña (Una mayúscula, un número y 8 dígitos como mínimo):");
        JPasswordField contraseñaField = new JPasswordField(20);
        panel.add(contraseñaLabel);
        panel.add(contraseñaField);

        JLabel dniLabel = new JLabel("DNI (8 dígitos):");
        JTextField dniField = new JTextField(8);
        panel.add(dniLabel);
        panel.add(dniField);

        JLabel edadLabel = new JLabel("Edad:");
        JTextField edadField = new JTextField(3);
        panel.add(edadLabel);
        panel.add(edadField);

        JLabel tipoMailLabel = new JLabel("Tipo de mail:");
        JComboBox<String> tipoMailCombo = new JComboBox<>(new String[]{"Gmail", "Hotmail", "Yahoo", "Otros"});
        panel.add(tipoMailLabel);
        panel.add(tipoMailCombo);

        JLabel parteDelanteraMailLabel = new JLabel("Parte delantera del mail (antes de @):");
        JTextField parteDelanteraMailField = new JTextField(20);
        panel.add(parteDelanteraMailLabel);
        panel.add(parteDelanteraMailField);

        tipoMailCombo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int tipoMail = tipoMailCombo.getSelectedIndex() + 1;
                if (tipoMail == 4) {
                    parteDelanteraMailField.setText("Dejar el espacio vacio");
                    parteDelanteraMailField.setEditable(false);
                } else {
                    parteDelanteraMailField.setText("");
                    parteDelanteraMailField.setEditable(true);
                }
            }
        });

        int opcion = JOptionPane.showConfirmDialog(null, panel, "Ingrese su información de registro", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (opcion == JOptionPane.CANCEL_OPTION || opcion == JOptionPane.CLOSED_OPTION) {
            throw new CancelarException("Inicio de sesión cancelado por el usuario");
        }

        String nombre = nombreField.getText();
        if (!nombre.matches("^[a-zA-Z\\s]+$")) { // Verificar que el nombre solo contenga letras y espacios
            throw new NombreYApellidoIncorrectoException("Nombre inválido. No debe contener números.");
        }

        String contraseña = new String(contraseñaField.getPassword());
        Contraseña contra = null;
        try {
            Contraseña.verificacion(contraseña);
            contra = new Contraseña(contraseña);
        } catch (TotalDigitosException | CantNumException | CantMayusException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

        String dni = "";
        if (!Objects.equals(dniField.getText(), "")) {
            dni = dniField.getText();
            if (!dni.matches("\\d{8}")) { // Verificar que el DNI tenga 8 dígitos
                JOptionPane.showMessageDialog(null, "DNI inválido. Debe tener 8 dígitos.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            throw new DniInvalidoException("No ingreso nada en el dni");
        }

        int edad = 0;
        if (!Objects.equals(edadField.getText(), "")) {
            edad = Integer.parseInt(edadField.getText());
            if (edad < 0 || edad > 120) { // Verificar que la edad esté en un rango razonable
                JOptionPane.showMessageDialog(null, "Edad inválida. Debe estar entre 0 y 120.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            throw new EdadInvalidadException("No ha ingresado ninguna Edad");
        }

        String mail = "";
        int tipoMail = tipoMailCombo.getSelectedIndex() + 1;
        System.out.println(tipoMail);
        String parteDelanteraMail = parteDelanteraMailField.getText();
        switch (tipoMail) {
            case 1:
                if (!parteDelanteraMail.trim().isEmpty()) {
                    mail = TiposMail.Gmail.getTipomail();
                } else {
                    throw new MailNoIngresadoException("No ingreso la parte de delante del mail");
                }
                break;
            case 2:
                if (!parteDelanteraMail.trim().isEmpty()) {
                    mail = TiposMail.Hotmail.getTipomail();
                } else {
                    throw new MailNoIngresadoException("No ingreso la parte de delante del mail");
                }
                break;
            case 3:
                if (!parteDelanteraMail.trim().isEmpty()) {
                    mail = TiposMail.Yahoo.getTipomail();
                } else {
                    throw new MailNoIngresadoException("No ingreso la parte de delante del mail");
                }
                break;
            case 4:
                parteDelanteraMail = "";
                if (parteDelanteraMail.trim().isEmpty()) {
                    mail = parteDelanteraMail + "@" + menuTipoMailGUI(tipoMail);
                    while (!Mail.validarMail(parteDelanteraMail + "@" + mail)) {
                        JOptionPane.showMessageDialog(null, "Mail ingresado inválido. Inténtelo de nuevo.", "Error", JOptionPane.ERROR_MESSAGE);
                        parteDelanteraMail = JOptionPane.showInputDialog(null, "Ingrese la parte delantera del mail (antes de @):", "Registro", JOptionPane.PLAIN_MESSAGE);
                        if (parteDelanteraMail == null) {
                            JOptionPane.showMessageDialog(null, "Registro cancelado por el usuario", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                } else {
                    throw new MailNoIngresadoException("Dejar el espacio vacio");
                }
                break;
            default:
                JOptionPane.showMessageDialog(null, "La opcion ingresada es invalida", "Error", JOptionPane.ERROR_MESSAGE);
        }

        Mail correo = new Mail(mail);

        Usuario usuario = new Usuario(nombre, contra, dni, correo, edad, true);
        return usuario;
    }

    public String menuTipoMailGUI(int eleccion) throws CancelarException {
        JPanel panel = new JPanel(new GridLayout(2, 1));

        JLabel mensajeLabel = new JLabel("Ingrese su mail completo:");
        JTextField mensajeField = new JTextField(20);
        panel.add(mensajeLabel);
        panel.add(mensajeField);

        int opcion = JOptionPane.showConfirmDialog(null, panel, "Ingrese su información de registro", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (opcion == JOptionPane.CANCEL_OPTION || opcion == JOptionPane.CLOSED_OPTION) {
            throw new CancelarException("Inicio de sesión cancelado por el usuario");
        }

        String mail = mensajeField.getText();
        switch (eleccion) {
            case 1:
                mail += TiposMail.Gmail.getTipomail();
                break;
            case 2:
                mail += TiposMail.Hotmail.getTipomail();
                break;
            case 3:
                mail += TiposMail.Yahoo.getTipomail();
                break;
            default:
                break;
        }

        return mail;
    }

    public static void menuUsuarioGUI(Inmobiliaria inmobiliaria, Usuario usuario) throws CancelarException {
        String continuar = "si";
        String direccion = "";
        String tipoInmueble = "";

        do {
            String[] opciones = {"Ver lista de inmuebles", "Buscar un inmueble", "Comprar un inmueble", "Alquilar un inmueble"};
            int opcion = JOptionPane.showOptionDialog(null,
                    "Hola " + usuario.getNombreYApellido() + ", ¿qué desea hacer?", "Menú de Usuario",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, opciones, opciones[0]);
            switch (opcion) {
                case 0:
                    listarInmuebleGUI(inmobiliaria);
                    break;

                case 1:
                    try {
                        buscarInmuebleGUI(inmobiliaria);
                    } catch (EleccionIncorrectaException e) {
                        JOptionPane.showMessageDialog(null, e.getMessage(),
                                "Error", JOptionPane.ERROR_MESSAGE);
                    }
                    break;

                case 2:
                    boolean validoTipoInmueble = false;
                    while (!validoTipoInmueble) {
                        tipoInmueble = (String) JOptionPane.showInputDialog(null, "¿Ingrese el tipo de inmueble que desea comprar?",
                                "Listar Inmueble", JOptionPane.QUESTION_MESSAGE, null,
                                new String[]{"Casa", "Departamento", "Local", "Cochera"}, "Casa");
                        if (tipoInmueble == null ||
                                !(tipoInmueble.equalsIgnoreCase("casa") ||
                                        tipoInmueble.equalsIgnoreCase("departamento") ||
                                        tipoInmueble.equalsIgnoreCase("local") ||
                                        tipoInmueble.equalsIgnoreCase("cochera"))) {
                            JOptionPane.showMessageDialog(null,
                                    "Tipo de inmueble inválido. Por favor, ingrese uno válido.",
                                    "Error",
                                    JOptionPane.ERROR_MESSAGE);
                        } else {
                            validoTipoInmueble = true;
                        }
                    }

                    while (continuar.equalsIgnoreCase("si")) {
                        direccion = JOptionPane.showInputDialog(null,
                                "Ingrese la dirección del inmueble que desea comprar",
                                "Dirección",
                                JOptionPane.QUESTION_MESSAGE);

                        LocalDate fechaIngreso = LocalDate.now();
                        LocalDate fechaSalida = LocalDate.now();

                        Fecha fecha = new Fecha(fechaIngreso, fechaSalida);

                        try {
                            inmobiliaria.ventaGUI(usuario, direccion, tipoInmueble, fecha);
                            opcion = JOptionPane.showConfirmDialog(null,
                                    "Venta realizada con éxito. ¿Desea comprar otro inmueble?",
                                    "Confirmación",
                                    JOptionPane.YES_NO_OPTION);
                            continuar = (opcion == JOptionPane.YES_OPTION) ? "si" : "no";
                        } catch (LugarExistenteException e) {
                            opcion = JOptionPane.showConfirmDialog(null,
                                    e.getMessage() + "\n\n¿Quiere intentar otra vez?",
                                    "Error",
                                    JOptionPane.YES_NO_OPTION);
                            continuar = (opcion == JOptionPane.YES_OPTION) ? "si" : "no";
                        }
                    }

                    break;
                case 3:
                    boolean valido = false;
                    do {
                        tipoInmueble = (String) JOptionPane.showInputDialog(null, "¿Ingrese el tipo de inmueble que desea alquilar?",
                                "Listar Inmueble", JOptionPane.QUESTION_MESSAGE, null,
                                new String[]{"Casa", "Departamento", "Local", "Cochera"}, "Casa");
                        if (!(tipoInmueble.equalsIgnoreCase("casa") || tipoInmueble.equalsIgnoreCase("departamento") || tipoInmueble.equalsIgnoreCase("local") || tipoInmueble.equalsIgnoreCase("cochera"))) {
                            valido = false;
                        } else {
                            valido = true;
                        }
                    } while (valido == false);

                    do {
                        direccion = JOptionPane.showInputDialog(null, "Ingrese la dirección del inmueble que desea alquilar");
                        if (!Inmobiliaria.validarDireccion(direccion)) {
                            JOptionPane.showMessageDialog(null, "Direccion ingresada es invalida", "Error", JOptionPane.ERROR_MESSAGE);
                            break;
                        }

                        LocalDate fechaIngreso = null;
                        LocalDate fechaSalida = null;
                        do {
                            try {
                                fechaIngreso = crearFechaAlquilerGUI();

                                String cantDiasString = JOptionPane.showInputDialog(null, "Ingrese cuantos dias desea estar: ");
                                int cantDias = Integer.parseInt(cantDiasString);
                                fechaSalida = fechaIngreso.plusDays(cantDias);
                                continuar = "no";
                            } catch (EleccionIncorrectaException e) {
                                JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                                opcion = JOptionPane.showConfirmDialog(null, "¿Quiere intentar otra vez?", "Reintentar", JOptionPane.YES_NO_OPTION);
                                if (opcion == JOptionPane.NO_OPTION) {
                                    break;
                                }
                            }
                        } while (continuar.equalsIgnoreCase("si"));
                        Fecha fecha = new Fecha(fechaIngreso, fechaSalida);
                        try {
                            inmobiliaria.alquilarGUI(usuario, direccion, tipoInmueble, fecha);
                        } catch (NoDisponibleException e1) {
                            JOptionPane.showMessageDialog(null, e1.getMessage() + "\nFechas ocupadas: " + e1.getDisponibilidad(), "Error", JOptionPane.ERROR_MESSAGE);
                            opcion = JOptionPane.showConfirmDialog(null, "¿Quiere intentar otra vez?", "Reintentar", JOptionPane.YES_NO_OPTION);
                            if (opcion == JOptionPane.NO_OPTION) {
                                break;
                            }
                        } catch (LugarExistenteException e2) {
                            JOptionPane.showMessageDialog(null, e2.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                            opcion = JOptionPane.showConfirmDialog(null, "¿Quiere intentar otra vez?", "Reintentar", JOptionPane.YES_NO_OPTION);
                            if (opcion == JOptionPane.NO_OPTION) {
                                break;
                            }
                        }
                    } while (continuar.equalsIgnoreCase("si"));
                    break;

                case 4:
                    JTextArea textAreaFacturas = new JTextArea();
                    JScrollPane scrollPane = new JScrollPane(textAreaFacturas);
                    textAreaFacturas.setEditable(false);
                    textAreaFacturas.setText(usuario.mostrarFacturas());
                    JOptionPane.showMessageDialog(null, scrollPane, "Facturas", JOptionPane.INFORMATION_MESSAGE);
                    break;

                default:
                    JOptionPane.showMessageDialog(null, "Opción inválida.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    break;
            }

            int seleccion = JOptionPane.showOptionDialog(null, "¿Desea hacer otra acción?",
                    "Menú de Usuario", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,
                    null, new Object[]{"Sí", "No"}, "No");
            continuar = (seleccion == JOptionPane.YES_OPTION) ? "si" : "no";
        } while (continuar.equalsIgnoreCase("si"));
    }

    public static LocalDate crearFechaAlquilerGUI() throws EleccionIncorrectaException {
        JTextField diaTextField = new JTextField();
        JTextField mesTextField = new JTextField();
        JTextField anioTextField = new JTextField();

        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Ingrese el día:"));
        panel.add(diaTextField);
        panel.add(new JLabel("Ingrese el mes:"));
        panel.add(mesTextField);
        panel.add(new JLabel("Ingrese el año:"));
        panel.add(anioTextField);

        int result = JOptionPane.showConfirmDialog(null, panel, "Ingrese la fecha de alquiler", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                int dia = Integer.parseInt(diaTextField.getText());
                if (dia <= 0 || dia > 31) { // Validamos que el día esté entre 1 y 31
                    throw new EleccionIncorrectaException("El día ingresado no es válido");
                }
                int mes = Integer.parseInt(mesTextField.getText());
                if (mes <= 0 || mes > 12) { // Validamos que el mes esté entre 1 y 12
                    throw new EleccionIncorrectaException("El mes ingresado no es válido");
                }
                int anio = Integer.parseInt(anioTextField.getText());
                if (anio < 1900 || anio > 2100) { // Validamos que el año esté entre 1900 y 2100
                    throw new EleccionIncorrectaException("El año ingresado no es válido");
                }

                LocalDate fecha = LocalDate.of(anio, mes, dia);
                boolean valido = Fecha.validarFecha(fecha.getYear(), fecha.getMonthValue(), fecha.getDayOfMonth());

                if (!valido) {
                    throw new EleccionIncorrectaException("La fecha no es válida");
                }

                return fecha;
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                throw new EleccionIncorrectaException("El formato de la fecha es incorrecto");
            }
        } else {
            throw new EleccionIncorrectaException("Se ha cancelado la operación");
        }
    }

    public static void agregarInmueblesGUI(Inmobiliaria inmobiliaria) {
        JFrame frame = new JFrame("Agregar Inmueble");

        boolean continuar = true;
        do {
            String[] opcionesInmuebles = {"Casa", "Departamento", "Local", "Cochera"};

            String tipoInmueble = (String) JOptionPane.showInputDialog(
                    null,
                    "¿Qué tipo de inmueble desea agregar?",
                    "Agregar Inmueble",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    opcionesInmuebles,
                    opcionesInmuebles[0]
            );

            switch (tipoInmueble.toUpperCase()) {
                case "CASA":
                    try {
                        agregarCasaGUI(frame, inmobiliaria);
                    } catch (DireccionInvalidaException e) {
                        JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                    break;
                case "DEPARTAMENTO":
                    try {
                        agregarDepartamentoGUI(frame, inmobiliaria);
                    } catch (DireccionInvalidaException e) {
                        JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                    break;
                case "LOCAL":
                    try {
                        agregarLocalGUI(frame, inmobiliaria);
                    } catch (DireccionInvalidaException e) {
                        JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                    break;
                case "COCHERA":
                    try {
                        agregarCocheraGUI(frame, inmobiliaria);
                    } catch (DireccionInvalidaException e) {
                        JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                    break;
                default:
                    JOptionPane.showMessageDialog(null, "La opcion ingresada es incorrecta.", "Error", JOptionPane.ERROR_MESSAGE);
                    break;
            }

            int opcionContinuar = JOptionPane.showConfirmDialog(
                    null,
                    "Desea agregar otro inmueble?",
                    "Agregar Inmueble",
                    JOptionPane.YES_NO_OPTION
            );

            if (opcionContinuar == JOptionPane.NO_OPTION) {
                continuar = false;
            }
        } while (continuar);

        frame.setVisible(true);
    }

    public static void agregarCasaGUI(JFrame frame, Inmobiliaria inmobiliaria) throws DireccionInvalidaException {
        JTextField direccionField = new JTextField();
        JSpinner ambientesSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 100, 1));
        JSpinner pisosSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 100, 1));
        JSpinner baniosSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 100, 1));
        JSpinner metrosCuadradosSpinner = new JSpinner(new SpinnerNumberModel(0, 0, Integer.MAX_VALUE, 1));
        JRadioButton amuebladoSiRadio = new JRadioButton("Sí");
        JRadioButton amuebladoNoRadio = new JRadioButton("No");
        ButtonGroup grupoAmueblado = new ButtonGroup();
        grupoAmueblado.add(amuebladoSiRadio);
        grupoAmueblado.add(amuebladoNoRadio);
        JRadioButton patioSiRadio = new JRadioButton("Sí");
        JRadioButton patioNoRadio = new JRadioButton("No");
        ButtonGroup grupoPatio = new ButtonGroup();
        grupoPatio.add(patioSiRadio);
        grupoPatio.add(patioNoRadio);
        JRadioButton cocheraSiRadio = new JRadioButton("Sí");
        JRadioButton cocheraNoRadio = new JRadioButton("No");
        ButtonGroup grupoCochera = new ButtonGroup();
        grupoCochera.add(cocheraSiRadio);
        grupoCochera.add(cocheraNoRadio);
        JTextField precioField = new JTextField();
        JComboBox<String> estadoComboBox = new JComboBox<>(new String[]{"Alquiler", "Venta"});

        JPanel panel = new JPanel(new GridLayout(10, 2));
        panel.add(new JLabel("Dirección del departamento:"));
        panel.add(direccionField);
        panel.add(new JLabel("Cantidad de ambientes:"));
        panel.add(ambientesSpinner);
        panel.add(new JLabel("Cantidad de pisos:"));
        panel.add(pisosSpinner);
        panel.add(new JLabel("Cantidad de baños:"));
        panel.add(baniosSpinner);
        panel.add(new JLabel("Metros cuadrados:"));
        panel.add(metrosCuadradosSpinner);
        panel.add(new JLabel("¿Está amueblado?"));
        JPanel amuebladoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        amuebladoPanel.add(amuebladoSiRadio);
        amuebladoPanel.add(amuebladoNoRadio);
        panel.add(amuebladoPanel);
        panel.add(new JLabel("¿Tiene Patio?"));
        JPanel patioPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        patioPanel.add(patioSiRadio);
        patioPanel.add(patioNoRadio);
        panel.add(patioPanel);
        panel.add(new JLabel("¿Tiene cochera?"));
        JPanel cocheraPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        cocheraPanel.add(cocheraSiRadio);
        cocheraPanel.add(cocheraNoRadio);
        panel.add(cocheraPanel);
        panel.add(new JLabel("Precio del inmueble:"));
        panel.add(precioField);
        panel.add(new JLabel("Estado:"));
        panel.add(estadoComboBox);

        int result = JOptionPane.showConfirmDialog(frame, panel, "Agregar nueva Casa",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            String direccion = direccionField.getText();
            if (!Inmobiliaria.validarDireccion(direccion)) {
                throw new DireccionInvalidaException("Dirección ingresada es inválida");
            }
            short ambientes = ((Integer) ambientesSpinner.getValue()).shortValue();
            short banios = ((Integer) baniosSpinner.getValue()).shortValue();
            int metrosCuadrados = ((Integer) metrosCuadradosSpinner.getValue()).intValue();
            boolean amueblado = amuebladoSiRadio.isSelected();
            boolean patio = patioSiRadio.isSelected();
            boolean cochera = cocheraSiRadio.isSelected();
            double precio = Double.parseDouble(precioField.getText());
            short pisos = ((Integer) pisosSpinner.getValue()).shortValue();
            Estado estado = null;
            if (estadoComboBox.getSelectedIndex() == 0) {
                estado = Estado.EnAlquiler;
            } else if (estadoComboBox.getSelectedIndex() == 1) {
                estado = Estado.EnVenta;
            }
            Casa casa = new Casa(estado, direccion, ambientes, banios,
                    metrosCuadrados, amueblado, cochera, precio, patio, pisos);
            inmobiliaria.agregar(casa);
        }
    }

    public static void agregarDepartamentoGUI(JFrame frame, Inmobiliaria inmobiliaria) throws DireccionInvalidaException {
        JTextField direccionField = new JTextField();
        JSpinner ambientesSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 100, 1));
        JSpinner baniosSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 100, 1));
        JSpinner metrosCuadradosSpinner = new JSpinner(new SpinnerNumberModel(0, 0, Integer.MAX_VALUE, 1));
        JRadioButton amuebladoSiRadio = new JRadioButton("Sí");
        JRadioButton amuebladoNoRadio = new JRadioButton("No");
        ButtonGroup grupoAmueblado = new ButtonGroup();
        grupoAmueblado.add(amuebladoSiRadio);
        grupoAmueblado.add(amuebladoNoRadio);
        JRadioButton cocheraSiRadio = new JRadioButton("Sí");
        JRadioButton cocheraNoRadio = new JRadioButton("No");
        ButtonGroup grupoCochera = new ButtonGroup();
        grupoCochera.add(cocheraSiRadio);
        grupoCochera.add(cocheraNoRadio);
        JTextField precioField = new JTextField();
        JComboBox<String> disposicionComboBox = new JComboBox<>(new String[]{"Calle", "Interno"});
        JTextField numeroPisoField = new JTextField();
        JComboBox<String> estadoComboBox = new JComboBox<>(new String[]{"Alquiler", "Venta"});

        JPanel panel = new JPanel(new GridLayout(10, 2));
        panel.add(new JLabel("Dirección del departamento:"));
        panel.add(direccionField);
        panel.add(new JLabel("Cantidad de ambientes:"));
        panel.add(ambientesSpinner);
        panel.add(new JLabel("Cantidad de baños:"));
        panel.add(baniosSpinner);
        panel.add(new JLabel("Metros cuadrados:"));
        panel.add(metrosCuadradosSpinner);
        panel.add(new JLabel("¿Está amueblado?"));
        JPanel amuebladoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        amuebladoPanel.add(amuebladoSiRadio);
        amuebladoPanel.add(amuebladoNoRadio);
        panel.add(amuebladoPanel);
        panel.add(new JLabel("¿Tiene cochera?"));
        JPanel cocheraPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        cocheraPanel.add(cocheraSiRadio);
        cocheraPanel.add(cocheraNoRadio);
        panel.add(cocheraPanel);
        panel.add(new JLabel("Precio del inmueble:"));
        panel.add(precioField);
        panel.add(new JLabel("Disposición:"));
        panel.add(disposicionComboBox);
        panel.add(new JLabel("Número del piso:"));
        panel.add(numeroPisoField);
        panel.add(new JLabel("Estado:"));
        panel.add(estadoComboBox);

        int result = JOptionPane.showConfirmDialog(frame, panel, "Agregar nuevo departamento",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            String direccion = direccionField.getText();
            if (!Inmobiliaria.validarDireccion(direccion)) {
                throw new DireccionInvalidaException("Dirección ingresada es inválida");
            }
            short ambientes = ((Integer) ambientesSpinner.getValue()).shortValue();
            short banios = ((Integer) baniosSpinner.getValue()).shortValue();
            int metrosCuadrados = ((Integer) metrosCuadradosSpinner.getValue()).intValue();
            boolean amueblado = amuebladoSiRadio.isSelected();
            boolean cochera = cocheraSiRadio.isSelected();
            double precio = Double.parseDouble(precioField.getText());
            String disposicion = disposicionComboBox.getSelectedItem().toString();
            String nroPiso = numeroPisoField.getText();
            Estado estado = null;
            if (estadoComboBox.getSelectedIndex() == 0) {
                estado = Estado.EnAlquiler;
            } else if (estadoComboBox.getSelectedIndex() == 1) {
                estado = Estado.EnVenta;
            }
            Departamento departamento = new Departamento(estado, direccion, ambientes, banios,
                    metrosCuadrados, amueblado, cochera, precio, nroPiso, disposicion);
            inmobiliaria.agregar(departamento);
        }
    }

    public static void agregarLocalGUI(JFrame frame, Inmobiliaria inmobiliaria) throws DireccionInvalidaException {
        JTextField direccionField = new JTextField();
        JSpinner ambientesSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 100, 1));
        JCheckBox vidrierasCheckbox = new JCheckBox("Tiene vidrieras?");
        JTextField precioField = new JTextField();
        JComboBox<String> estadoComboBox = new JComboBox<>(new String[]{"Alquiler", "Venta"});

        JPanel panel = new JPanel(new GridLayout(5, 2));
        panel.add(new JLabel("Dirección del local:"));
        panel.add(direccionField);
        panel.add(new JLabel("Cantidad de ambientes:"));
        panel.add(ambientesSpinner);
        panel.add(new JLabel("Vidrieras:"));
        panel.add(vidrierasCheckbox);
        panel.add(new JLabel("Precio del inmueble:"));
        panel.add(precioField);
        panel.add(new JLabel("Estado:"));
        panel.add(estadoComboBox);

        int result = JOptionPane.showConfirmDialog(frame, panel, "Agregar nuevo local",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            String direccion = direccionField.getText();
            if (!Inmobiliaria.validarDireccion(direccion)) {
                throw new DireccionInvalidaException("Dirección ingresada es inválida");
            }
            short ambientes = ((Integer) ambientesSpinner.getValue()).shortValue();
            boolean vidrieras = vidrierasCheckbox.isSelected();
            double precio = Double.parseDouble(precioField.getText());
            Estado estado = null;
            if (estadoComboBox.getSelectedIndex() == 0) {
                estado = Estado.EnAlquiler;
            } else if (estadoComboBox.getSelectedIndex() == 1) {
                estado = Estado.EnVenta;
            }
            Local local = new Local(direccion, estado, ambientes, vidrieras, precio);
            inmobiliaria.agregar(local);
        }
    }

    public static void agregarCocheraGUI(JFrame frame, Inmobiliaria inmobiliaria) throws DireccionInvalidaException {
        JTextField direccionField = new JTextField();
        JSpinner pisoSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 100, 1));
        JSpinner posicionSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 100, 1));
        JRadioButton ascensorRadio = new JRadioButton("Ascensor");
        JRadioButton rampaRadio = new JRadioButton("Rampa");
        ButtonGroup grupoMedioDeAcceso = new ButtonGroup();
        grupoMedioDeAcceso.add(ascensorRadio);
        grupoMedioDeAcceso.add(rampaRadio);
        JTextField precioField = new JTextField();
        JComboBox<String> estadoComboBox = new JComboBox<>(new String[]{"Alquiler", "Venta"});

        JPanel panel = new JPanel(new GridLayout(6, 2));
        panel.add(new JLabel("Dirección de la cochera:"));
        panel.add(direccionField);
        panel.add(new JLabel("Número de piso:"));
        panel.add(pisoSpinner);
        panel.add(new JLabel("Posición en el piso:"));
        panel.add(posicionSpinner);
        panel.add(new JLabel("Medio de acceso:"));
        JPanel radioPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        radioPanel.add(ascensorRadio);
        radioPanel.add(rampaRadio);
        panel.add(radioPanel);
        panel.add(new JLabel("Precio del inmueble:"));
        panel.add(precioField);
        panel.add(new JLabel("Estado:"));
        panel.add(estadoComboBox);

        int result = JOptionPane.showConfirmDialog(frame, panel, "Agregar nueva cochera",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            String direccion = direccionField.getText();
            if (!Inmobiliaria.validarDireccion(direccion)) {
                throw new DireccionInvalidaException("Dirección ingresada es inválida");
            }
            short piso = ((Integer) pisoSpinner.getValue()).shortValue();
            short posicion = ((Integer) posicionSpinner.getValue()).shortValue();
            String medioDeAcceso = "";
            if (ascensorRadio.isSelected()) {
                medioDeAcceso = "Ascensor";
            } else if (rampaRadio.isSelected()) {
                medioDeAcceso = "Rampa";
            }
            double precio = Double.parseDouble(precioField.getText());
            Estado estado = null;
            if (estadoComboBox.getSelectedIndex() == 0) {
                estado = Estado.EnAlquiler;
            } else if (estadoComboBox.getSelectedIndex() == 1) {
                estado = Estado.EnVenta;
            }
            Cochera cochera = new Cochera(direccion, estado, piso, posicion, medioDeAcceso, precio);
            inmobiliaria.agregar(cochera);
        }
    }


    public static void darDeBajaInmuebleGUI(Inmobiliaria inmobiliaria) {
        String continuar = "Sí";
        while (continuar.equalsIgnoreCase("Sí")) {
            String tipoInmueble = (String) JOptionPane.showInputDialog(null, "¿Qué tipo de inmueble desea dar de baja?",
                    "Dar de Baja Inmueble", JOptionPane.QUESTION_MESSAGE, null,
                    new String[]{"Casa", "Departamento", "Local", "Cochera"}, "Casa");

            try {
                if (!(tipoInmueble.equalsIgnoreCase("Casa") || tipoInmueble.equalsIgnoreCase("Departamento")
                        || tipoInmueble.equalsIgnoreCase("Local") || tipoInmueble.equalsIgnoreCase("Cochera"))) {
                    JOptionPane.showMessageDialog(null, "La opción ingresada es incorrecta", "Error",
                            JOptionPane.ERROR_MESSAGE);
                    continue;
                } else if (tipoInmueble.equalsIgnoreCase("Casa")) {
                    String direccion = JOptionPane.showInputDialog(null, "Ingrese la dirección del inmueble:",
                            "Dar de Baja Casa", JOptionPane.QUESTION_MESSAGE);
                    Casa casa = inmobiliaria.buscarCasa(direccion);
                    if (casa != null) {
                        inmobiliaria.baja(casa);
                    } else {
                        throw new EleccionIncorrectaException("Dirección no existente");
                    }
                } else if (tipoInmueble.equalsIgnoreCase("Departamento")) {
                    String direccion = JOptionPane.showInputDialog(null, "Ingrese la dirección del inmueble:",
                            "Dar de Baja Departamento", JOptionPane.QUESTION_MESSAGE);
                    Departamento departamento = inmobiliaria.buscarDepartamento(direccion);
                    if (departamento != null) {
                        inmobiliaria.baja(departamento);
                    } else {
                        throw new EleccionIncorrectaException("Dirección no existente");
                    }
                } else if (tipoInmueble.equalsIgnoreCase("Local")) {
                    String direccion = JOptionPane.showInputDialog(null, "Ingrese la dirección del inmueble:",
                            "Dar de Baja Local", JOptionPane.QUESTION_MESSAGE);
                    Local local = inmobiliaria.buscarLocal(direccion);
                    if (local != null) {
                        inmobiliaria.baja(local);
                    } else {
                        throw new EleccionIncorrectaException("Dirección no existente");
                    }
                } else if (tipoInmueble.equalsIgnoreCase("Cochera")) {
                    String direccion = JOptionPane.showInputDialog(null, "Ingrese la dirección del inmueble:",
                            "Dar de Baja Cochera", JOptionPane.QUESTION_MESSAGE);
                    Cochera cochera = inmobiliaria.buscarCochera(direccion);
                    if (cochera != null) {
                        inmobiliaria.baja(cochera);
                    } else {
                        throw new EleccionIncorrectaException("Dirección no existente");
                    }
                }
            } catch (EleccionIncorrectaException e) {
                JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                continue;
            }

            continuar = (String) JOptionPane.showInputDialog(null, "¿Desea dar de baja otro inmueble?", "Confirmación",
                    JOptionPane.QUESTION_MESSAGE, null, new String[]{"Sí", "No"}, "Sí");
        }
    }

    public static void modificarInmuebleGUI(Inmobiliaria inmobiliaria) {
        String continuar = "";
        String tipoInmueble = (String) JOptionPane.showInputDialog(null, "¿Qué inmueble desea modificar?",
                "Modificar Inmueble", JOptionPane.QUESTION_MESSAGE, null,
                new String[]{"Casa", "Departamento", "Local", "Cochera"}, "Casa");
        while (!continuar.equalsIgnoreCase("No")) {
            try {
                if (!(tipoInmueble.equalsIgnoreCase("Casa") || tipoInmueble.equalsIgnoreCase("Departamento")
                        || tipoInmueble.equalsIgnoreCase("Local") || tipoInmueble.equalsIgnoreCase("Cochera"))) {
                    JOptionPane.showMessageDialog(null, "La opción ingresada es incorrecta", "Error",
                            JOptionPane.ERROR_MESSAGE);
                    continue;
                } else if (tipoInmueble.equalsIgnoreCase("Casa")) {
                    modificarCasaGUI(inmobiliaria);
                } else if (tipoInmueble.equalsIgnoreCase("Departamento")) {
                    modificarDepartamentoGUI(inmobiliaria);
                } else if (tipoInmueble.equalsIgnoreCase("Local")) {
                    modificarLocalGUI(inmobiliaria);
                } else if (tipoInmueble.equalsIgnoreCase("Cochera")) {
                    modificarCocheraGUI(inmobiliaria);
                }
            } catch (EleccionIncorrectaException e) {
                JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
            continuar = (String) JOptionPane.showInputDialog(null, "¿Desea modificar otro inmueble?", "Confirmación",
                    JOptionPane.QUESTION_MESSAGE, null, new String[]{"Sí", "No"}, "Sí");
            if (continuar.equalsIgnoreCase("No")) {
                break;
            } else {
                modificarInmuebleGUI(inmobiliaria);
            }
        }
    }

    public static void modificarCasaGUI(Inmobiliaria inmobiliaria) throws EleccionIncorrectaException {
        String direccion = JOptionPane.showInputDialog(null, "Ingrese la dirección del inmueble");
        Casa casaEncontrada = inmobiliaria.buscarCasa(direccion);

        if (casaEncontrada != null) {
            Casa casa = null;
            String continuar = "";
            while (!continuar.equalsIgnoreCase("No")) {
                String[] opciones = {"Estado", "Ambientes", "Cantidad de baños", "Metros cuadrados", "Amueblado",
                        "Cochera", "Precio", "Patio", "Pisos"};
                int opcion = JOptionPane.showOptionDialog(null, "¿Qué atributo desea modificar?",
                        "Modificar Casa - " + casaEncontrada.getDireccion(), 0, JOptionPane.QUESTION_MESSAGE, null,
                        opciones, opciones[0]);

                switch (opcion) {
                    case 0:
                        Object[] opcionesEstado = {"Alquiler", "Venta"};
                        int opcionEstado = JOptionPane.showOptionDialog(null, "¿Desea alquilar o vender la casa?",
                                "Modificar Estado", 0, JOptionPane.QUESTION_MESSAGE, null, opcionesEstado,
                                opcionesEstado[0]);
                        Estado estado = null;
                        if (opcionEstado == 0) {
                            estado = Estado.EnAlquiler;
                        } else if (opcionEstado == 1) {
                            estado = Estado.EnVenta;
                        }
                        casa = new Casa(estado, casaEncontrada.getDireccion(), casaEncontrada.getAmbientes(),
                                casaEncontrada.getCantBanios(), casaEncontrada.getMetrosCuadrados(),
                                casaEncontrada.isAmueblado(), casaEncontrada.isCochera(), casaEncontrada.getPrecio(),
                                casaEncontrada.isPatio(), casaEncontrada.getPisos());
                        inmobiliaria.modificar(casa);
                        break;
                    case 1:
                        String ambientesStr = JOptionPane.showInputDialog(null, "Ingrese la cantidad de ambientes",
                                casaEncontrada.getAmbientes());
                        short ambientes = Short.parseShort(ambientesStr);
                        casa = new Casa(casaEncontrada.getEstado(), casaEncontrada.getDireccion(), ambientes,
                                casaEncontrada.getCantBanios(), casaEncontrada.getMetrosCuadrados(),
                                casaEncontrada.isAmueblado(), casaEncontrada.isCochera(), casaEncontrada.getPrecio(),
                                casaEncontrada.isPatio(), casaEncontrada.getPisos());
                        inmobiliaria.modificar(casa);
                        break;
                    case 2:
                        String cantBañosStr = JOptionPane.showInputDialog(null, "Ingrese la cantidad de baños",
                                casaEncontrada.getCantBanios());
                        short cantBaños = Short.parseShort(cantBañosStr);
                        casa = new Casa(casaEncontrada.getEstado(), casaEncontrada.getDireccion(),
                                casaEncontrada.getAmbientes(), cantBaños, casaEncontrada.getMetrosCuadrados(),
                                casaEncontrada.isAmueblado(), casaEncontrada.isCochera(), casaEncontrada.getPrecio(),
                                casaEncontrada.isPatio(), casaEncontrada.getPisos());
                        inmobiliaria.modificar(casa);
                        break;
                    case 3:
                        String metrosCuadradosStr = JOptionPane.showInputDialog(null,
                                "Ingrese los metros cuadrados del inmueble", casaEncontrada.getMetrosCuadrados());
                        int metrosCuadrados = Integer.parseInt(metrosCuadradosStr);
                        casa = new Casa(casaEncontrada.getEstado(), casaEncontrada.getDireccion(),
                                casaEncontrada.getAmbientes(), casaEncontrada.getCantBanios(), metrosCuadrados,
                                casaEncontrada.isAmueblado(), casaEncontrada.isCochera(), casaEncontrada.getPrecio(),
                                casaEncontrada.isPatio(), casaEncontrada.getPisos());
                        inmobiliaria.modificar(casa);
                        break;
                    case 4:
                        Object[] opcionesAmueblado = {"Sí", "No"};
                        int opcionAmueblado = JOptionPane.showOptionDialog(null, "¿Está amueblado el inmueble?",
                                "Modificar Amueblado", 0, JOptionPane.QUESTION_MESSAGE, null, opcionesAmueblado,
                                opcionesAmueblado[0]);
                        boolean amueblado = false;
                        if (opcionAmueblado == 0) {
                            amueblado = true;
                        }
                        casa = new Casa(casaEncontrada.getEstado(), casaEncontrada.getDireccion(),
                                casaEncontrada.getAmbientes(), casaEncontrada.getCantBanios(),
                                casaEncontrada.getMetrosCuadrados(), amueblado, casaEncontrada.isCochera(),
                                casaEncontrada.getPrecio(), casaEncontrada.isPatio(), casaEncontrada.getPisos());
                        inmobiliaria.modificar(casa);
                        break;
                    case 5:
                        Object[] opcionesCochera = {"Sí", "No"};
                        int opcionCochera = JOptionPane.showOptionDialog(null, "¿Tiene cochera el inmueble?",
                                "Modificar Cochera", 0, JOptionPane.QUESTION_MESSAGE, null, opcionesCochera,
                                opcionesCochera[0]);
                        boolean cochera = false;
                        if (opcionCochera == 0) {
                            cochera = true;
                        }
                        casa = new Casa(casaEncontrada.getEstado(), casaEncontrada.getDireccion(),
                                casaEncontrada.getAmbientes(), casaEncontrada.getCantBanios(),
                                casaEncontrada.getMetrosCuadrados(), casaEncontrada.isAmueblado(), cochera,
                                casaEncontrada.getPrecio(), casaEncontrada.isPatio(), casaEncontrada.getPisos());
                        inmobiliaria.modificar(casa);
                        break;
                    case 6:
                        String precioStr = JOptionPane.showInputDialog(null, "Ingrese el precio del inmueble",
                                casaEncontrada.getPrecio());
                        double precio = Double.parseDouble(precioStr);
                        casa = new Casa(casaEncontrada.getEstado(), casaEncontrada.getDireccion(),
                                casaEncontrada.getAmbientes(), casaEncontrada.getCantBanios(),
                                casaEncontrada.getMetrosCuadrados(), casaEncontrada.isAmueblado(),
                                casaEncontrada.isCochera(), precio, casaEncontrada.isPatio(), casaEncontrada.getPisos());
                        inmobiliaria.modificar(casa);
                        break;
                    case 7:
                        Object[] opcionesPatio = {"Sí", "No"};
                        int opcionPatio = JOptionPane.showOptionDialog(null, "¿Tiene patio el inmueble?",
                                "Modificar Patio", 0, JOptionPane.QUESTION_MESSAGE, null, opcionesPatio,
                                opcionesPatio[0]);
                        boolean patio = false;
                        if (opcionPatio == 0) {
                            patio = true;
                        }
                        casa = new Casa(casaEncontrada.getEstado(), casaEncontrada.getDireccion(),
                                casaEncontrada.getAmbientes(), casaEncontrada.getCantBanios(),
                                casaEncontrada.getMetrosCuadrados(), casaEncontrada.isAmueblado(),
                                casaEncontrada.isCochera(), casaEncontrada.getPrecio(), patio, casaEncontrada.getPisos());
                        inmobiliaria.modificar(casa);
                        break;
                    case 8:
                        String pisosStr = JOptionPane.showInputDialog(null, "Ingrese la cantidad de pisos",
                                casaEncontrada.getPisos());
                        short pisos = Short.parseShort(pisosStr);
                        casa = new Casa(casaEncontrada.getEstado(), casaEncontrada.getDireccion(),
                                casaEncontrada.getAmbientes(), casaEncontrada.getCantBanios(),
                                casaEncontrada.getMetrosCuadrados(), casaEncontrada.isAmueblado(),
                                casaEncontrada.isCochera(), casaEncontrada.getPrecio(), casaEncontrada.isPatio(), pisos);
                        inmobiliaria.modificar(casa);
                        break;
                }

                continuar = (String) JOptionPane.showInputDialog(null, "¿Desea seguir modificando esta casa?",
                        "Confirmación", JOptionPane.QUESTION_MESSAGE, null, new String[]{"Sí", "No"}, "Sí");
            }
        } else {
            JOptionPane.showMessageDialog(null, "No se encontró una casa con esa dirección", "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void modificarDepartamentoGUI(Inmobiliaria inmobiliaria) throws EleccionIncorrectaException {
        String direccion = JOptionPane.showInputDialog(null, "Ingrese la dirección del inmueble");
        Departamento departamentoEncontrado = inmobiliaria.buscarDepartamento(direccion);

        if (departamentoEncontrado != null) {
            Departamento departamento = null;
            String continuar = "";
            while (!continuar.equalsIgnoreCase("No")) {
                String[] opciones = {"Estado", "Ambientes", "Cantidad de baños", "Metros cuadrados", "Amueblado",
                        "Cochera", "Precio", "Patio", "Pisos"};
                int opcion = JOptionPane.showOptionDialog(null, "¿Qué atributo desea modificar?",
                        "Modificar Departamento - " + departamentoEncontrado.getDireccion(), 0, JOptionPane.QUESTION_MESSAGE, null,
                        opciones, opciones[0]);

                switch (opcion) {
                    case 0:
                        Object[] opcionesEstado = {"Alquiler", "Venta"};
                        int opcionEstado = JOptionPane.showOptionDialog(null, "¿Desea alquilar o vender la Departamento?",
                                "Modificar Estado", 0, JOptionPane.QUESTION_MESSAGE, null, opcionesEstado,
                                opcionesEstado[0]);
                        Estado estado = null;
                        if (opcionEstado == 0) {
                            estado = Estado.EnAlquiler;
                        } else if (opcionEstado == 1) {
                            estado = Estado.EnVenta;
                        }
                        departamento = new Departamento(estado, departamentoEncontrado.getDireccion(), departamentoEncontrado.getAmbientes(),
                                departamentoEncontrado.getCantBanios(), departamentoEncontrado.getMetrosCuadrados(),
                                departamentoEncontrado.isAmueblado(), departamentoEncontrado.isCochera(), departamentoEncontrado.getPrecio(),
                                departamentoEncontrado.getNroPiso(), departamentoEncontrado.getDisposicion());
                        inmobiliaria.modificar(departamento);
                        break;
                    case 1:
                        String ambientesStr = JOptionPane.showInputDialog(null, "Ingrese la cantidad de ambientes",
                                departamentoEncontrado.getAmbientes());
                        short ambientes = Short.parseShort(ambientesStr);
                        departamento = new Departamento(departamentoEncontrado.getEstado(), departamentoEncontrado.getDireccion(), ambientes,
                                departamentoEncontrado.getCantBanios(), departamentoEncontrado.getMetrosCuadrados(),
                                departamentoEncontrado.isAmueblado(), departamentoEncontrado.isCochera(), departamentoEncontrado.getPrecio(),
                                departamentoEncontrado.getNroPiso(), departamentoEncontrado.getDisposicion());
                        inmobiliaria.modificar(departamento);
                        break;
                    case 2:
                        String cantBañosStr = JOptionPane.showInputDialog(null, "Ingrese la cantidad de baños",
                                departamentoEncontrado.getCantBanios());
                        short cantBaños = Short.parseShort(cantBañosStr);
                        departamento = new Departamento(departamentoEncontrado.getEstado(), departamentoEncontrado.getDireccion(),
                                departamentoEncontrado.getAmbientes(), cantBaños, departamentoEncontrado.getMetrosCuadrados(),
                                departamentoEncontrado.isAmueblado(), departamentoEncontrado.isCochera(), departamentoEncontrado.getPrecio(),
                                departamentoEncontrado.getNroPiso(), departamentoEncontrado.getDisposicion());
                        inmobiliaria.modificar(departamento);
                        break;
                    case 3:
                        String metrosCuadradosStr = JOptionPane.showInputDialog(null,
                                "Ingrese los metros cuadrados del inmueble", departamentoEncontrado.getMetrosCuadrados());
                        int metrosCuadrados = Integer.parseInt(metrosCuadradosStr);
                        departamento = new Departamento(departamentoEncontrado.getEstado(), departamentoEncontrado.getDireccion(),
                                departamentoEncontrado.getAmbientes(), departamentoEncontrado.getCantBanios(), metrosCuadrados,
                                departamentoEncontrado.isAmueblado(), departamentoEncontrado.isCochera(), departamentoEncontrado.getPrecio(),
                                departamentoEncontrado.getNroPiso(), departamentoEncontrado.getDisposicion());
                        inmobiliaria.modificar(departamento);
                        break;
                    case 4:
                        Object[] opcionesAmueblado = {"Sí", "No"};
                        int opcionAmueblado = JOptionPane.showOptionDialog(null, "¿Está amueblado el inmueble?",
                                "Modificar Amueblado", 0, JOptionPane.QUESTION_MESSAGE, null, opcionesAmueblado,
                                opcionesAmueblado[0]);
                        boolean amueblado = false;
                        if (opcionAmueblado == 0) {
                            amueblado = true;
                        }
                        departamento = new Departamento(departamentoEncontrado.getEstado(), departamentoEncontrado.getDireccion(),
                                departamentoEncontrado.getAmbientes(), departamentoEncontrado.getCantBanios(),
                                departamentoEncontrado.getMetrosCuadrados(), amueblado, departamentoEncontrado.isCochera(),
                                departamentoEncontrado.getPrecio(), departamentoEncontrado.getNroPiso(), departamentoEncontrado.getDisposicion());
                        inmobiliaria.modificar(departamento);
                        break;
                    case 5:
                        Object[] opcionesCochera = {"Sí", "No"};
                        int opcionCochera = JOptionPane.showOptionDialog(null, "¿Tiene cochera el inmueble?",
                                "Modificar Cochera", 0, JOptionPane.QUESTION_MESSAGE, null, opcionesCochera,
                                opcionesCochera[0]);
                        boolean cochera = false;
                        if (opcionCochera == 0) {
                            cochera = true;
                        }
                        departamento = new Departamento(departamentoEncontrado.getEstado(), departamentoEncontrado.getDireccion(),
                                departamentoEncontrado.getAmbientes(), departamentoEncontrado.getCantBanios(),
                                departamentoEncontrado.getMetrosCuadrados(), departamentoEncontrado.isAmueblado(), cochera,
                                departamentoEncontrado.getPrecio(), departamentoEncontrado.getNroPiso(), departamentoEncontrado.getDisposicion());
                        inmobiliaria.modificar(departamento);
                        break;
                    case 6:
                        String precioStr = JOptionPane.showInputDialog(null, "Ingrese el precio del inmueble",
                                departamentoEncontrado.getPrecio());
                        double precio = Double.parseDouble(precioStr);
                        departamento = new Departamento(departamentoEncontrado.getEstado(), departamentoEncontrado.getDireccion(),
                                departamentoEncontrado.getAmbientes(), departamentoEncontrado.getCantBanios(),
                                departamentoEncontrado.getMetrosCuadrados(), departamentoEncontrado.isAmueblado(),
                                departamentoEncontrado.isCochera(), precio, departamentoEncontrado.getNroPiso(), departamentoEncontrado.getDisposicion());
                        inmobiliaria.modificar(departamento);
                        break;
                    case 7:
                        Object[] opcionesDisposicion = {"A la calle", "Interno"};
                        int opcionDisposicion = JOptionPane.showOptionDialog(null, "¿Cual es la disposicion?",
                                "Modificar Disposicion", 0, JOptionPane.QUESTION_MESSAGE, null, opcionesDisposicion,
                                opcionesDisposicion[0]);
                        String disposicion = "";
                        if (opcionDisposicion == 0) {
                            disposicion = "A la calle";
                        } else {
                            disposicion = "Interno";
                        }
                        departamento = new Departamento(departamentoEncontrado.getEstado(), departamentoEncontrado.getDireccion(),
                                departamentoEncontrado.getAmbientes(), departamentoEncontrado.getCantBanios(),
                                departamentoEncontrado.getMetrosCuadrados(), departamentoEncontrado.isAmueblado(),
                                departamentoEncontrado.isCochera(), departamentoEncontrado.getPrecio(), departamentoEncontrado.getNroPiso(), disposicion);
                        inmobiliaria.modificar(departamento);
                        break;
                    case 8:
                        String disposicionstr = JOptionPane.showInputDialog(null, "¿Cual es la disposicion?",
                                departamentoEncontrado.getDisposicion());
                        departamento = new Departamento(departamentoEncontrado.getEstado(), departamentoEncontrado.getDireccion(),
                                departamentoEncontrado.getAmbientes(), departamentoEncontrado.getCantBanios(),
                                departamentoEncontrado.getMetrosCuadrados(), departamentoEncontrado.isAmueblado(),
                                departamentoEncontrado.isCochera(), departamentoEncontrado.getPrecio(), departamentoEncontrado.getNroPiso(), disposicionstr);
                        inmobiliaria.modificar(departamento);
                        break;
                }

                continuar = (String) JOptionPane.showInputDialog(null, "¿Desea seguir modificando este departamento?",
                        "Confirmación", JOptionPane.QUESTION_MESSAGE, null, new String[]{"Sí", "No"}, "Sí");
            }
        } else {
            JOptionPane.showMessageDialog(null, "No se encontró un Departamento con esa dirección", "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void modificarCocheraGUI(Inmobiliaria inmobiliaria) throws EleccionIncorrectaException {
        String direccion = JOptionPane.showInputDialog(null, "Ingrese la dirección del inmueble");
        Cochera cocheraEncontrado = inmobiliaria.buscarCochera(direccion);

        if (cocheraEncontrado != null) {
            Cochera cochera = null;
            String continuar = "";
            while (!continuar.equalsIgnoreCase("No")) {
                String[] opciones = {"Estado", "Piso", "Posicion", "Medio De Acceso", "Precio"};
                int opcion = JOptionPane.showOptionDialog(null, "¿Qué atributo desea modificar?",
                        "Modificar Cochera - " + cocheraEncontrado.getDireccion(), 0, JOptionPane.QUESTION_MESSAGE, null,
                        opciones, opciones[0]);
                switch (opcion) {
                    case 0:
                        Object[] opcionesEstado = {"Alquiler", "Venta"};
                        int opcionEstado = JOptionPane.showOptionDialog(null, "¿Desea alquilar o vender la Cochera?",
                                "Modificar Estado", 0, JOptionPane.QUESTION_MESSAGE, null, opcionesEstado,
                                opcionesEstado[0]);
                        Estado estado = null;
                        if (opcionEstado == 0) {
                            estado = Estado.EnAlquiler;
                        } else if (opcionEstado == 1) {
                            estado = Estado.EnVenta;
                        }
                        cochera = new Cochera(cocheraEncontrado.getDireccion(), estado, cocheraEncontrado.getPiso(),
                                cocheraEncontrado.getPosicion(), cocheraEncontrado.getMedioDeAcceso(),
                                cocheraEncontrado.getPrecio());
                        inmobiliaria.modificar(cochera);
                        break;
                    case 1:
                        String pisoStr = JOptionPane.showInputDialog(null, "Ingrese el piso",
                                cocheraEncontrado.getPiso());
                        short piso = Short.parseShort(pisoStr);
                        cochera = new Cochera(cocheraEncontrado.getDireccion(), cocheraEncontrado.getEstado(), piso,
                                cocheraEncontrado.getPosicion(), cocheraEncontrado.getMedioDeAcceso(),
                                cocheraEncontrado.getPrecio());
                        inmobiliaria.modificar(cochera);
                        break;
                    case 2:
                        String posicionStr = JOptionPane.showInputDialog(null, "Ingrese la posicion",
                                cocheraEncontrado.getPosicion());
                        short posicion = Short.parseShort(posicionStr);
                        cochera = new Cochera(cocheraEncontrado.getDireccion(), cocheraEncontrado.getEstado(), cocheraEncontrado.getPiso(),
                                posicion, cocheraEncontrado.getMedioDeAcceso(),
                                cocheraEncontrado.getPrecio());
                        inmobiliaria.modificar(cochera);
                        break;
                    case 3:
                        Object[] opcionesMedioDeAcceso = {"Ascensor", "Escalera"};
                        int opcionMedioDeAcceso = JOptionPane.showOptionDialog(null, "¿Cual es la disposicion?",
                                "Modificar Disposicion", 0, JOptionPane.QUESTION_MESSAGE, null, opcionesMedioDeAcceso,
                                opcionesMedioDeAcceso[0]);
                        String medioDeAcceso = "";
                        if (opcionMedioDeAcceso == 0) {
                            medioDeAcceso = "Ascensor";
                        } else {
                            medioDeAcceso = "Escalera";
                        }
                        cochera = new Cochera(cocheraEncontrado.getDireccion(), cocheraEncontrado.getEstado(), cocheraEncontrado.getPiso(),
                                cocheraEncontrado.getPosicion(), medioDeAcceso,
                                cocheraEncontrado.getPrecio());
                        inmobiliaria.modificar(cochera);
                        break;
                    case 4:
                        String precioStr = JOptionPane.showInputDialog(null, "Ingrese el precio del inmueble",
                                cocheraEncontrado.getPrecio());
                        double precio = Double.parseDouble(precioStr);
                        cochera = new Cochera(cocheraEncontrado.getDireccion(), cocheraEncontrado.getEstado(), cocheraEncontrado.getPiso(),
                                cocheraEncontrado.getPosicion(), cocheraEncontrado.getMedioDeAcceso(),
                                precio);
                        inmobiliaria.modificar(cochera);
                        break;
                }

                continuar = (String) JOptionPane.showInputDialog(null, "¿Desea seguir modificando esta cochera?",
                        "Confirmación", JOptionPane.QUESTION_MESSAGE, null, new String[]{"Sí", "No"}, "Sí");
            }
        } else {
            JOptionPane.showMessageDialog(null, "No se encontró una cochera con esa dirección", "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void modificarLocalGUI(Inmobiliaria inmobiliaria) throws EleccionIncorrectaException {
        String direccion = JOptionPane.showInputDialog(null, "Ingrese la dirección del inmueble");
        Local localEncontrado = inmobiliaria.buscarLocal(direccion);

        if (localEncontrado != null) {
            Local local = null;
            String continuar = "";
            while (!continuar.equalsIgnoreCase("No")) {
                String[] opciones = {"Estado", "Ambientes", "Vidriera", "Precio"};
                int opcion = JOptionPane.showOptionDialog(null, "¿Qué atributo desea modificar?",
                        "Modificar Local - " + localEncontrado.getDireccion(), 0, JOptionPane.QUESTION_MESSAGE, null,
                        opciones, opciones[0]);

                switch (opcion) {
                    case 0:
                        Object[] opcionesEstado = {"Alquiler", "Venta"};
                        int opcionEstado = JOptionPane.showOptionDialog(null, "¿Desea alquilar o vender la local?",
                                "Modificar Estado", 0, JOptionPane.QUESTION_MESSAGE, null, opcionesEstado,
                                opcionesEstado[0]);
                        Estado estado = null;
                        if (opcionEstado == 0) {
                            estado = Estado.EnAlquiler;
                        } else if (opcionEstado == 1) {
                            estado = Estado.EnVenta;
                        }
                        local = new Local(localEncontrado.getDireccion(), estado, localEncontrado.getAmbientes(),
                                localEncontrado.isVidriera(), localEncontrado.getPrecio());
                        inmobiliaria.modificar(local);
                        break;
                    case 1:
                        String ambientesStr = JOptionPane.showInputDialog(null, "Ingrese la cantidad de ambientes",
                                localEncontrado.getAmbientes());
                        short ambientes = Short.parseShort(ambientesStr);
                        local = new Local(localEncontrado.getDireccion(), localEncontrado.getEstado(), ambientes,
                                localEncontrado.isVidriera(), localEncontrado.getPrecio());
                        inmobiliaria.modificar(local);
                        break;
                    case 2:
                        Object[] opcionesvidriera = {"Sí", "No"};
                        int opcionVidriera = JOptionPane.showOptionDialog(null, "¿Tiene vidriera el inmueble?",
                                "Modificar Vidriera", 0, JOptionPane.QUESTION_MESSAGE, null, opcionesvidriera,
                                opcionesvidriera[0]);
                        boolean vidriera = false;
                        if (opcionVidriera == 0) {
                            vidriera = true;
                        }
                        local = new Local(localEncontrado.getDireccion(), localEncontrado.getEstado(), localEncontrado.getAmbientes(),
                                vidriera, localEncontrado.getPrecio());
                        inmobiliaria.modificar(local);
                        break;
                    case 3:
                        String precioStr = JOptionPane.showInputDialog(null, "Ingrese el precio del inmueble",
                                localEncontrado.getPrecio());
                        double precio = Double.parseDouble(precioStr);
                        local = new Local(localEncontrado.getDireccion(), localEncontrado.getEstado(), localEncontrado.getAmbientes(),
                                localEncontrado.isVidriera(), precio);
                        inmobiliaria.modificar(local);
                        break;
                }
                continuar = (String) JOptionPane.showInputDialog(null, "¿Desea seguir modificando este local?",
                        "Confirmación", JOptionPane.QUESTION_MESSAGE, null, new String[]{"Sí", "No"}, "Sí");
            }
        } else {
            JOptionPane.showMessageDialog(null, "No se encontró una cochera con esa dirección", "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void buscarInmuebleGUI(Inmobiliaria inmobiliaria) throws EleccionIncorrectaException {
        String continuar = "no";
        do {
            String tipoInmueble = (String) JOptionPane.showInputDialog(null, "¿Qué inmueble desea listar?",
                    "Listar Inmueble", JOptionPane.QUESTION_MESSAGE, null,
                    new String[]{"Casa", "Departamento", "Local", "Cochera"}, "Casa");
            if (tipoInmueble == null) { // Si el usuario cierra la ventana emergente sin seleccionar una opción
                return;
            } else if (!(tipoInmueble.equalsIgnoreCase("Casa") || tipoInmueble.equalsIgnoreCase("Departamento")
                    || tipoInmueble.equalsIgnoreCase("Local") || tipoInmueble.equalsIgnoreCase("Cochera"))) {
                continuar = "si";
                JOptionPane.showMessageDialog(null, "Opción ingresada es incorrecta");
            } else {
                try {
                    System.out.println("Ingrese la dirección del inmueble:");
                    String direccion = JOptionPane.showInputDialog(null, "Dirección:", "Buscar Inmueble", JOptionPane.PLAIN_MESSAGE);
                    if (direccion == null) {
                        return;
                    } else if (tipoInmueble.equalsIgnoreCase("Casa")) {
                        Casa casa = inmobiliaria.buscarCasa(direccion);
                        if (casa != null) {
                            JOptionPane.showMessageDialog(null, casa.toString(), "Resultado de búsqueda", JOptionPane.PLAIN_MESSAGE);
                        } else {
                            throw new EleccionIncorrectaException("Dirección no existente");
                        }
                    } else if (tipoInmueble.equalsIgnoreCase("Departamento")) {
                        Departamento departamento = inmobiliaria.buscarDepartamento(direccion);
                        if (departamento != null) {
                            JOptionPane.showMessageDialog(null, departamento.toString(), "Resultado de búsqueda", JOptionPane.PLAIN_MESSAGE);
                        } else {
                            throw new EleccionIncorrectaException("Dirección no existente");
                        }
                    } else if (tipoInmueble.equalsIgnoreCase("Local")) {
                        Local local = inmobiliaria.buscarLocal(direccion);
                        if (local != null) {
                            JOptionPane.showMessageDialog(null, local.toString(), "Resultado de búsqueda", JOptionPane.PLAIN_MESSAGE);
                        } else {
                            throw new EleccionIncorrectaException("Dirección no existente");
                        }
                    } else if (tipoInmueble.equalsIgnoreCase("Cochera")) {
                        Cochera cochera = inmobiliaria.buscarCochera(direccion);
                        if (cochera != null) {
                            JOptionPane.showMessageDialog(null, cochera.toString(), "Resultado de búsqueda", JOptionPane.PLAIN_MESSAGE);
                        } else {
                            throw new EleccionIncorrectaException("Dirección no existente");
                        }
                    }
                } catch (EleccionIncorrectaException e) {
                    JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }

                int seleccion = JOptionPane.showOptionDialog(
                        null, "¿Desea mostrar otro inmueble?", "Buscar Inmueble",
                        JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,
                        null, new Object[]{"Sí", "No"}, "No");
                continuar = (seleccion == JOptionPane.YES_OPTION) ? "si" : "no";
            }
        } while (continuar.equalsIgnoreCase("si"));
    }

    public static void darDeBajaUsuarioGUI(Inmobiliaria inmobiliaria) {
        String continuar = "no";
        do {
            String mailUsuario = JOptionPane.showInputDialog(null, "¿Qué usuario desea dar de baja? (Mail del Usuario)",
                    "Dar de Baja Usuario", JOptionPane.PLAIN_MESSAGE);
            if (mailUsuario == null) { // Si el usuario cierra la ventana emergente sin ingresar datos
                return;
            } else if (!inmobiliaria.darBaja(mailUsuario)) { // Si el método devuelve falso
                JOptionPane.showMessageDialog(null, "No se encontró ningún usuario con ese mail.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Usuario dado de baja exitosamente.",
                        "Dar de Baja Usuario", JOptionPane.INFORMATION_MESSAGE);
            }

            int seleccion = JOptionPane.showOptionDialog(
                    null, "¿Desea dar de baja otro usuario?", "Dar de Baja Usuario",
                    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,
                    null, new Object[]{"Sí", "No"}, "No");
            continuar = (seleccion == JOptionPane.YES_OPTION) ? "si" : "no";
        } while (continuar.equalsIgnoreCase("si"));
    }

    public static void mostrarUsuarioGUI(Inmobiliaria inmobiliaria) throws UsuarioNoEncontradoException {
        String continuar = "Sí";
        while (continuar.equalsIgnoreCase("Sí")) {
            String mail = JOptionPane.showInputDialog("Mail del usuario: ");
            Usuario usuario = inmobiliaria.buscarUsuario(mail);
            if (usuario == null) {
                throw new UsuarioNoEncontradoException("Usuario no encontrado");
            }
            JOptionPane.showMessageDialog(null, usuario.toString());
            continuar = (String) JOptionPane.showInputDialog(null, "¿Desea mostrar otro usuario?", "Confirmación",
                    JOptionPane.QUESTION_MESSAGE, null, new String[]{"Sí", "No"}, "Sí");
        }
    }

    public static void listarInmuebleGUI(Inmobiliaria inmobiliaria) {
        String tipoInmueble = (String) JOptionPane.showInputDialog(null, "¿Qué inmueble desea listar?",
                "Listar Inmueble", JOptionPane.QUESTION_MESSAGE, null,
                new String[]{"Casa", "Departamento", "Local", "Cochera"}, "Casa");

        String listado = "";
        try {
            if (!(tipoInmueble.equalsIgnoreCase("Casa") || tipoInmueble.equalsIgnoreCase("Departamento")
                    || tipoInmueble.equalsIgnoreCase("Local") || tipoInmueble.equalsIgnoreCase("Cochera"))) {
                throw new EleccionIncorrectaException("Ese tipo de inmueble no existe");
            } else if (tipoInmueble.equalsIgnoreCase("Casa") || tipoInmueble.equalsIgnoreCase("Departamento")) {
                listado = inmobiliaria.listarViviendad(tipoInmueble);
            } else if (tipoInmueble.equalsIgnoreCase("Local")) {
                listado = inmobiliaria.listarLocales();
            } else if (tipoInmueble.equalsIgnoreCase("Cochera")) {
                listado = inmobiliaria.listarCocheras();
            }
        } catch (EleccionIncorrectaException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JTextArea textArea = new JTextArea(listado);
        JScrollPane scrollPane = new JScrollPane(textArea);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        scrollPane.setPreferredSize(new Dimension(400, 300));
        JOptionPane.showMessageDialog(null, scrollPane, "Listado de Inmuebles", JOptionPane.PLAIN_MESSAGE);
    }
}
