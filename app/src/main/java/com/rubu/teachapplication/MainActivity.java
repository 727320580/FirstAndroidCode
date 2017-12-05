package com.rubu.teachapplication;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class MainActivity extends AppCompatActivity {
    // 广播接收的参数
    private IntentFilter intentFilter;
    // 注册广播接受者，来接收手机自带的断网操作的广播
    private NetWorkChangeReceiver netWorkChangeReceiver;

    private Button button;
    private Button btnNext;
    private Button btnDial;
    private Button btnContract;
    private EditText editText;

    private static final String TAG = "MainActivity";

    private static final int REQUEST_CALL_PHONE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = (Button) findViewById(R.id.btn_send_broadCast);
        btnNext = (Button) findViewById(R.id.btn_next);
        btnDial = (Button) findViewById(R.id.btn_dial);
        btnContract = (Button) findViewById(R.id.btn_contract);
        editText = (EditText) findViewById(R.id.et_input);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                ******************************    广播代码      ******************************//
                // 发送广播
//                intentFilter = new IntentFilter();
//                intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
//                netWorkChangeReceiver = new NetWorkChangeReceiver();
//                registerReceiver(netWorkChangeReceiver, intentFilter);
                // 保存本地文件
//                String inputs = editText.getText().toString();
//                saveEditText(inputs);
                // 发送通知

//                ******************************    通知代码     ******************************* //
//                NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//                Intent intent = new Intent(MainActivity.this, Main2Activity.class);
//                PendingIntent pi = PendingIntent.getActivity(MainActivity.this, 0, intent, 0);
//                Notification notification = new NotificationCompat.Builder(MainActivity.this)
//                        .setContentTitle("通知")
//                        .setContentText("内容")
//                        .setWhen(System.currentTimeMillis())
//                        .setSmallIcon(R.mipmap.ic_launcher)
//                        .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
//                        // 添加一个跳转的延迟意图
//                        .setContentIntent(pi)
//                        // 设置通知语音
////                        .setSound();
//                        // 设置震动(参数代表时长) , 添加震动权限
//                        .setVibrate(new long[]{0, 800, 800, 800})
//                        // 设置提示灯() (三个参数,颜色,亮的时长,暗的时长)
////                        .setLights()
//                        // 添加一个点击取消的功能(第一种方法，第二种是通过notificationManager来调用取消操作,在第二个页面创建的时候弹出来)
//                        .setAutoCancel(true)
//                        .build();
//                manager.notify(1, notification);
//                ********************************   启动服务 ***************************//
                final Intent intent = new Intent(MainActivity.this, ServiceActivity.class);
                startActivity(intent);
//                ******************************  启动子线程服务 ************************//
            }
        });
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CameraAlbumTest.class);
                startActivity(intent);
            }
        });
        String editTexts = load();
        if (!TextUtils.isEmpty(editTexts)) {
            editText.setText(editTexts);
            // 设置第一位光标
            editText.setSelection(editTexts.length());
        }
//        ************************ 服务代码 ************************************//

        // ************************ 拨打电话 **********************************//
        btnDial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CALL_PHONE)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL_PHONE);
                } else {
                    callPhone();
                }
                // 直接打开拨号盘,不需要动态申请权限
//                Intent callIntent = new Intent(Intent.ACTION_DIAL);
//                startActivity(callIntent);
            }
        });
        // **********************  查询手机联系人 *****************************//
        btnContract.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent phoneContractIntent = new Intent(MainActivity.this,ShowPhoneContractActivity.class);
                startActivity(phoneContractIntent);
            }
        });
    }

    /**
     * 电话拨打类
     */
    private void callPhone() {
        // 拨打电话内容 （直接拨打电话）
        Intent dialIntent = new Intent(Intent.ACTION_CALL);
        dialIntent.setData(Uri.parse("tel:18572536320"));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        startActivity(dialIntent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            // 拨打手机的动态权限
            case REQUEST_CALL_PHONE:
                if (grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    callPhone();
                }else {
                    Toast.makeText(MainActivity.this,"用户关闭打电话权限",Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(netWorkChangeReceiver);
        String inputs = editText.getText().toString();
        saveEditText(inputs);
    }

    /**
     * 保存到文件中
     *
     * @param inputs
     */
    private void saveEditText(String inputs) {
        FileOutputStream outputStream = null;
        BufferedWriter writer = null;
        try {
            outputStream = openFileOutput("data", Context.MODE_PRIVATE);
            writer = new BufferedWriter(new OutputStreamWriter(outputStream));
            writer.write(inputs);
            Log.d(TAG, "saveEditText: 保存的内容" + inputs);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                // 关闭数据流
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 从文件中加载
     *
     * @return
     */
    private String load() {

        FileInputStream inputStream = null;
        BufferedReader reader = null;
        StringBuilder content = new StringBuilder();
        try {
            inputStream = openFileInput("data");
            reader = new BufferedReader(new InputStreamReader(inputStream));
            String line = "";
            while ((line = reader.readLine()) != null) {
                content = content.append(line);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content.toString();
    }

    class NetWorkChangeReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Toast.makeText(MainActivity.this, "网络开关", Toast.LENGTH_LONG).show();
        }
    }
}
