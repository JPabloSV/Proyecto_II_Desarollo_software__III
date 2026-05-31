/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.connextion.helpdesk.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.util.List;

/**
 *
 * @author Kenneth
 */
//representa cualquier usuario del sistema, cliente, tecnico, supervisor/admin
@Entity
@Table(name = "Users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

     @Column(nullable = false, length = 100)
    private String name;                  
 
    @Column(nullable = false, length = 100)
    private String firstName;  
    
    @Column(nullable = false, length = 100)
    private String secondSurname;       
 
    @Column(nullable = false, unique = true, length = 100)
    private String email;                 
 
    @Column(nullable = false)
    private String password;             
 
    @Column(nullable = false, length = 50)
    private String role;  //cual rol es

    
    //caampos opcionales del cliente
    @Column(length = 255)
    private String address; //direccion fisica      
 
    @Column(length = 20)
    private String phone;                 
 
   //para saber si el soporte puede ser supervisor 
    @Column(nullable = false)
    private boolean isSupervisor = false;

    //relacion M-N, un usuario puede ser asignado a multiples servicios
       @ManyToMany
    @JoinTable(
        name = "User_Services",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "service_id")
    )
    @JsonIgnoreProperties("subscribers")  // evita bucle infinito al serializar
    private List<Service> subscribedServices;
    
       //tickets
    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL)
    @JsonIgnoreProperties("client")
    private List<Ticket> createdTickets;

    @OneToMany(mappedBy = "technician")
    @JsonIgnoreProperties("client")
    private List<Ticket> assignedTickets;

    //const
   public User() {}
 
    public User(String name, String firstName, String secondSurname,
                String email, String password, String role) {
        this.name = name;
        this.firstName = firstName;
        this.secondSurname = secondSurname;
        this.email = email;
        this.password = password;
        this.role = role;
    }
    
    //geters y sertters
   public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
 
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
 
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
 
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
 
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
 
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
 
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
 
 
    public boolean isSupervisor() { return isSupervisor; }
    public void setSupervisor(boolean isSupervisor) { this.isSupervisor = isSupervisor; }
 
    public List<Service> getSubscribedServices() { return subscribedServices; }
    public void setSubscribedServices(List<Service> subscribedServices) {
        this.subscribedServices = subscribedServices;
    }
 
    public List<Ticket> getCreatedTickets() { return createdTickets; }
    public void setCreatedTickets(List<Ticket> createdTickets) {
        this.createdTickets = createdTickets;
    }
 
    public List<Ticket> getAssignedTickets() { return assignedTickets; }
    public void setAssignedTickets(List<Ticket> assignedTickets) {
        this.assignedTickets = assignedTickets;
    }
}
