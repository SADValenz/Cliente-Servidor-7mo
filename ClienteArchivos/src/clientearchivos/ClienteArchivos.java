
package clientearchivos;

public class ClienteArchivos {

    public static void main(String[] args) {
        Cliente Cl=null;
        try{
            Cl = new Cliente(args);
            Cl.Iniciar();
        }catch(Exception e){
            System.out.println("Error en la definición de argumentos de conexión: "+e.toString());
        }
    }
}
