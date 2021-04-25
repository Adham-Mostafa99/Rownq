package com.example.pray.database;

public final class PrayContract  {
    public PrayContract() {
    }

    public static final class MonthPrayEntry{
        public static final String TABLE_NAME="month_pray";
        public static final String DAY_NUMBER="day_number";
        public static final String DATE="date";

    }
    public static final class DayPrayEntry{
        public static final String TABLE_NAME="day_pray";
        public static final String DAY_NUMBER="day_number";
        public static final String Name_Prayer="name";
        public static final String TIME="time";

    }
}
