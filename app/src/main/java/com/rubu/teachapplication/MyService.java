package com.rubu.teachapplication;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Binder;
import android.os.IBinder;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

public class MyService extends Service {
    private static final String TAG = "MyService";

    private DownLoadBinder binder = new DownLoadBinder();

    class DownLoadBinder extends Binder {
        public void startDownLod() {
            Log.d(TAG, "startDownLod: excute");
        }

        public int getPrograss() {
            Log.d(TAG, "getPrograss: excute");
            return 0;
        }
    }

    public MyService() {

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return binder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
//        服务被创建的时候调用的方法
        Log.d(TAG, "onCreate: executed");
//      ********************  创建一个前台服务  ************************//
        Intent intent = new Intent(this, Main2Activity.class);
        PendingIntent pi = PendingIntent.getActivity(this, 0, intent, 0);
        Notification notification = new NotificationCompat.Builder(this)
                .setContentTitle("This is a title")
                .setContentText("This is a text")
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                .setContentIntent(pi)
                .build();
        startForeground(2, notification);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand: executed");
//        **********************  开启线程执行异步操作(可以直接创建IntentService规避这些问题)   *************************//
        new Thread() {
            @Override
            public void run() {
                // 异步操作具体逻辑
                Log.d(TAG, "服务子线程 " + Thread.currentThread());
                // 线程执行完了自己调用关闭服务
                stopSelf();
            }
        }.start();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        服务销毁时候调用的方法
        Log.d(TAG, "onDestroy: executed");
    }
}
