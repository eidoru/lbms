package com.lbms.app.object;

public class Reserve extends Request {

    private int reserveId;
    private String startDate;
    private String endDate;

    public Reserve() {
    }

    public Reserve(int reserveId, int bookId, int userId, int duration, String startDate, String endDate) {
        super(reserveId, bookId, userId, duration);
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Reserve(int reserveId, String bookTitle, String userFirstName, String userLastName, int duration, String startDate, String endDate) {
        super(reserveId, bookTitle, userFirstName, userLastName, duration);
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public int getReserveId() {
        return reserveId;
    }

    public void setReserveId(int reserveId) {
        this.reserveId = reserveId;
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
