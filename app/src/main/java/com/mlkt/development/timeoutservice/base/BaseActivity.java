package com.mlkt.development.timeoutservice.base;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.mlkt.development.timeoutservice.events.TimeoutEvent;
import com.mlkt.development.timeoutservice.service.TimeoutService;

import de.greenrobot.event.EventBus;

public abstract class BaseActivity extends AppCompatActivity {

    public BaseActivity activity = this;
    public abstract int getLayoutId();

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        Intent intent = new Intent(this, TimeoutService.class);
        bindService(intent, connection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        unbindService(connection);
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    public void startTimerService(){
        stopService(new Intent(this, TimeoutService.class));
        startService(new Intent(this, TimeoutService.class));
    }

    public void stopTimerService(){
        stopService(new Intent(this, TimeoutService.class));
    }

    @Override
    public void onUserInteraction() {
        super.onUserInteraction();
        TimeoutService.updateTimer();
    }

    public void onEvent(TimeoutEvent event) {
        // Do logout or something else
    }
}
