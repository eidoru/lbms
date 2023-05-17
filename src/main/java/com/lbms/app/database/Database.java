package com.lbms.app.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Database {
    private Connection connect;
    
    private final String DB_URL     = "jdbc:mysql://localhost:3306/lbms";
    private final String DB_USER    = "root";
    private final String DB_PASS    = "";
    
    public Database() {
        try {
            connect = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
        } catch (SQLException e) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, e);
        }
    }
    
    public int login(int userId, String password) {
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            statement = connect.createStatement();
            resultSet = statement.executeQuery("SELECT user_id, password, user_type FROM user WHERE user_id = '" + userId + "' AND password = '" + password + "'");
            if (!resultSet.next()) {
                return -1;
            }
            return resultSet.getInt(3);
        } catch (SQLException e) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
                if (resultSet != null) {
                    resultSet.close();
                }
            } catch (SQLException e) {
                Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, e);
            }
        }
        return -1;
    }
}