package com.rubu.teachapplication;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ShowPhoneContractActivity extends AppCompatActivity {

    private Button mBtnShowContract;
    private ListView mListView;
    private List<String> mList= new ArrayList<>(); ;
    private ArrayAdapter<String> mAdapter ;

    // 获取读取手机联系人权限
    private static final int REQUEST_READ_CONTRACT = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_phone_contract);
        mBtnShowContract = (Button) findViewById(R.id.btn_show_phone_contract);
        mListView = (ListView) findViewById(R.id.lv_contract);
        // 数组适配
        mAdapter = new ArrayAdapter<String>(ShowPhoneContractActivity.this,android.R.layout.simple_list_item_1,mList);
        mListView.setAdapter(mAdapter);
        mBtnShowContract.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 没有获取读取联系人权限
                if (ContextCompat.checkSelfPermission(ShowPhoneContractActivity.this, Manifest.permission.READ_CONTACTS)!= PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(ShowPhoneContractActivity.this,new String[]{Manifest.permission.READ_CONTACTS},REQUEST_READ_CONTRACT);
                }else {
                    readPhoneContract();
                }
            }
        });
    }

    // 读取手机联系人权限
    private void readPhoneContract() {
        // 获取手机联系人游标
        Cursor cursor = null;
        // 查询联系人数据
        cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,null,null,null);
        if (cursor!=null){
            while (cursor.moveToNext()){
                // 获取联系人的名字
                String contractName = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                // 获取联系人的手机号
                String contractPhoneNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                mList.add("用户名:"+contractName+"\n"+"手机号码:"+contractPhoneNumber);
            }
            mAdapter.notifyDataSetChanged();
        }
        // 记住关闭游标的内容
        if (cursor!=null){
            cursor.close();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            // 获取读取联系人 权限
            case REQUEST_READ_CONTRACT:
                if (grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    readPhoneContract();
                }else {
                    Toast.makeText(ShowPhoneContractActivity.this,"权限被禁止",Toast.LENGTH_LONG).show();
                }
                break;
        }
    }
}
