package com.rubu.teachapplication;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class ServiceActivity extends AppCompatActivity implements View.OnClickListener {

    // 创建服务里面的对应的绑定的Ibinder对象
    private MyService.DownLoadBinder mBinder;

    // 绑定服务和活动之间的联系
    ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
//            用来关联服务和活动的操作IBinder,可以向下转化为DownLoadBinder; (在服务绑定的时候启用)
            mBinder = (MyService.DownLoadBinder) service;
            // 调用下载
            mBinder.startDownLod();
            // 调用显示进度
            mBinder.getPrograss();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
//            在服务解除绑定的时候调用

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service);
        Button startService = (Button) findViewById(R.id.btn_start);
        Button bindService = (Button) findViewById(R.id.btn_bind);
        Button unBindService = (Button) findViewById(R.id.btn_unBind);
        Button stopService = (Button) findViewById(R.id.btn_stop);
        Button startIntentService = (Button) findViewById(R.id.btn_startIntentService);
        startService.setOnClickListener(this);
        bindService.setOnClickListener(this);
        unBindService.setOnClickListener(this);
        stopService.setOnClickListener(this);
        startIntentService.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            // 启动服务
            case R.id.btn_start:
                Intent startIntent = new Intent(this, MyService.class);
                startService(startIntent);
                Log.d("MyService", "activity线程 " + Thread.currentThread());
                break;
            // 绑定服务
            case R.id.btn_bind:
                Intent bindIntent = new Intent(this, MyService.class);
                bindService(bindIntent, serviceConnection, BIND_AUTO_CREATE);
                break;
            // 解除绑定
            case R.id.btn_unBind:
                unbindService(serviceConnection);
                break;
            // 停止服务
            case R.id.btn_stop:
                Intent stopIntent = new Intent(this, MyService.class);
                stopService(stopIntent);
                break;
            // 启动线程服务
            case R.id.btn_startIntentService:
                Intent startIntentService = new Intent(this, MyIntentService.class);
                startService(startIntentService);
                break;
        }
    }

}
