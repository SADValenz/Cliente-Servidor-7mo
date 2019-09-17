package servidorsimple;

import java.net.*;
import java.io.*;

public class ServidorSimple {

    public static void main(String[] args) {
        try {
            CrearServidor(args);
        } catch (Exception e) {
            System.out.println("Error: Ingrese puerto");
        }
    }

    public static void CrearServidor(String[] args) {
        int puerto = Integer.valueOf(args[0]);
        ServerSocket socketServidor = null;
        Socket socket = null;
        String Entrada = "";
        BufferedReader lector = null;

        try {
            socketServidor = new ServerSocket(puerto);
        } catch (IOException ex) {
            System.out.println("Error al crear socket de servidor");
        }
        while (true) {
            try {
                socket = socketServidor.accept();
            } catch (IOException ex) {
                System.out.println("Error al crear socket de conexión");
            }

            try {
                lector = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            } catch (IOException ex) {
                System.out.println("Error al crear lector de datos");
            }
            try {
                while ((Entrada = lector.readLine()) != null) {
                    System.out.println("El cliente dijo: " + Entrada);
                }
            } catch (IOException ex) {
                System.out.println("Error al leer datos");
            }
        }
    }

}
