package com.app.mirrorsensei.UtilMax;
import com.app.mirrorsensei.UtilMax.utilmax;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.PersistableBundle;
import android.widget.DatePicker;
import android.widget.TimePicker;

import java.util.Calendar;

import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
//import io.realm.Realm;
//import io.realm.RealmResults;

//all thing don't want to check in main utilmax
public class utilmaxOther {
    private static Context APP_CONTEXT;

    /** DATE TIME PICKER */
    private static Calendar calendar;
    public static Calendar getCalender(){
        return calendar;
    }
    public static void makeTimePicker(Context CURR_CONTEXT){
        final Calendar calendar_now = Calendar.getInstance();
        TimePickerDialog timePickerDialog = new TimePickerDialog(CURR_CONTEXT, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                calendar.set(Calendar.HOUR_OF_DAY,hourOfDay);
                calendar.set(Calendar.MINUTE,minute);
            }
        },calendar_now.get(Calendar.HOUR_OF_DAY),calendar_now.get(Calendar.MINUTE),true);
        timePickerDialog.show();
    }
    public static void makeTimePicker(final String action, Context CURR_CONTEXT){
        final Calendar calendar_now = Calendar.getInstance();
        TimePickerDialog timePickerDialog = new TimePickerDialog(CURR_CONTEXT, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                calendar.set(Calendar.HOUR_OF_DAY,hourOfDay);
                calendar.set(Calendar.MINUTE,minute);
                LocalBroadcastManager.getInstance(CURR_CONTEXT).sendBroadcast(new Intent(action));
            }
        },calendar_now.get(Calendar.HOUR_OF_DAY),calendar_now.get(Calendar.MINUTE),true);
        timePickerDialog.show();
    }
    public static void makeDatePicker(Context CURR_CONTEXT){
        final Calendar calendar_now = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(CURR_CONTEXT, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR,year);
                calendar.set(Calendar.MONTH,month);
                calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);
            }
        },calendar_now.get(Calendar.YEAR),calendar_now.get(Calendar.MONTH),calendar_now.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }
    public static void makeDatePicker(final String action, Context CURR_CONTEXT){
        final Calendar calendar_now = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(CURR_CONTEXT, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR,year);
                calendar.set(Calendar.MONTH,month);
                calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);
                LocalBroadcastManager.getInstance(CURR_CONTEXT).sendBroadcast(new Intent(action));
            }
        },calendar_now.get(Calendar.YEAR),calendar_now.get(Calendar.MONTH),calendar_now.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }
    public static void makeDateTimePicker(Context CURR_CONTEXT){
        makeDatePicker(CURR_CONTEXT);
        makeTimePicker(CURR_CONTEXT);
    }
/* disabled on 27-12-2021
    //SCHEDULER
    private static AlarmManager alarmManager;
    private static JobScheduler jobScheduler;
    private static void init_Scheduler(){
        alarmManager = (AlarmManager) APP_CONTEXT.getSystemService(Context.ALARM_SERVICE);
        jobScheduler = (JobScheduler) APP_CONTEXT.getSystemService(Context.JOB_SCHEDULER_SERVICE);
    }
    public static int makeSchedule_AlarmManager(Intent intent, BroadcastReceiver receiver, Calendar fire_time){
        Custom_Scheduler_Record_AlarmManager custom_scheduler_record_alarmManager = new Custom_Scheduler_Record_AlarmManager(intent.getAction(),fire_time);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(custom_scheduler_record_alarmManager.getAction());
        APP_CONTEXT.registerReceiver(receiver,intentFilter);
        PendingIntent pendingIntent = custom_scheduler_record_alarmManager.makePendingIntent(intent);
        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,fire_time.getTimeInMillis(),pendingIntent);
        return custom_scheduler_record_alarmManager.getRequestCode();
    }
    public static int makeSchedule_AlarmManager_Repeat(Intent intent, final BroadcastReceiver receiver, final Calendar fire_time, final int interval_s_int){
        Custom_Scheduler_Record_AlarmManager custom_scheduler_record_alarmManager = new Custom_Scheduler_Record_AlarmManager(intent.getAction(),fire_time);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(custom_scheduler_record_alarmManager.getAction());
        APP_CONTEXT.registerReceiver(receiver,intentFilter);
        BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                fire_time.add(Calendar.SECOND,interval_s_int);
                makeSchedule_AlarmManager_Repeat(intent,receiver,fire_time,interval_s_int);
            }
        };
        APP_CONTEXT.registerReceiver(broadcastReceiver,intentFilter);
        PendingIntent pendingIntent = custom_scheduler_record_alarmManager.makePendingIntent(intent);
        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,fire_time.getTimeInMillis(),pendingIntent);
        return custom_scheduler_record_alarmManager.getRequestCode();
    }
    private static final long DEFAULT_MAX_DELAY_JOBSERVICE = 100;
    public static int makeSchedule_JobService(String action, Calendar fire_time, @Nullable Long max_delay_ms, @Nullable PersistableBundle extras){
        if (max_delay_ms==null){
            max_delay_ms = DEFAULT_MAX_DELAY_JOBSERVICE;
        }
        Custom_Scheduler_Record_JobService record = new Custom_Scheduler_Record_JobService(action,fire_time,max_delay_ms);
        JobInfo jobInfo = record.makeJobInfo(extras);
        jobScheduler.schedule(jobInfo);
        return jobInfo.getId();
    }
    public static boolean cancelSchedule_AlarmManager(int requestCode){
//        //query it
//        final RealmResults<Custom_Scheduler_Record_AlarmManager> results =
//                realm.where(Custom_Scheduler_Record_AlarmManager.class)
//                        .equalTo("requestCode",requestCode)
//                        .findAll();
//        if (results.size()!=1){
//            return false;
//        }
//        //cancel alarm
//        PendingIntent pendingIntent = results.get(0).makePendingIntent();
//        alarmManager.cancel(pendingIntent);
//        //remove it from realm
//        realm.executeTransaction(new Realm.Transaction() {
//            @Override
//            public void execute(Realm realm) {
//                results.deleteAllFromRealm();
//            }
//        });
        return true;
    }
    public static boolean cancelSchedule_JobService(int jobID){
        jobScheduler.cancel(jobID);
        return true;
    }
*/
    /** SERVICE */
//    public static final String TEST = "test";
//    //broadcast receiver
//    public static final String IS_NEED_RECEIVER = "need_receiver";
//    public static final String ACTION_TO_RECEIVE_KEY = "actions_to_receive";
//    public static final String[] ACTION_TO_RECEIVE = {
//            ConnectivityManager.CONNECTIVITY_ACTION,
//            Intent.ACTION_POWER_CONNECTED,
//            Intent.ACTION_POWER_DISCONNECTED,
//            BluetoothDevice.ACTION_ACL_CONNECTED,
//            BluetoothDevice.ACTION_ACL_DISCONNECTED,
//            "asdf",
//    };
//    //sensor
//    public static final String IS_NEED_SENSOR = "need_sensor";
//    public static final String SENSOR_TO_SENSE_KEY = "sensor_to_sense";
//    public static final int[] SENSOR_TO_SENSE = {
////            Sensor.TYPE_AMBIENT_TEMPERATURE,  //my asus doesnt have temperature sensor
//            Sensor.TYPE_LIGHT,
//            Sensor.TYPE_ACCELEROMETER,
////            Sensor.TYPE_LINEAR_ACCELERATION,
//    };
//    public static final String IS_NEED_DATA = "need_data";
//    public static final String IS_CONSTANT_SENSOR = "constant sensor";
//    //local broadcast receiver
//    public static final String IS_NEED_LBR = "need_local_broadcast_receiver";
//    public static final String ACTION_TO_RECEIVE_LBR_KEY = "actions_for_lbr";
//    public static final String[] ACTION_TO_RECEIVE_LBR = {
//            SP_service.SERVICE_DATA_CHANGE,
//            TEST,
//    };
//
//    //service
//    private static int booToIntforService(Boolean b){
//        if (b == null){
//            return 0;
//        }
//        if (!b){
//            return 1;
//        }
//        if (b){
//            return 2;
//        }
//        return 0;
//    }
//    private static boolean isStartedService = false;
//    public static boolean isServiceStarted(){
//        return isStartedService;
//    }
//    public static void setServiceStarted(boolean b){isStartedService =b;}
//    //null == 0, false == 1, true == 2
//    public static void startService(Boolean is_need_receiver, String[] extra_action_br,
//                                    Boolean is_need_sensor,int[] extra_sensor_type, Boolean is_need_data, Boolean is_constant_sensor,
//                                    Boolean is_need_lbr, String[] extra_action_lbr){
////        if (isStartedService){return;}
//        Intent intent = new Intent(APP_CONTEXT,SP_service.class);
//        //receiver
//        if (is_need_receiver) {
//            intent.putExtra(IS_NEED_RECEIVER,2);
//            intent.putExtra(ACTION_TO_RECEIVE_KEY, extra_action_br);
//        } else {
//            intent.putExtra(IS_NEED_RECEIVER,1);
//        }
//        //sensor
//        if (is_need_sensor) {
//            intent.putExtra(IS_NEED_SENSOR,2);
//            intent.putExtra(SENSOR_TO_SENSE_KEY, extra_sensor_type);
//            intent.putExtra(IS_NEED_DATA,booToIntforService(is_need_data));
//            intent.putExtra(IS_CONSTANT_SENSOR,booToIntforService(is_constant_sensor));
//        } else {
//            intent.putExtra(IS_NEED_SENSOR,1);
//        }
//        //local broadcast receiver
//        if (is_need_lbr) {
//            intent.putExtra(IS_NEED_LBR,2);
//            intent.putExtra(ACTION_TO_RECEIVE_LBR_KEY, extra_action_lbr);
//        } else {
//            intent.putExtra(IS_NEED_LBR,1);
//        }
//
//        intent.putExtra(TEST,"received test");
//        APP_CONTEXT.startService(intent);
//        isStartedService = true;
//    }
//    public static void startService(){
//        startService(true,null,true,null,false,true,true,null);
//    }
//    public static boolean stopService(){
//        if (!isStartedService){
//            utilmax.makeToastLog("service not started, cant stop");
//            return false;
//        }
//        APP_CONTEXT.stopService(new Intent(APP_CONTEXT,SP_service.class));
//        utilmax.makeToastLog("service stopped");
//        isStartedService = false;
//        return true;
//    }
//    public static boolean sendBroadcast(String action){
//        Intent intent = new Intent(action);
//        APP_CONTEXT.sendBroadcast(intent);
//        return true;
//    }
//    public static boolean sendBroadcast(Intent intent){
//        APP_CONTEXT.sendBroadcast(intent);
//        return true;
//    }
}
