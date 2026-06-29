/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cr.ac.ucr.paraiso.progra2.chat.servidor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;
import java.util.Set;

/**
 *
 * @author Kenneth
 */
public class ClientHandler implements Runnable {
    private Socket socket;
    private Set<PrintWriter> clientes;
    private PrintWriter salida;

    public ClientHandler(Socket socket, Set<PrintWriter> clientes) {
        this.socket = socket;
        this.clientes = clientes;
    }

    @Override
    public void run() {
        try {
            BufferedReader entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            salida = new PrintWriter(socket.getOutputStream(), true);
            
            // Agregamos este cliente a la lista global
            clientes.add(salida);

            String mensaje;
            while ((mensaje = entrada.readLine()) != null) {
                System.out.println("Mensaje recibido: " + mensaje);
                // Difundir el mensaje a todos
                ChatServidor.broadcast(mensaje, salida);
            }
        } catch (IOException e) {
            System.err.println("Error manejando cliente: " + e.getMessage());
        } finally {
            // Limpieza al desconectarse
            try {
                if (salida != null) clientes.remove(salida);
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
