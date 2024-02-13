package chat;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Manejador implements Runnable {
    private final Socket clienteSocket;
    private final PrintWriter salidaCliente;

    public Manejador(Socket socket) throws IOException {
        this.clienteSocket = socket;
        this.salidaCliente = new PrintWriter(socket.getOutputStream(), true);
    }

    @Override
    public void run() {
        try (
                Scanner entradaCliente = new Scanner(clienteSocket.getInputStream())
        ) {
            String nombreCliente = entradaCliente.nextLine();
            System.out.println("Nuevo cliente identificado como: " + nombreCliente);

            ServidorChat.broadcast(nombreCliente + " se ha unido al chat.", this);

            String mensajeCliente;
            do {
                mensajeCliente = entradaCliente.nextLine();
                ServidorChat.broadcast(nombreCliente + ": " + mensajeCliente, this);
            } while (!mensajeCliente.equalsIgnoreCase("adios"));

            System.out.println(nombreCliente + " ha abandonado el chat.");
            ServidorChat.broadcast(nombreCliente + " ha abandonado el chat.", this);
            clienteSocket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void enviarMensaje(String mensaje) {
        salidaCliente.println(mensaje);
    }
}
