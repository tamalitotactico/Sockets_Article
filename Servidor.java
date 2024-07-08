import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Servidor {
  Scanner scanner = new Scanner(System.in);
  static final int PUERTO=5000;
  public Servidor( ) {
    try {
      ServerSocket skServidor = new ServerSocket(PUERTO);
      System.out.println("Escucho el puerto " + PUERTO);
      for ( int numCli = 0; numCli < 3; numCli++ ) {
        Socket skCliente = skServidor.accept(); // Crea objeto
        System.out.println("Cliente conectado desde " + skCliente.getInetAddress());
        System.out.println("Sirvo al cliente " + numCli);

        OutputStream aux = skCliente.getOutputStream();
        DataOutputStream flujo= new DataOutputStream( aux );
        flujo.writeUTF( "Hola cliente " + numCli );

        System.out.println("inercambiaremos  mensajes ");

        while(true){
          String msg = scanner.nextLine();
          flujo.writeUTF(msg);
          if (msg.equalsIgnoreCase("exit")) {
            break;
          }
        }
        scanner.close();
        skCliente.close();
      }
      System.out.println("Demasiados clientes por hoy");
    } catch( Exception e ) {
      System.out.println( e.getMessage() );
    }
  }
  public static void main( String[] arg ) {
    new Servidor();
  }
}
