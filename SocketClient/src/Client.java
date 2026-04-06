import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.IOException;
import java.net.Socket;

/**
 * Clase de la maquina Socket Cliente
 */
public class Client {

    private static final String HOST = "127.0.0.1";
    private static final int PORT = 5000;

    /**
     * Metodo principal de la clase Client
     * @param args
     */
    public static void main(String[] args) {

        // Control de errores y generación del Socket
        try (
            Socket socket = new Socket(HOST, PORT);
            BufferedReader teclado = new BufferedReader(new InputStreamReader(System.in));
            PrintWriter salida = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()))
        ) {

            System.out.println("¡Conectado al servidor!");

            while (true) {

                System.out.print("Ingrese número telefónico de la persona que desea consultar: ");
                String telefono = teclado.readLine();

                salida.println(telefono);

                String respuesta = entrada.readLine();
                System.out.println("Respuesta del Servidor: " + respuesta);

                System.out.print("¿Desea continuar? (SI/NO): ");
                String opc = teclado.readLine();

                if (opc.equalsIgnoreCase("NO")) {
                    salida.println("NO");
                    System.out.println("Conexión finalizada");
                    break;
                }
            }

        } catch (IOException e) {
            System.out.println("Error de conexión: " + e.getMessage());
        }
    }
}