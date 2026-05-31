/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.connextion.helpdesk.repository;

import com.connextion.helpdesk.model.Service;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
/**
 *
 * @author Jefferson
 */
@Repository
public interface ServiceRepository extends JpaRepository<Service, Long> {
 
    //buscar service por nombre
    Optional<Service> findByName(String name);
}
