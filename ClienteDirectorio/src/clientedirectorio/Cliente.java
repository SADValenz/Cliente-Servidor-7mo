package clientedirectorio;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class Cliente {

    private String ip;
    private int puerto;
    private String ruta;
    private Socket Socket;

    public Cliente(String[] args) {
        ip = args[0];
        puerto = Integer.valueOf(args[1]);
        ruta = args[2];
        Socket = null;
    }

    public void Iniciar() {
        AbrirSocket();
        RealizarPeticion();
        VerRespuesta();
    }

    private void AbrirSocket() {
        try {
            Socket = new Socket(ip, puerto);
        } catch (IOException ex) {
            System.out.println("No se ha podido conectar con el host: " + ex);
        }
    }

    private void RealizarPeticion() {
        PrintWriter Escritor = null;
        try {
            Escritor = new PrintWriter(Socket.getOutputStream(), true);
        } catch (IOException ex) {
            System.out.println("Error al crear escritor de datos: " + ex);
        }
        try {
            Escritor.println(ruta);
        } catch (Exception e) {
            System.out.println("Error al enviar petición al servidor: " + e);
        }
    }

    private void VerRespuesta() {
        Boolean Recibir = false;
        DataInputStream dis = null;
        try {
            dis = new DataInputStream(Socket.getInputStream());
        } catch (IOException ex) {
            System.out.println("Error al crear entrada de datos: " + ex);
        }
        try {
            Recibir = dis.readBoolean();
        } catch (IOException ex) {
            System.out.println("Error al leer respuesta del servidor: " + ex);
        }
        if (Recibir) {
            try {
                System.out.println(dis.readUTF());
            } catch (IOException ex) {
                System.out.println("Error al leer datos recibidos: " + ex);
            }
        } else {
            System.out.println("La ruta especificada no existe o no es un directorio válido");
        }
    }
}
