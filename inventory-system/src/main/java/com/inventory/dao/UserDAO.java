package com.inventory.dao;

import com.inventory.model.User;
import java.sql.SQLException;

public interface UserDAO {

    void addUser(User user) throws SQLException;
    User getUserByUsername(String username) throws SQLException;
}
