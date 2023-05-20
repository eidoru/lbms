package com.lbms.app.object;

public class Borrow {
    private int duration;
    private int bookId;
    private int borrowId;
    private int userId;
    private String bookTitle;
    private String startDate;
    private String endDate;
    private String userFirstName;
    private String userLastName;
    
    public Borrow() {}
    
    public Borrow(int borrowId, int bookId, int userId, int duration, String startDate, String endDate) {
        this.borrowId = borrowId;
        this.bookId = bookId;
        this.userId = userId;
        this.duration = duration;
        this.startDate = startDate;
        this.endDate = endDate;
    }
    
    public Borrow(int borrowId, String bookTitle, String userFirstName, String userLastName, int duration, String startDate, String endDate) {
        this.borrowId = borrowId;
        this.bookTitle = bookTitle;
        this.userFirstName = userFirstName;
        this.userLastName = userLastName;
        this.duration = duration;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public int getBorrowId() {
        return borrowId;
    }

    public void setBorrowId(int borrowId) {
        this.borrowId = borrowId;
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

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
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
