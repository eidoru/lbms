package com.lbms.app.database;

import com.lbms.app.object.Request;
import com.lbms.app.object.Book;
import com.lbms.app.object.Borrow;
import com.lbms.app.object.User;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Database implements DatabaseMethods {

    private Connection connect;

    private final String DB_URL = "jdbc:mysql://localhost:3306/lbms";
    private final String DB_USER = "root";
    private final String DB_PASS = "";

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

    public ArrayList<User> getUsers() {
        ArrayList<User> users = new ArrayList<>();
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            statement = connect.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM user");
            while (resultSet.next()) {
                User user = new User();
                user.setUserId(resultSet.getInt(1));
                user.setPassword(resultSet.getString(2));
                user.setFirstName(resultSet.getString(3));
                user.setLastName(resultSet.getString(4));
                user.setEmail(resultSet.getString(5));
                user.setAddress(resultSet.getString(6));
                user.setContactNumber(resultSet.getString(7));
                user.setCourse(resultSet.getString(8));
                user.setYearLevel(resultSet.getInt(9));
                user.setUserType(resultSet.getInt(10));
                users.add(user);
            }
            return users;
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
        return null;
    }

    public ArrayList<Book> getBooks() {
        ArrayList<Book> books = new ArrayList<>();
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            statement = connect.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM book");
            while (resultSet.next()) {
                Book book = new Book();
                book.setBookId(resultSet.getInt(1));
                book.setTitle(resultSet.getString(2));
                book.setAuthor(resultSet.getString(3));
                book.setIsbn(resultSet.getString(4));
                book.setStatus(resultSet.getInt(5));
                books.add(book);
            }
            return books;
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
        return null;
    }
    
    public ArrayList<Borrow> getBorrowers() {
        ArrayList<Borrow> borrowers = new ArrayList<>();
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            statement = connect.createStatement();
            resultSet = statement.executeQuery("SELECT borrow.borrow_id, book.title, user.first_name, user.last_name, borrow.duration, borrow.start_date, borrow.end_date FROM borrow INNER JOIN book ON borrow.book_id=book.book_id INNER JOIN user ON borrow.user_id=user.user_id");
            while (resultSet.next()) {
                Borrow borrow = new Borrow();
                borrow.setBorrowId(resultSet.getInt(1));
                borrow.setBookTitle(resultSet.getString(2));
                borrow.setUserFirstName(resultSet.getString(3));
                borrow.setUserLastName(resultSet.getString(4));
                borrow.setDuration(resultSet.getInt(5));
                borrow.setStartDate(resultSet.getString(6));
                borrow.setEndDate(resultSet.getString(7));
                borrowers.add(borrow);
            }
            return borrowers;
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
        return null;
    }

    public ArrayList<Request> getRequests() {
        ArrayList<Request> requests = new ArrayList<>();
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            statement = connect.createStatement();
            resultSet = statement.executeQuery("SELECT request.request_id, book.title, user.first_name, user.last_name, request.duration FROM request INNER JOIN book on request.book_id=book.book_id INNER JOIN user ON request.user_id=user.user_id");
            while (resultSet.next()) {
                Request request = new Request();
                request.setRequestId(resultSet.getInt(1));
                request.setBookTitle(resultSet.getString(2));
                request.setUserFirstName(resultSet.getString(3));
                request.setUserLastName(resultSet.getString(4));
                request.setDuration(resultSet.getInt(5));
                requests.add(request);
            }
            return requests;
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
        return null;
    }
}
