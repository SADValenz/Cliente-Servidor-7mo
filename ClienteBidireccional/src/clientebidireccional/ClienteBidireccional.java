package clientebidireccional;
import java.io.*;
import java.net.*;
import java.util.Scanner;
public class ClienteBidireccional {
    
    public static void main(String[] args){
        Socket socket = null;
        PrintWriter escritor = null;
        BufferedReader lector=null;
        String datos="";
        String datosEntrada="";
        Scanner scan = new Scanner(System.in);
        try {
            socket = new Socket("127.0.0.1", 2500);
        } catch (IOException ex) {
            System.out.println("Error al crear socket de conexión");
        }
        try {
            escritor = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException ex) {
            System.out.println("Error al crear escritor de datos");
        }
        try {
            lector = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException ex) {
            System.out.println("Error al crear lector de datos");
        }
        
        while (true){
            datos = scan.nextLine();
            escritor.println(datos);
            try {
                datosEntrada = lector.readLine();
            } catch (IOException ex) {
                System.out.println("Error al leer datos de entrada");
            }
            System.out.println(datosEntrada);
        }
    }
    
}
