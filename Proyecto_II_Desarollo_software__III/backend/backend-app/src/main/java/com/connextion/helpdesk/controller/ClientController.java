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


    @GetMapping
    public String listClients(Model model) {
        model.addAttribute("clients", userRepository.findByRole(UserRole.CLIENT.name())); 
        return "clients"; 
    }


    @GetMapping("/new")
    public String showRegistrationForm(Model model) {
        model.addAttribute("client", new User()); 
        return "client-form"; 
    }


    @PostMapping("/save")
    public String saveClient(@ModelAttribute("client") User client) {
        client.setRole(UserRole.CLIENT.name()); 
        client.setSupervisor(false); 
        
        userRepository.save(client);
        return "redirect:/clients"; 
    }


    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model) {
        User client = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid client ID: " + id));
        model.addAttribute("client", client); 
        return "client-form"; 
    }
}