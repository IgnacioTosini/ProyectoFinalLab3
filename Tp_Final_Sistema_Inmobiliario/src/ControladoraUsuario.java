import Cliente.Contraseña;
import Cliente.Mail;
import Cliente.TiposMail;
import Cliente.Usuario;
import Excepciones.*;

import java.util.Objects;
import java.util.Scanner;

public class ControladoraUsuario {
    Scanner teclado = new Scanner(System.in);

    /**
     * Esta es una funcion donde se gestiona el menu principal de usuario al inicio para loguear y registrarse
     *
     * @param inmobiliaria
     * @return retorna el usuario que se quiso registrar/logear
     */
    public Usuario menu(Inmobiliaria inmobiliaria) {
        Usuario usuario = new Usuario();
        System.out.println("Buen día ¿Qué le gustaría realizar?");
        int opcion = 0;
        System.out.println("1. Loguearse \n2. Registrarse");
        opcion = teclado.nextInt();
        String respuesta = "si";

        do {
            switch (opcion) {
                case 1:
                    while (respuesta.equalsIgnoreCase("si")) {
                        try {
                            usuario = login(inmobiliaria);
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
            System.out.println("Quiero volver al menu?, presione s");
            respuesta = teclado.nextLine();
        } while (respuesta.equals("s"));
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
        String nombre = teclado.nextLine();

        Usuario usuario = inmobiliaria.buscarUsuario(nombre);
        if (usuario == null) {
            throw new UsuarioNoEncontradoException("Usuario no encontrado");

        }
        System.out.println("Ingrese su contraseña");
        teclado.nextLine();
        String contraseña = teclado.nextLine();
        if (!usuario.getContraseña().equals(contraseña)) {
            throw new MalContraseñaException("Contraseña incorrecta");
        }
        return usuario;
    }

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

        Usuario usuario = new Usuario(nombre, contra, dni, correo, edad);
        System.out.println("termine y usuario: " + usuario);

        return usuario;
    }

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
}



