package com.inventory.model;

public class User {
    private int id;
    private String username;
    private String password;
    private String role;
    private String email;
    private boolean is_verified;

    public User() {}

    public User(int id, String username, String password, String role, String email, boolean is_verified) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
        this.email=email;
        this.is_verified=is_verified;
    }


    public User(String username, String password, String role, String email) { 
        this.username = username;
        this.password = password;
        this.role = role;
        this.email=email;       
    }

    public int getId() { return id; }
    public String getUserName() { return username; }
    public String getPassword() { return password; }
    public String getRole() { return role; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email=email; }
    public boolean is_verified() { return is_verified; }
    public void isVerified(boolean isVerified) {this.is_verified=is_verified; }
    
}
