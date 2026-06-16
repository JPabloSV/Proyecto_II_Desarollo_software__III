package com.connextion.helpdesk.model.base;


public abstract class SystemUser {

    protected Long id;
    protected String name;
    protected String email;

    public SystemUser() {}

    public SystemUser(Long id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    public abstract String getRoleLabel();


    public abstract boolean canAssignTickets();


    public String getDisplayName() {
        return name + " <" + email + ">";
    }


    public String getDisplayName(boolean includeEmail) {
        return includeEmail ? getDisplayName() : name;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}
