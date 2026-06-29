/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cr.ac.ucr.paraiso.progra2.chat.cliente;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

/**
 *
 * @author Kenneth
 */
public class ChatCliente {
    public static void main(String[] args) {
        String host = "localhost";
        int puerto = 5000;

        try {
            Socket socket = new Socket(host, puerto);
            System.out.println("Conectado al servidor.");

            // Hilo para escuchar mensajes del servidor
            new Thread(new EscuchaServidor(socket)).start();

            // Flujo para escribir mensajes al servidor
            PrintWriter salida = new PrintWriter(socket.getOutputStream(), true);
            Scanner scanner = new Scanner(System.in);

            System.out.println("Escribe tus mensajes (escribe 'salir' para terminar):");
            while (true) {
                String mensaje = scanner.nextLine();
                if (mensaje.equalsIgnoreCase("salir")) break;
                salida.println(mensaje);
            }

            socket.close();
        } catch (IOException e) {
            System.err.println("Error de conexión: " + e.getMessage());
        }
    }
}
