package com.yen.androbe;

import android.content.Intent;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.yen.androbe.library.Config;
import com.yen.androbe.library.GlideImageLoader;
import com.yen.androbe.library.Helper;

public abstract class BaseActivity extends AppCompatActivity {

    public DrawerLayout toolbar_drawer_layout;
    public DrawerLayout drawer_layout;

    protected Toolbar toolbar;
    protected GlideImageLoader imageLoader;
    protected ImageView logo;
    protected ImageView search_icon;
    protected ImageView language_icon;
    protected ImageView cart_icon;
    protected Config config;
    protected Helper helper;

    public NavigationView navigation_view;
    public NavigationView nav;

    @Override
    public void setContentView(int layoutResID) {

        config = new Config();
        config.setHost("https://abba-114-24-181-234.ngrok.io");
        config.setToken("6Cy0==QfiAjLw4SMiojI2JCLiEkI6ICZiwiIlNHb1BXZk92YiojIzJyeXtRP");

        helper = new Helper();

        super.setContentView(layoutResID);
        imageLoader = new GlideImageLoader();
        drawer_layout = findViewById(R.id.drawer_layout);
        toolbar = findViewById(R.id.toolbar);


    }

    public void setToolbar() {
        logo = findViewById(R.id.logo);
        search_icon = findViewById(R.id.search_icon);
        language_icon = findViewById(R.id.language_icon);
        cart_icon = findViewById(R.id.cart_icon);

        // 載入 logo
        imageLoader.displayImage(this, config.getHost() + "/image/catalog/chen.png", logo);

        // 用toolbar做為 APP的ActionBar
        setSupportActionBar(toolbar);
        // 隱藏app name
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        // 將drawerLayout和toolbar整合，會出現「三」按鈕
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, drawer_layout, toolbar,R.string.app_name, R.string.app_name);
        drawer_layout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
    }

    public void setupNav() {
        navigation_view = findViewById(R.id.navigation_view);
        navigation_view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                // 點選時收起選單
                drawer_layout.closeDrawer(GravityCompat.START);
                Intent intent = new Intent();
                switch (menuItem.getItemId()){
                    case R.id.action_home:
//                        Toast.makeText(getApplicationContext(), "首頁", Toast.LENGTH_SHORT).show();
                        intent.setClass(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                        finish();
                        break;
                    case R.id.action_login:
                        intent.setClass(getApplicationContext(), LoginActivity.class);
                        startActivity(intent);
                        break;
                }
                return false;
            }
        });
    }

}
