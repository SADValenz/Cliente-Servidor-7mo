
package servidorarchivosbarra;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class Servidor {

    private int puerto;
    private Socket Socket;
    private ServerSocket Servidor;

    public Servidor(String[] args) {
        puerto = Integer.valueOf(args[0]);
        Socket = null;
        Servidor = null;
    }

    public void Iniciar() {
        while (true) {
            AbrirSockets();
            ContestarPeticion(LeerPeticion());
            CerrarSockets();
        }
    }

    private void AbrirSockets() {
        try {
            Servidor = new ServerSocket(puerto);
        } catch (IOException ex) {
            System.out.println("Error al crear socket de servidor: " + ex.toString());
        }
        try {
            Socket = Servidor.accept(); 
        } catch (IOException ex) {
            System.out.println("Error al crear socket de conexión: " + ex.toString());
        }
    }

    private void CerrarSockets() {
        try {
            Socket.close();
            Servidor.close();
        } catch (IOException ex) {
            System.out.println("Error al cerrar sockets de conexión");
        }
    }

    private String LeerPeticion() {
        BufferedReader Lector = null;
        String Direccion = "";
        try {
            Lector = new BufferedReader(new InputStreamReader(Socket.getInputStream()));
        } catch (IOException ex) {
            System.out.println("Error al crear lector de datos");
        }
        try {
            Direccion = Lector.readLine();
        } catch (IOException ex) {
            System.out.println("Error al leer datos: " + ex.toString());
        }
        return Direccion;
    }

    private void ContestarPeticion(String ruta) {
        DataOutputStream DOS = null;
        try {
            DOS = new DataOutputStream(Socket.getOutputStream());
        } catch (IOException ex) {
            System.out.println("Error al crear salida de datos: " + ex.toString());
        }
        File Archivo = new File(ruta);
        try {
            if (Archivo.exists()) {
                DOS.writeBoolean(true);
                EnviarArchivo(Archivo, DOS);
            } else {
                DOS.writeBoolean(false);
            }
        } catch (IOException ex) {
            System.out.println("Error al enviar respuesta: " + ex.toString());
        }
    }

    private void EnviarArchivo(File Archivo, DataOutputStream DOS) {
        BufferedInputStream BIS = null;
        BufferedOutputStream BOS = null;
        try {
            BIS = new BufferedInputStream(new FileInputStream(Archivo));
        } catch (FileNotFoundException ex) {
            System.out.println("Archivo no encontrado: " + ex.toString());
        }
        try {
            BOS = new BufferedOutputStream(Socket.getOutputStream());
        } catch (IOException ex) {
            System.out.println("Error al salida de datos con buffer: " + ex.toString());
        }
        try {
            DOS.writeUTF(Archivo.getName());
        } catch (IOException ex) {
            System.out.println("No se encontró nombre de archivo: " + ex.toString());
        }
        try {
            DOS.writeLong(Archivo.length());
        } catch (IOException ex) {
            System.out.println("Error al enviar tamaño de archivo: " + ex.toString());
        }
        byte[] TamañoPaq = new byte[1048576];
        try {
            DOS.writeInt(TamañoPaq.length);
        } catch (IOException ex) {
            System.out.println("Error al enviar tamaño de buffer: " + ex.toString());
        }
        int EntradaDeBytes;
        try {
            while ((EntradaDeBytes = BIS.read(TamañoPaq)) != -1) {
                BOS.write(TamañoPaq, 0, EntradaDeBytes);
            }
        } catch (IOException ex) {
            System.out.println("Error en la transmisión: " + ex.toString());
        }
        try {
            BIS.close();
            BOS.close();
        } catch (IOException ex) {
            System.out.println("Error al cerrar buffers de datos: " + ex.toString());
        }
    }
}

