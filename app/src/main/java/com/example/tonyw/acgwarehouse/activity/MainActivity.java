package com.example.tonyw.acgwarehouse.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.tonyw.acgwarehouse.R;
import com.example.tonyw.acgwarehouse.adapters.HomeViewPagerAdapter;
import com.example.tonyw.acgwarehouse.entity.UserEntity;
import com.example.tonyw.acgwarehouse.utils.CircleImageView;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,View.OnClickListener{
    private ActionBarDrawerToggle mActionBarDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private CircleImageView mLoginButton;
    private UserEntity mUserEntity;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*初始化各控件*/
        ViewPager mViewPager= (ViewPager) findViewById(R.id.vp);
        TabLayout mTabLayout= (TabLayout) findViewById(R.id.tl);
        Toolbar mToolbar= (Toolbar) findViewById(R.id.tb);
        NavigationView mNavigationView= (NavigationView) findViewById(R.id.navigation);
        mActionBarDrawerToggle=new ActionBarDrawerToggle(this,mDrawerLayout,mToolbar,R.string.open,R.string.close);
        mDrawerLayout= (DrawerLayout) findViewById(R.id.drawer_layout);
        mLoginButton= (CircleImageView)findViewById(R.id.pic);
        mLoginButton.setOnClickListener(this);
        /*为TabLayout绑定ViewPager*/
        HomeViewPagerAdapter adapter=new HomeViewPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(adapter);
        mViewPager.setOffscreenPageLimit(1);
        mTabLayout.setupWithViewPager(mViewPager);
        /*为导航栏绑定菜单*/
        mNavigationView.setNavigationItemSelectedListener(this);
        /*设置DrawerLayout的动作*/
        setSupportActionBar(mToolbar);
        mActionBarDrawerToggle=new ActionBarDrawerToggle(this,mDrawerLayout,mToolbar,R.string.open,R.string.close);
        mDrawerLayout.setDrawerListener(mActionBarDrawerToggle);
        mActionBarDrawerToggle.syncState();
        /*使用全局变量userEntity*/
        mUserEntity= (UserEntity) getApplication();
        mUserEntity.setUserName("");
        /*注册广播事件*/
        IntentFilter filter = new IntentFilter("RegisterActivity");
        registerReceiver(mBroadcastReceiver,filter);
        filter = new IntentFilter("LoginActivity");
        registerReceiver(mBroadcastReceiver,filter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_toolbar_menu, menu);
        final SearchView mSearchView = (SearchView) menu.findItem(R.id.search).getActionView();
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if(query.equals(""))
                {
                    return false;
                }
                else
                {
                    Intent it=new Intent(MainActivity.this, SearchActivity.class);
                    try {
                        String name = URLEncoder.encode(query,"utf-8");
                        it.putExtra("videoName",name);
                        MainActivity.this.startActivity(it);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return true;
    }

    /*在此处设置导航栏菜单*/
    @Override
    public boolean onNavigationItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.home:
                mDrawerLayout.closeDrawer(GravityCompat.START);
                mActionBarDrawerToggle.syncState();
                break;
            case R.id.collect:
                if(mUserEntity.getUserName().equals(""))
                {
                    Intent collect_intent=new Intent(MainActivity.this,LoginActivity.class);
                    startActivity(collect_intent);
                }
                else
                {
                    Intent collect_intent=new Intent(MainActivity.this,CollectActivity.class);
                    startActivity(collect_intent);
                }
                break;
            case R.id.about:
                Intent about_intent=new Intent(MainActivity.this,AboutActivity.class);
                startActivity(about_intent);
                break;
            case R.id.logout:
                if(!mUserEntity.getUserName().equals(""))
                {
                    mUserEntity.setUserName("");
                    mLoginButton.setImageResource(R.drawable.login);
                    mLoginButton.setClickable(true);
                    Toast.makeText(this,"已注销",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(this,"不登录就想退出?",Toast.LENGTH_SHORT).show();
                }
                break;
        }
        return true;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig)
    {
        super.onConfigurationChanged(newConfig);
        mActionBarDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.pic:
                Intent it=new Intent(MainActivity.this,LoginActivity.class);
                startActivity(it);
                break;
        }
    }

    BroadcastReceiver mBroadcastReceiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("receive","broad");
            byte[] bis=intent.getByteArrayExtra("bitmap");
            String name=intent.getStringExtra("userName");
            Bitmap bitmap= BitmapFactory.decodeByteArray(bis,0,bis.length);
            mLoginButton.setImageBitmap(bitmap);
            mUserEntity.setUserName(name);
            mLoginButton.setClickable(false);
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mBroadcastReceiver);
    }
}
