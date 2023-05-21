package com.lbms.app.object;

public class Reserve extends Request {

    private int reserveId;

    public Reserve() {
    }

    public Reserve(int reserveId, int bookId, int userId, int duration, String startDate, String endDate) {
        super(reserveId, bookId, userId, duration);
    }

    public Reserve(int reserveId, String bookTitle, String userFirstName, String userLastName, int duration) {
        super(reserveId, bookTitle, userFirstName, userLastName, duration);
    }

    public int getReserveId() {
        return reserveId;
    }

    public void setReserveId(int reserveId) {
        this.reserveId = reserveId;
    }
}
