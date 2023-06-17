package Controladores;

import Cliente.Contraseña;
import Cliente.Mail;
import Cliente.TiposMail;
import Cliente.Usuario;
import Empresa.Inmobiliaria;
import Excepciones.Contraseña.CantMayusException;
import Excepciones.Contraseña.CantNumException;
import Excepciones.Contraseña.MalContraseñaException;
import Excepciones.Contraseña.TotalDigitosException;
import Excepciones.ControladoraUsuario.DniInvalidoException;
import Excepciones.ControladoraUsuario.EdadInvalidadException;
import Excepciones.ControladoraUsuario.NombreYApellidoIncorrectoException;
import Excepciones.ControladoraUsuario.UsuarioNoEncontradoException;
import Excepciones.EleccionIncorrectaException;
import Excepciones.Mail.ArrobaException;
import Excepciones.Mail.PuntoComException;
import Lugares.*;
import Swing.MenuInicioGUI;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;
import java.util.Scanner;

public class ControladoraUsuario extends Component {
    Scanner teclado = new Scanner(System.in);

    /**
     * Esta es una funcion donde se gestiona el menu principal de usuario al inicio para loguear y registrarse
     *
     * @param inmobiliaria
     * @return retorna el usuario que se quiso registrar/logear
     */
    public Usuario menu(Inmobiliaria inmobiliaria) {
        Usuario usuario = new Usuario();
        //new MenuInicioGUI(inmobiliaria);
        String respuesta = "si";
        do {
            System.out.println("Buen día ¿Qué le gustaría realizar?");
            int opcion = 0;
            System.out.println("1. Loguearse \n2. Registrarse");
            opcion = teclado.nextInt();

            switch (opcion) {
                case 1:
                    while (respuesta.equalsIgnoreCase("si")) {
                        try {
                            usuario = login(inmobiliaria);
                            if (Usuario.comprobarAdmin(usuario)) {
                                do {
                                    System.out.println("¿Qué le gustaría realizar?");
                                    opcion = 0;
                                    System.out.println("1. Agregar inmueble \n2. Remover inmueble \n3. Modificar inmueble \n4. Listar inmueble \n5. Mostrar inmueble \n6. Dar de baja usuario  \n 7. Mostrar usuario");
                                    opcion = teclado.nextInt();
                                    String continuar = "si";
                                    switch (opcion) {
                                        case 1:
                                            agregarInmuebles(inmobiliaria);
                                            break;

                                        case 2:
                                            darDeBajaInmueble(inmobiliaria);
                                            break;

                                        case 3:
                                            modificarInmueble(inmobiliaria);
                                            break;

                                        case 4:

                                            listarInmueble(inmobiliaria);
                                            break;

                                        case 5:
                                            buscarInmueble(inmobiliaria);
                                            break;

                                        case 6:
                                            darDeBajaUsuario(inmobiliaria);
                                            break;

                                        case 7:
                                            mostrarUsuario(inmobiliaria);
                                            break;

                                        default:
                                            System.out.println("Valor ingresado no valido");
                                            break;
                                    }

                                } while (respuesta.equalsIgnoreCase("si"));
                            }
                            respuesta = "no";
                        } catch (UsuarioNoEncontradoException | MalContraseñaException e) {
                            System.err.println(e.getMessage());
                            System.out.println("¿Desea volver a intentar?");
                            respuesta = teclado.nextLine();
                        }
                    }

                    break;

                case 2:
                    while (respuesta.equalsIgnoreCase("si")) {
                        try {
                            usuario = registrarse();
                            respuesta = "no";
                        } catch (DniInvalidoException | NombreYApellidoIncorrectoException | EdadInvalidadException e) {
                            System.err.println(e.getMessage());
                        }
                    }
                    inmobiliaria.agregarUsuario(usuario);
                    break;

                default:
                    System.out.println("Valor ingresado no valido");
                    break;
            }
            System.out.println("Quiero volver al menu?, presione si");
            respuesta = teclado.nextLine();
        } while (respuesta.equals("si"));
        return usuario;
    }

    /**
     * Función propia de logueo donde se busca el usuario, hace comprobaciones de contraseña y si existe el usuario
     *
     * @param inmobiliaria
     * @return Retorna el usuarui buscado, en caso de que no lo encuentre retorna null
     * @throws UsuarioNoEncontradoException
     * @throws MalContraseñaException
     */
    public Usuario login(Inmobiliaria inmobiliaria) throws UsuarioNoEncontradoException, MalContraseñaException {
        System.out.println("Ingrese su nombre");
        teclado.nextLine();
        String nombre = teclado.nextLine();

        Usuario usuario = inmobiliaria.buscarUsuario(nombre);
        if (usuario == null) {
            throw new UsuarioNoEncontradoException("Usuario no encontrado");

        }
        System.out.println("Ingrese su contraseña");
        String contraseña = teclado.nextLine();
        if (!usuario.getContraseña().equals(contraseña)) {
            throw new MalContraseñaException("Contraseña incorrecta");
        }
        System.out.println(usuario);
        return usuario;
    }

    /*public Usuario login(Inmobiliaria inmobiliaria) throws UsuarioNoEncontradoException, MalContraseñaException {
        JFrame frame = new JFrame("Inicio de sesión");
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
            throw new RuntimeException("Inicio de sesión cancelado por el usuario");
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
    }*/


    /**
     * Creacion de usuario
     *
     * @return retorna el usuario que se registro
     */
    public Usuario registrarse() throws DniInvalidoException, NombreYApellidoIncorrectoException, EdadInvalidadException {
        String nombre = "";
        String contraseña = "";
        String dni = "";
        String mail = "";
        String auxMail = "";
        int edad = 0;
        boolean validacion = true;
        int tipoMail = 0;

        boolean nombreValido = false;
        while (!nombreValido) {
            System.out.println("Ingrese su nombre y apellido");
            teclado.nextLine();
            nombre = teclado.nextLine();
            if (nombre.matches("^[a-zA-Z\\s]+$")) { // Verificar que el nombre solo contenga letras y espacios
                nombreValido = true;
            } else {
                throw new NombreYApellidoIncorrectoException("Nombre inválido. No debe contener números.");
            }
        }
        Contraseña contra = null;

        while (validacion) {
            System.out.println("Ingrese la contraseña (Una mayúscula, un número y 8 digitos como mínumo)");
            contraseña = teclado.nextLine();
            try {
                Contraseña.verificacion(contraseña);
                contra = new Contraseña(contraseña); // Ver que conviene mejor
                validacion = false;
            } catch (TotalDigitosException e) {
                System.err.println(e.getMessage());//Preguntar al profe como hacemos para decirle la cantidad de algo
            } catch (CantNumException | CantMayusException e) {
                System.err.println(e.getMessage());
            }
        }

        boolean dniValido = false;
        while (!dniValido) {
            System.out.println("Ingrese su DNI");
            dni = teclado.nextLine();
            if (dni.matches("\\d{8}")) { // Verificar que el DNI tenga 8 dígitos
                dniValido = true;
            } else {
                throw new DniInvalidoException("DNI inválido. Debe tener 8 dígitos.");
            }
        }

        // Validar edad
        boolean edadValida = false;
        while (!edadValida) {
            System.out.println("Ingrese su edad");//no escriba letras, no numeros negativos ni mas de 3 letras
            edad = Integer.parseInt(teclado.nextLine());
            if (edad >= 0 && edad <= 120) { // Verificar que la edad esté en un rango razonable
                edadValida = true;
            } else {
                throw new EdadInvalidadException("Edad inválida. Debe estar entre 0 y 120 o ingreso una letra.");
            }
        }

        while (Objects.equals(auxMail, "")) {
            System.out.println("Ingrese que tipo de mail usa.\n1.Gmail\n2.Hotmail\n3.Yahoo\n4.Otros");
            tipoMail = teclado.nextInt();
            auxMail = menuTipoMail(tipoMail);
        }
        if (tipoMail == 4) {
            mail = auxMail;

        } else if (tipoMail == 1 || tipoMail == 2 || tipoMail == 3) {
            System.out.println("Ingrese la parte delantera del mail (Antes de @)");
            teclado.nextLine();
            String aux = teclado.nextLine();
            mail = aux.concat(auxMail);
        }
        Mail correo = new Mail(mail);

        Usuario usuario = new Usuario(nombre, contra, dni, correo, edad, true);
        System.out.println("termine y usuario: " + usuario);

        return usuario;
    }

    /*public Usuario registrarse() throws DniInvalidoException, NombreYApellidoIncorrectoException, EdadInvalidadException, PuntoComException, ArrobaException {
        JFrame frame = new JFrame("Registro");
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

        int opcion = JOptionPane.showConfirmDialog(null, panel, "Ingrese su información de registro", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (opcion == JOptionPane.CANCEL_OPTION || opcion == JOptionPane.CLOSED_OPTION) {
            throw new RuntimeException("Registro cancelado por el usuario");
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

        String dni = dniField.getText();
        if (!dni.matches("\\d{8}")) { // Verificar que el DNI tenga 8 dígitos
            JOptionPane.showMessageDialog(null, "DNI inválido. Debe tener 8 dígitos.", "Error", JOptionPane.ERROR_MESSAGE);
        }

        int edad = Integer.parseInt(edadField.getText());
        if (edad < 0 || edad > 120) { // Verificar que la edad esté en un rango razonable
            JOptionPane.showMessageDialog(null, "Edad inválida. Debe estar entre 0 y 120.", "Error", JOptionPane.ERROR_MESSAGE);
        }

        String mail = "";
        int tipoMail = tipoMailCombo.getSelectedIndex() + 1;
        switch (tipoMail) {
            case 1:
                mail = TiposMail.Gmail.getTipomail();
                break;
            case 2:
                mail = TiposMail.Hotmail.getTipomail();
                break;
            case 3:
                mail = TiposMail.Yahoo.getTipomail();
                break;
            case 4:
                String parteDelanteraMail = parteDelanteraMailField.getText();
                while (!Mail.validarMail(parteDelanteraMail + "@" + mail)) {
                    JOptionPane.showMessageDialog(null, "Mail ingresado inválido. Inténtelo de nuevo.", "Error", JOptionPane.ERROR_MESSAGE);
                    parteDelanteraMail = JOptionPane.showInputDialog(null, "Ingrese la parte delantera del mail (antes de @):", "Registro", JOptionPane.PLAIN_MESSAGE);
                    if (parteDelanteraMail == null) {
                        JOptionPane.showMessageDialog(null, "Registro cancelado por el usuario", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
                mail = parteDelanteraMail + "@" + menuTipoMail(tipoMail);
                break;
            default:
                JOptionPane.showMessageDialog(null, "La opcion ingresada es invalida", "Error", JOptionPane.ERROR_MESSAGE);
        }

        Mail correo = new Mail(mail);

        Usuario usuario = new Usuario(nombre, contra, dni, correo, edad);
        return usuario;
    }*/

    /**
     * Función el cual te crea un mail de distintos tipos.
     *
     * @param eleccion
     * @return Retorna un String en formato de mail
     */
    public String menuTipoMail(int eleccion) {
        String mail = "";
        boolean valido = false;
        switch (eleccion) {
            case 1:
                mail = TiposMail.Gmail.getTipomail();
                break;

            case 2:
                mail = TiposMail.Hotmail.getTipomail();
                break;

            case 3:
                mail = TiposMail.Yahoo.getTipomail();
                break;

            case 4:
                while (valido == false) {
                    System.out.println("Ingrese el mail completo por favor.");
                    teclado.nextLine();
                    mail = teclado.nextLine();
                    try {
                        valido = Mail.validarMail(mail);
                    } catch (ArrobaException | PuntoComException e) {
                        System.err.println(e.getMessage());
                    }
                    if (valido == false) {
                        System.err.println("EL mail ingresado es incorrecto");
                    }
                }
                break;
            default:
                System.out.println("La opcion ingresada es invalida");
        }
        return mail;
    }

    /*public String menuTipoMail(int eleccion) {
        JFrame frame = new JFrame("Tipo de mail");
        JPanel panel = new JPanel(new GridLayout(2, 1));

        JLabel mensajeLabel = new JLabel("Ingrese su mail completo:");
        JTextField mensajeField = new JTextField(20);
        panel.add(mensajeLabel);
        panel.add(mensajeField);

        int opcion = JOptionPane.showConfirmDialog(null, panel, "Ingrese su información de registro", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (opcion == JOptionPane.CANCEL_OPTION || opcion == JOptionPane.CLOSED_OPTION) {
            JOptionPane.showMessageDialog(null, "Registro cancelado por el usuario", "Error", JOptionPane.ERROR_MESSAGE);
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
    }*/

    public void mostrarUsuario(Inmobiliaria inmobiliaria) {
        String continuar = "";
        do {
            System.out.println("Mail del usuario: ");
            String mail = teclado.nextLine();
            System.out.println(inmobiliaria.buscarUsuario(mail).toString());

            System.out.println("Desea mostrar otro usuario? (Si/No)");
            continuar = teclado.nextLine();
        } while (continuar.equalsIgnoreCase("si"));

    }

    public void darDeBajaUsuario(Inmobiliaria inmobiliaria) {
        String continuar = "no";
        do {
            System.out.println("Que usuario desea dar de baja? (Mail del Usuario)");
            String mailUsuario = teclado.nextLine();
            if (!inmobiliaria.darBaja(mailUsuario)) {
                //excepcion
            }
            System.out.println("Desea dar de baja otro usuario?");
            continuar = teclado.nextLine();
        } while (continuar.equalsIgnoreCase("si"));
    }

    public void darDeBajaInmueble(Inmobiliaria inmobiliaria) {
        String continuar = "no";
        do {
            System.out.println("Que tipo de inmueble desea dar de baja? (Casa/Departamento/Local/Cochera)");
            String tipoInmueble = teclado.nextLine();
            if (!(tipoInmueble.equalsIgnoreCase("Casa") && tipoInmueble.equalsIgnoreCase("Departamento") && tipoInmueble.equalsIgnoreCase("Local") && tipoInmueble.equalsIgnoreCase("Cochera"))) {
                continuar = "si";
                System.out.println("Opcion ingresada es incorrecta");
            } else if (tipoInmueble.equalsIgnoreCase("Casa")) {
                System.out.println("Ingrese la direccion del inmueble: ");
                String direccion = teclado.nextLine();
                Casa casa = inmobiliaria.buscarCasa(direccion);
                if (casa != null) {
                    inmobiliaria.baja(casa);
                } else {
                    //Excepcion
                }
            } else if (tipoInmueble.equalsIgnoreCase("Departamento")) {
                System.out.println("Ingrese la direccion del inmueble: ");
                String direccion = teclado.nextLine();
                Departamento departamento = inmobiliaria.buscarDepartamento(direccion);
                if (departamento != null) {
                    inmobiliaria.baja(departamento);
                } else {
                    //Excepcion
                }
            } else if (tipoInmueble.equalsIgnoreCase("Local")) {
                System.out.println("Ingrese la direccion del inmueble: ");
                String direccion = teclado.nextLine();
                Local local = inmobiliaria.buscarLocal(direccion);
                if (local != null) {
                    inmobiliaria.baja(local);
                } else {
                    //Excepcion
                }
            } else if (tipoInmueble.equalsIgnoreCase("Cochera")) {
                System.out.println("Ingrese la direccion del inmueble: ");
                String direccion = teclado.nextLine();
                Cochera cochera = inmobiliaria.buscarCochera(direccion);
                if (cochera != null) {
                    inmobiliaria.baja(cochera);
                } else {
                    //Excepcion
                }
            }
            System.out.println("Desea dar de baja otro inmueble?");
            continuar = teclado.nextLine();
        } while (continuar.equalsIgnoreCase("si"));
    }

    public void modificarInmueble(Inmobiliaria inmobiliaria) {
        System.out.println("Que inmueble desea modificar? (Casa/Departamento/Local/Cochera)");
        String tipoInmueble = teclado.nextLine();
        String continuar = "no";
        do {
            if (!(tipoInmueble.equalsIgnoreCase("Casa") && tipoInmueble.equalsIgnoreCase("Departamento") && tipoInmueble.equalsIgnoreCase("Local") && tipoInmueble.equalsIgnoreCase("Cochera"))) {
                continuar = "si";
                System.out.println("Opcion ingresada es incorrecta");
            } else if (tipoInmueble.equalsIgnoreCase("Casa")) {
                try {
                    modificarCasa(inmobiliaria);
                } catch (EleccionIncorrectaException e) {
                    //Comportamiento
                }
            } else if (tipoInmueble.equalsIgnoreCase("Departamento")) {
                try {
                    modificarDepartamento(inmobiliaria);
                } catch (EleccionIncorrectaException e) {
                    //Comportamiento
                }

            } else if (tipoInmueble.equalsIgnoreCase("Local")) {
                try {
                    modificarLocal(inmobiliaria);
                } catch (EleccionIncorrectaException e) {
                    //Comportamiento
                }

            } else if (tipoInmueble.equalsIgnoreCase("Cochera")) {
                try {
                    modificarCochera(inmobiliaria);
                } catch (EleccionIncorrectaException e) {
                    //Comportamiento
                }
            }
        } while (continuar.equalsIgnoreCase("si"));
    }

    public void modificarCasa(Inmobiliaria inmobiliaria) throws EleccionIncorrectaException {
        System.out.println("Ingrese la direccion del inmueble: ");
        String direccion = teclado.nextLine();
        Casa casaEncontrada = inmobiliaria.buscarCasa(direccion);
        Casa casa = null;
        String continuar = "";
        do {
            if (casaEncontrada != null) {
                System.out.println("Que atributo desea modificar? (1-estado, 2-ambientes, 3-cantBaños, 4-metrosCuadrados, 5-amueblado, 6-cochera, 7-precio, 8-patio, 9-pisos)");
                int opcion = teclado.nextInt();
                switch (opcion) {
                    case 1:
                        Estado estado = null;
                        do {
                            System.out.println("Desea alquilar o vender? (1 -Alquiler/ 2 -Venta)");
                            opcion = teclado.nextInt();
                            if (opcion == 1) {
                                estado = Estado.EnAlquiler;
                            } else if (opcion == 2) {
                                estado = Estado.EnVenta;
                            } else {
                                System.out.println("Opcion invalida");
                            }
                        } while (opcion != 1 && opcion != 2);

                        casa = new Casa(estado, casaEncontrada.getDireccion(), casaEncontrada.getAmbientes(), casaEncontrada.getCantBanios(), casaEncontrada.getMetrosCuadrados(), casaEncontrada.isAmueblado(), casaEncontrada.isCochera(), casaEncontrada.getPrecio(), casaEncontrada.isPatio(), casaEncontrada.getPisos());
                        inmobiliaria.modificar(casa);
                        break;

                    case 2:
                        System.out.println("Cantidad de ambientes?");
                        short ambientes = teclado.nextShort();

                        casa = new Casa(casaEncontrada.getEstado(), casaEncontrada.getDireccion(), ambientes, casaEncontrada.getCantBanios(), casaEncontrada.getMetrosCuadrados(), casaEncontrada.isAmueblado(), casaEncontrada.isCochera(), casaEncontrada.getPrecio(), casaEncontrada.isPatio(), casaEncontrada.getPisos());
                        inmobiliaria.modificar(casa);
                        break;

                    case 3:
                        System.out.println("Cantidad de baños?");
                        short cantBaños = teclado.nextShort();

                        casa = new Casa(casaEncontrada.getEstado(), casaEncontrada.getDireccion(), casaEncontrada.getAmbientes(), cantBaños, casaEncontrada.getMetrosCuadrados(), casaEncontrada.isAmueblado(), casaEncontrada.isCochera(), casaEncontrada.getPrecio(), casaEncontrada.isPatio(), casaEncontrada.getPisos());
                        inmobiliaria.modificar(casa);
                        break;

                    case 4:
                        System.out.println("Metros cuadrados?");
                        int metrosCuadrados = teclado.nextInt();

                        casa = new Casa(casaEncontrada.getEstado(), casaEncontrada.getDireccion(), casaEncontrada.getAmbientes(), casaEncontrada.getCantBanios(), metrosCuadrados, casaEncontrada.isAmueblado(), casaEncontrada.isCochera(), casaEncontrada.getPrecio(), casaEncontrada.isPatio(), casaEncontrada.getPisos());
                        inmobiliaria.modificar(casa);
                        break;

                    case 5:
                        boolean amueblado = false;
                        do {
                            System.out.println("Esta amueblado? (1 -Si/ 2 -No)");
                            opcion = teclado.nextInt();
                            if (opcion == 1) {
                                amueblado = true;
                            } else if (opcion == 2) {
                                amueblado = false;
                            } else {
                                System.out.println("Opcion invalida");
                            }
                        } while (opcion != 1 && opcion != 2);

                        casa = new Casa(casaEncontrada.getEstado(), casaEncontrada.getDireccion(), casaEncontrada.getAmbientes(), casaEncontrada.getCantBanios(), casaEncontrada.getMetrosCuadrados(), amueblado, casaEncontrada.isCochera(), casaEncontrada.getPrecio(), casaEncontrada.isPatio(), casaEncontrada.getPisos());
                        inmobiliaria.modificar(casa);
                        break;

                    case 6:
                        boolean cochera = false;
                        do {
                            System.out.println("Tiene cochera? (1 -Si/ 2 -No)");
                            opcion = teclado.nextInt();
                            if (opcion == 1) {
                                cochera = true;
                            } else if (opcion == 2) {
                                cochera = false;
                            } else {
                                System.out.println("Opcion invalida");
                            }
                        } while (opcion != 1 && opcion != 2);

                        casa = new Casa(casaEncontrada.getEstado(), casaEncontrada.getDireccion(), casaEncontrada.getAmbientes(), casaEncontrada.getCantBanios(), casaEncontrada.getMetrosCuadrados(), casaEncontrada.isAmueblado(), cochera, casaEncontrada.getPrecio(), casaEncontrada.isPatio(), casaEncontrada.getPisos());
                        inmobiliaria.modificar(casa);
                        break;

                    case 7:
                        System.out.println("Precio del inmueble?");
                        double precio = teclado.nextDouble();

                        casa = new Casa(casaEncontrada.getEstado(), casaEncontrada.getDireccion(), casaEncontrada.getAmbientes(), casaEncontrada.getCantBanios(), casaEncontrada.getMetrosCuadrados(), casaEncontrada.isAmueblado(), casaEncontrada.isCochera(), precio, casaEncontrada.isPatio(), casaEncontrada.getPisos());
                        inmobiliaria.modificar(casa);
                        break;

                    case 8:
                        boolean patio = false;
                        do {
                            System.out.println("Tiene patio? (1 -Si/ 2 -No)");
                            opcion = teclado.nextInt();
                            if (opcion == 1) {
                                patio = true;
                            } else if (opcion == 2) {
                                patio = false;
                            } else {
                                System.out.println("Opcion invalida");
                            }
                        } while (opcion != 1 && opcion != 2);

                        casa = new Casa(casaEncontrada.getEstado(), casaEncontrada.getDireccion(), casaEncontrada.getAmbientes(), casaEncontrada.getCantBanios(), casaEncontrada.getMetrosCuadrados(), casaEncontrada.isAmueblado(), casaEncontrada.isCochera(), casaEncontrada.getPrecio(), patio, casaEncontrada.getPisos());
                        inmobiliaria.modificar(casa);
                        break;

                    case 9:
                        System.out.println("Cantidad de pisos?");
                        short pisos = teclado.nextShort();

                        casa = new Casa(casaEncontrada.getEstado(), casaEncontrada.getDireccion(), casaEncontrada.getAmbientes(), casaEncontrada.getCantBanios(), casaEncontrada.getMetrosCuadrados(), casaEncontrada.isAmueblado(), casaEncontrada.isCochera(), casaEncontrada.getPrecio(), casaEncontrada.isPatio(), pisos);
                        inmobiliaria.modificar(casa);
                        break;

                    default:
                        throw new EleccionIncorrectaException("Opcion incorrecta");
                }
            }
            System.out.println("Desea modificar otro dato? (Si/No)");
            continuar = teclado.nextLine();
        } while (continuar.equalsIgnoreCase("si"));
    }

    public void modificarDepartamento(Inmobiliaria inmobiliaria) throws EleccionIncorrectaException {
        System.out.println("Ingrese la direccion del inmueble: ");
        String direccion = teclado.nextLine();
        Departamento departamentoEncontrado = inmobiliaria.buscarDepartamento(direccion);
        Departamento departamento = null;
        String continuar = "";
        do {
            if (departamentoEncontrado != null) {
                System.out.println("Que atributo desea modificar? (1-estado, 2-ambientes, 3-cantBaños, 4-metrosCuadrados, 5-amueblado, 6-cochera, 7-precio, 8-nroPiso, 9-disposicion)");
                int opcion = teclado.nextInt();
                switch (opcion) {
                    case 1:
                        Estado estado = null;
                        do {
                            System.out.println("Desea alquilar o vender? (1 -Alquiler/ 2 -Venta)");
                            opcion = teclado.nextInt();
                            if (opcion == 1) {
                                estado = Estado.EnAlquiler;
                            } else if (opcion == 2) {
                                estado = Estado.EnVenta;
                            } else {
                                System.out.println("Opcion invalida");
                            }
                        } while (opcion != 1 && opcion != 2);

                        departamento = new Departamento(estado, departamentoEncontrado.getDireccion(), departamentoEncontrado.getAmbientes(), departamentoEncontrado.getCantBanios(), departamentoEncontrado.getMetrosCuadrados(), departamentoEncontrado.isAmueblado(), departamentoEncontrado.isCochera(), departamentoEncontrado.getPrecio(), departamentoEncontrado.getNroPiso(), departamentoEncontrado.getDisposicion());
                        inmobiliaria.modificar(departamento);
                        break;

                    case 2:
                        System.out.println("Cantidad de ambientes?");
                        short ambientes = teclado.nextShort();

                        departamento = new Departamento(departamentoEncontrado.getEstado(), departamentoEncontrado.getDireccion(), ambientes, departamentoEncontrado.getCantBanios(), departamentoEncontrado.getMetrosCuadrados(), departamentoEncontrado.isAmueblado(), departamentoEncontrado.isCochera(), departamentoEncontrado.getPrecio(), departamentoEncontrado.getNroPiso(), departamentoEncontrado.getDisposicion());
                        inmobiliaria.modificar(departamento);
                        break;

                    case 3:
                        System.out.println("Cantidad de baños?");
                        short cantBaños = teclado.nextShort();

                        departamento = new Departamento(departamentoEncontrado.getEstado(), departamentoEncontrado.getDireccion(), departamentoEncontrado.getAmbientes(), cantBaños, departamentoEncontrado.getMetrosCuadrados(), departamentoEncontrado.isAmueblado(), departamentoEncontrado.isCochera(), departamentoEncontrado.getPrecio(), departamentoEncontrado.getNroPiso(), departamentoEncontrado.getDisposicion());
                        inmobiliaria.modificar(departamento);
                        break;

                    case 4:
                        System.out.println("Metros cuadrados?");
                        int metrosCuadrados = teclado.nextInt();

                        departamento = new Departamento(departamentoEncontrado.getEstado(), departamentoEncontrado.getDireccion(), departamentoEncontrado.getAmbientes(), departamentoEncontrado.getCantBanios(), metrosCuadrados, departamentoEncontrado.isAmueblado(), departamentoEncontrado.isCochera(), departamentoEncontrado.getPrecio(), departamentoEncontrado.getNroPiso(), departamentoEncontrado.getDisposicion());
                        inmobiliaria.modificar(departamento);
                        break;

                    case 5:
                        boolean amueblado = false;
                        do {
                            System.out.println("Esta amueblado? (1 -Si/ 2 -No)");
                            opcion = teclado.nextInt();
                            if (opcion == 1) {
                                amueblado = true;
                            } else if (opcion == 2) {
                                amueblado = false;
                            } else {
                                System.out.println("Opcion invalida");
                            }
                        } while (opcion != 1 && opcion != 2);

                        departamento = new Departamento(departamentoEncontrado.getEstado(), departamentoEncontrado.getDireccion(), departamentoEncontrado.getAmbientes(), departamentoEncontrado.getCantBanios(), departamentoEncontrado.getMetrosCuadrados(), amueblado, departamentoEncontrado.isCochera(), departamentoEncontrado.getPrecio(), departamentoEncontrado.getNroPiso(), departamentoEncontrado.getDisposicion());
                        inmobiliaria.modificar(departamento);
                        break;

                    case 6:
                        boolean cochera = false;
                        do {
                            System.out.println("Tiene cochera? (1 -Si/ 2 -No)");
                            opcion = teclado.nextInt();
                            if (opcion == 1) {
                                cochera = true;
                            } else if (opcion == 2) {
                                cochera = false;
                            } else {
                                System.out.println("Opcion invalida");
                            }
                        } while (opcion != 1 && opcion != 2);

                        departamento = new Departamento(departamentoEncontrado.getEstado(), departamentoEncontrado.getDireccion(), departamentoEncontrado.getAmbientes(), departamentoEncontrado.getCantBanios(), departamentoEncontrado.getMetrosCuadrados(), departamentoEncontrado.isAmueblado(), cochera, departamentoEncontrado.getPrecio(), departamentoEncontrado.getNroPiso(), departamentoEncontrado.getDisposicion());
                        inmobiliaria.modificar(departamento);
                        break;

                    case 7:
                        System.out.println("Precio del inmueble?");
                        double precio = teclado.nextDouble();

                        departamento = new Departamento(departamentoEncontrado.getEstado(), departamentoEncontrado.getDireccion(), departamentoEncontrado.getAmbientes(), departamentoEncontrado.getCantBanios(), departamentoEncontrado.getMetrosCuadrados(), departamentoEncontrado.isAmueblado(), departamentoEncontrado.isCochera(), precio, departamentoEncontrado.getNroPiso(), departamentoEncontrado.getDisposicion());
                        inmobiliaria.modificar(departamento);
                        break;

                    case 8:
                        System.out.println("Numero del piso?");
                        String nroPiso = teclado.nextLine();

                        departamento = new Departamento(departamentoEncontrado.getEstado(), departamentoEncontrado.getDireccion(), departamentoEncontrado.getAmbientes(), departamentoEncontrado.getCantBanios(), departamentoEncontrado.getMetrosCuadrados(), departamentoEncontrado.isAmueblado(), departamentoEncontrado.isCochera(), departamentoEncontrado.getPrecio(), nroPiso, departamentoEncontrado.getDisposicion());
                        inmobiliaria.modificar(departamento);
                        break;

                    case 9:
                        String disposicion = "";
                        do {
                            System.out.println("Cual es la disposicion? (1 -A la calle/ 2 - Interno)");
                            opcion = teclado.nextInt();
                            if (opcion == 1) {
                                disposicion = "Calle";
                            } else if (opcion == 2) {
                                disposicion = "Interno";
                            } else {
                                System.out.println("Opcion invalida");
                            }
                        } while (opcion != 1 && opcion != 2);

                        departamento = new Departamento(departamentoEncontrado.getEstado(), departamentoEncontrado.getDireccion(), departamentoEncontrado.getAmbientes(), departamentoEncontrado.getCantBanios(), departamentoEncontrado.getMetrosCuadrados(), departamentoEncontrado.isAmueblado(), departamentoEncontrado.isCochera(), departamentoEncontrado.getPrecio(), departamentoEncontrado.getNroPiso(), disposicion);
                        inmobiliaria.modificar(departamento);
                        break;

                    default:
                        throw new EleccionIncorrectaException("Opcion incorrecta");
                }
            }
            System.out.println("Desea modificar otro dato? (Si/No)");
            continuar = teclado.nextLine();
        } while (continuar.equalsIgnoreCase("si"));
    }

    public void modificarCochera(Inmobiliaria inmobiliaria) throws EleccionIncorrectaException {
        System.out.println("Ingrese la direccion del inmueble: ");
        String direccion = teclado.nextLine();
        Cochera cocheraEncontrada = inmobiliaria.buscarCochera(direccion);
        Cochera cochera = null;
        String continuar = "";
        do {
            if (cocheraEncontrada != null) {
                System.out.println("Que atributo desea modificar? (1-estado, 2-piso, 3-posicion, 4-medio de acceso,5-precio)");
                int opcion = teclado.nextInt();
                switch (opcion) {

                    case 1:
                        Estado estado = null;
                        do {
                            System.out.println("Desea alquilar o vender? (1 -Alquiler/ 2 -Venta)");
                            opcion = teclado.nextInt();
                            if (opcion == 1) {
                                estado = Estado.EnAlquiler;
                            } else if (opcion == 2) {
                                estado = Estado.EnVenta;
                            } else {
                                System.out.println("Opcion invalida");
                            }
                        } while (opcion != 1 && opcion != 2);

                        cochera = new Cochera(cocheraEncontrada.getDireccion(), estado, cocheraEncontrada.getPiso(), cocheraEncontrada.getPosicion(), cocheraEncontrada.getMedioDeAcceso(), cocheraEncontrada.getPrecio());
                        inmobiliaria.modificar(cochera);

                        break;
                    case 2:
                        System.out.println("Numero de piso?");
                        short piso = teclado.nextShort();

                        cochera = new Cochera(cocheraEncontrada.getDireccion(), cocheraEncontrada.getEstado(), piso, cocheraEncontrada.getPosicion(), cocheraEncontrada.getMedioDeAcceso(), cocheraEncontrada.getPrecio());
                        inmobiliaria.modificar(cochera);

                        break;
                    case 3:
                        System.out.println("Posicion en el piso?");
                        short posicion = teclado.nextShort();

                        cochera = new Cochera(cocheraEncontrada.getDireccion(), cocheraEncontrada.getEstado(), cocheraEncontrada.getPiso(), posicion, cocheraEncontrada.getMedioDeAcceso(), cocheraEncontrada.getPrecio());
                        inmobiliaria.modificar(cochera);

                        break;
                    case 4:
                        String medioDeAcceso = "";
                        do {
                            System.out.println("Medio de acceso (1 -Ascensor/ 2 -Rampa)");
                            opcion = teclado.nextInt();
                            if (opcion == 1) {
                                medioDeAcceso = "Ascensor";
                            } else if (opcion == 2) {
                                medioDeAcceso = "Rampa";
                            } else {
                                System.out.println("Opcion invalida");
                            }
                        } while (opcion != 1 && opcion != 2);

                        cochera = new Cochera(cocheraEncontrada.getDireccion(), cocheraEncontrada.getEstado(), cocheraEncontrada.getPiso(), cocheraEncontrada.getPosicion(), medioDeAcceso, cocheraEncontrada.getPrecio());
                        inmobiliaria.modificar(cochera);

                        break;
                    case 5:
                        System.out.println("Precio del inmueble?");
                        double precio = teclado.nextDouble();

                        cochera = new Cochera(cocheraEncontrada.getDireccion(), cocheraEncontrada.getEstado(), cocheraEncontrada.getPiso(), cocheraEncontrada.getPosicion(), cocheraEncontrada.getMedioDeAcceso(), precio);
                        inmobiliaria.modificar(cochera);

                        break;
                    default:
                        throw new EleccionIncorrectaException("Opcion incorrecta");
                }
            }
            System.out.println("Desea modificar otro dato? (Si/No)");
            continuar = teclado.nextLine();
        } while (continuar.equalsIgnoreCase("si"));
    }

    public void modificarLocal(Inmobiliaria inmobiliaria) throws EleccionIncorrectaException {
        System.out.println("Ingrese la direccion del inmueble: ");
        String direccion = teclado.nextLine();
        Local localEncontrado = inmobiliaria.buscarLocal(direccion);
        Local local = null;
        String continuar = "";
        do {
            if (localEncontrado != null) {
                System.out.println("Que atributo desea modificar? (1-estado, 2-piso, 3-posicion, 4-medio de acceso,5-precio)");
                int opcion = teclado.nextInt();
                switch (opcion) {
                    case 1:
                        Estado estado = null;
                        do {
                            System.out.println("Desea alquilar o vender? (1 -Alquiler/ 2 -Venta)");
                            opcion = teclado.nextInt();
                            if (opcion == 1) {
                                estado = Estado.EnAlquiler;
                            } else if (opcion == 2) {
                                estado = Estado.EnVenta;
                            } else {
                                System.out.println("Opcion invalida");
                            }
                        } while (opcion != 1 && opcion != 2);

                        local = new Local(localEncontrado.getDireccion(), estado, localEncontrado.getAmbientes(), localEncontrado.isVidriera(), localEncontrado.getPrecio());
                        inmobiliaria.modificar(local);

                        break;
                    case 2:
                        System.out.println("Cantidad de ambientes?");
                        short ambientes = teclado.nextShort();

                        local = new Local(localEncontrado.getDireccion(), localEncontrado.getEstado(), ambientes, localEncontrado.isVidriera(), localEncontrado.getPrecio());
                        inmobiliaria.modificar(local);

                        break;
                    case 3:

                        boolean vidrieras = false;
                        do {
                            System.out.println("Tiene vidrieras? (1 -Si/ 2 -No)");
                            opcion = teclado.nextInt();
                            if (opcion == 1) {
                                vidrieras = true;
                            } else if (opcion == 2) {
                                vidrieras = false;
                            } else {
                                System.out.println("Opcion invalida");
                            }
                        } while (opcion != 1 && opcion != 2);

                        local = new Local(localEncontrado.getDireccion(), localEncontrado.getEstado(), localEncontrado.getAmbientes(), vidrieras, localEncontrado.getPrecio());
                        inmobiliaria.modificar(local);

                        break;
                    case 4:

                        System.out.println("Precio del inmueble?");
                        double precio = teclado.nextDouble();

                        local = new Local(localEncontrado.getDireccion(), localEncontrado.getEstado(), localEncontrado.getAmbientes(), localEncontrado.isVidriera(), precio);
                        inmobiliaria.modificar(local);

                        break;
                    default:
                        throw new EleccionIncorrectaException("Opcion incorrecta");
                }
            }
            System.out.println("Desea modificar otro dato? (Si/No)");
            continuar = teclado.nextLine();
        } while (continuar.equalsIgnoreCase("si"));
    }

    public void listarInmueble(Inmobiliaria inmobiliaria) {
        System.out.println("Que inmueble desea listar? (Casa/Departamento/Local/Cochera)");
        String tipoInmueble = teclado.nextLine();
        if (!(tipoInmueble.equalsIgnoreCase("Casa") && tipoInmueble.equalsIgnoreCase("Departamento") && tipoInmueble.equalsIgnoreCase("Local") && tipoInmueble.equalsIgnoreCase("Cochera"))) {
            //Excepcion
        } else if (tipoInmueble.equalsIgnoreCase("Casa")) {
            inmobiliaria.listarViviendad(tipoInmueble);
        } else if (tipoInmueble.equalsIgnoreCase("Departamento")) {
            inmobiliaria.listarViviendad(tipoInmueble);
        } else if (tipoInmueble.equalsIgnoreCase("Local")) {
            inmobiliaria.listarLocales();
        } else if (tipoInmueble.equalsIgnoreCase("Cochera")) {
            inmobiliaria.listarCocheras();
        }
    }

    public void buscarInmueble(Inmobiliaria inmobiliaria) {
        String continuar = "no";
        do {
            System.out.println("Que tipo de inmueble desea buscar? (Casa/Departamento/Local/Cochera)");
            String tipoInmueble = teclado.nextLine();
            if (!(tipoInmueble.equalsIgnoreCase("Casa") && tipoInmueble.equalsIgnoreCase("Departamento") && tipoInmueble.equalsIgnoreCase("Local") && tipoInmueble.equalsIgnoreCase("Cochera"))) {
                continuar = "si";
                System.out.println("Opcion ingresada es incorrecta");
            } else if (tipoInmueble.equalsIgnoreCase("Casa")) {
                System.out.println("Ingrese la direccion del inmueble: ");
                String direccion = teclado.nextLine();
                Casa casa = inmobiliaria.buscarCasa(direccion);
                if (casa != null) {
                    System.out.println(casa.toString());
                } else {
                    //Excepcion
                }
            } else if (tipoInmueble.equalsIgnoreCase("Departamento")) {
                System.out.println("Ingrese la direccion del inmueble: ");
                String direccion = teclado.nextLine();
                Departamento departamento = inmobiliaria.buscarDepartamento(direccion);
                if (departamento != null) {
                    System.out.println(departamento.toString());
                } else {
                    //Excepcion
                }
            } else if (tipoInmueble.equalsIgnoreCase("Local")) {
                System.out.println("Ingrese la direccion del inmueble: ");
                String direccion = teclado.nextLine();
                Local local = inmobiliaria.buscarLocal(direccion);
                if (local != null) {
                    System.out.println(local.toString());
                } else {
                    //Excepcion
                }
            } else if (tipoInmueble.equalsIgnoreCase("Cochera")) {
                System.out.println("Ingrese la direccion del inmueble: ");
                String direccion = teclado.nextLine();
                Cochera cochera = inmobiliaria.buscarCochera(direccion);
                if (cochera != null) {
                    System.out.println(cochera.toString());
                } else {
                    //Excepcion
                }
            }
            System.out.println("Desea mostrar otro inmueble?");
            continuar = teclado.nextLine();
        } while (continuar.equalsIgnoreCase("si"));
    }

    public void agregarInmuebles(Inmobiliaria inmobiliaria) {
        String continuar = "si";
        int opcion = 0;
        do {
            System.out.println("Que tipo de inmueble desea agregar? (Casa/Departamento/Local/Cochera)");
            String tipoInmueble = teclado.nextLine();
            if (!(tipoInmueble.equalsIgnoreCase("Casa") && tipoInmueble.equalsIgnoreCase("Departamento") && tipoInmueble.equalsIgnoreCase("Local") && tipoInmueble.equalsIgnoreCase("Cochera"))) {
                continuar = "no";
                System.out.println("Opcion ingresada es incorrecta");
            } else if (tipoInmueble.equalsIgnoreCase("Casa")) {
                agregarCasa(inmobiliaria);
            } else if (tipoInmueble.equalsIgnoreCase("Departamento")) {
                agregarDepartamento(inmobiliaria);
            } else if (tipoInmueble.equalsIgnoreCase("Local")) {
                agregarLocal(inmobiliaria);
            } else if (tipoInmueble.equalsIgnoreCase("Cochera")) {
                agregarCochera(inmobiliaria);
            }

            System.out.println("Desea agregar otro inmueble? (1 - Si/ 2 - No)");
            continuar = teclado.nextLine();
        } while (continuar.equalsIgnoreCase("si"));
    }

    public void agregarCasa(Inmobiliaria inmobiliaria) {
        int opcion = 0;
        System.out.println("Direccion de la casa: ");
        String direccion = teclado.nextLine();

        System.out.println("Cantidad de ambientes?");
        short ambientes = teclado.nextShort();

        System.out.println("Cantidad de baños?");
        short cantBaños = teclado.nextShort();

        System.out.println("Metros cuadrados?");
        int metrosCuadrados = teclado.nextInt();

        boolean amueblado = false;
        do {
            System.out.println("Esta amueblado? (1 -Si/ 2 -No)");
            opcion = teclado.nextInt();
            if (opcion == 1) {
                amueblado = true;
            } else if (opcion == 2) {
                amueblado = false;
            } else {
                System.out.println("Opcion invalida");
            }
        } while (opcion != 1 && opcion != 2);

        boolean cochera = false;
        do {
            System.out.println("Tiene cochera? (1 -Si/ 2 -No)");
            opcion = teclado.nextInt();
            if (opcion == 1) {
                cochera = true;
            } else if (opcion == 2) {
                cochera = false;
            } else {
                System.out.println("Opcion invalida");
            }
        } while (opcion != 1 && opcion != 2);

        System.out.println("Precio del inmueble?");
        double precio = teclado.nextDouble();

        boolean patio = false;
        do {
            System.out.println("Tiene patio? (1 -Si/ 2 -No)");
            opcion = teclado.nextInt();
            if (opcion == 1) {
                patio = true;
            } else if (opcion == 2) {
                patio = false;
            } else {
                System.out.println("Opcion invalida");
            }
        } while (opcion != 1 && opcion != 2);

        System.out.println("Cantidad de pisos?");
        short pisos = teclado.nextShort();

        Estado estado = null;
        do {
            System.out.println("Desea alquilar o vender? (1 -Alquiler/ 2 -Venta)");
            opcion = teclado.nextInt();
            if (opcion == 1) {
                estado = Estado.EnAlquiler;
            } else if (opcion == 2) {
                estado = Estado.EnVenta;
            } else {
                System.out.println("Opcion invalida");
            }
        } while (opcion != 1 && opcion != 2);

        Casa casa = new Casa(estado, direccion, ambientes, cantBaños, metrosCuadrados, amueblado, cochera, precio, patio, pisos);

        inmobiliaria.agregar(casa);
    }

    public void agregarDepartamento(Inmobiliaria inmobiliaria) {
        int opcion = 0;
        System.out.println("Direccion del departamento: ");
        String direccion = teclado.nextLine();

        System.out.println("Cantidad de ambientes?");
        short ambientes = teclado.nextShort();

        System.out.println("Cantidad de baños?");
        short cantBaños = teclado.nextShort();

        System.out.println("Metros cuadrados?");
        int metrosCuadrados = teclado.nextInt();

        boolean amueblado = false;
        do {
            System.out.println("Esta amueblado? (1 -Si/ 2 -No)");
            opcion = teclado.nextInt();
            if (opcion == 1) {
                amueblado = true;
            } else if (opcion == 2) {
                amueblado = false;
            } else {
                System.out.println("Opcion invalida");
            }
        } while (opcion != 1 && opcion != 2);

        boolean cochera = false;
        do {
            System.out.println("Tiene cochera? (1 -Si/ 2 -No)");
            opcion = teclado.nextInt();
            if (opcion == 1) {
                cochera = true;
            } else if (opcion == 2) {
                cochera = false;
            } else {
                System.out.println("Opcion invalida");
            }
        } while (opcion != 1 && opcion != 2);

        System.out.println("Precio del inmueble?");
        double precio = teclado.nextDouble();

        String disposicion = "";
        do {
            System.out.println("Cual es la disposicion? (1 -A la calle/ 2 - Interno)");
            opcion = teclado.nextInt();
            if (opcion == 1) {
                disposicion = "Calle";
            } else if (opcion == 2) {
                disposicion = "Interno";
            } else {
                System.out.println("Opcion invalida");
            }
        } while (opcion != 1 && opcion != 2);

        System.out.println("Numero del piso?");
        String nroPiso = teclado.nextLine();

        Estado estado = null;
        do {
            System.out.println("Desea alquilar o vender? (1 -Alquiler/ 2 -Venta)");
            opcion = teclado.nextInt();
            if (opcion == 1) {
                estado = Estado.EnAlquiler;
            } else if (opcion == 2) {
                estado = Estado.EnVenta;
            } else {
                System.out.println("Opcion invalida");
            }
        } while (opcion != 1 && opcion != 2);

        Departamento departamento = new Departamento(estado, direccion, ambientes, cantBaños, metrosCuadrados, amueblado, cochera, precio, nroPiso, disposicion);

        inmobiliaria.agregar(departamento);
    }

    public void agregarLocal(Inmobiliaria inmobiliaria) {
        int opcion = 0;
        System.out.println("Direccion del local: ");
        String direccion = teclado.nextLine();

        System.out.println("Cantidad de ambientes?");
        short ambientes = teclado.nextShort();

        boolean vidrieras = false;
        do {
            System.out.println("Tiene vidrieras? (1 -Si/ 2 -No)");
            opcion = teclado.nextInt();
            if (opcion == 1) {
                vidrieras = true;
            } else if (opcion == 2) {
                vidrieras = false;
            } else {
                System.out.println("Opcion invalida");
            }
        } while (opcion != 1 && opcion != 2);

        System.out.println("Precio del inmueble?");
        double precio = teclado.nextDouble();

        Estado estado = null;
        do {
            System.out.println("Desea alquilar o vender? (1 -Alquiler/ 2 -Venta)");
            opcion = teclado.nextInt();
            if (opcion == 1) {
                estado = Estado.EnAlquiler;
            } else if (opcion == 2) {
                estado = Estado.EnVenta;
            } else {
                System.out.println("Opcion invalida");
            }
        } while (opcion != 1 && opcion != 2);

        Local local = new Local(direccion, estado, ambientes, vidrieras, precio);

        inmobiliaria.agregar(local);
    }

    public void agregarCochera(Inmobiliaria inmobiliaria) {
        int opcion = 0;
        System.out.println("Direccion de la cochera: ");
        String direccion = teclado.nextLine();

        System.out.println("Numero de piso?");
        short piso = teclado.nextShort();

        System.out.println("Posicion en el piso?");
        short posicion = teclado.nextShort();

        String medioDeAcceso = "";
        do {
            System.out.println("Medio de acceso (1 -Ascensor/ 2 -Rampa)");
            opcion = teclado.nextInt();
            if (opcion == 1) {
                medioDeAcceso = "Ascensor";
            } else if (opcion == 2) {
                medioDeAcceso = "Rampa";
            } else {
                System.out.println("Opcion invalida");
            }
        } while (opcion != 1 && opcion != 2);

        System.out.println("Precio del inmueble?");
        double precio = teclado.nextDouble();

        Estado estado = null;
        do {
            System.out.println("Desea alquilar o vender? (1 -Alquiler/ 2 -Venta)");
            opcion = teclado.nextInt();
            if (opcion == 1) {
                estado = Estado.EnAlquiler;
            } else if (opcion == 2) {
                estado = Estado.EnVenta;
            } else {
                System.out.println("Opcion invalida");
            }
        } while (opcion != 1 && opcion != 2);

        Cochera cochera = new Cochera(direccion, estado, piso, posicion, medioDeAcceso, precio);

        inmobiliaria.agregar(cochera);
    }
}