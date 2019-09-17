
package servidorbidireccional;

import java.net.*;
import java.io.*;
import java.util.Scanner;

public class ServidorBidireccional {

    public static void main(String[] args){
        ServerSocket socketServidor = null;
        Socket socket = null;
        BufferedReader lector = null;
        PrintWriter escritor = null;
        String entrada="";
        Scanner scan=new Scanner(System.in);
        String salida="";
        try {
            socketServidor = new ServerSocket(2500);
        } catch (IOException ex) {
            System.out.println("Error al crear socket de servidor");
        }
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
            escritor = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException ex) {
            System.out.println("Error al crear escritor de datos");
        }
        do{
            try {
                entrada=lector.readLine();
            } catch (IOException ex) {
                System.out.println("Error al leer datos");
            }
            System.out.println(entrada);
            if(entrada.equalsIgnoreCase("fin")){
                System.out.println("Me voy");
                try {
                    socket.close();
                } catch (IOException ex) {
                    System.out.println("Error al cerrar socket de conexión");
                }
                try {
                    socketServidor.close();
                } catch (IOException ex) {
                    System.out.println("Error al cerrar socket de servidor");
                }
                System.exit(0);
            }
            salida=scan.nextLine();
            escritor.println(salida);
        }while(!entrada.equalsIgnoreCase("fin"));
    }
    
}
