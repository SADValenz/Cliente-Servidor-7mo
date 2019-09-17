package servidordirectorio;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
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
        String Ruta = "";
        try {
            Lector = new BufferedReader(new InputStreamReader(Socket.getInputStream()));
        } catch (IOException ex) {
            System.out.println("Error al crear lector de datos");
        }
        try {
            Ruta = Lector.readLine();
            System.out.println("Petición para ver directorio: " + Ruta);
        } catch (IOException ex) {
            System.out.println("Error al leer datos: " + ex.toString());
        }
        return Ruta;
    }

    private void ContestarPeticion(String ruta) {
        DataOutputStream dos = null;
        try {
            dos = new DataOutputStream(Socket.getOutputStream());
        } catch (IOException ex) {
            System.out.println("Error al crear salida de datos: " + ex.toString());
        }
        File Directorio = new File(ruta);
        try {
            if ((Directorio.exists()) && (Directorio.isDirectory())) {
                dos.writeBoolean(true);
            } else {
                dos.writeBoolean(false);
            }
        } catch (IOException ex) {
            System.out.println("Error al enviar respuesta" + ex.toString());
        }
        File[] Contenido = Directorio.listFiles();
        String ListaArchivos = "";
        int ContadorArchivos = 0;
        String ListaCarpetas = "";
        int ContadorCarpetas = 0;
        for (File X : Contenido) {
            if (X.isDirectory()) {
                ListaCarpetas += "-" + X.getName() + "\n";
                ContadorCarpetas++;
            } else if (X.isFile()) {
                ListaArchivos += "+" + X.getName() + "\n";
                ContadorArchivos++;
            }
        }
        String ListaTotal = "\n" + ContadorArchivos + " archivos encontrados en este directorio:\n" + ListaArchivos
                + "\n" + ContadorCarpetas + " carpetas encontradas en este directorio:\n" + ListaCarpetas;
        try {
            dos.writeUTF(ListaTotal);
        } catch (IOException ex) {
            System.out.println("Error al enviar datos");
        }
    }
}

