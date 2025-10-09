package com.inventory.model;

public class User {
    private int id;
    private String username;
    private String password;
    private String role;

    public User() {}

    public User(int id, String username, String password, String role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public int getId() { return id; }
    public String getUserName() { return username; }
    public String getPassword() { return password; }
    public String getRole() { return role; }

    @Override
    public String toString() {
        return "User [ID=" + id + ", Username=" + username + ", Role=" + role + "]";
    }
}
