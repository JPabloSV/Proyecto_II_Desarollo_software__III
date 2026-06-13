package com.connextion.helpdesk.controller;

import com.connextion.helpdesk.model.User;
import com.connextion.helpdesk.model.enums.UserRole;
import com.connextion.helpdesk.repository.UserRepository; 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/clients") 
public class ClientController {

    @Autowired
    private UserRepository userRepository; 

    // 1. Display the table with all clients
    @GetMapping
    public String listClients(Model model) {
        // Extraemos "CLIENT" de forma segura usando el Enum
        model.addAttribute("clients", userRepository.findByRole(UserRole.CLIENT.name())); 
        return "clients"; 
    }

    // 2. Display the form to register a new client
    @GetMapping("/new")
    public String showRegistrationForm(Model model) {
        model.addAttribute("client", new User()); 
        return "client-form"; 
    }

    // 3. Process saving/creation
    @PostMapping("/save")
    public String saveClient(@ModelAttribute("client") User client) {
        client.setRole(UserRole.CLIENT.name()); 
        client.setSupervisor(false); // CORREGIDO: Llama al método setSupervisor(boolean) de tu User.java
        
        userRepository.save(client);
        return "redirect:/clients"; 
    }

    // 4. Display pre-filled form for editing
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model) {
        User client = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid client ID: " + id));
        model.addAttribute("client", client); 
        return "client-form"; 
    }
}