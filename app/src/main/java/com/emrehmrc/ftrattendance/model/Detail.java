package com.emrehmrc.ftrattendance.model;

public class Detail {
    private String date;
    private int state;

    public Detail(String id, String date, int state) {
        this.date = date;
        this.state = state;
    }

    public Detail(int state) {
        this.state = state;
    }

    public Detail() {

    }


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
