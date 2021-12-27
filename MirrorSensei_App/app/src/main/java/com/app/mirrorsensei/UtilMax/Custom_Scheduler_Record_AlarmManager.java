/*
package com.max.memo3.Util;

import android.app.PendingIntent;
import android.content.Intent;

import java.util.Calendar;
import java.util.concurrent.atomic.AtomicInteger;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

@Deprecated
public class Custom_Scheduler_Record_AlarmManager
        extends RealmObject
{
    private String action;
    @PrimaryKey
    private int requestCode;
    private int year;
    private int month;
    private int date;
    private int hour;
    private int minute;
    private int second;
    private static AtomicInteger requestCode_count = new AtomicInteger(1);

    public Custom_Scheduler_Record_AlarmManager() {
    }

    public Custom_Scheduler_Record_AlarmManager(String action, int requestCode, int year, int month, int date, int hour, int minute, int second) {
        this.action = action;
        this.requestCode = requestCode;
        this.year = year;
        this.month = month;
        this.date = date;
        this.hour = hour;
        this.minute = minute;
        this.second = second;
    }

    public Custom_Scheduler_Record_AlarmManager(String action, Calendar fire_time) {
        this.action = action;
        this.requestCode = requestCode_count.incrementAndGet();
        year = fire_time.get(Calendar.YEAR);
        month = fire_time.get(Calendar.MONTH);
        date = fire_time.get(Calendar.DAY_OF_MONTH);
        hour = fire_time.get(Calendar.HOUR_OF_DAY);
        minute = fire_time.get(Calendar.MINUTE);
        second = fire_time.get(Calendar.SECOND);
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public int getRequestCode() {
        return requestCode;
    }

    public Calendar getFireTime(){
        Calendar calendar = new Calendar.Builder().build();
        calendar.set(year,month,date,hour,minute,second);
        return calendar;
    }
    public void setFireTime(Calendar fire_time){
        year = fire_time.get(Calendar.YEAR);
        month = fire_time.get(Calendar.MONTH);
        date = fire_time.get(Calendar.DAY_OF_MONTH);
        hour = fire_time.get(Calendar.HOUR_OF_DAY);
        minute = fire_time.get(Calendar.MINUTE);
        second = fire_time.get(Calendar.SECOND);
    }
    public Intent makeIntent(){
        Intent intent = new Intent(action);
        return intent;
    }
    public PendingIntent makePendingIntent(){
        PendingIntent pendingIntent = PendingIntent.getBroadcast(utilmax.APP_CONTEXT,requestCode,makeIntent(),0);
        return pendingIntent;
    }
    public PendingIntent makePendingIntent(Intent intent){
        PendingIntent pendingIntent = PendingIntent.getBroadcast(utilmax.APP_CONTEXT,requestCode,intent,0);
        return pendingIntent;
    }


    //auto generated stuff only
    public void setRequestCode(int requestCode) {
        this.requestCode = requestCode;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDate() {
        return date;
    }

    public void setDate(int date) {
        this.date = date;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public int getSecond() {
        return second;
    }

    public void setSecond(int second) {
        this.second = second;
    }
}
*/