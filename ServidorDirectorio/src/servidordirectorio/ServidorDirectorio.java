package servidordirectorio;

public class ServidorDirectorio {

    public static void main(String[] args) {
        Servidor SV = null;
        try {
            SV = new Servidor(args);
            SV.Iniciar();
        } catch (Exception e) {
            System.out.println("Error en la definición de argumentos de conexión");
        }
    }

}
