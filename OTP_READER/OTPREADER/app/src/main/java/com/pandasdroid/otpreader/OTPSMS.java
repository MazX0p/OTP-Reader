package com.pandasdroid.otpreader;

public class OTPSMS {

    String id,msg,from,number;
    int otp;

    public OTPSMS(String id, String msg, String from, String number, int otp) {
        this.id = id;
        this.msg = msg;
        this.from = from;
        this.number = number;
        this.otp = otp;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public int getOtp() {
        return otp;
    }

    public void setOtp(int otp) {
        this.otp = otp;
    }
}
