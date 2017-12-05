package com.rubu.teachapplication;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;


/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions and extra parameters.
 */
public class MyIntentService extends IntentService {

    public MyIntentService() {
        super("MyIntentService");
        Log.d("MyService", "MyIntentService: excute ");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
//        *****************   自带异步线程执行操作,以及自带关闭服务操作   ******************//
        Log.d("MyService", "onHandleIntent: excute");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("MyService", "onDestroy: excute");
    }
}
