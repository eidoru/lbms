package com.lbms.app.object;

public class Request {

    private int bookId;
    private int duration;
    private int id;
    private int userId;
    private String bookTitle;
    private String userFirstName;
    private String userLastName;

    public Request() {
    }

    public Request(int requestId, int bookId, int userId, int duration) {
        this.id = requestId;
        this.bookId = bookId;
        this.userId = userId;
        this.duration = duration;
    }

    public Request(int requestId, String bookTitle, String userFirstName, String userLastName, int duration) {
        this.id = requestId;
        this.bookTitle = bookTitle;
        this.userFirstName = userFirstName;
        this.userLastName = userLastName;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }

    public String getUserFirstName() {
        return userFirstName;
    }

    public void setUserFirstName(String userFirstName) {
        this.userFirstName = userFirstName;
    }

    public String getUserLastName() {
        return userLastName;
    }

    public void setUserLastName(String userLastName) {
        this.userLastName = userLastName;
    }
}
