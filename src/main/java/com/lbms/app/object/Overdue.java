package com.lbms.app.object;

public class Overdue {
    private int overdueId;
    private int bookId;
    private int userId;
    private String bookTitle;
    private String userFirstName;
    private String userLastName;
    private double fine;

    public Overdue() {
    }

    public Overdue(int overdueId, int bookId, int userId, double fine) {
        this.overdueId = overdueId;
        this.bookId = bookId;
        this.userId = userId;
        this.fine = fine;
    }
    
    public Overdue(int overdueId, String bookTitle, String userFirstName, String userLastName, double fine) {
        this.overdueId = overdueId;
        this.bookTitle = bookTitle;
        this.userFirstName = userFirstName;
        this.userLastName = userLastName;
        this.fine = fine;
    }
    
    public Overdue(int overdueId, String bookTitle, double fine) {
        this.overdueId = overdueId;
        this.bookTitle = bookTitle;
        this.fine = fine;
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

    public int getOverdueId() {
        return overdueId;
    }

    public void setOverdueId(int overdueId) {
        this.overdueId = overdueId;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public double getFine() {
        return fine;
    }

    public void setFine(double fine) {
        this.fine = fine;
    }
}
