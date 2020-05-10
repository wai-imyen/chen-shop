package com.yen.androbe;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yen.androbe.object.Customer;
import com.yen.androbe.object.JsonBean;

import java.lang.reflect.Type;

public class AccountActivity extends BaseActivity {

    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        // 初始化
        setToolbar();
        setupNav();

        final LinearLayout EditLayout = findViewById(R.id.EditLayout);
        final LinearLayout PasswordLayout = findViewById(R.id.PasswordLayout);
        final LinearLayout LogoutLayout = findViewById(R.id.LogoutLayout);

        // 取得 SP
        sp = getSharedPreferences("customer", MODE_PRIVATE);

        final String accountToken = sp.getString("accountToken", "");
        final Integer customerId = sp.getInt("customerId", 0);

        // 檢查是否已為登入狀態
        if (accountToken.isEmpty()){
            // 回登入頁
            Intent intent = new Intent();
            intent.setClass(AccountActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();

        }else{
            // 初始化 Request 序列
            RequestQueue queue = Volley.newRequestQueue(AccountActivity.this);

            // Api 網址
            String url = config.getHost() + "/api/users/" + customerId + "?token=" + accountToken;

            // Get  - 取得會員資料
            StringRequest stringGetRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    // 解析 Json
                    Gson gson = new Gson();
                    Type objectType = new TypeToken<JsonBean<Customer>>() {}.getType();
                    JsonBean<Customer> jsonData = gson.fromJson(response, objectType);

                    // 取得會員資料
                    Customer customer = jsonData.getData();

                    if (customer.getEmail() == null || customer.getEmail().isEmpty()){
                        // 清除 SP
                        sp.edit().clear().commit();

                        // 回登入頁
                        Intent intent = new Intent();
                        intent.setClass(AccountActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
//                Log.d("tag", error.getMessage());
                }
            });

            queue.add(stringGetRequest);
        }

        EditLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getApplicationContext(), AccountEditActivity.class);
                startActivity(intent);
            }
        });

        PasswordLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getApplicationContext(), PasswordActivity.class);
                startActivity(intent);
            }
        });

        LogoutLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });
    }

    public void logout(){

        // 清除 SP
        sp.edit().clear().commit();

        new AlertDialog.Builder(AccountActivity.this)
                .setTitle("會員登出")
                .setMessage("登出成功").
                setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent();
                        intent.setClass(AccountActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                })
                .show();
    }
}
