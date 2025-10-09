package com.inventory.services;

import com.inventory.dao.UserDAO;
import com.inventory.dao.impl.UserDAOImpl;
import com.inventory.exceptions.DuplicateUserException;
import com.inventory.exceptions.InvalidCredentialsException;
import com.inventory.model.User;

import java.sql.SQLException;

public class UserService {

    private final UserDAO userDAO;

    public UserService() {
        this.userDAO = new UserDAOImpl();
    }

    public boolean register(String username, String password, String role) throws DuplicateUserException {
        try {
            User existing = userDAO.getUserByUsername(username);
            if (existing != null) {
                System.out.println("⚠️ Username already exists!");
                return false;
            }

            User newUser = new User(0, username, password, role);
            userDAO.addUser(newUser);
            
            return true;

        } catch (SQLException e) {
            System.out.println("Error registering user: " + e.getMessage());
            return false;
        }
    }

    public User login(String username, String password) throws InvalidCredentialsException{
        try {
            User user = userDAO.getUserByUsername(username);
            if (user != null && user.getPassword().equals(password)) {
                System.out.println("Congratulations, Login successful! ");
                return user;
            } else {
                System.out.println("Invalid username or password!");
            }
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
        return null;
    }
}
