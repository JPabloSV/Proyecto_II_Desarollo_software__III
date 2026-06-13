package com.connextion.helpdesk.controller;

import com.connextion.helpdesk.model.Service;
import com.connextion.helpdesk.model.User;
import com.connextion.helpdesk.repository.ServiceRepository;
import com.connextion.helpdesk.repository.UserRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ServiceRepository serviceRepository;

    @PostMapping("/users/register")
    public ResponseEntity<?> registerClient(@RequestBody Map<String, Object> body) {

        String email = (String) body.get("email");
        if (userRepository.findByEmail(email).isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "El correo electrónico ya está registrado."));
        }

        User user = new User();
        user.setName((String) body.get("name"));
        user.setFirstName((String) body.get("firstName"));
        user.setSecondSurname((String) body.get("secondSurname"));
        user.setEmail(email);
        user.setPassword((String) body.get("password"));
        user.setRole("CLIENT");
        user.setAddress((String) body.get("address"));
        user.setPhone((String) body.get("phone"));

        List<Integer> serviceIds = (List<Integer>) body.get("serviceIds");
        if (serviceIds == null || serviceIds.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "Debe seleccionar al menos un servicio."));
        }
        List<Service> services = serviceRepository.findAllById(
                serviceIds.stream().map(Long::valueOf).toList()
        );
        user.setSubscribedServices(services);

        User saved = userRepository.save(user);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("message", "Usuario registrado exitosamente.", "userId", saved.getId()));
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody Map<String, String> credentials) {
        String email = credentials.get("email");
        String password = credentials.get("password");

        Optional<User> userOpt = userRepository.findByEmail(email);

        if (userOpt.isEmpty() || !userOpt.get().getPassword().trim().equals(password.trim())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Credenciales incorrectas. Verifique correo o contraseña."));
        }

        User user = userOpt.get();

        if (!"CLIENT".equals(user.getRole())) {
            if (user.getSubscribedServices() == null || user.getSubscribedServices().isEmpty()) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(Map.of("message", "El usuario de soporte no tiene servicios asignados."));
            }
        }

        return ResponseEntity.ok(Map.of(
                "id", user.getId(),
                "name", user.getName(),
                "firstName", user.getFirstName() != null ? user.getFirstName() : "",
                "secondSurname", user.getSecondSurname() != null ? user.getSecondSurname() : "",
                "email", user.getEmail(),
                "role", user.getRole(),
                "isSupervisor", user.getIsSupervisor(), // CORREGIDO: Uso de getIsSupervisor()
                "subscribedServices", user.getSubscribedServices()
        ));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser() {
        return ResponseEntity.ok(Map.of("message", "Sesión cerrada correctamente."));
    }

    @PostMapping("/support-users")
    public ResponseEntity<?> registerSupportUser(@RequestBody Map<String, Object> body) {

        String email = (String) body.get("email");
        if (userRepository.findByEmail(email).isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "El correo electrónico ya está registrado."));
        }

        boolean isSupervisor = Boolean.TRUE.equals(body.get("isSupervisor"));

        User user = new User();
        user.setName((String) body.get("name"));
        user.setFirstName((String) body.get("firstName"));
        user.setSecondSurname((String) body.get("secondSurname"));
        user.setEmail(email);
        user.setPassword((String) body.get("password"));
        user.setRole(isSupervisor ? "SUPERVISOR" : "SUPPORTER");
        user.setSupervisor(isSupervisor);

        List<Integer> serviceIds = (List<Integer>) body.get("serviceIds");
        if (serviceIds == null || serviceIds.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "Debe asignar al menos un servicio al usuario de soporte."));
        }
        List<Service> services = serviceRepository.findAllById(
                serviceIds.stream().map(Long::valueOf).toList()
        );
        user.setSubscribedServices(services);

        User saved = userRepository.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                "message", "Usuario de soporte registrado exitosamente.",
                "userId", saved.getId(),
                "role", saved.getRole(),
                "isSupervisor", saved.getIsSupervisor() // CORREGIDO: Uso de getIsSupervisor()
        ));
    }

    @GetMapping("/services")
    public ResponseEntity<?> getAllServices() {
        return ResponseEntity.ok(serviceRepository.findAll());
    }

    @GetMapping("/users")
    public ResponseEntity<?> getAllUsers() {
        return ResponseEntity.ok(userRepository.findAll());
    }
}
