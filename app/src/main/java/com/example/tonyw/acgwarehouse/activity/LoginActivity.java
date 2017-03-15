package com.example.tonyw.acgwarehouse.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.tonyw.acgwarehouse.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static com.example.tonyw.acgwarehouse.R.id.userPassword;
import static com.example.tonyw.acgwarehouse.utils.ConstantUtils.CHECK_PASS;
import static com.example.tonyw.acgwarehouse.utils.HttpUtils.getHttpBitmap;
import static com.example.tonyw.acgwarehouse.utils.HttpUtils.getJsonData;
import static com.example.tonyw.acgwarehouse.utils.MessageUtils.getPasswordMD5;
import static com.example.tonyw.acgwarehouse.utils.MessageUtils.sendMessage;

/**
 * 登录模块
 */

public class LoginActivity extends AppCompatActivity{
    Button regButton;
    Button loginButton;
    EditText loginUserName;
    EditText loginUserPassword;
    Boolean isLoginUserName=false;
    Boolean isLoginUserPassword=false;
    Boolean isValidate=false;
    ProgressDialog mProgressDialog;
    public String checkValidate="";
    public String userName="";
    public Bitmap userAvatarBitmap;
    public static Activity loginActivity=null;
    private Handler mHandler=new Handler()
    {
        public void handleMessage(Message msg)
        {
            switch (msg.what)
            {
                case CHECK_PASS:
                    if(isValidate)
                    {
                        try {
                            mProgressDialog.dismiss();
                            finish();
                            ByteArrayOutputStream baos=new ByteArrayOutputStream();
                            userAvatarBitmap.compress(Bitmap.CompressFormat.JPEG,100,baos);
                            byte[] bitmapByte=baos.toByteArray();
                            Intent it=new Intent("LoginActivity");
                            it.putExtra("bitmap",bitmapByte);
                            it.putExtra("userName",userName);
                            sendBroadcast(it);
                            baos.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        break;
                    }
                    else
                    {
                        mProgressDialog.dismiss();
                        Toast.makeText(getApplicationContext(),"用户名或密码错误",Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        regButton= (Button) findViewById(R.id.regButton);
        loginActivity=this;
        mProgressDialog=new ProgressDialog(this);
        loginButton= (Button) findViewById(R.id.loginButton);
        loginUserName= (EditText) findViewById(R.id.userName);
        loginButton.setEnabled(false);
        loginUserPassword= (EditText) findViewById(userPassword);
        loginUserName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
            @Override
            public void afterTextChanged(Editable s) {
                if(s.length()>0)
                {
                    isLoginUserName=true;
                    if(isLoginUserPassword)
                    {
                        loginButton.setEnabled(true);
                        loginButton.setTextColor(Color.BLACK);
                    }
                }
                else
                {
                    isLoginUserName=false;
                    loginButton.setEnabled(false);
                    loginButton.setTextColor(Color.parseColor("#949494"));
                }
            }
        });
        loginUserPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length()>0)
                {
                    isLoginUserPassword=true;
                    if(isLoginUserName)
                    {
                        loginButton.setEnabled(true);
                        loginButton.setTextColor(Color.BLACK);
                    }
                }
                else
                {
                    isLoginUserPassword=false;
                    loginButton.setEnabled(false);
                    loginButton.setTextColor(Color.parseColor("#949494"));
                }
            }
        });
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProgressDialog.show();
                mProgressDialog.setMessage("登录中");
                mProgressDialog.setCanceledOnTouchOutside(false);
                new Thread(new CheckUserValidate()).start();
            }
        });

        regButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });
        Toolbar tb= (Toolbar) findViewById(R.id.login_toolbar);
        setSupportActionBar(tb);
        tb.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    //用于检查用户身份
    private class CheckUserValidate implements Runnable{
        @Override
        public void run() {
            String name=loginUserName.getText().toString();
            String password=getPasswordMD5(loginUserPassword.getText().toString());
            String path="http://tonywu10.imwork.net:16284/ACGWarehouse/UserLoginDemo?userName="+name+"&userPassword="+password;
            checkValidate=getJsonData(path);
            try {
                JSONArray jsonArray = new JSONArray(checkValidate);
                for (int i=0;i<jsonArray.length();i++)
                {
                    JSONObject jsonObject=jsonArray.getJSONObject(i);
                    if(jsonObject.getInt("exist")==1)
                    {
                        isValidate=true;
                        userAvatarBitmap=getHttpBitmap("http://tonywu10.imwork.net:16284/ACGWarehouse/img"+jsonObject.getString("userAvatar")+".jpg");
                        userName=jsonObject.getString("userName");
                    }
                    else
                    {
                        isValidate=false;
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            sendMessage(mHandler,CHECK_PASS);
        }
    }
}
