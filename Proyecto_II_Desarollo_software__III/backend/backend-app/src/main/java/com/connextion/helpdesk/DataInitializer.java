/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.connextion.helpdesk;

import com.connextion.helpdesk.model.Service;
import com.connextion.helpdesk.repository.ServiceRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 *
 * @author Jefferson
 */
@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initializeServices(ServiceRepository serviceRepository) {
        return args -> {
            List<String> serviceNames = List.of(
                    "Telefonía móvil",
                    "Cable",
                    "Internet",
                    "Telefonía fija"
            );

            for (String name : serviceNames) {
                // Solo inserta si no existe ya
                if (serviceRepository.findByName(name).isEmpty()) {
                    serviceRepository.save(new Service(name));
                    System.out.println("Servicio insertado: " + name);
                }
            }
        };
    }
}
