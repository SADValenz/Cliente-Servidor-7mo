package clientesimple;

import java.io.*;
import java.net.*;

public class ClienteSimple {

    public static void main(String[] args) {
        try {
            CrearCliente(args);
        } catch (Exception e) {
            System.out.println("Error: Datos de conexión incompletos incorrectos");
        }
    }
    public static void CrearCliente(String[] args) {
        String ip = args[0];
        int puerto = Integer.valueOf(args[1]);
        String mensaje = args[2];
        Socket socket = null;
        PrintWriter escritor = null;
        try {
            socket = new Socket(ip, puerto);
        } catch (IOException ex) {
            System.out.println("Error al crear el socket de conexión");
            System.exit(1);
        }
        try {
            escritor = new PrintWriter(socket.getOutputStream(), true);
            escritor.println(mensaje);
        } catch (IOException ex) {
            System.out.println("Error al enviar datos");
            System.exit(2);
        }
        try {
            socket.close();
        } catch (IOException ex) {
            System.out.println("Error al cerrar socket de conexión");
            System.exit(3);
        }
    }
}
