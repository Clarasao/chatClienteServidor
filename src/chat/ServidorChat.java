package chat;

import chat.Manejador;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ServidorChat {
    private static final int PUERTO = 12345;
    private static List<Manejador> clientes = new ArrayList<>();

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PUERTO)) {
            System.out.println("Servidor en línea. Esperando conexiones...");

            while (true) {
                Socket clienteSocket = serverSocket.accept();
                System.out.println("Nuevo cliente conectado desde: " + clienteSocket.getInetAddress());

                Manejador nuevoCliente = new Manejador(clienteSocket);
                clientes.add(nuevoCliente);

                Thread clientThread = new Thread(nuevoCliente);
                clientThread.start();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static void broadcast(String mensaje, Manejador remitente) {
        for (Manejador cliente : clientes) {
            if (cliente != remitente) {
                cliente.enviarMensaje(mensaje);
            }
        }
        System.out.println(mensaje);  // Imprime el mensaje en la consola del servidor
    }
}
