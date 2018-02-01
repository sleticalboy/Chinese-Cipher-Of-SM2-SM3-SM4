package com.example.smscypher;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.example.method.SM4;
import com.example.ways.JustTest;

import java.util.ArrayList;

/**
 * 入口类
 */
public class MainActivity extends PermissionCheckActivity {

    private CheckBox cbCbc;
    private CheckBox cbEcb;
    private int mode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        cbCbc = findViewById(R.id.cb_cbc);
        cbEcb = findViewById(R.id.cb_ecb);
        cbCbc.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    cbEcb.setChecked(false);
                    mode = SM4.ENCRYPT_MODE_CBC;
                }
            }
        });
        cbEcb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    cbCbc.setChecked(false);
                    mode = SM4.ENCRYPT_MODE_ECB;
                }
            }
        });
        if (cbEcb.isChecked()) {
            mode = SM4.ENCRYPT_MODE_ECB;
        }
        if (cbCbc.isChecked()) {
            mode = SM4.ENCRYPT_MODE_CBC;
        }
    }

    // 发送加密短信
    public void click1(View v) {
        Intent intent = new Intent(this, SenderActivity.class);
        intent.putExtra("mode", mode);
        startActivity(intent);
    }

    // 破解加密短信
    public void click2(View v) {
        Intent intent = new Intent(this, ReceiverActivity.class);
        intent.putExtra("mode", mode);
        startActivity(intent);
    }

    // 秘钥生成
    public void click3(View v) {
        Intent intent = new Intent(this, CreateKeyActivity.class);
        startActivity(intent);
    }

    // 口令生成
    public void click4(View v) {
        Toast.makeText(getApplicationContext(), "正在生成中，请稍后。。。", Toast.LENGTH_SHORT).show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                final JustTest test = new JustTest();
                final String filePath = Environment.getExternalStorageDirectory() + "/Test/";
                final String fileName = "数据源.txt";
                final ArrayList<String> testArray = test.getArr();
                for (int i = 0; i < testArray.size(); i++) {
                    test.writeTxtToFile(testArray.get(i), filePath, fileName);
                }
            }
        }).start();
        Toast.makeText(getApplicationContext(), "已保存到私有目录下", Toast.LENGTH_LONG).show();
    }
}
