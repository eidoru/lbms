package com.lbms.app.database;

import com.lbms.app.object.Book;
import com.lbms.app.object.Borrow;
import com.lbms.app.object.Overdue;
import com.lbms.app.object.Reserve;
import com.lbms.app.object.Request;
import com.lbms.app.object.User;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
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

    @Override
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

    @Override
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

    @Override
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

    @Override
    public ArrayList<Borrow> getBorrowers() {
        ArrayList<Borrow> borrowers = new ArrayList<>();
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            statement = connect.createStatement();
            resultSet = statement.executeQuery("SELECT borrow.borrow_id, book.title, user.first_name, user.last_name, borrow.duration, borrow.start_date, borrow.end_date FROM borrow INNER JOIN book ON borrow.book_id=book.book_id INNER JOIN user ON borrow.user_id=user.user_id");
            while (resultSet.next()) {
                Borrow borrow = new Borrow();
                borrow.setId(resultSet.getInt(1));
                borrow.setBookTitle(resultSet.getString(2));
                borrow.setUserFirstName(resultSet.getString(3));
                borrow.setUserLastName(resultSet.getString(4));
                borrow.setDuration(resultSet.getInt(5));
                borrow.setStartDate(resultSet.getDate(6).toString());
                borrow.setEndDate(resultSet.getDate(7).toString());
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

    @Override
    public ArrayList<Request> getRequests() {
        ArrayList<Request> requests = new ArrayList<>();
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            statement = connect.createStatement();
            resultSet = statement.executeQuery("SELECT request.request_id, book.title, user.first_name, user.last_name, request.duration FROM request INNER JOIN book on request.book_id=book.book_id INNER JOIN user ON request.user_id=user.user_id");
            while (resultSet.next()) {
                Request request = new Request();
                request.setId(resultSet.getInt(1));
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

    @Override
    public ArrayList<Overdue> getOverdues() {
        ArrayList<Overdue> overdues = new ArrayList<>();
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            statement = connect.createStatement();
            resultSet = statement.executeQuery("SELECT overdue.overdue_id, book.title, user.first_name, user.last_name, overdue.fine FROM overdue INNER JOIN book ON overdue.book_id=book.book_id INNER JOIN user ON overdue.user_id=user.user_id");
            while (resultSet.next()) {
                Overdue overdue = new Overdue();
                overdue.setOverdueId(resultSet.getInt(1));
                overdue.setBookTitle(resultSet.getString(2));
                overdue.setUserFirstName(resultSet.getString(3));
                overdue.setUserLastName(resultSet.getString(4));
                overdue.setFine(resultSet.getDouble(5));
                overdues.add(overdue);
            }
            return overdues;
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

    public ArrayList<Overdue> getUserOverdues(int userId) {
        ArrayList<Overdue> overdues = new ArrayList<>();
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            statement = connect.createStatement();
            resultSet = statement.executeQuery("SELECT overdue.overdue_id, book.title, user.first_name, user.last_name, overdue.fine FROM overdue INNER JOIN book ON overdue.book_id=book.book_id INNER JOIN user ON overdue.user_id=user.user_id WHERE overdue.user_id='" + userId + "'");
            while (resultSet.next()) {
                Overdue overdue = new Overdue();
                overdue.setOverdueId(resultSet.getInt(1));
                overdue.setBookTitle(resultSet.getString(2));
                overdue.setUserFirstName(resultSet.getString(3));
                overdue.setUserLastName(resultSet.getString(4));
                overdue.setFine(resultSet.getDouble(5));
                overdues.add(overdue);
            }
            return overdues;
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

    public ArrayList<Borrow> getUserBorrows(int userId) {
        ArrayList<Borrow> borrowers = new ArrayList<>();
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            statement = connect.createStatement();
            resultSet = statement.executeQuery("SELECT borrow.borrow_id, book.title, borrow.start_date, borrow.end_date FROM borrow INNER JOIN book ON borrow.book_id=book.book_id WHERE borrow.user_id='" + userId + "'");
            while (resultSet.next()) {
                Borrow borrow = new Borrow();
                borrow.setId(resultSet.getInt(1));
                borrow.setBookTitle(resultSet.getString(2));
                borrow.setStartDate(resultSet.getDate(3).toString());
                borrow.setEndDate(resultSet.getDate(4).toString());
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

    @Override
    public int getTableId(String table, String id) {
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            statement = connect.createStatement();
            resultSet = statement.executeQuery("SELECT MAX(" + id + ") FROM " + table);
            if (resultSet.next()) {
                return resultSet.getInt(1) + 1;
            } else {
                return 1;
            }
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
        return 0;
    }

    public User getCurrentUser(int userId) {
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            statement = connect.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM user WHERE user_id = '" + userId + "'");
            if (resultSet.next()) {
                User user = new User();
                user.setUserId(userId);
                user.setPassword(resultSet.getString(2));
                user.setFirstName(resultSet.getString(3));
                user.setLastName(resultSet.getString(4));
                user.setEmail(resultSet.getString(5));
                user.setAddress(resultSet.getString(6));
                user.setContactNumber(resultSet.getString(7));
                user.setCourse(resultSet.getString(8));
                user.setYearLevel(resultSet.getInt(9));
                user.setUserType(resultSet.getInt(10));
                return user;
            } else {
                return null;
            }

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

    public Request getRequest(int requestId) {
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            statement = connect.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM request WHERE request_id = '" + requestId + "'");
            if (resultSet.next()) {
                Request request = new Request();
                request.setId(resultSet.getInt(1));
                request.setBookId(resultSet.getInt(2));
                request.setUserId(resultSet.getInt(3));
                request.setDuration(resultSet.getInt(4));
                return request;
            }
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

    @Override
    public void resetAutoIncrement(String table) {
        Statement statement = null;
        try {
            statement = connect.createStatement();
            statement.executeUpdate("ALTER TABLE " + table + " AUTO_INCREMENT = 1");
        } catch (SQLException e) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException e) {
                Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, e);
            }
        }
    }

    @Override
    public boolean insertUser(User user) {
        PreparedStatement statement = null;
        String SQL;
        try {
            resetAutoIncrement("user");
            SQL = "INSERT INTO user (password, first_name, last_name, email, address, contact_number, course, year_level, user_type) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)";
            statement = connect.prepareStatement(SQL);
            statement.setString(1, user.getPassword());
            statement.setString(2, user.getFirstName());
            statement.setString(3, user.getLastName());
            statement.setString(4, user.getEmail());
            statement.setString(5, user.getAddress());
            statement.setString(6, user.getContactNumber());
            statement.setString(7, user.getCourse());
            statement.setInt(8, user.getYearLevel());
            statement.setInt(9, user.getUserType());
            statement.executeUpdate();
        } catch (SQLException e) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException e) {
                Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, e);
            }
        }
        return false;
    }

    @Override
    public boolean insertBook(Book book) {
        PreparedStatement statement = null;
        String SQL;
        try {
            resetAutoIncrement("book");
            SQL = "INSERT INTO book (title, author, isbn, status) VALUES(?, ?, ?, ?)";
            statement = connect.prepareStatement(SQL);
            statement.setString(1, book.getTitle());
            statement.setString(2, book.getAuthor());
            statement.setString(3, book.getIsbn());
            statement.setInt(4, book.getStatus());
            statement.executeUpdate();
        } catch (SQLException e) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException e) {
                Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, e);
            }
        }
        return false;
    }

    public boolean insertOverdue(Overdue overdue) {
        PreparedStatement statement = null;
        String SQL;
        try {
            resetAutoIncrement("overdue");
            SQL = "INSERT INTO overdue (book_id, user_id, fine) VALUES(?, ?, ?)";
            statement = connect.prepareStatement(SQL);
            statement.setInt(1, overdue.getBookId());
            statement.setInt(2, overdue.getUserId());
            statement.setDouble(3, overdue.getFine());
            statement.executeUpdate();
        } catch (SQLException e) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException e) {
                Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, e);
            }
        }
        return false;
    }

    public <E> boolean insertRequest(E request, String table) {
        PreparedStatement statement = null;
        String SQL;
        try {
            SQL = "INSERT INTO ";
            switch (table) {
                case "request":
                    resetAutoIncrement("request");
                    SQL += "request (book_id, user_id, duration) VALUES(?, ?, ?)";
                    statement = connect.prepareStatement(SQL);
                    statement.setInt(1, ((Request) request).getBookId());
                    statement.setInt(2, ((Request) request).getUserId());
                    statement.setInt(3, ((Request) request).getDuration());
                    break;
                case "borrow":
                    resetAutoIncrement("borrow");
                    SQL += "borrow (book_id, user_id, duration, start_date, end_date) VALUES(?, ?, ?, ?, ?)";
                    statement = connect.prepareStatement(SQL);
                    statement.setInt(1, ((Borrow) request).getBookId());
                    statement.setInt(2, ((Borrow) request).getUserId());
                    statement.setInt(3, ((Borrow) request).getDuration());
                    statement.setString(4, ((Borrow) request).getStartDate());
                    statement.setString(5, ((Borrow) request).getEndDate());
                    break;
                case "reserve":
                    resetAutoIncrement("reserve");
                    SQL += "reserve (book_id, user_id, duration) VALUES(?, ?, ?)";
                    statement = connect.prepareStatement(SQL);
                    statement.setInt(1, ((Reserve) request).getBookId());
                    statement.setInt(2, ((Reserve) request).getUserId());
                    statement.setInt(3, ((Reserve) request).getDuration());
                    break;
            }
            statement.executeUpdate();
        } catch (SQLException e) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException e) {
                Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, e);
            }
        }
        return false;
    }

    public boolean deleteUser(int id) {
        PreparedStatement select = null;
        PreparedStatement delete = null;
        String SQL;
        try {
            SQL = "SELECT user_id FROM user WHERE user_id = ?";
            select = connect.prepareStatement(SQL);
            select.setInt(1, id);
            ResultSet resultSet = select.executeQuery();
            if (resultSet.next()) {
                SQL = "DELETE FROM user WHERE user_id = ?";
                delete = connect.prepareStatement(SQL);
                delete.setInt(1, id);
                delete.executeUpdate();
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            try {
                if (select != null) {
                    select.close();
                }
                if (delete != null) {
                    delete.close();
                }
            } catch (SQLException e) {
                Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, e);
            }
        }
        return false;
    }

    public boolean deleteBook(int id) {
        PreparedStatement select = null;
        PreparedStatement delete = null;
        String SQL;
        try {
            SQL = "SELECT book_id FROM book WHERE book_id = ?";
            select = connect.prepareStatement(SQL);
            select.setInt(1, id);
            ResultSet resultSet = select.executeQuery();
            if (resultSet.next()) {
                SQL = "DELETE FROM book WHERE book_id = ?";
                delete = connect.prepareStatement(SQL);
                delete.setInt(1, id);
                delete.executeUpdate();
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            try {
                if (select != null) {
                    select.close();
                }
                if (delete != null) {
                    delete.close();
                }
            } catch (SQLException e) {
                Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, e);
            }
        }
        return false;
    }

    public boolean deleteRequest(String table, int requestId) {
        PreparedStatement statement = null;
        String SQL;
        try {
            SQL = "DELETE FROM ";
            switch (table) {
                case "request":
                    resetAutoIncrement("request");
                    SQL += "request WHERE request_id = ?";
                    statement = connect.prepareStatement(SQL);
                    statement.setInt(1, requestId);
                    break;
                case "borrow":
                    resetAutoIncrement("borrow");
                    SQL += "borrow WHERE borrow_id = ?";
                    statement = connect.prepareStatement(SQL);
                    statement.setInt(1, requestId);
                    break;
                case "reserve":
                    resetAutoIncrement("reserve");
                    SQL += "reserve WHERE reserve_id = ?";
                    statement = connect.prepareStatement(SQL);
                    statement.setInt(1, requestId);
                    break;
            }
            statement.executeUpdate();
            return true;
        } catch (SQLException e) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException e) {
                Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, e);
            }
        }
        return false;
    }

    public boolean updateUser(User user) {
        PreparedStatement statement = null;
        String SQL;
        try {
            resetAutoIncrement("user");
            SQL = "UPDATE user SET password = ?, first_name = ?, last_name = ?, email = ?, address = ?, contact_number = ? WHERE user_id = ?";
            statement = connect.prepareStatement(SQL);
            statement.setString(1, user.getPassword());
            statement.setString(2, user.getFirstName());
            statement.setString(3, user.getLastName());
            statement.setString(4, user.getEmail());
            statement.setString(5, user.getAddress());
            statement.setString(6, user.getContactNumber());
            statement.setInt(7, user.getUserId());
            statement.executeUpdate();
        } catch (SQLException e) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException e) {
                Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, e);
            }
        }
        return false;
    }

    public void sendBookRequest(int book_id, int user_id, int duration) {
        Statement statement = null;
        String SQL;
        try {
            resetAutoIncrement("book");
            statement = connect.createStatement();
            SQL = "INSERT INTO request (book_id, user_id, duration) VALUES ('" + book_id + "', '" + user_id + "', '" + duration + "')";
            statement.executeUpdate(SQL);
        } catch (SQLException e) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException e) {
                Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, e);
            }
        }
    }

    public void sendBookReservation(int book_id, int user_id, int Duration) {
        String sql;
        Statement stmt;
        ResultSet rs;

        try {
            stmt = connect.createStatement();
            sql = "insert into reserve (book_id, user_id, Duration) values (" + book_id + ", " + user_id + ", " + Duration + ")";
            stmt.executeUpdate(sql);
        } catch (SQLException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public ArrayList<User> displayUserSearch(String[] keywords) {
        ArrayList<User> users = new ArrayList<>();
        int listSize = keywords.length;
        String SQL = "SELECT * FROM user WHERE ";
        for (int i = 0; i < listSize; i++) {
            String word = keywords[i];
            SQL += "LOWER(first_name) LIKE '%" + word + "%' OR ";
        }
        for (int i = 0; i < listSize; i++) {
            String word = keywords[i];
            SQL += "LOWER(last_name) LIKE '%" + word + "%' OR ";
        }
        for (int i = 0; i < listSize; i++) {
            String word = keywords[i];
            SQL += "LOWER(email) LIKE '%" + word + "%'";
            if (i < listSize - 1) {
                SQL += " OR ";
            }
        }
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            statement = connect.createStatement();
            resultSet = statement.executeQuery(SQL);
            while (resultSet.next()) {
                User user = new User();
                user.setUserId(resultSet.getInt(1));
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

    public ArrayList<Book> displayBookSearch(String[] keywords) {
        ArrayList<Book> books = new ArrayList<>();
        int listSize = keywords.length;
        String SQL = "SELECT * FROM book WHERE ";
        for (int i = 0; i < listSize; i++) {
            String word = keywords[i];
            SQL += "LOWER(title) LIKE '%" + word + "%' OR ";
        }
        for (int i = 0; i < listSize; i++) {
            String word = keywords[i];
            SQL += "LOWER(author) LIKE '%" + word + "%'";
            if (i < listSize - 1) {
                SQL += " OR ";
            }
        }
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            statement = connect.createStatement();
            resultSet = statement.executeQuery(SQL);
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

    public ArrayList<Request> displayRequestSearch(String[] keywords) {
        ArrayList<Request> requests = new ArrayList<>();
        int listSize = keywords.length;
        String SQL = "SELECT request.request_id, book.title, user.first_name, user.last_name, request.duration "
                + "FROM request "
                + "INNER JOIN book ON request.book_id = book.book_id "
                + "INNER JOIN user ON request.user_id = user.user_id "
                + "WHERE ";
        for (int i = 0; i < listSize; i++) {
            String word = keywords[i];
            SQL += "LOWER(book.title) LIKE '%" + word + "%' OR ";
        }
        for (int i = 0; i < listSize; i++) {
            String word = keywords[i];
            SQL += "LOWER(user.first_name) LIKE '%" + word + "%' OR ";
        }
        for (int i = 0; i < listSize; i++) {
            String word = keywords[i];
            SQL += "LOWER(user.last_name) LIKE '%" + word + "%'";
            if (i < listSize - 1) {
                SQL += " OR ";
            }
        }
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            statement = connect.createStatement();
            resultSet = statement.executeQuery(SQL);
            while (resultSet.next()) {
                Request request = new Request();
                request.setId(resultSet.getInt(1));
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

    public ArrayList<Borrow> displayBorrowSearch(String[] keywords) {
        ArrayList<Borrow> borrowers = new ArrayList<>();
        int listSize = keywords.length;
        String SQL = "SELECT borrow.borrow_id, book.title, user.first_name, user.last_name, borrow.duration, borrow.start_date, borrow.end_date "
                + "FROM borrow "
                + "INNER JOIN book ON borrow.book_id = book.book_id "
                + "INNER JOIN user ON borrow.user_id = user.user_id "
                + "WHERE ";
        for (int i = 0; i < listSize; i++) {
            String word = keywords[i];
            SQL += "LOWER(book.title) LIKE '%" + word + "%' OR ";
        }
        for (int i = 0; i < listSize; i++) {
            String word = keywords[i];
            SQL += "LOWER(user.first_name) LIKE '%" + word + "%' OR ";
        }
        for (int i = 0; i < listSize; i++) {
            String word = keywords[i];
            SQL += "LOWER(user.last_name) LIKE '%" + word + "%'";
            if (i < listSize - 1) {
                SQL += " OR ";
            }
        }
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            statement = connect.createStatement();
            resultSet = statement.executeQuery(SQL);
            while (resultSet.next()) {
                Borrow borrow = new Borrow();
                borrow.setId(resultSet.getInt(1));
                borrow.setBookTitle(resultSet.getString(2));
                borrow.setUserFirstName(resultSet.getString(3));
                borrow.setUserLastName(resultSet.getString(4));
                borrow.setDuration(resultSet.getInt(5));
                borrow.setStartDate(resultSet.getDate(6).toString());
                borrow.setEndDate(resultSet.getDate(7).toString());
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

    public ArrayList<Overdue> displayOverdueSearch(String[] keywords) {
        ArrayList<Overdue> overdues = new ArrayList<>();
        int listSize = keywords.length;
        String SQL = "SELECT overdue.overdue_id, book.title, user.first_name, user.last_name, overdue.fine "
                + "FROM overdue "
                + "INNER JOIN book ON overdue.book_id = book.book_id "
                + "INNER JOIN user ON overdue.user_id = user.user_id "
                + "WHERE ";
        for (int i = 0; i < listSize; i++) {
            String word = keywords[i];
            SQL += "LOWER(book.title) LIKE '%" + word + "%' OR ";
        }
        for (int i = 0; i < listSize; i++) {
            String word = keywords[i];
            SQL += "LOWER(user.first_name) LIKE '%" + word + "%' OR ";
        }
        for (int i = 0; i < listSize; i++) {
            String word = keywords[i];
            SQL += "LOWER(user.last_name) LIKE '%" + word + "%'";
            if (i < listSize - 1) {
                SQL += " OR ";
            }
        }
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            statement = connect.createStatement();
            resultSet = statement.executeQuery(SQL);
            while (resultSet.next()) {
                Overdue overdue = new Overdue();
                overdue.setOverdueId(resultSet.getInt(1));
                overdue.setBookTitle(resultSet.getString(2));
                overdue.setUserFirstName(resultSet.getString(3));
                overdue.setUserLastName(resultSet.getString(4));
                overdue.setFine(resultSet.getDouble(5));
                overdues.add(overdue);
            }
            return overdues;
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

    public boolean hasRequest(int bookId) {
        Statement statement = null;
        ResultSet resultSet = null;
        String SQL;
        try {
            statement = connect.createStatement();
            SQL = "select * from request where book_id ='" + bookId + "'";

            resultSet = statement.executeQuery(SQL);
            return resultSet.next();
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
        return false;
    }

    public boolean hasUserMadeRequest(int userId, String table, int bookId) {
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            statement = connect.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM " + table + " WHERE user_id = '" + userId + "' AND book_id = '" + bookId + "'");
            if (resultSet.next()) {
                return true;
            }
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
        return false;
    }
    

    public boolean updateBookStatus(int bookId) {
        PreparedStatement statement = null;
        String SQL;
        try {
            resetAutoIncrement("user");
            SQL = "UPDATE book SET status = ? WHERE book_id = ?";
            statement = connect.prepareStatement(SQL);
            statement.setInt(1, 1);
            statement.setInt(2, bookId);
            statement.executeUpdate();
        } catch (SQLException e) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException e) {
                Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, e);
            }
        }
        return false;
    }

    public void returnBook(int borrowId) {
        Statement statement = null;
        ResultSet resultSet = null;
        String SQL;
        try {
            statement = connect.createStatement();
            SQL = "SELECT * FROM borrow WHERE borrow_id = '" + borrowId + "'";

            resultSet = statement.executeQuery(SQL);

            int bookId = -1;
            int userId = -1;
            LocalDate endDate = null;
            LocalDate today = LocalDate.now();
            if (resultSet.next()) {
                bookId = resultSet.getInt(2);
                userId = resultSet.getInt(3);
                endDate = LocalDate.parse(resultSet.getDate(6).toString());
            }

            SQL = "UPDATE book SET status = '0' WHERE book_id = '" + bookId + "'";
            statement.executeUpdate(SQL);

            SQL = "DELETE FROM borrow WHERE borrow_id = '" + borrowId + "'";
            statement.executeUpdate(SQL);

            if (today.isAfter(endDate)) {
                double fine = ChronoUnit.DAYS.between(endDate, today) * 10;
                SQL = "INSERT INTO overdue(book_id, user_id, fine) VALUES('" + bookId + "', '" + userId + "', '" + fine + "')";
                statement.executeUpdate(SQL);
            }

            SQL = "SELECT * FROM reserve WHERE book_id = '" + bookId + "'";
            resultSet = statement.executeQuery(SQL);

            if (resultSet.next()) {
                int rid = resultSet.getInt(1);
                int nextuser = resultSet.getInt(3);
                int duration = resultSet.getInt(4);
                SQL = "INSERT INTO request(book_id, user_id, duration) values('" + bookId + "', '" + nextuser + "', '" + duration + "')";
                statement.executeUpdate(SQL);
                SQL = "DELETE INTO reserve where reserve_id = '" + rid + "'";
                statement.executeUpdate(SQL);
            }

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
    }
    
    public boolean checkForReserveAfterReject(int bookId) {
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            statement = connect.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM reserve WHERE book_id = '" + bookId + "' ORDER BY reserve.reserve_id ASC LIMIT 1");
            if (resultSet.next()) {
                int userId = resultSet.getInt(3);
                int duration = resultSet.getInt(4);
                statement.executeUpdate("INSERT INTO request (book_id, user_id, duration) VALUES('" + bookId + "', '" + userId + "', '" + duration + "')");
                resultSet = statement.executeQuery("SELECT * FROM reserve WHERE book_id = '" + bookId + "' ORDER BY reserve.reserve_id ASC LIMIT 1");
                statement.executeUpdate("DELETE FROM reserve WHERE book_id = '" + bookId + "' LIMIT 1");
                return true;
            }
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
        return false;
    }
}
