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
 * @author Jefferson
 */
@Entity
@Table(name = "Services")
public class Service {
 
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
 
    // Nombre del servicio (ej: "Internet", "Cable")
    @Column(nullable = false, unique = true, length = 100)
    private String name;
 
    //lista de usuarios del servicio
      @ManyToMany(mappedBy = "subscribedServices")
    @JsonIgnoreProperties("subscribedServices") 
    private List<User> subscribers;
 
    public Service() {}
 
    public Service(String name) {
        this.name = name;
    }
    
    //geters y setterss
    public Long getId() {
        return id;
    }
 
    public void setId(Long id) {
        this.id = id;
    }
 
    public String getName() {
        return name;
    }
 
    public void setName(String name) {
        this.name = name;
    }
 
    public List<User> getSubscribers() {
        return subscribers;
    }
 
    public void setSubscribers(List<User> subscribers) {
        this.subscribers = subscribers;
    }
    
    
}