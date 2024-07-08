import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Cliente {

    static final String HOST = "localhost";
    static final int PUERTO = 5000;

    public Cliente() {
        try {
            Socket skCliente = new Socket(HOST, PUERTO);

            // Stream de entrada para recibir mensajes del servidor
            InputStream aux = skCliente.getInputStream();
            DataInputStream flujo = new DataInputStream(aux);

            // Stream de salida para enviar mensajes al servidor
            OutputStream auxOut = skCliente.getOutputStream();
            DataOutputStream flujoOut = new DataOutputStream(auxOut);

            // Hilo para recibir mensajes del servidor
            Thread recibirMensajes = new Thread(new Runnable() {
                public void run() {
                    try {
                        while (true) {
                            System.out.println(flujo.readUTF());
                        }
                    } catch (IOException e) {
                        System.out.println("Error al recibir mensajes: " + e.getMessage());
                    }
                }
            });

            recibirMensajes.start();

            // Hilo principal para enviar mensajes al servidor
            Scanner scanner = new Scanner(System.in);
            String msg = "";
            while (true) {
                msg = scanner.nextLine();
                flujoOut.writeUTF(msg);
                if (msg.equalsIgnoreCase("exit")) {
                    break;
                }
            }

            skCliente.close();
            scanner.close();
        } catch (Exception e) {
            System.out.println("Esto ocurrió: " + e.getMessage());
        }
    }

    public static void main(String[] arg) {
        new Cliente();
    }
}
