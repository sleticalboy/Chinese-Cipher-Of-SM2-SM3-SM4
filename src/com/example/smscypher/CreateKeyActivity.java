package com.example.smscypher;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.method.SM3Digest;

/**
 * 生成秘钥
 */
public class CreateKeyActivity extends PermissionCheckActivity {

    private Context mContext;
    private EditText et_getKey;
    private Integer flag = 0;
    private String originStr = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activivty_createkey);
        mContext = this;

        et_getKey = (EditText) findViewById(R.id.et_getKey);
    }

    /**
     * 生成/还原秘钥
     */
    public void click1(View v) {
        String key = et_getKey.getText().toString().trim();
        SM3Digest sm3 = new SM3Digest();
        if (pendingContent(key)) {
            if (flag++ % 2 == 0) {
                originStr = key;
                et_getKey.setText(sm3.getKey(key));
            } else {
                et_getKey.setText(originStr);
            }
        }
    }

    public Boolean pendingContent(String content) {
        if (content.length() == 0) {
            Toast.makeText(mContext, "请输入要加密的公钥", 0).show();
            return false;
        }
        return true;
    }

}
