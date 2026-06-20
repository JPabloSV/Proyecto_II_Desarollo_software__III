package com.connextion.helpdesk;

import com.connextion.helpdesk.model.Category;
import com.connextion.helpdesk.model.Service;
import com.connextion.helpdesk.model.User;
import com.connextion.helpdesk.repository.CategoryRepository;
import com.connextion.helpdesk.repository.ServiceRepository;
import com.connextion.helpdesk.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initializeData(ServiceRepository serviceRepository,
                                            CategoryRepository categoryRepository,
                                            UserRepository userRepository) {
        return args -> {
            seedServices(serviceRepository);
            seedCategories(categoryRepository);
            seedDefaultSupervisor(serviceRepository, userRepository);
        };
    }

    private void seedServices(ServiceRepository serviceRepository) {
        List<String> serviceNames = List.of(
                "Telefonía móvil",
                "Cable",
                "Internet",
                "Telefonía fija"
        );
        for (String name : serviceNames) {
            if (serviceRepository.findByName(name).isEmpty()) {
                serviceRepository.save(new Service(name));
                System.out.println("Servicio insertado: " + name);
            }
        }
    }

    private void seedCategories(CategoryRepository categoryRepository) {
        List<String[]> categories = List.of(
                new String[]{"Facturación", "Problemas y consultas de facturación"},
                new String[]{"Conectividad", "Problemas de red, señal o conexión"},
                new String[]{"Hardware", "Equipos, módems y dispositivos"},
                new String[]{"Software", "Aplicaciones y configuración"},
                new String[]{"Otro", "Solicitudes no clasificadas"}
        );

        List<Category> existing = categoryRepository.findAll();
        for (String[] data : categories) {
            String name = data[0];
            boolean alreadyExists = existing.stream()
                    .anyMatch(category -> category.getName().equalsIgnoreCase(name));
            if (!alreadyExists) {
                categoryRepository.save(new Category(name, data[1]));
                System.out.println("Categoría insertada: " + name);
            }
        }
    }

    private void seedDefaultSupervisor(ServiceRepository serviceRepository,
                                       UserRepository userRepository) {
        String supervisorEmail = "admin@connextion.com";
        if (userRepository.findByEmail(supervisorEmail).isPresent()) {
            return;
        }

        User supervisor = new User();
        supervisor.setName("Admin");
        supervisor.setFirstName("Supervisor");
        supervisor.setSecondSurname("ConneXtion");
        supervisor.setEmail(supervisorEmail);
        supervisor.setPassword("admin123");
        supervisor.setRole("SUPERVISOR");
        supervisor.setSupervisor(true);
        // A support user must have at least one service assigned in order to log in.
        supervisor.setSubscribedServices(serviceRepository.findAll());

        userRepository.save(supervisor);
        System.out.println("Supervisor por defecto creado: " + supervisorEmail + " / admin123");
    }
}