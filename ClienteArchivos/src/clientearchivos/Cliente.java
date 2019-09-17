package clientearchivos;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
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
        VerificarRespuesta();
    }

    public void AbrirSocket() {
        try {
            Socket = new Socket(ip, puerto);
        } catch (IOException ex) {
            System.out.println("No se ha podido conectar con el host: " + ex.toString());
        }
    }

    public void RealizarPeticion() {
        PrintWriter Escritor = null;
        try {
            Escritor = new PrintWriter(Socket.getOutputStream(), true);
        } catch (IOException ex) {
            System.out.println("Error al crear escritor de datos: " + ex.toString());
        }
        try {
            Escritor.println(ruta);
        } catch (Exception e) {
            System.out.println("Error al enviar petición al servidor: " + e.toString());
        }
    }

    public void VerificarRespuesta() {
        Boolean Recibir = false;
        DataInputStream dis = null;
        try {
            dis = new DataInputStream(Socket.getInputStream());
        } catch (IOException ex) {
            System.out.println("Error al crear entrada de datos: " + ex.toString());
        }
        try {
            Recibir = dis.readBoolean();
        } catch (IOException ex) {
            System.out.println("Error al leer respuesta del servidor: " + ex.toString());
        }
        if (Recibir) {
            RecibirArchivo(dis);
        } else {
            System.out.println("El archivo no se encuentra/no existe");
        }
    }

    public void RecibirArchivo(DataInputStream dis) {
        byte[] TamañoPaq = new byte[1048576];
        int EntradaDeBytes;
        BufferedInputStream BIS = null;
        BufferedOutputStream BOS = null;
        String NombreDeArchivo = "";
        try {
            BIS = new BufferedInputStream(Socket.getInputStream());
        } catch (IOException ex) {
            System.out.println("Error al crear entrada de datos con buffer: " + ex.toString());
        }
        try {
            NombreDeArchivo = dis.readUTF();
        } catch (IOException ex) {
            System.out.println("Error al leer nombre de archivo recibido: " + ex.toString());
        }
        File Directorio = new File("");
        String Ruta = Directorio.getAbsolutePath() + "\\Downloads\\" + NombreDeArchivo;
        System.out.println("El archivo se guardará en: " + Ruta);
        try {
            BOS = new BufferedOutputStream(new FileOutputStream(Ruta));
        } catch (FileNotFoundException ex) {
            System.out.println("Archivo perdido: " + ex.toString());
        }
        try {
            while ((EntradaDeBytes = BIS.read(TamañoPaq)) != -1) {
                BOS.write(TamañoPaq, 0, EntradaDeBytes);
            }
            BOS.close();
        } catch (IOException ex) {
            System.out.println("Error en la transmisión de datos: " + ex.toString());
        }
        try {
            dis.close();
        } catch (IOException ex) {
            System.out.println("Error al cerrar conexiones: " + ex.toString());
        }
    }
}
