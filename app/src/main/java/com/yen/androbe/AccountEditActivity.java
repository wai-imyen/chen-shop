package com.yen.androbe;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
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
import java.util.HashMap;
import java.util.Map;

public class AccountEditActivity extends BaseActivity {

    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_edit);

        // 初始化
        setToolbar();
        setupNav();

        final Button logout = findViewById(R.id.logout);
        final TextView email = findViewById(R.id.textEmail);
        final EditText name = findViewById(R.id.name);
        final EditText telephone = findViewById(R.id.telephone);
        Button submit = findViewById(R.id.submit);

        // 取得 SP
        sp = getSharedPreferences("customer", MODE_PRIVATE);

        final String accountToken = sp.getString("accountToken", "");
        final Integer customerId = sp.getInt("customerId", 0);

        // 初始化 Request 序列
        RequestQueue queue = Volley.newRequestQueue(AccountEditActivity.this);

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

            if (customer.getEmail() != null && !customer.getEmail().isEmpty()){
                // 渲染畫面
                email.setText("帳號：" + customer.getEmail());
                name.setText(customer.getName());
                telephone.setText(customer.getTelephone());
            }else{
                logout();
            }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                Log.d("tag", error.getMessage());
            }
        });

        queue.add(stringGetRequest);

        // 點選登出
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String nameText = name.getText().toString() ;
                final String telephoneText = telephone.getText().toString();

                // 初始化 Request 序列
                RequestQueue queue = Volley.newRequestQueue(AccountEditActivity.this);

                // Api 網址
                String url = config.getHost() + "/api/users/" +  customerId +"?token=" + accountToken;

                // Post
                StringRequest stringPostRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        Log.d("tag", "結果：" + response);

                        // 解析 Json
                        Gson gson = new Gson();
                        JsonBean jsonBean = gson.fromJson(response, JsonBean.class);

                        if (jsonBean.getCode().equals("0")){
                            new AlertDialog.Builder(AccountEditActivity.this)
                                    .setTitle("會員修改")
                                    .setMessage("修改成功")
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Intent intent = new Intent();
                                            intent.setClass(AccountEditActivity.this, AccountEditActivity.class);
                                            startActivity(intent);
                                            finish();
                                        }
                                    })
                                    .show();
                        }else{
                            new AlertDialog.Builder(AccountEditActivity.this)
                                    .setTitle("會員修改")
                                    .setMessage("發生錯誤")
                                    .setPositiveButton("OK", null)
                                    .show();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("tag", error.getMessage());
                    }
                }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {

                        Map<String,String> map = new HashMap<String,String>();
                        map.put("name",nameText);
                        map.put("telephone",telephoneText);
                        return map;
                    }
                };

                queue.add(stringPostRequest);
            }
        });
    }

    public void logout(){

        // 清除 SP
        sp.edit().clear().commit();

        new AlertDialog.Builder(AccountEditActivity.this)
                .setTitle("會員登出")
                .setMessage("登出成功").
                setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent();
                        intent.setClass(AccountEditActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                })
                .show();
    }
}
