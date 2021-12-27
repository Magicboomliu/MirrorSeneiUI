/*
package com.max.memo3.Util;

import android.app.job.JobInfo;
import android.content.ComponentName;
import android.os.PersistableBundle;

import com.max.memo3.Background.Job_Service;

import java.util.Calendar;
import java.util.concurrent.atomic.AtomicInteger;

import androidx.annotation.Nullable;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

@Deprecated
public class Custom_Scheduler_Record_JobService extends RealmObject {
    //var
    private String action;
    @PrimaryKey
    private int jobID;
    private long max_delay_ms;
    private int year;
    private int month;
    private int date;
    private int hour;
    private int minute;
    private int second;
    private static AtomicInteger requestCode_count = new AtomicInteger(1);
    public static final String ACTION_TODO_JOBSERVICE = "action";

    //func
    public Custom_Scheduler_Record_JobService() {
    }

    public Custom_Scheduler_Record_JobService(String action, int jobID, long max_delay_ms, int year, int month, int date, int hour, int minute, int second) {
        this.action = action;
        this.jobID = jobID;
        this.max_delay_ms = max_delay_ms;
        this.year = year;
        this.month = month;
        this.date = date;
        this.hour = hour;
        this.minute = minute;
        this.second = second;
    }
    public Custom_Scheduler_Record_JobService(String action, Calendar fire_time, long max_delay_ms){
        this.action = action;
        this.jobID = requestCode_count.incrementAndGet();
        this .max_delay_ms = max_delay_ms;
        year = fire_time.get(Calendar.YEAR);
        month = fire_time.get(Calendar.MONTH);
        date = fire_time.get(Calendar.DAY_OF_MONTH);
        hour = fire_time.get(Calendar.HOUR_OF_DAY);
        minute = fire_time.get(Calendar.MINUTE);
        second = fire_time.get(Calendar.SECOND);
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
    public JobInfo makeJobInfo(@Nullable PersistableBundle extras){
        PersistableBundle bundle;
        if (extras!=null){
            bundle = new PersistableBundle(extras);
        } else {
            bundle = new PersistableBundle();
        }
        bundle.putString(ACTION_TODO_JOBSERVICE,action);
        long minLatency = getFireTime().getTimeInMillis()-System.currentTimeMillis();
        JobInfo jobInfo = new JobInfo.Builder(jobID, new ComponentName(utilmax.APP_CONTEXT, Job_Service.class))
                            .setExtras(bundle)
                            .setMinimumLatency(minLatency)
                            .setOverrideDeadline(minLatency+max_delay_ms)
                            .setPersisted(true)
                            .setRequiresCharging(false)
                            .setRequiresDeviceIdle(false)
                            .setRequiresBatteryNotLow(false)
                            .setRequiresStorageNotLow(false)
                            .build();
        return jobInfo;
    }

    //dont work, dont know why
//    public JobInfo makeJobInfo_repeat(@Nullable PersistableBundle extras){
//        PersistableBundle bundle;
//        if (extras!=null){
//            bundle = new PersistableBundle(extras);
//        } else {
//            bundle = new PersistableBundle();
//        }
//        bundle.putString(ACTION_TODO_JOBSERVICE,action);
//        long minLatency = getFireTime().getTimeInMillis()-System.currentTimeMillis();
//        JobInfo jobInfo = new JobInfo.Builder(jobID, new ComponentName(utilmax.APP_CONTEXT,Job_Service.class))
//                .setExtras(bundle)
//                .setPeriodic(minLatency)
//                .setPersisted(true)
//                .setRequiresCharging(false)
//                .setRequiresDeviceIdle(false)
//                .setRequiresBatteryNotLow(false)
//                .setRequiresStorageNotLow(false)
//                .build();
//        return jobInfo;
//    }

    public String getAction() {
        return action;
    }

    public int getJobID() {
        return jobID;
    }

    public long getMax_delay_ms() {
        return max_delay_ms;
    }

    //other auto generated getter setter

    public void setAction(String action) {
        this.action = action;
    }

    public void setJobID(int jobID) {
        this.jobID = jobID;
    }

    public void setMax_delay_ms(long max_delay_ms) {
        this.max_delay_ms = max_delay_ms;
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