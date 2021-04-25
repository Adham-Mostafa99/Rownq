package com.example.pray.network;

public class PrayData extends MonthDate {
    private String[] prayName;
    private String[] prayTime;

    public PrayData(  String dayDate, String[] prayName, String[] prayTime) {
        super(dayDate);
        this.prayName = prayName;
        this.prayTime = prayTime;
    }

    public String[] getPrayName() {
        return prayName;
    }

    public String[] getPrayTime() {
        return prayTime;
    }
}
