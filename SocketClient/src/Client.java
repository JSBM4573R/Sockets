import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.IOException;
import java.net.Socket;

public class Client {

    public static void main(String[] args) {

        String host = "127.0.0.1";
        int port = 5000;

        try (
            Socket socket = new Socket(host, port);
            BufferedReader teclado = new BufferedReader(new InputStreamReader(System.in));
            PrintWriter salida = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()))
        ) {

            System.out.println("¡Conectado al servidor!");

            while (true) {

                System.out.print("Ingrese número telefónico: ");
                String telefono = teclado.readLine();

                salida.println(telefono);

                String respuesta = entrada.readLine();
                System.out.println("Respuesta del Servidor: " + respuesta);

                System.out.print("¿Desea continuar? (SI/NO): ");
                String opcion = teclado.readLine();

                if (opcion.equalsIgnoreCase("NO")) {
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