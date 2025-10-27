package com.inventory.dao.impl;

import com.inventory.dao.UserDAO;
import com.inventory.model.User;
import com.inventory.utility.DBConnection;

import java.sql.*;

public class UserDAOImpl implements UserDAO {

    @Override
    public void addUser(User user) throws SQLException {
        String sql = "INSERT INTO users (name, password, role, email, is_verified) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, user.getUserName());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getRole());
            ps.setString(4, user.getEmail());
            ps.setBoolean(5, user.is_verified());
            ps.executeUpdate();

        } catch (SQLIntegrityConstraintViolationException e) {
            System.err.println("⚠️ Duplicate username or email: " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("Error adding user: " + e.getMessage());
            throw e;
        }
    }

    @Override
    public User getUserByUsername(String username) throws SQLException {
        String sql = "SELECT * FROM users WHERE name = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new User(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getString("password"),
                            rs.getString("role"),
                            rs.getString("email"),
                            rs.getBoolean("is_verified"));
                }
            }
        }
        return null;
    }

    @Override
    public User getUserByEmail(String email) throws SQLException {
        String sql = "SELECT * FROM users WHERE email = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new User(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getString("password"),
                            rs.getString("role"),
                            rs.getString("email"),
                            rs.getBoolean("is_verified"));
                }
            }
        }
        return null;
    }

    @Override
    public void verifyUser(String email) throws SQLException {
        updateVerificationStatus(email);
    }

    public boolean verifyEmail(String email) throws SQLException {
        return updateVerificationStatus(email);
    }

    private boolean updateVerificationStatus(String email) throws SQLException {
        String sql = "UPDATE users SET is_verified = 1 WHERE email = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email);
            int rows = ps.executeUpdate();
            return rows > 0;
        }
    }
}
