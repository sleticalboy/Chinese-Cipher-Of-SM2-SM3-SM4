package com.example.smscypher;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.method.SM4Utils;

import java.util.ArrayList;

public class SenderActivity extends PermissionCheckActivity {

    private Context mContext;
    private EditText et_number;
    private EditText et_content;
    private EditText et_inputKey;
    private Integer flag = 0;

    private int mode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sender);

        if (getIntent() != null) {
            mode = getIntent().getIntExtra("mode", -1);
        }

        mContext = this;

        et_number = (EditText) findViewById(R.id.et_number);
        et_content = (EditText) findViewById(R.id.et_content);
        et_inputKey = (EditText) findViewById(R.id.et_inputKey);

    }

    // 联系人界面
    public void add(View v) {
        Intent intent = new Intent(this, ContactsActivity.class);
        //startActivity(intent);
        startActivityForResult(intent, 1); //先写成1
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        //请求码和结果码，习惯哪个就用哪个就可以了，一个是第一界面的，一个是第二界面的
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 10) {
            String phone = data.getStringExtra("phone");
            et_number.setText(phone);
        }
    }

    // 加密短信内容
    public void clickEvent(View v) {
        String content = et_content.getText().toString().trim();
        String key = et_inputKey.getText().toString().trim();

        SM4Utils sm4Utils = SM4Utils.getInstance();
        if (pendingContent_edit(content) && pendingKey(key)) {
            if (flag++ % 2 == 0) {
                String encryptStr = sm4Utils.getEncryptStr(content, key, mode);
                et_content.setText(encryptStr);
            } else {
                String decryptStr = sm4Utils.getDecryptStr(content, key, mode);
                et_content.setText(decryptStr);
            }
        }
    }

    public void sendMessage(View v) {
        String number = et_number.getText().toString().trim();
        String content = et_content.getText().toString().trim();
        String key = et_inputKey.getText().toString().trim();

        if (pendingNumber(number) && pendingContent_send(content) && pendingKey(key)) {
            SmsManager smsManager = SmsManager.getDefault();

            //这段代码是表示，如果短信字数过于长可以分段发送
            ArrayList<String> divideMessages = smsManager.divideMessage(content);
            for (String div : divideMessages) {
                smsManager.sendTextMessage(number, null, div, null, null);
            }
            Toast.makeText(mContext, "发送成功", Toast.LENGTH_SHORT).show();
            this.finish();
        }
    }


    /**
     * 实现方法
     */
    public Boolean pendingNumber(String number) {
        if (number.length() == 0) {
            Toast.makeText(mContext, "请输入联系人号码", 0).show();
            return false;
        }
        return true;
    }

    public Boolean pendingContent_edit(String content) {
        if (content.length() == 0) {
            Toast.makeText(mContext, "请编辑短信", 0).show();
            return false;
        }
        return true;
    }

    public Boolean pendingContent_send(String content) {
        if (content.length() == 0) {
            Toast.makeText(mContext, "请输入要发送的短信内容", 0).show();
            return false;
        }
        return true;
    }

    public Boolean pendingKey(String key) {
        if (key.length() == 0) {
            Toast.makeText(mContext, "请输入秘钥", 0).show();
            return false;
        }
        return true;
    }
}
