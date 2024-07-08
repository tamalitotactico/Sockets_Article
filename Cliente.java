import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Cliente {

  static final String HOST = "localhost";
  static final int PUERTO=5000;

  public Cliente() {
    try{
      Socket skCliente = new Socket( HOST , PUERTO );

      InputStream aux = skCliente.getInputStream();
      DataInputStream flujo = new DataInputStream( aux );

      OutputStream auxOut = skCliente.getOutputStream();
      DataOutputStream flujoOut = new DataOutputStream(auxOut);

      Scanner scanner = new Scanner(System.in);
      String msg =" ";
      while(true){
        //msg = scanner.nextLine();
        //flujoOut.writeUTF(msg);
        System.out.println( flujo.readUTF());
      }
    } catch( Exception e ) {
      System.out.println("Esto ocurrio "+ e.getMessage() );
    }
  }

  public static void main( String[] arg ) {
    new Cliente();
  }
}
