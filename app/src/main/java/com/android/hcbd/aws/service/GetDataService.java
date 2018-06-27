package com.android.hcbd.aws.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import com.android.hcbd.aws.event.MessageEvent;
import com.android.hcbd.aws.util.LogUtils;

import org.greenrobot.eventbus.EventBus;

public class GetDataService extends Service {

    private MyBinder binder = new MyBinder();

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        LogUtils.LogShow("Service启动");
    }

    public class MyBinder extends Binder {
        public GetDataService getService() {
            return GetDataService.this;
        }
    }

    public boolean preFlag = false;
    public boolean carFlag = false;
    public void sendPreData(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    try {
                        Thread.sleep(800);
                        if(preFlag){
                            MessageEvent messageEvent = new MessageEvent();
                            messageEvent.setEventId(MessageEvent.EVENT_GETPREDATA_THREAD);
                            EventBus.getDefault().post(messageEvent);
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }
    public void sendCarData(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    try {
                        Thread.sleep(800);
                        if(carFlag){
                            MessageEvent messageEvent = new MessageEvent();
                            messageEvent.setEventId(MessageEvent.EVENT_GETCARDATA_THREAD);
                            EventBus.getDefault().post(messageEvent);
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }
}
