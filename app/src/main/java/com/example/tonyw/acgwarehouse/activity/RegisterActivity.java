package com.example.tonyw.acgwarehouse.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tonyw.acgwarehouse.R;
import com.example.tonyw.acgwarehouse.utils.CircleImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import static com.example.tonyw.acgwarehouse.activity.LoginActivity.loginActivity;
import static com.example.tonyw.acgwarehouse.utils.ConstantUtils.SAME_NAME;
import static com.example.tonyw.acgwarehouse.utils.ConstantUtils.UPLOAD_FINISH;
import static com.example.tonyw.acgwarehouse.utils.HttpUtils.getJsonData;
import static com.example.tonyw.acgwarehouse.utils.HttpUtils.sendUserData;
import static com.example.tonyw.acgwarehouse.utils.MessageUtils.getPasswordMD5;
import static com.example.tonyw.acgwarehouse.utils.MessageUtils.sendMessage;

/**
 * Created by tonyw on 2017/2/16.
 */

public class RegisterActivity extends AppCompatActivity{
    public String checkName="";

    Thread checkNameThread;
    Thread uploadToServer;
    CircleImageView regUserAvatar;
    Bitmap userAvatarBitmap;
    Button submitButton;
    TextView regUserName;
    TextView regPassword;
    TextView regRePassword;
    Boolean isRegUserName=false;
    Boolean isRegPassword=false;
    Boolean isRegRePassword=false;
    Boolean isPassCheck=false;
    TextInputLayout regRePasswordWrapper;
    TextInputLayout regUserNameWrapper;
    ProgressDialog mProgressDialog;
    private Handler mHandler=new Handler()
    {
        public void handleMessage(Message msg)
        {
            switch (msg.what)
            {
                case UPLOAD_FINISH:
                    mProgressDialog.dismiss();
                    finish();
                    loginActivity.finish();
                    ByteArrayOutputStream baos=new ByteArrayOutputStream();
                    userAvatarBitmap.compress(Bitmap.CompressFormat.JPEG,100,baos);
                    byte[] bitmapByte=baos.toByteArray();
                    String name=regUserName.getText().toString().trim();
                    Intent it=new Intent("RegisterActivity");
                    it.putExtra("bitmap",bitmapByte);
                    it.putExtra("userName",name);
                    sendBroadcast(it);
                    Log.d("send","broad");
                    break;
                case SAME_NAME:
                    mProgressDialog.dismiss();
                    Toast.makeText(getApplicationContext(),"用户名已存在",Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };
    public static final int PICK_PHOTO_FOR_AVATAR=100;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        userAvatarBitmap=BitmapFactory.decodeResource(getResources(),R.drawable.akalin);
        regUserAvatar= (CircleImageView) findViewById(R.id.regUserAvatar);
        regUserAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it=new Intent(Intent.ACTION_GET_CONTENT);
                it.setType("image/*");
                startActivityForResult(it,PICK_PHOTO_FOR_AVATAR);
            }
        });
        mProgressDialog=new ProgressDialog(this);
        submitButton= (Button) findViewById(R.id.submitButton);
        submitButton.setEnabled(false);
        regUserName= (TextView) findViewById(R.id.regUserName);
        regPassword= (TextView) findViewById(R.id.regPassword);
        regRePassword= (TextView) findViewById(R.id.regRePassword);
        regRePasswordWrapper= (TextInputLayout) findViewById(R.id.regRePasswordWrapper);
        regUserNameWrapper= (TextInputLayout) findViewById(R.id.regUserNameWrapper);
        regUserName.addTextChangedListener(new TextWatcher() {
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
                    isRegUserName=true;
                    if(isRegPassword&&isRegRePassword)
                    {
                        submitButton.setEnabled(true);
                        submitButton.setTextColor(Color.BLACK);
                    }
                }
                else
                {
                    isRegUserName=false;
                    submitButton.setEnabled(false);
                    submitButton.setTextColor(Color.parseColor("#949494"));
                }
            }
        });
        regPassword.addTextChangedListener(new TextWatcher() {
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
                    isRegPassword=true;
                    if(comparePassword(regRePassword.getText().toString(),s.toString())&&isRegUserName&&isRegRePassword)
                    {
                        submitButton.setEnabled(true);
                        submitButton.setTextColor(Color.BLACK);
                    }
                    else
                    {
                        submitButton.setEnabled(false);
                        submitButton.setTextColor(Color.parseColor("#949494"));
                    }
                }
                else
                {
                    isRegPassword=false;
                    submitButton.setEnabled(false);
                    submitButton.setTextColor(Color.parseColor("#949494"));
                }
            }
        });
        regRePassword.addTextChangedListener(new TextWatcher() {
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
                    isRegRePassword=true;
                    if(comparePassword(regPassword.getText().toString(),s.toString())&&isRegUserName&&isRegPassword)
                    {
                        submitButton.setEnabled(true);
                        submitButton.setTextColor(Color.BLACK);
                    }
                    else
                    {
                        submitButton.setEnabled(false);
                        submitButton.setTextColor(Color.parseColor("#949494"));
                    }
                }
                else
                {
                    isRegRePassword=false;
                    submitButton.setEnabled(false);
                    submitButton.setTextColor(Color.parseColor("#949494"));
                }
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProgressDialog.show();
                mProgressDialog.setMessage("上传中");
                mProgressDialog.setCanceledOnTouchOutside(false);
                checkNameThread=new Thread(new CheckUserName());
                uploadToServer=new Thread(new UploadToServer());
                checkNameThread.start();
                uploadToServer.start();
            }
        });

        Toolbar tb= (Toolbar) findViewById(R.id.register_toolbar);
        setSupportActionBar(tb);
        tb.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_PHOTO_FOR_AVATAR && resultCode == Activity.RESULT_OK) {
            if (data == null) {
                Toast.makeText(getApplicationContext(),"好像出了点故障",Toast.LENGTH_SHORT).show();
                return;
            }
            try {
                InputStream inputStream = getApplicationContext().getContentResolver().openInputStream(data.getData());
                userAvatarBitmap=BitmapFactory.decodeStream(inputStream);
                regUserAvatar.setImageBitmap(userAvatarBitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean comparePassword(String password,String checkPassword)
    {
        Log.d("password",password);
        Log.d("checkPassword",checkPassword);
        if(!password.equals(checkPassword)&&password.length()>0&&checkPassword.length()>0)
        {
            Log.d("check","not same");
            regRePasswordWrapper.setError("两次输入的密码不一致！");
            return false;
        }
        else
        {
            Log.d("check","same");
            regRePasswordWrapper.setError("");
            return true;
        }
    }

    public class UploadToServer implements Runnable{
        @Override
        public void run() {
            try {
                checkNameThread.join();
                if(isPassCheck)
                {
                    ByteArrayOutputStream baos=new ByteArrayOutputStream();
                    userAvatarBitmap.compress(Bitmap.CompressFormat.JPEG,50,baos);
                    String avatar= Base64.encodeToString(baos.toByteArray(), Base64.NO_WRAP);
                    String name=regUserName.getText().toString().trim();
                    String password=getPasswordMD5(regPassword.getText().toString());
                    Log.d("avatar",avatar);
                    String path="http://tonywu10.imwork.net:16284/ACGWarehouse/UserRegDemo";
                    sendUserData(path,name,password,avatar);
                    sendMessage(mHandler,UPLOAD_FINISH);
                }
                else
                {
                    sendMessage(mHandler,SAME_NAME);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public class CheckUserName implements Runnable{
        @Override
        public void run() {
            String userName=regUserName.getText().toString();
            String path="http://tonywu10.imwork.net:16284/ACGWarehouse/UserRegDemo?userName="+userName;
            checkName=getJsonData(path);
            try {
                JSONArray jsonArray = new JSONArray(checkName);
                for (int i=0;i<jsonArray.length();i++)
                {
                    JSONObject jsonObject=jsonArray.getJSONObject(i);
                    Log.d("getJSON", String.valueOf(jsonObject.getInt("exist")));
                    if(jsonObject.getInt("exist")==1)
                    {
                        isPassCheck=false;
                    }
                    else
                    {
                        isPassCheck=true;
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
