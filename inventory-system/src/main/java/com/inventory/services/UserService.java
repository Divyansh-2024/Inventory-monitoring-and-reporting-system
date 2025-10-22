package com.inventory.services;

import com.inventory.dao.UserDAO;
import com.inventory.dao.impl.UserDAOImpl;

import com.inventory.exceptions.DuplicateUserException;
import com.inventory.exceptions.InvalidCredentialsException;
import com.inventory.model.User;

import java.sql.SQLException;

public class UserService {

    private final UserDAO userDAO;
    private final OTPService otpService;
     private final UserDAOImpl userDao;


    public UserService() throws SQLException {
        this.userDAO = new UserDAOImpl();
        this.otpService = new OTPService();
        userDao = new UserDAOImpl();
    }

   
   
    // Step 1️⃣ Register User
    public boolean register(String username, String password, String role, String email, boolean is_verified) throws DuplicateUserException {
        try {
            User existing = userDAO.getUserByUsername(username);
            if (existing != null) {
                System.out.println("⚠️ Username already exists!");
                return false;
            }

            User newUser = new User(0, username, password, role, email, false); // not verified
            userDAO.addUser(newUser);
            return true;

        } catch (SQLException e) {
            System.out.println("Error registering user: " + e.getMessage());
            return false;
        }
    }

    // Step 2️ Send OTP for email verification
    public void sendVerificationOTP(String email) {
        try {
            User user = userDAO.getUserByEmail(email);
            if (user == null) {
                System.out.println("⚠️ Email not registered!");
                return;
            }
            if (user.is_verified()) {
                System.out.println("Email is already verified!");
                return;
            }

            String otp = otpService.generateOTP(email);
            System.out.println(" OTP sent to " + email + ": " + otp); // simulate email sending
        } catch (SQLException e) {
            System.out.println("Error sending OTP: " + e.getMessage());
        }
    }

    // Step 3️ Verify OTP
    public boolean verifyEmailWithOTP(String email, String enteredOTP) {
        boolean verified = otpService.validateOTP(email, enteredOTP);
        if (verified) {
            try {
                userDAO.verifyUser(email); // call DAO to mark verified
                System.out.println("Email verified successfully! You can now log in.");
                return true;
            } catch (SQLException e) {
                System.out.println("Failed to update verification status: " + e.getMessage());
            }
        } else {
            System.out.println("⚠️ Invalid or expired OTP!");
        }
        return false;
    }

    // Step 4️ Login
    public User login(String username, String password) throws InvalidCredentialsException {
        try {
            User user = userDAO.getUserByUsername(username);
            if (user == null) {
                throw new InvalidCredentialsException(" User not found!");
            }
            if (user.getPassword().equals(password)) {
                System.out.println("Login successful! welcome, " + user.getUserName());
                System.out.println("Role: " + user.getRole());
                return user;
            } else {
                System.out.println("Invalid credentials");
                return null;
            }

        } catch (SQLException e) {
            System.out.println(" Database error during login: " + e.getMessage());
            return null;
        }
    }

    public User getUserByEmail(String email) throws SQLException {
        return userDao.getUserByEmail(email);
    }

}
