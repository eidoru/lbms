package com.lbms.app.object;

public class Request {
    private int bookId;
    private int duration;
    private int requestId;
    private int userId;
    
    public Request() {}
    
    public Request(int requestId, int bookId, int userId, int duration) {
        this.requestId = requestId;
        this.bookId = bookId;
        this.userId = userId;
        this.duration = duration;
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

    public int getRequestId() {
        return requestId;
    }

    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
    
    
}
