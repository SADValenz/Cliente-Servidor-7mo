package clientearchivosbarra;

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

    private void AbrirSocket() {
        try {
            Socket = new Socket(ip, puerto);
        } catch (IOException ex) {
            System.out.println("No se ha podido conectar con el host: " + ex.toString());
        }
    }

    private void RealizarPeticion() {
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

    private void VerificarRespuesta() {
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

    private void RecibirArchivo(DataInputStream DIS) {

        byte[] TamañoPaq = null;
        long TamañoArchivo = 0;
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
            NombreDeArchivo = DIS.readUTF();
        } catch (IOException ex) {
            System.out.println("Error al leer nombre de archivo recibido: " + ex.toString());
        }
        try {
            TamañoArchivo = DIS.readLong();
        } catch (IOException ex) {
            System.out.println("Error al recibir tamaño de archivo: " + ex.toString());
        }
        try {
            //int tp= DIS.readInt();
            //TamañoPaq= new byte [tp];
            TamañoPaq = new byte[DIS.readInt()];
        } catch (IOException ex) {
            System.out.println("Error al recibir tamaño de buffer: " + ex.toString());
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
            long Contador = 0;
            while ((EntradaDeBytes = BIS.read(TamañoPaq)) != -1) {
                BOS.write(TamañoPaq, 0, EntradaDeBytes);
                Contador += EntradaDeBytes;
                VerProgreso(Contador, TamañoArchivo);
            }
            BOS.close();
        } catch (IOException ex) {
            System.out.println("Error en la transmisión de datos: " + ex.toString());
        }
        try {
            DIS.close();
        } catch (IOException ex) {
            System.out.println("Error al cerrar conexiones: " + ex.toString());
        }
    }

    private void VerProgreso(long Enviado, long Total) {
        double Porcentaje;
        int TamañoBarra = 25;
        int Division = 100 / TamañoBarra;
        char espacio = ' ';
        char pedazo = '█';
        Porcentaje = ((double) Enviado / (double) Total) * 100;
        int completado = (int)Porcentaje / Division;
        int faltante = TamañoBarra - completado;
        String comp = new String(new char[completado]).replace('\0', pedazo);
        String falt = new String(new char[faltante]).replace('\0', espacio);
        String Barra = "[" + comp + falt + "]";
        System.out.print("Recibido: " + Barra + "(" + (int)Porcentaje + "%" + " completado) [" + Convertir(Enviado) + "/" + Convertir(Total) + "]    \r");
    }

    private String Convertir(long x) {
        String C = "";
        double y = 0.0;
        if (x < 1024) {
            y = x;
            C = "Bytes";
        } else if (x >= 1024 && x < 1048576) {
            y = (double) x / 1024;
            C = "KB";
        } else if (x >= 1048576 && x < 1073741824) {
            y = (double) x / 1048576;
            C = "MB";
        } else if (x >= 1073741824) {
            y = (double) x / 1073741824;
            C = "GB";
        }
        y = Math.round(y * 100) / 100d;
        C = y + " " + C;
        return C;
    }
}
