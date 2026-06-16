package com.connextion.helpdesk.model.base;


public class SupportUser extends SystemUser {

    private boolean isSupervisor;

    public SupportUser() {}

    public SupportUser(Long id, String name, String email, boolean isSupervisor) {
        super(id, name, email);
        this.isSupervisor = isSupervisor;
    }

    @Override
    public String getRoleLabel() {
        return isSupervisor ? "Supervisor" : "Soportista";
    }

    @Override
    public boolean canAssignTickets() {
        return isSupervisor; 
    }

    public String getDisplayName(boolean includeEmail, boolean includeRole) {
        String base = includeEmail ? getDisplayName() : name;
        return includeRole ? base + " [" + getRoleLabel() + "]" : base;
    }

    public boolean getIsSupervisor() { return isSupervisor; }
    public void setSupervisor(boolean isSupervisor) { this.isSupervisor = isSupervisor; }
}
