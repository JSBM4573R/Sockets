import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

/**
 * Clase de la maquina Socket Servidor
 */
public class Server {

    private static final String HOST = "localhost";
    private static final String PORT_DB = "3306";
    private static final String DATABASE = "sistema_personas";
    private static final String TIMEZONE = "?serverTimezone=America/Bogota";
    private static final int PORT_SOCKET_SERVER = 5000;

    /**
     * Metodo principal de la clase Server
     * @param args
     */
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        System.out.print("Ingrese usuario DB: ");
        String user = sc.nextLine();

        System.out.print("Ingrese contraseña DB: ");
        String password = sc.nextLine();

        // Control de errores y generación del Socket
        try (ServerSocket serverSocket = new ServerSocket(PORT_SOCKET_SERVER)) {
            System.out.println("Servidor iniciado en el puerto: " + PORT_SOCKET_SERVER);
            System.out.println("Escuchando...");

            int contador = 1;

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("¡Cliente " + contador + " conectado!");

                new Thread(new ClientHandler(clientSocket, contador, user, password)).start();
                contador++;
            }

        } catch (IOException e) {
            System.out.println("Error en el servidor: " + e.getMessage());
        }
    }

    /**
     * Clase que maneja cada cliente bajo un hilo único de procesamiento
     */
    static class ClientHandler implements Runnable {

        private Socket socket;
        private int count;
        private String user;
        private String password;

        // Constructor de la clase
        public ClientHandler(Socket socket, int count, String user, String password) {
            this.socket = socket;
            this.count = count;
            this.user = user;
            this.password = password;
        }

        /**
         * Se sobre escribe el metodo para incluir la logica del procesamiento
         */
        @Override
        public void run() {

            try (
                    BufferedReader entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    PrintWriter salida = new PrintWriter(socket.getOutputStream(), true)) {

                String mensaje;

                while ((mensaje = entrada.readLine()) != null) {

                    if (mensaje.equalsIgnoreCase("NO")) {
                        System.out.println("¡Cliente " + count + " desconectado!");
                        break;
                    }

                    System.out.println("Mensaje del cliente " + count + ": " + mensaje);

                    String respuesta = consultarPersona(mensaje, user, password);
                    salida.println(respuesta);
                }

            } catch (Exception e) {
                System.out.println("Error con cliente" + count + ": " + e.getMessage());
            }
        }
    }

    /**
     * Método de consulta a la DB
     * @param numberPhone
     * @param user
     * @param password
     * @return
     */
    public static String consultarPersona(String numberPhone, String user, String password) {

        String jdbc = "jdbc:mysql://" + HOST + ":" + PORT_DB + "/" + DATABASE + TIMEZONE;

        String query = "SELECT p.dir_tel, p.dir_nombre, p.dir_direccion, c.ciud_nombre " +
                "FROM personas p JOIN ciudades c ON p.dir_ciud_id = c.ciud_id WHERE p.dir_tel = ?";

        try (
                Connection conn = DriverManager.getConnection(jdbc, user, password);
                PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, numberPhone);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                System.out.println("Consulta encontrada en la DB");
                return rs.getString("dir_tel") + ", " +
                        rs.getString("dir_nombre") + ", " +
                        rs.getString("dir_direccion") + ", " +
                        rs.getString("ciud_nombre");
            } else {
                System.out.println("Consulta no encontrada en la DB");
                return "Persona dueña de ese número telefónico no existe.";
            }

        } catch (SQLException e) {
            System.out.println("Error en la consulta: " + e.getMessage());
            return "Error en la consulta: " + e.getMessage();
        }
    }
}