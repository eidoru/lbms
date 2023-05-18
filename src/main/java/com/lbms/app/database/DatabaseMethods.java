package com.lbms.app.database;

import com.lbms.app.object.Book;
import com.lbms.app.object.Request;
import com.lbms.app.object.User;
import java.util.ArrayList;

public interface DatabaseMethods {
    public int login(int userId, String password);
    public ArrayList<User> getUsers();
    public ArrayList<Book> getBooks();
    public ArrayList<Request> getRequests();
}
