package chat;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ClienteChat {
    public static void main(String[] args) {
        final String host = "localhost";
        final int puerto = 12345;

        try (Socket socket = new Socket(host, puerto);
             Scanner entradaServidor = new Scanner(socket.getInputStream());
             PrintWriter salidaCliente = new PrintWriter(socket.getOutputStream(), true);
             Scanner scanner = new Scanner(System.in)) {

            System.out.print("Dime tu nombre: ");
            String nombre = scanner.nextLine();
            salidaCliente.println(nombre);

            Thread recibirMensajesThread = new Thread(() -> {
                try {
                    while (true) {
                        String mensajeServidor = entradaServidor.nextLine();
                        System.out.println(mensajeServidor);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

            recibirMensajesThread.start();

            String mensajeCliente;
            do {
                mensajeCliente = scanner.nextLine();
                salidaCliente.println(mensajeCliente);

            } while (!mensajeCliente.equalsIgnoreCase("adios"));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
