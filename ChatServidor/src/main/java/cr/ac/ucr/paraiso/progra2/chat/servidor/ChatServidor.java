/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package cr.ac.ucr.paraiso.progra2.chat.servidor;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

/**
 *
 * @author Kenneth
 */
public class ChatServidor {
    // Lista segura para hilos que almacena los flujos de salida de cada cliente
    private static Set<PrintWriter> clientes = ConcurrentHashMap.newKeySet();

    public static void main(String[] args) {
        int puerto = 5000;
        System.out.println("El servidor de chat ha iniciado en el puerto " + puerto);

        try (ServerSocket serverSocket = new ServerSocket(puerto)) {
            while (true) {
                // Acepta una nueva conexión
                Socket clienteSocket = serverSocket.accept();
                System.out.println("Nuevo cliente conectado: " + clienteSocket.getInetAddress());

                // Crea y lanza un hilo para manejar a este cliente
                ClientHandler manejador = new ClientHandler(clienteSocket, clientes);
                new Thread(manejador).start();
            }
        } catch (IOException e) {
            System.err.println("Error en el servidor: " + e.getMessage());
        }
    }

    // Método para enviar mensajes a todos los clientes conectados
    public static void broadcast(String mensaje, PrintWriter remitente) {
        for (PrintWriter cliente : clientes) {
            // Opcional: Evita enviarle el mensaje a quien lo escribió
            // if (cliente != remitente) {
                cliente.println(mensaje);
                cliente.flush();
            // }
        }
    }
}
