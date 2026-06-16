package com.connextion.helpdesk.model.base;


public class ClientUser extends SystemUser {

    public ClientUser() {}

    public ClientUser(Long id, String name, String email) {
        super(id, name, email);
    }

    @Override
    public String getRoleLabel() {
        return "Cliente";
    }

    @Override
    public boolean canAssignTickets() {
        return false; 
    }
}
