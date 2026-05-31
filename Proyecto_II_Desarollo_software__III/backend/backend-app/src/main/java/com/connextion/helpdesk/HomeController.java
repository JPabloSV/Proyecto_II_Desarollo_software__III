package com.connextion.helpdesk; // Asegúrate de que coincida con tu paquete real

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping("/")
    public String inicio() {
        return "¡ConneXtion HelpDesk está en línea y conectado a SQL Server! 🚀";
    }
}