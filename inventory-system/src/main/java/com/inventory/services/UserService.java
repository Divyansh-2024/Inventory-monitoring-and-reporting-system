package com.inventory.services;

import com.inventory.dao.UserDAO;
import com.inventory.dao.impl.UserDAOImpl;
import com.inventory.model.User;

import java.sql.SQLException;

public class UserService {

    private final UserDAO userDAO;

    public UserService() {
        this.userDAO = new UserDAOImpl();
    }

    // Register a new user
    public boolean register(String name, String password, String role) {
        try {
            // Check if user already exists
            User existingUser = userDAO.getUserByUsername(name);
            if (existingUser != null) {
                System.out.println("⚠️ Username already exists.");
                return false;
            }

            // Create a User object with id=0 (DB will auto-increment)
            User newUser = new User(0, name, password, role);

            // Pass it to DAO
            userDAO.addUser(newUser);

            System.out.println("✅ User registered successfully!");
            return true;

        } catch (SQLException e) {
            System.out.println("❌ Database error during registration: " + e.getMessage());
            return false;
        }
    }

    // Login user
    public User login(String name, String password) {
        try {
            User user = userDAO.getUserByUsername(name);
            if (user != null && user.getPassword().equals(password)) {
                System.out.println("✅ Login successful! Welcome " + user.getUserName());
                return user;
            } else {
                System.out.println("❌ Invalid username or password.");
            }
        } catch (SQLException e) {
            System.out.println("❌ Database error during login: " + e.getMessage());
        }
        return null;
    }
}
