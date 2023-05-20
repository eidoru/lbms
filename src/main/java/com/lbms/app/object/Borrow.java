package com.lbms.app.object;

public class Borrow extends Request {

    private String startDate;
    private String endDate;

    public Borrow() {
    }

    public Borrow(int borrowId, int bookId, int userId, int duration, String startDate, String endDate) {
        super(borrowId, bookId, userId, duration);
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Borrow(int borrowId, String bookTitle, String userFirstName, String userLastName, int duration, String startDate, String endDate) {
        super(borrowId, bookTitle, userFirstName, userLastName, duration);
        this.startDate = startDate;
        this.endDate = endDate;
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
}
