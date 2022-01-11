package com.app.mirrorsensei.UtilMax;

import android.Manifest;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.BatteryManager;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.util.Log;
import android.util.Size;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

//import com.google.android.gms.auth.api.signin.GoogleSignIn;
//import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
//import com.google.android.gms.auth.api.signin.GoogleSignInClient;
//import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
//import com.google.android.gms.common.api.ApiException;
//import com.google.android.gms.tasks.OnCompleteListener;
//import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
//import com.google.firebase.auth.AuthCredential;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;
//import com.google.firebase.auth.GoogleAuthProvider;
//import com.google.firebase.firestore.FirebaseFirestore;
//import com.max.memo3.BR;
//import com.max.memo3.Background.MEMO3_BroadcastReceiver;
//import com.max.memo3.Background.MEMO3_Service;
//import com.max.memo3.Confidential.Conf_Info;
//import com.max.memo3.MainActivity;
//import com.max.memo3.R;
//import com.max.memo3.TestSubject.Test7_Background;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
//import io.realm.Realm;
//import io.realm.RealmConfiguration;

//all copy from MEMO3
//firebase and google signin disabled

//TOAST, snackbar, vibrator, notification, alarm manager & date time, log, exception
public final class utilmax {
    /** BASE */
    public static Context APP_CONTEXT;
    public static FragmentActivity CURR_ACTIVITY;   //fragment act || act
    public static Context CURR_CONTEXT;
    public static View CURR_VIEW;  //view || view group
    private static boolean AppContextSeted = false;

    public static void setAppContext(Context context) {
        if (!AppContextSeted) {
            APP_CONTEXT = context;
            AppContextSeted = true;
        }
    }

    public static void setCurrActivity(FragmentActivity activity) {
        CURR_ACTIVITY = activity;
        CURR_CONTEXT = activity;
        CURR_VIEW = (ViewGroup) ((ViewGroup) activity.findViewById(android.R.id.content)).getChildAt(0);
    }

    /** LIFECYCLE */
    private static boolean isInited = false;
    public static boolean getIsInited(){return isInited;}

    public static void INIT() {
        if (isInited) {
            return;
        } else {
            //Toast no init
            //Log no init
            //Toast + Log no init
            //Snackbar no init
//            init_Notification();
            init_Vibrator();
            //Scheduler no init
//            init_WorkManager();
            //Calendar + Time no init
            init_Camera();
            //Thread no init
            //Easy Data Transfer no init
//            init_LocalBroadcastReceiver();
//            init_Realm();
//            init_Google();
//            init_Firebase();
//            init_Firestore();
            init_Service();
            //Glide, Alert Dialog, Sensor not implemented
            init_Network();
            init_File();
            init_Preferences();
            //Sharing no init
            //Device Data no init

            isInited = true;
        }
    }

    public static void PAUSE() {
        stopToast();
        stopSnackbar();
        stopVibrator();
    }

    public static void DESTROY() {
        destroyCamera();
//        destroyRealm();
    }

    /** TOAST */
    private static Toast TOAST;

    public static void makeToast_full(Context context, CharSequence theThing, int duration) {
        TOAST = Toast.makeText(context, theThing != null ? theThing : "Null", duration);
        TOAST.show();
    }

    public static void makeToast(Context context, CharSequence theThing) {
        stopToast();
        makeToast_full(context, theThing, Toast.LENGTH_SHORT);
    }

    public static void makeToast(CharSequence theThing) {
        makeToast(APP_CONTEXT, theThing);
    }

    public static void makeToast_diffActivity(FragmentActivity activity, CharSequence theThing) {
        activity.runOnUiThread(() -> makeToast(theThing));
    }

    public static void makeToast_later(FragmentActivity activityToRunOn, CharSequence theThing, long delay) {
        makeSchedule_short(delay, () -> makeToast_diffActivity(activityToRunOn, theThing));
    }

    public static void makeToast_repeat(FragmentActivity activity, CharSequence theThing, long interval, int repeatForXTime) {
        makeSchedule_long(0, () -> makeToast_diffActivity(activity, theThing), interval, repeatForXTime);
    }

    public static void stopToast() {
        if (TOAST == null) {
            return;
        }
        TOAST.cancel();
        TOAST = null;
    }

    /** LOG */
    public static final String LOG_TAG = "gg<3";

    public enum SERIOUS_LEVEL {VERBOSE, DEBUG, INFO, WARN, ERROR, ASSERT}

    public static void makeLog(SERIOUS_LEVEL level, String message) {
        switch (level) {
            case VERBOSE:
                Log.v(LOG_TAG, message);
                break;
            case DEBUG:
                Log.d(LOG_TAG, message);
                break;
            case INFO:
                Log.i(LOG_TAG, message);
                break;
            case WARN:
                Log.w(LOG_TAG, message);
                break;
            case ERROR:
                Log.e(LOG_TAG, message);
                break;
            case ASSERT:
                Log.wtf(LOG_TAG, message);
                break;
        }
    }

    public static void log(String message) {
        makeLog(SERIOUS_LEVEL.DEBUG, message != null ? message : "Null");
    }

    public static void log(int message) {
        log(Integer.toString(message));
    }

    public static void log(long message) {
        log(Long.toString(message));
    }

    public static void log(boolean message) {
        log(message ? "TRUE" : "FALSE");
    }

    public static void log(Object message) {
        log(message.toString());
    }

    public static void log() {
        log("log");
    }

    /** TOAST + LOG */
    //no toast in other thread or fire later
    public static void makeToastLog(Context context, SERIOUS_LEVEL level, String message) {
        makeToast(context, message);
        makeLog(level, message);
    }

    public static void makeToastLog(SERIOUS_LEVEL level, String message) {
        makeToastLog(APP_CONTEXT, level, message);
    }

    public static void makeToastLog(Context context, String message) {
        makeToastLog(context, SERIOUS_LEVEL.DEBUG, message);
    }

    public static void makeToastLog(String message) {
        makeToastLog(APP_CONTEXT, SERIOUS_LEVEL.DEBUG, message);
    }

    /** SNACKBAR */
    //TODO custom snackbar
    private static Snackbar SNACKBAR;
    private static boolean snackbar_isShowing = false;

    public static void makeSnackbar_full(View view, CharSequence theThing, CharSequence actionStr, SnackbarOnClick snackbarOnClick, SnackbarCallback... snackbarCallbacks) {
        stopSnackbar();
        SNACKBAR = Snackbar.make(view, theThing, Snackbar.LENGTH_SHORT);
        if (actionStr != null && snackbarOnClick != null) {
            SNACKBAR.setAction(actionStr, snackbarOnClick);
        }
        SNACKBAR.addCallback(snackbar_Callback_Base);
        if (snackbarCallbacks != null) {
            for (SnackbarCallback snackbarCallback : snackbarCallbacks) {
                SNACKBAR.addCallback(snackbarCallback);
            }
        }
        SNACKBAR.show();
    }

    public static void makeSnackbar(View view, CharSequence theThing) {
        makeSnackbar_full(view, theThing, null, null, (SnackbarCallback[]) null);
    }

    public static void makeSnackbar(CharSequence theThing) {
        makeSnackbar(CURR_VIEW, theThing);
    }

    public static void stopSnackbar() {
        if (SNACKBAR != null) {
            SNACKBAR.dismiss();
            SNACKBAR = null;
            snackbar_isShowing = false;
        } else {
            snackbar_isShowing = false;
        }
    }

    public static boolean isSnackbarShowing() {
        return snackbar_isShowing;
    }

    public static abstract class SnackbarOnClick implements View.OnClickListener {
        @Override
        public abstract void onClick(View v);
    }

    public static abstract class SnackbarCallback extends Snackbar.Callback {
        @Override
        public abstract void onShown(Snackbar sb);

        @Override
        public abstract void onDismissed(Snackbar transientBottomBar, int event);
    }

    private static final Snackbar.Callback snackbar_Callback_Base = new SnackbarCallback() {
        @Override
        public void onShown(Snackbar sb) {
            snackbar_isShowing = true;
//            log("snackbar show");
        }

        @Override
        public void onDismissed(Snackbar transientBottomBar, int event) {
            snackbar_isShowing = false;
//            log("snackbar dismiss");
        }
    };
/* disabled on 27-12-2021
    //NOTIFICATION
    private static NotificationManagerCompat notiManager;
    public static final String NOTI_CHANNELID_DEFAULT = "MEMO3_NOTICHANNEL_ID";
    private static final String NOTI_ID_KEY_IN_EXTRAS = "noti_id_key_in_extras";
    private static AtomicInteger notiCount = new AtomicInteger(1);

    private static void init_Notification() {
        //get noti manager
        notiManager = NotificationManagerCompat.from(APP_CONTEXT);

        //make default channel
        NotificationChannel defaultChannel = new NotificationChannel(NOTI_CHANNELID_DEFAULT, "Default Channel", NotificationManager.IMPORTANCE_HIGH);
        defaultChannel.setDescription("Default Notification Channel for MEMO3");
        defaultChannel.enableVibration(true);
        defaultChannel.enableLights(false);
        defaultChannel.setLockscreenVisibility(NotificationCompat.VISIBILITY_PUBLIC);
        notiManager.createNotificationChannel(defaultChannel);
    }

    public static void makeNotification(NotiBuilder notiBuilder) {
        notiManager.notify(notiBuilder.notiID, notiBuilder.build());
    }

    public static void makeNotification(CharSequence title, CharSequence text) {
        makeNotification(new NotiBuilder(title, text));
    }

    public static void makeNotification(Notification notification) {
        notiManager.notify(notiCount.incrementAndGet(), notification);
    }

    public static class NotiBuilder {
        private static final int NOTI_DEFAULT_COLOUR = 0x00bfff;
        private NotificationCompat.Builder builder;
        private int notiID;

        public NotiBuilder(@NonNull CharSequence contentTitle, @NonNull CharSequence contentText) {
            notiID = notiCount.incrementAndGet();
            builder = new NotificationCompat.Builder(APP_CONTEXT, NOTI_CHANNELID_DEFAULT);
            builder.setDefaults(NotificationCompat.DEFAULT_ALL);
            builder.setSmallIcon(R.drawable.ic_stat_name);
            builder.setContentTitle(contentTitle);
            builder.setContentText(contentText);
            builder.setColorized(true);
            builder.setColor(NOTI_DEFAULT_COLOUR);
            builder.setAutoCancel(true);
            builder.setBadgeIconType(NotificationCompat.BADGE_ICON_SMALL);
            builder.setOngoing(false);
        }

        public NotiBuilder setSubText(CharSequence subText) {
            builder.setSubText(subText);
            return this;
        }

        public NotiBuilder setStyle(NotificationCompat.Style style) {
            builder.setStyle(style);
            return this;
        }

        public NotiBuilder setAutoCancel(boolean isAutoCancel) {
            builder.setAutoCancel(isAutoCancel);
            return this;
        }

        public NotiBuilder setOnPressAction(String action, Class<? extends BroadcastReceiver> BRclass, Bundle extraBundle) {
            Intent intent = new Intent(APP_CONTEXT, BRclass);
            intent.setAction(action);
            intent.putExtra(NOTI_ID_KEY_IN_EXTRAS, notiID);
            if (extraBundle != null) {
                intent.putExtras(extraBundle);
            }
            PendingIntent pendingIntent = PendingIntent.getBroadcast(APP_CONTEXT, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            builder.setContentIntent(pendingIntent);
            return this;
        }

        public NotiBuilder setOnPressAction(String action, Class<? extends BroadcastReceiver> BRclass) {
            return setOnPressAction(action, BRclass, null);
        }

        public NotiBuilder setOnPressAction(String action) {
            return setOnPressAction(action, MEMO3_BroadcastReceiver.class, null);
        }

        public NotiBuilder setOnPressAction(String action, Bundle extraBundle) {
            return setOnPressAction(action, MEMO3_BroadcastReceiver.class, extraBundle);
        }

        public NotiBuilder setBottomAction(NotiBottomActionBuilder action1, NotiBottomActionBuilder action2, NotiBottomActionBuilder action3) {
            if (action1 != null) {
                action1.setNotiID(this.notiID);
                builder.addAction(action1.build());
            }
            if (action2 != null) {
                action2.setNotiID(this.notiID);
                builder.addAction(action2.build());
            }
            if (action3 != null) {
                action3.setNotiID(this.notiID);
                builder.addAction(action3.build());
            }
            return this;
        }

        public static class NotiBottomActionBuilder {
            int notiId;
            CharSequence action;
            CharSequence notiLabel;
            Class<? extends BroadcastReceiver> receiveClass;
            Bundle extraBundle = null;

            public NotiBottomActionBuilder(@NonNull CharSequence action,
                                           @NonNull CharSequence notiLabel,
                                           @NonNull Class<? extends BroadcastReceiver> receiveClass) {
                this.action = action;
                this.notiLabel = notiLabel;
                this.receiveClass = receiveClass;
            }

            public NotiBottomActionBuilder(@NonNull CharSequence action, @NonNull CharSequence notiLabel) {
                this.action = action;
                this.notiLabel = notiLabel;
                this.receiveClass = MEMO3_BroadcastReceiver.class;
            }

            protected NotiBottomActionBuilder setNotiID(int ID) {
                this.notiId = ID;
                return this;
            }

            public NotiBottomActionBuilder setExtraBundle(Bundle extraBundle) {
                this.extraBundle = extraBundle;
                return this;
            }

            protected NotificationCompat.Action build() {
                Intent intent = new Intent(APP_CONTEXT, receiveClass);
                intent.setAction(action.toString());
                intent.putExtra(NOTI_ID_KEY_IN_EXTRAS, notiId);
                if (extraBundle != null) {
                    intent.putExtras(extraBundle);
                }
                PendingIntent pendingIntent = PendingIntent.getBroadcast(APP_CONTEXT, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                return new NotificationCompat.Action(0, notiLabel, pendingIntent);
            }
        }

        public static class NotiBottomActionRemoteInputBuilder extends NotiBottomActionBuilder {
            private CharSequence resultKey;
            private CharSequence label;

            public NotiBottomActionRemoteInputBuilder(@NonNull CharSequence action,
                                                      @NonNull CharSequence notiTitle,
                                                      @NonNull Class<? extends BroadcastReceiver> receiveClass,
                                                      @NonNull CharSequence resultKey,
                                                      @NonNull CharSequence label) {
                super(action, notiTitle, receiveClass);
                this.resultKey = resultKey;
                this.label = label;
            }

            public NotiBottomActionRemoteInputBuilder(@NonNull CharSequence action,
                                                      @NonNull CharSequence notiTitle,
                                                      @NonNull CharSequence resultKey,
                                                      @NonNull CharSequence label) {
                super(action, notiTitle);
                this.resultKey = resultKey;
                this.label = label;
            }

            @Override
            protected NotificationCompat.Action build() {
                RemoteInput remoteInput = new RemoteInput.Builder(resultKey.toString()).setLabel(label).build();
                Intent intent = new Intent(APP_CONTEXT, receiveClass);
                intent.setAction(action.toString());
                intent.putExtra(NOTI_ID_KEY_IN_EXTRAS, notiId);
                if (extraBundle != null) {
                    intent.putExtras(extraBundle);
                }
                PendingIntent pendingIntent = PendingIntent.getBroadcast(APP_CONTEXT, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                return new NotificationCompat.Action.Builder(0, notiLabel, pendingIntent).addRemoteInput(remoteInput).setAllowGeneratedReplies(false).build();
            }
        }

        public NotiBuilder setHistory(CharSequence[] history) {
            builder.setRemoteInputHistory(history);
            return this;
        }

        public NotiBuilder setProgress() {
            builder.setProgress(0, 0, true);
            return this;
        }

        public NotiBuilder setProgress(int max, int now) {
            builder.setProgress(max, now, false);
            return this;
        }

        public NotiBuilder setOngoing(boolean isOngoing) {
            builder.setOngoing(isOngoing);
            return this;
        }

        public NotiBuilder setTimeoutAfter(long ms) {
            builder.setTimeoutAfter(ms);
            return this;
        }

        private Notification build() {
            return builder.build();
        }
    }
*/
    /** VIBRATOR */
    //(max amplitude = 255)
    //<uses-permission android:name="android.permission.VIBRATE" />
    private static Vibrator vibrator;

    private static void init_Vibrator() {
        vibrator = (Vibrator) APP_CONTEXT.getSystemService(Context.VIBRATOR_SERVICE);
    }

    public static void makeVibrate_full(long ms, int amplitude) {
        stopVibrator();
        vibrator.vibrate(VibrationEffect.createOneShot(ms, amplitude));
    }

    public static void makeVibrate(long ms) {
        makeVibrate_full(ms, VibrationEffect.DEFAULT_AMPLITUDE);
    }

    public static void makeVibrate_pattern(long[] ms, int[] amplitude, int repeat) {
        if (ms.length != amplitude.length) {
            return;
        }
        stopVibrator();
        vibrator.vibrate(VibrationEffect.createWaveform(ms, amplitude, repeat));
    }

    public static void makeVibrate_pattern(long[] ms, int[] amplitude) {
        makeVibrate_pattern(ms, amplitude, -1);
    }

    public static void makeVibrate_pattern(long[] ms, int repeat) {
        stopVibrator();
        vibrator.vibrate(VibrationEffect.createWaveform(ms, repeat));
    }

    public static void makeVibrate_pattern(long[] ms) {
        makeVibrate_pattern(ms, -1);
    }

    public static void stopVibrator() {
        vibrator.cancel();
    }

    /** COUNT DOWN TIMER (dont use) */
    //interval will run once on start
    @Deprecated
    public interface CountDownTimerImplementation {
        void onTick(long ms_untilFinish);

        void onFinish();
    }

    @Deprecated
    public static void makeCountDownTimer(long duration_ms, long interval_ms, CountDownTimerImplementation implementation) {
        new CountDownTimer(duration_ms, interval_ms) {
            @Override
            public void onTick(long millisUntilFinished) {
                implementation.onTick(millisUntilFinished);
            }

            @Override
            public void onFinish() {
                implementation.onFinish();
            }
        }.start();
    }

    @Deprecated
    public static void makeCountDownTimer_Xinterval(long duration_ms, CountDownTimerImplementation implementation) {
        new CountDownTimer(duration_ms, duration_ms + 5) {
            @Override
            public void onTick(long millisUntilFinished) {
                implementation.onTick(millisUntilFinished);
            }

            @Override
            public void onFinish() {
                implementation.onFinish();
            }
        }.start();
    }

    /** TIMER (dont use) */
    //daemon = end autoly if all other non-daemon ended
    @Deprecated
    public static void makeTimer(long delay, TimerTask timerTask) {
        new Timer(false).schedule(timerTask, delay);
    }

    @Deprecated
    public static void makeTimer_daemon(long delay, TimerTask timerTask) {
        new Timer(true).schedule(timerTask, delay);
    }

    /** SCHEDULER (TIMER + COUNT DOWN TIMER) (in runtime) */
    //COUNT DOWN TIMER interval will run once on start
    public static void makeSchedule_short(long delayms, Scheduler_onFinish onFinish, long intervalms, Scheduler_onTick onTick) {
        new CountDownTimer(delayms, intervalms) {
            @Override
            public void onTick(long millisUntilFinished) {
                onTick.onTick(millisUntilFinished);
            }

            @Override
            public void onFinish() {
                onFinish.onFinish();
            }
        }.start();
    }

    public static void makeSchedule_short(long delayms, Scheduler_onFinish onFinish) {
        new CountDownTimer(delayms, delayms + 5) {
            @Override
            public void onTick(long millisUntilFinished) {
            }

            @Override
            public void onFinish() {
                onFinish.onFinish();
            }
        }.start();
    }

    public static Timer makeSchedule_long(long delayms, Scheduler_onFinish onFinish, long periodms) {
        Timer timer = new Timer(true);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                onFinish.onFinish();
            }
        }, delayms, periodms);
        return timer;
    }

    public static void makeSchedule_long(long delayms, Scheduler_onFinish onFinish) {
        new Timer(true).schedule(new TimerTask() {
            @Override
            public void run() {
                onFinish.onFinish();
            }
        }, delayms);
    }

    public static void makeSchedule_long(long delayms, Scheduler_onFinish onFinish, long periodms, int repeatForXTime) {
        for (int i = 0; i < repeatForXTime; i++) {
            makeSchedule_long(delayms + i * periodms, onFinish);
        }
    }

    public static Timer makeSchedule_long(Calendar triggerTime, Scheduler_onFinish onFinish, long periodms) {
        if (!isTimeAfterNow(triggerTime)) {
            return null;
        }
        return makeSchedule_long(triggerTime.getTimeInMillis() - getCurrTime_ms(), onFinish, periodms);
    }

    public static void makeSchedule_long(Calendar triggerTime, Scheduler_onFinish onFinish) {
        if (!isTimeAfterNow(triggerTime)) {
            return;
        }
        makeSchedule_long(triggerTime.getTimeInMillis() - getCurrTime_ms(), onFinish);
    }

    public static void makeSchedule_long(Calendar triggerTime, Scheduler_onFinish onFinish, long periodms, int repeatForXTime) {
        if (!isTimeAfterNow(triggerTime)) {
            return;
        }
        makeSchedule_long(triggerTime.getTimeInMillis() - getCurrTime_ms(), onFinish, periodms, repeatForXTime);
    }

    public static void makeSchedule_long(int year_start, int month_start, int day_of_month_start, int hour_start, int minute_start, int second_start,
                                         Scheduler_onFinish onFinish, int periodms,
                                         int year_stop, int month_stop, int day_of_month_stop, int hour_stop, int minute_stop, int second_stop) {
        Calendar startCalendar = getCalendar(year_start, month_start, day_of_month_start, hour_start, minute_start, second_start, 0);
        Calendar stopCalendar = getCalendar(year_stop, month_stop, day_of_month_stop, hour_stop, minute_stop, second_stop, 0);
        if (!isTimeAfterNow(startCalendar)) {
            return;
        }
        if (!stopCalendar.after(startCalendar)) {
            return;
        }
        new Thread(() -> {
            Timer timer = makeSchedule_long(startCalendar, onFinish, periodms);
            try {
                Thread.sleep(stopCalendar.getTimeInMillis() - getCurrTime_ms());
            } catch (Throwable e) {
                log(e.getMessage());
            }
            timer.cancel();
        }).start();
    }

    public static void makeSchedule_long(int year, int month, int day_of_month, int hour, int minute, int second, Scheduler_onFinish onFinish) {
        Calendar startCalendar = getCalendar(year, month, day_of_month, hour, minute, second, 0);
        if (!isTimeAfterNow(startCalendar)) {
            return;
        }
        makeSchedule_long(startCalendar, onFinish);
    }

    public interface Scheduler_onTick {
        void onTick(long millisUntilFinished);
    }

    public interface Scheduler_onFinish {
        void onFinish();
    }
/* disabled on 27-12-2021
    //WORK MANAGER (both in & out runtime)
    private static WorkManager workManager;

    private static void init_WorkManager() {
        workManager = WorkManager.getInstance(APP_CONTEXT);
    }

    public static UUID makeWork_enqueue(Class<? extends Worker> workerClass, long delay_ms) {
        OneTimeWorkRequest request = new OneTimeWorkRequest.Builder(workerClass)
                .setInitialDelay(delay_ms, TimeUnit.MILLISECONDS)
                .build();
        workManager.enqueue(request);
        return request.getId();
    }

    public static UUID makeWork_enqueue(Class<? extends Worker> workerClass, Calendar fireTime) {
        return makeWork_enqueue(workerClass, fireTime.getTimeInMillis() - getCurrTime_ms());
    }

    public static UUID makeWork_enqueue(Class<? extends Worker> workerClass) {
        return makeWork_enqueue(workerClass, 1);
    }

    public static UUID makeWork_enqueue(Class<? extends Worker> workerClass, long delay_ms, long repeat, TimeUnit timeUnit) {
        long realRepeat = TimeUnit.MINUTES.convert(repeat, timeUnit);
        if (realRepeat < 15) {
            return null;
        }
        PeriodicWorkRequest request = new PeriodicWorkRequest.Builder(workerClass, repeat, TimeUnit.MINUTES)
                .setInitialDelay(delay_ms, TimeUnit.MILLISECONDS)
                .build();
        workManager.enqueue(request);
        return request.getId();
    }

    public static UUID makeWork_enqueue(Class<? extends Worker> workerClass, Calendar fireTime, long repeat, TimeUnit timeUnit) {
        return makeWork_enqueue(workerClass, fireTime.getTimeInMillis() - getCurrTime_ms(), repeat, timeUnit);
    }

    public static UUID makeWork_enqueue(Class<? extends Worker> workerClass, long repeat, TimeUnit timeUnit) {
        return makeWork_enqueue(workerClass, 1, repeat, timeUnit);
    }

    public static void makeWork_cancel(UUID uuid) {
        workManager.cancelWorkById(uuid);
    }

    public static void makeWork_cancel(String uuid_string) {
        makeWork_cancel(UUID.fromString(uuid_string));
    }

    public static void makeWork_cancelAll() {
        workManager.cancelAllWork();
    }
*/
    /** CALENDAR + TIME */
    public static long getCurrTime_ms() {
        return System.currentTimeMillis();
    }

    public static Calendar getCurrTime_calendar() {
        return new Calendar.Builder().setInstant(new Date(getCurrTime_ms())).build();
    }

    public static Date getCurrTime_date() {
        return getCurrTime_calendar().getTime();
    }

    public static String getCurrTime_string() {
        return getCurrTime_date().toString();
    }

    public static Calendar getCalendar(int year, int month, int day, int hour, int minute, int second, int mills) {
        return new Calendar.Builder().setDate(year, month - 1, day).setTimeOfDay(hour, minute, second, mills).build();
    }

    public static boolean isTimeAfterNow(Calendar time) {
        return time.after(getCurrTime_calendar());
    }

    public static boolean isTimeBeforeNow(Calendar time) {
        return time.before(getCurrTime_calendar());
    }

    /** CAMERA + FLASH */
    //camera permission includes flashlight permission
    //<uses-feature android:name="android.hardware.camera.any" />
    //<uses-permission android:name="android.permission.CAMERA" />

    // (11-1-2022)
    // To start analyzing image, need to feed Analyzer to ImageAnalysis, hopefully after starting camera
    // To stop analyzing, need to clearAnalyzer() from ImageAnalysis
    // To show on screen, need to feed "Preview" class object to camera, with previewView element

    //permission part
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 14;
    //camera part
    private static ProcessCameraProvider cameraProvider;
    //flashlight part
    private static CameraManager cameraManager;
    private static String flashlightID;
    private static boolean flashOnNow = false;
    //template part
    public static ImageAnalysis templateImageAnalysis;

    private static void init_Camera() {
        //permission part
        if (ContextCompat.checkSelfPermission(APP_CONTEXT, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(CURR_ACTIVITY, new String[]{Manifest.permission.CAMERA},CAMERA_PERMISSION_REQUEST_CODE);
        }
        //camera part
        ProcessCameraProvider.getInstance(APP_CONTEXT).addListener(() -> {
            try {
                cameraProvider = ProcessCameraProvider.getInstance(APP_CONTEXT).get();
                log("cameraProvider set-ed");
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }, ContextCompat.getMainExecutor(APP_CONTEXT));
        //flashlight part
        cameraManager = (CameraManager) APP_CONTEXT.getSystemService(Context.CAMERA_SERVICE);
        try {
            if (APP_CONTEXT.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)) {
                flashlightID = cameraManager.getCameraIdList()[0];
            } else {
                log("no flashlight on this device");
            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
        //template part
        templateImageAnalysis = new ImageAnalysis.Builder()
                .setTargetResolution(new Size(720,1280))
                .setOutputImageRotationEnabled(true)
                .setTargetRotation(Surface.ROTATION_0)
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build();
    }

    private static void destroyCamera(){
        stopCamera();
    }

    public static ImageAnalysis startCamera(ImageAnalysis imageAnalysis, Preview previewView){
        if (cameraProvider==null){
            log("no cameraProvider");
            return imageAnalysis;
        }
        if (imageAnalysis==null && previewView==null){return null;}
        stopCamera();
        log("start camera");
        if (imageAnalysis==null){
            cameraProvider.bindToLifecycle(
                    CURR_ACTIVITY,
                    new CameraSelector.Builder().requireLensFacing(CameraSelector.LENS_FACING_BACK).build(),
                    previewView
            );
        } else if (previewView==null){
            cameraProvider.bindToLifecycle(
                    CURR_ACTIVITY,
                    new CameraSelector.Builder().requireLensFacing(CameraSelector.LENS_FACING_BACK).build(),
                    imageAnalysis
            );
        } else {
            cameraProvider.bindToLifecycle(
                    CURR_ACTIVITY,
                    new CameraSelector.Builder().requireLensFacing(CameraSelector.LENS_FACING_BACK).build(),
                    imageAnalysis,
                    previewView
            );
        }
        return imageAnalysis;
    }

    public static void stopCamera(){
        if (cameraProvider==null){return;}
        log("stop camera");
        cameraProvider.unbindAll();
    }

    public static void makeFlash(boolean set_to_on) {
        if (flashlightID == null) {
            return;
        }
        try {
            cameraManager.setTorchMode(flashlightID, set_to_on);
        } catch (CameraAccessException e) {
            log(e.getMessage());
        }
        flashOnNow = set_to_on;
    }

    public static void makeFlash_ON() {
        makeFlash(true);
    }

    public static void makeFlash_OFF() {
        makeFlash(false);
    }

    public static void makeFlash_toggle() {
        flashOnNow = !flashOnNow;
        makeFlash(flashOnNow);
    }

    public static boolean isFlashOn() {
        return flashOnNow;
    }

    /** THREAD */
    public static Thread makeThread(Runnable toRun) {
        Thread thread = new Thread(toRun);
        thread.setDaemon(true);
        thread.start();
        return thread;
    }

    /** EASY DATA TRANSFER (in runtime) */
    private static Map<Integer, Object> packetInTransport_runtime = new HashMap<>();
    private static AtomicInteger packetCount_runtime = new AtomicInteger(1);

    public static int makeRuntimePacket_send(Object thingToSend) {
        int index = packetCount_runtime.incrementAndGet();
        if (index == 65000) {
            packetCount_runtime.set(1);
        }
        if (packetInTransport_runtime.containsKey(index)) {
            return makeRuntimePacket_send(thingToSend);
        }
        packetInTransport_runtime.put(index, thingToSend);
        return index;
    }

    public static Object makeRuntimePacket_get(int index) {
        Object o = packetInTransport_runtime.get(index);
        if (o != null) {
            packetInTransport_runtime.remove(index);
        }
        return o;
    }
/* disabled on 27-12-2021
    //LOCAL BROADCAST RECEIVER (in runtime)
    private static LocalBroadcastManager localBroadcastManager;
    private static final ArrayList<String> defaultLocalBRActions = new ArrayList<>(Arrays.asList(
//            ConnectivityManager.CONNECTIVITY_ACTION
    ));
    private static final util_LBR_DefaultActionHandler defaultLocalBRActionsHandler = new util_LBR_DefaultActionHandler();
    private static final Map<String, BroadcastReceiver> registeredLocalBRActions = new HashMap<>();

    private static void init_LocalBroadcastReceiver() {
        localBroadcastManager = LocalBroadcastManager.getInstance(APP_CONTEXT);

        //reg default action
        defaultLocalBRActions.forEach(s -> localBroadcastManager.registerReceiver(defaultLocalBRActionsHandler, new IntentFilter(s)));
    }

    public static void makeLocalBR_register(String action, LocalBRrun whatToRun) {
        if (registeredLocalBRActions.containsKey(action)) {
            return;
        }
        BroadcastReceiver receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                whatToRun.run(context, intent);
            }
        };
        registeredLocalBRActions.put(action, receiver);
        localBroadcastManager.registerReceiver(receiver, new IntentFilter(action));
    }

    public static void makeLocalBR_UNregister(String action) {
        if (!registeredLocalBRActions.containsKey(action)) {
            return;
        }
        localBroadcastManager.unregisterReceiver(registeredLocalBRActions.get(action));
        registeredLocalBRActions.remove(action);
    }

    public static void makeLocalBR_broadcast(String action) {
        localBroadcastManager.sendBroadcast(new Intent(action));
    }

    public static void makeLocalBR_broadcast(String action, Bundle bundle) {
        Intent intent = new Intent(action);
        intent.putExtras(bundle);
        localBroadcastManager.sendBroadcast(intent);
    }

    public interface LocalBRrun {
        void run(Context context, Intent intent);
    }

    //BROADCAST RECEIVER (all time)
    //TODO need to do this?
    public static Class getBR_class() {
        return MEMO3_BroadcastReceiver.class;
    }
*/
/* disabled on 27-12-2021
    //REALM DATABASE
    private static Realm realm;

    //schema version = 4/2/2020 22:37
    private static void init_Realm() {
        Realm.init(APP_CONTEXT);
        RealmConfiguration config = new RealmConfiguration.Builder()
                .schemaVersion(4)
                .migration(new util_RealmSchema())
//                .deleteRealmIfMigrationNeeded()     //for developing use
                .build();
        realm = Realm.getInstance(config);

        makeRealm_reset();   //for developing use
    }

    public static Realm getRealm() {
        return realm;
    }

    public static void makeRealm_executeTransaction(Realm.Transaction transaction) {
        realm.executeTransaction(transaction);
    }

    private static void destroyRealm() {
        realm.close();
    }

    public static void makeRealm_reset() {
        realm.executeTransaction(realm -> realm.deleteAll());
    }
*/
/* disabled on 27-12-2021
    //GOOGLE SIGNIN
    //beware of change of sha1 key, need update at 1)google credential, 2)firebase auth page (w new google-service.json), 3)id token from google credential
    //TODO sign in scope
    private static GoogleSignInAccount googleAccount;
    private static GoogleSignInClient googleSignInClient;
    public static final int googleSignInActivityRequestCode = 69;

    public static GoogleSignInAccount getGoogleAccount() {
        return googleAccount;
    }

    private static void init_Google() {
        //google ac
        googleAccount = GoogleSignIn.getLastSignedInAccount(CURR_ACTIVITY);

        //google signin client
        GoogleSignInOptions signInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestProfile()
                .requestEmail()
                .requestIdToken(Conf_Info.WEB_CLIENT_GOOGLE_SIGNIN)  //work   //OAuth 2.0 client IDs && type==web application will work
                .requestServerAuthCode(Conf_Info.WEB_CLIENT_GOOGLE_SIGNIN)
                .build();
        googleSignInClient = GoogleSignIn.getClient(APP_CONTEXT, signInOptions);
    }

    public static int makeGoogleSignIn_signIn() {
        makeGoogleSignIn_signOut();
        CURR_ACTIVITY.startActivityForResult(new Intent(googleSignInClient.getSignInIntent()), googleSignInActivityRequestCode);
        return googleSignInActivityRequestCode;
    }

    public static void makeGoogleSignIn_handleSignIn(Intent data) {
        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
        try {
            googleAccount = task.getResult(ApiException.class);
        } catch (Throwable e) {
            log(e.getMessage());
        }
    }

    public static void makeGoogleSignIn_signOut() {
        googleSignInClient.signOut();
    }

    public static void makeGoogleSignIn_disconnect() {
        googleSignInClient.revokeAccess();
    }

    public static boolean isGoogleSignIned() {
        return googleAccount != null;
    }

    //FIREBASE
    //beware of change of sha1 key, need update at 1)google credential, 2)firebase auth page (w new google-service.json), 3)id token from google credential
    //get && delete implemented later (later == some time in future)
    private static FirebaseUser firebaseUser;

    private static void init_Firebase() {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    }

    public static FirebaseUser getFirebaseUser() {
        return firebaseUser;
    }

    public static void makeFirebase_signIn(OnCompleteListener onComplete) {
        if (googleAccount == null) {
            return;
        }
        AuthCredential credential = GoogleAuthProvider.getCredential(googleAccount.getIdToken(), null);
        FirebaseAuth.getInstance().signInWithCredential(credential)
                .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                            }
                        }
                ).addOnCompleteListener(onComplete);
    }

    public static void makeFirebase_signIn() {
        if (googleAccount == null) {
            return;
        }
        AuthCredential credential = GoogleAuthProvider.getCredential(googleAccount.getIdToken(), null);
        FirebaseAuth.getInstance().signInWithCredential(credential)
                .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                            }
                        }
                );
    }

    public static void makeFirebase_signOut() {
        FirebaseAuth.getInstance().signOut();
    }

    public static boolean isFirebaseSignIned() {
        return firebaseUser != null;
    }

    //FIRESTORE
    private static FirebaseFirestore firestore;

    private static void init_Firestore() {
        firestore = FirebaseFirestore.getInstance();
    }

    public static FirebaseFirestore getFirestore() {
        return firestore;
    }
*/
    /** SERVICE */
    //comment because service currently no use
//    private static MEMO3_Service.MEMO3_ServiceBinder serviceBinder;
    private static void init_Service() {
//        startService();
//        APP_CONTEXT.bindService(new Intent(APP_CONTEXT,MEMO3_Service.class),serviceConnection,Context.BIND_AUTO_CREATE);
    }
//    private static boolean isServiceStarted = false;
//    public static boolean isServiceStarted(){return isServiceStarted;}
//    public static void startService(Bundle extraBundle){
//        Intent intent = new Intent(APP_CONTEXT, MEMO3_Service.class);
//        if (extraBundle != null){
//            intent.putExtras(extraBundle);
//        }
//        APP_CONTEXT.startService(intent);
//        isServiceStarted = true;
//    }
//    public static void startService(){startService(null);}
//    public static void stopService(){
//        APP_CONTEXT.stopService(new Intent(APP_CONTEXT,MEMO3_Service.class));
//        isServiceStarted = false;
//    }
//    public static final String Service_RunnableKey = "Service_RunnableKey";
//    public interface Service_BRRunnable{
//        void run(Context context, Intent intent);
//    }
//    private static final ServiceConnection serviceConnection = new ServiceConnection() {
//        @Override public void onServiceConnected(ComponentName name, IBinder service) {
//            log("service connected");
//            serviceBinder = (MEMO3_Service.MEMO3_ServiceBinder) service;
//        }
//        @Override public void onServiceDisconnected(ComponentName name) {}
//    };

    /** GLIDE */
    //not much because not really tried this
    //Glide.with().load()...into(View)

    /** ALERT DIALOG */
    //see main activity

    /** SENSOR */
//    private static boolean sensor_data_used = false;
//    private static void init_Sensor(){
//        //nothing for now
//    }
//    public static SP_service.Sensor_data get_SensorData(int sensor_type){
//        SP_service.Sensor_data sensor_data = (SP_service.Sensor_data) SP_service.sensor_value.get(sensor_type);
//        if (sensor_data == null){
//            log("sensor data not found, cant get data");
//            log("starting service to get data");
//            startService();
//            return null;
//        }
//        if (sensor_data_used){
//            log("data used, starting service to get new data");
//            startService();
//            sensor_data_used = false;
//            return null;
//        }
//        sensor_data_used = true;
//        return sensor_data;
//    }
//    public static HashMap<Object, Object> get_SensorDataList(){
//        if (sensor_data_used){
//            log("data used, starting service to get new data");
//            startService();
//            sensor_data_used = false;
//            return null;
//        }
//        for (int type : utilmax.SENSOR_TO_SENSE) {
//            SP_service.Sensor_data sensor_data = (SP_service.Sensor_data) SP_service.sensor_value.get(type);
//            if (sensor_data == null) {
//                log("sensor data not found, cant get data");
//                log("starting service to get data");
//                startService(null);
//                return null;
//            }
//        }
//        sensor_data_used = true;
//        return SP_service.sensor_value;
//    }
//
    /** NETWORK */
    private static ConnectivityManager networkConnectManager;
    private static void init_Network(){
         networkConnectManager = ((ConnectivityManager) APP_CONTEXT.getSystemService(Context.CONNECTIVITY_SERVICE));
    }
//    public static String getNetwork_type(){
//        NetworkInfo info = networkConnectManager.getActiveNetworkInfo();
//        if ()
//    }

    /** FILE */
    public static File FILE_DIR;
    public static File CACHE_DIR;
    private static void init_File(){
        FILE_DIR = APP_CONTEXT.getFilesDir();
        CACHE_DIR = APP_CONTEXT.getCacheDir();
    }
    public static File makeFile(String path, String filename){
        return new File(path,filename);
    }
    public static File makeFile(File path, String filename){
        return new File(path,filename);
    }
    public static File makeFile(String filename){
        return makeFile(FILE_DIR,filename);
    }
    public static boolean createFile(String path, String filename){
        try {
            return makeFile(path,filename).createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    public static boolean createFile(File path, String filename){
        try {
            return makeFile(path,filename).createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    public static boolean createFile(String filename){
        try {
            return makeFile(filename).createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    public static boolean createFile(File file){
        try {
            return file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    public static File createCacheFile(String filename){
        try {
            File file = makeFile(CACHE_DIR,filename);
            if (file.isFile()){
                return file;
            } else {
                return File.createTempFile(filename, null, CACHE_DIR);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    public static boolean deleteCacheFile(File file){
        if (!file.isFile()){return false;}
        if (!file.getAbsolutePath().contains(CACHE_DIR.getAbsolutePath())){return false;}
        return file.delete();
    }
    public static <T> void saveFileJSONClass(File filePath, T classObject){
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(
                    makeFile(new StringBuilder(filePath.getName()).append(".json").toString())));
            writer.write(new GsonBuilder().setPrettyPrinting().create().toJson(classObject));
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static <T> T loadFileJSONClass(File filePath, Class<T> theClass){
        try {
            BufferedReader reader = new BufferedReader(new FileReader(
                    makeFile(new StringBuilder(filePath.getName()).append(".json").toString())));
            return new Gson().fromJson(reader,theClass);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static void writeFileTemplate(File file, String content){
        if (!file.isFile()) {return;}

        try{
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            writer.write(content);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static String readFileTemplate(File file){
        if (!file.isFile()){return null;}

        StringBuilder out = new StringBuilder();
        try{
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null){
                out.append(line).append("\n");
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return out.toString();
    }
    public static String[] listFile(){
        return APP_CONTEXT.fileList();
    }

    /** PREFERENCES */
    public static SharedPreferences PREF;
    private static void init_Preferences(){
        PREF = PreferenceManager.getDefaultSharedPreferences(APP_CONTEXT);
    }
    public static void prefTemplate(){
        PREF.edit().putInt("prefTemp",123456)
                .apply();
        log(PREF.getInt("prefTemp",10));
    }

    /** SHARING */
    //https://developer.android.com/training/sharing/send
    public static Intent getSharingIntent(){return new Intent(Intent.ACTION_SEND);}
    public static void startSharingSharesheet(Intent send, String title){
        CURR_ACTIVITY.startActivity(Intent.createChooser(send,title));
    }
    public static void sharingTemplate(){
        Intent intent = getSharingIntent();
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, "actual content");
        intent.putExtra(Intent.EXTRA_TITLE,"title in sharesheet");
        startSharingSharesheet(intent,"title of the sharing action");
    }

    /** DEVICE DATA */
    public static void getDevice_DeviceData() {
        log("Below is Device Data Information");

        //basic device information
        log("SERIAL: " + Build.SERIAL);
        log("MODEL: " + Build.MODEL);
        log("ID: " + Build.ID);
        log("Manufacture: " + Build.MANUFACTURER);
        log("brand: " + Build.BRAND);
        log("type: " + Build.TYPE);
        log("user: " + Build.USER);
        log("BASE: " + Build.VERSION_CODES.BASE);
        log("INCREMENTAL " + Build.VERSION.INCREMENTAL);
        log("SDK  " + Build.VERSION.SDK);
        log("BOARD: " + Build.BOARD);
        log("HOST " + Build.HOST);
        log("FINGERPRINT: " + Build.FINGERPRINT);
        log("Version Code: " + Build.VERSION.RELEASE);
        log("TIME: " + Build.TIME);
        log("DISPLAY: " + Build.DISPLAY);
        log("TAGS: " + Build.TAGS);
        for (int i = 0; i < Build.SUPPORTED_ABIS.length; ++i) {
            log("SUPPORTED_ABIS: " + Build.SUPPORTED_ABIS[i]);
        }
        log("Radio Version: " + Build.getRadioVersion());
    }
    public static void getDevice_NetworkData(){
        log("Below is Network Information");

        ConnectivityManager manager = ((ConnectivityManager) APP_CONTEXT.getSystemService(Context.CONNECTIVITY_SERVICE));
        //all networks type and availability and connected  (btw, NetworkInfo is deprecated from android Q)
        //for seeing all networks in the phone
        for (Network network : manager.getAllNetworks()){
            NetworkInfo info = manager.getNetworkInfo(network);
            log(info.getTypeName().toUpperCase()+" available? : "+info.isAvailable());
            log(info.getTypeName().toUpperCase()+" connected? : "+info.isConnected());
        }
        //for getting the active, usable network in the phone
        NetworkInfo info = manager.getActiveNetworkInfo();
        if (info == null){
            log("Current active network = null");
        } else {
            log("Current active network = " + info.getTypeName() + ", and it is " + (info.isConnected() ? "" : "NOT ") + "CONNECTED");
        }
        if (info.getTypeName().equals("WIFI")) {
            WifiManager wifiManager = (WifiManager) APP_CONTEXT.getSystemService(Context.WIFI_SERVICE);
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            log("SSID " + wifiInfo.getSSID());
            log("Frequency " + wifiInfo.getFrequency() + WifiInfo.FREQUENCY_UNITS);
            int ipAdr = wifiInfo.getIpAddress();    //local ip only
            String ip = String.format("IP Adrress : %02d.%02d.%02d.%02d", (ipAdr >> 0) & 0xff, (ipAdr >> 8) & 0xff, (ipAdr >> 16) & 0xff, (ipAdr >> 24) & 0xff);
            log(ip);
//            log("MAC addr " + wifiInfo.getMacAddress());
            log("Link speed " + wifiInfo.getLinkSpeed() + WifiInfo.LINK_SPEED_UNITS);
            log("Network ID " + wifiInfo.getNetworkId());
            log("Signal level " + WifiManager.calculateSignalLevel(wifiInfo.getRssi(), 1000));
        }
        if (info.getTypeName().equals("MOBILE")) {
            log("IP address "+getDevice_IPAddress(true));
        }

        //other useful(?) stuff that can be set in the manager
//        manager.addDefaultNetworkActiveListener(new ConnectivityManager.OnNetworkActiveListener() {
//            @Override
//            public void onNetworkActive() {
//                Log.i("MAX","Network is active now");
//            }
//        });
//        manager.registerNetworkCallback(
//                new NetworkRequest.Builder().addCapability(
//                        NetworkCapabilities.NET_CAPABILITY_INTERNET||
//                        NetworkCapabilities.NET_CAPABILITY_NOT_ROAMING||
//                        ).build(),
//                new ConnectivityManager.NetworkCallback()
//                );
//        manager.reportNetworkConnectivity();
//        manager.requestNetwork();
    }
    private static String getDevice_IPAddress(boolean useIPv4) {
        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces) {
                List<InetAddress> addrs = Collections.list(intf.getInetAddresses());
                for (InetAddress addr : addrs) {
                    if (!addr.isLoopbackAddress()) {
                        String sAddr = addr.getHostAddress();
                        //boolean isIPv4 = InetAddressUtils.isIPv4Address(sAddr);
                        boolean isIPv4 = sAddr.indexOf(':')<0;

                        if (useIPv4) {
                            if (isIPv4)
                                return sAddr;
                        } else {
                            if (!isIPv4) {
                                int delim = sAddr.indexOf('%'); // drop ip6 zone suffix
                                return delim<0 ? sAddr.toUpperCase() : sAddr.substring(0, delim).toUpperCase();
                            }
                        }
                    }
                }
            }
        } catch (Exception ignored) { } // for now eat exceptions
        return "";
    }
    private static String getDevice_IPAddress() {return getDevice_IPAddress(true);}
    public static void getDevice_Battery(){
        log("Below is Battery information");

        //because battery stuff broadcast is sticky, can do it like this
        IntentFilter filter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = APP_CONTEXT.registerReceiver(null,filter);

        // Are we charging / charged?
        int status = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
        boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING;
        boolean isFull = status == BatteryManager.BATTERY_STATUS_FULL;
        if (isCharging){
            makeToastLog("Phone is now charging");
        }
        if (isFull){
            makeToastLog("Phone is now full");
        }

        // How are we charging?
        int chargePlug = batteryStatus.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
        boolean usbCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_USB;
        boolean acCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_AC;
        if (usbCharge){
            makeToastLog("charge through USB");
        }
        if (acCharge){
            makeToastLog("charge through AC");
        }

        //battery level
        int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL,-1);
        int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE,-1);
        float batteryPct = level/(float)scale;
        makeToastLog("battery level "+batteryPct);
    }

    //need to start service first, and then connect device
    //so that it can get detail of device
    //cause android cant show currently connected device directly, can only show paired device
    //so only able to listen to broadcast
//    public static void getDevice_Bluetooth(){
//        if (!isServiceStarted){
//            makeToastLog("service not started, can't get data");
//            return;
//        }
//        log("Below is Bluetooth device information");
//        BluetoothDevice bluetoothDevice = SP_service.getBluetoothDevice();
//        if (bluetoothDevice == null){
//            log("No Bluetooth device connected currently");
//            return;
//        }
//        makeToastLog("Name = "+bluetoothDevice.getName());
//        log("Address = "+bluetoothDevice.getAddress());
//    }
//    public static void get_Sensor(){
//        log("Below is Sensor Data");
//        if (SP_service.sensor_value.size() == 0){
//            log("no data in map");
//        }
//        for (int type : SENSOR_TO_SENSE){
//            SP_service.Sensor_data sensor = (SP_service.Sensor_data) SP_service.sensor_value.get(type);
//            if (sensor != null) {
//                log(sensor.name+" data = "+sensor.value);
//            }
//        }
//    }
//    public static void getDevice_Google(){
//        if (signInAccount == null){
//            log("no ac signed in currently");
//            return;
//        }
//        log("Below is Logined Google Account Information");
//        log("Display name: "+signInAccount.getDisplayName());
//        log("Family name: "+signInAccount.getFamilyName());
//        log("Given name: "+signInAccount.getGivenName());
//        log("Email: "+signInAccount.getEmail());
//        log("ID: "+signInAccount.getId());
//        log("ID token: "+signInAccount.getIdToken());
//        log("Server Auth Code: "+signInAccount.getServerAuthCode());
//    }
//    public static void getDevice_Firebase(){
//        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
//        if (firebaseUser==null){
//            log("firebase not logged in, bye");
//            return;
//        }else {
//            log("Below is Logined Firebase Account Information");
//            log("getDisplayName "+firebaseUser.getDisplayName());
//            log("getEmail "+firebaseUser.getEmail());
//            log("getPhoneNumber "+firebaseUser.getPhoneNumber());
//            log("getProviderId "+firebaseUser.getProviderId());
//            log("getUid "+firebaseUser.getUid());
//            log("getProviders().size() "+firebaseUser.getProviderData().size());
//            log("getProviders().get(0) "+firebaseUser.getProviderData().get(0));
//            log("isEmailVerified "+firebaseUser.isEmailVerified());
//        }
//    }

    /** sarcastic string generator */
    //TODO not done
    public static String upLowString(String message){
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < message.length(); i++) {
            char c = message.charAt(i);
            if ((c>64 && c<91) || (c>96 && c<123)){

            }
            builder.append(c);
        }
        return builder.toString();
    }
}
