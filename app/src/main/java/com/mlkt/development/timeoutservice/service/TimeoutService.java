package com.mlkt.development.timeoutservice.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.mlkt.development.timeoutservice.events.TimeoutEvent;

import java.util.Timer;
import java.util.TimerTask;

import de.greenrobot.event.EventBus;

public class TimeoutService extends Service {

    public static int TIMER_START_COUNT = 0;
    public static int TIMER_MAX_COUNT = 600;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        TIMER_START_COUNT = 0;
        updateTimer();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    private static Timer mTimer;
    private static TimerTask timerTask;

    public void onDestroy() {
        try {
            mTimer.cancel();
            timerTask.cancel();
            updateTimer();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void updateTimer() {

        TIMER_START_COUNT = 0;

        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }

        if (timerTask != null) {
            timerTask.cancel();
            timerTask = null;
        }

        mTimer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                TIMER_START_COUNT++;
                if (TIMER_START_COUNT > TIMER_MAX_COUNT) {
                    mTimer.cancel();
                    EventBus.getDefault().post(new TimeoutEvent());
                }
            }
        };
        mTimer.schedule(timerTask, 1000, 1000);
    }

}
