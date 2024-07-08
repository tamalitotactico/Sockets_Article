import java.io.*;
import java.net.*;
import java.util.*;

public class Servidor {
    static final int PUERTO = 5000;
    private static List<ClienteHandler> clientes = Collections.synchronizedList(new ArrayList<>());

    public Servidor() {
        try {
            ServerSocket skServidor = new ServerSocket(PUERTO);
            System.out.println("Escucho el puerto " + PUERTO);

            // Hilo para leer mensajes desde la consola y enviarlos a los clientes
            Thread enviarMensajes = new Thread(new Runnable() {
                Scanner scanner = new Scanner(System.in);

                public void run() {
                    while (true) {
                        String msg = scanner.nextLine();
                        enviarMensajeATodos("Servidor: " + msg);
                    }
                }
            });
            enviarMensajes.start();

            while (true) {
                Socket skCliente = skServidor.accept(); // Acepta conexión de cliente
                System.out.println("Cliente conectado desde " + skCliente.getInetAddress());

                // Crea un nuevo manejador para el cliente y lo añade a la lista
                ClienteHandler handler = new ClienteHandler(skCliente);
                clientes.add(handler);
                new Thread(handler).start();
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static void enviarMensajeATodos(String mensaje) {
        synchronized (clientes) {
            for (ClienteHandler cliente : clientes) {
                cliente.enviarMensaje(mensaje);
            }
        }
    }

    public static void main(String[] arg) {
        new Servidor();
    }

    // Clase manejadora de cada cliente
    private static class ClienteHandler implements Runnable {
        private Socket skCliente;
        private DataOutputStream flujoSalida;
        private DataInputStream flujoEntrada;

        public ClienteHandler(Socket skCliente) {
            this.skCliente = skCliente;
            try {
                this.flujoSalida = new DataOutputStream(skCliente.getOutputStream());
                this.flujoEntrada = new DataInputStream(skCliente.getInputStream());
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }

        public void enviarMensaje(String mensaje) {
            try {
                flujoSalida.writeUTF(mensaje);
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }

        @Override
        public void run() {
            try {
                flujoSalida.writeUTF("Hola cliente conectado desde " + skCliente.getInetAddress());
                while (true) {
                    String msg = flujoEntrada.readUTF();
                    System.out.println("Mensaje recibido de cliente: " + msg);
                    if (msg.equalsIgnoreCase("exit")) {
                        break;
                    }
                    enviarMensajeATodos("Cliente " + skCliente.getInetAddress() + ": " + msg);
                }
                skCliente.close();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
