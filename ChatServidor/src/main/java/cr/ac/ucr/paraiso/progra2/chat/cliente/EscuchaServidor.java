/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cr.ac.ucr.paraiso.progra2.chat.cliente;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 *
 * @author Kenneth
 */
public class EscuchaServidor implements Runnable {
    private BufferedReader entrada;

    public EscuchaServidor(Socket socket) throws IOException {
        this.entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    @Override
    public void run() {
        try {
            String mensaje;
            while ((mensaje = entrada.readLine()) != null) {
                System.out.println("\n[Servidor/Otros]: " + mensaje);
                System.out.print("> "); // Prompt visual para seguir escribiendo
            }
        } catch (IOException e) {
            System.out.println("Conexión cerrada.");
        }
    }
}
