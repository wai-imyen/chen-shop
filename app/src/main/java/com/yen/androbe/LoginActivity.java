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
import com.yen.androbe.object.FormError;
import com.yen.androbe.object.JsonBean;
import com.yen.androbe.object.Login;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoginActivity extends BaseActivity {

    private EditText account;
    private EditText password;
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // 初始化
        setToolbar();
        setupNav();

        account = findViewById(R.id.account);
        password = findViewById(R.id.password);
        Button submit = findViewById(R.id.submit);
        Button register = findViewById(R.id.register);
        TextView login = findViewById(R.id.forgotten);

        // 取得 SP
        sp = getSharedPreferences("customer", MODE_PRIVATE);

        // 檢查是否已為登入狀態
        String accountToken = sp.getString("accountToken", "");
        if ( ! accountToken.isEmpty()){
            Intent intent = new Intent();
            intent.setClass(LoginActivity.this, AccountEditActivity.class);
            startActivity(intent);
            finish();
        }

        // 點選註冊
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
//                LoginActivity.this.finish();
            }
        });

        // 點選忘記密碼
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(LoginActivity.this, ForgottenActivity.class);
                startActivity(intent);
            }
        });

        // 點選登入，送出表單
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String accountText = account.getText().toString() ;
                final String passwordText = helper.encrypt(password.getText().toString()) ; // 加密

                // 初始化 Request 序列
                RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);

                // Api 網址
                String url = config.getHost() + "/api/users/login?token=" + config.getToken();

                // Post - 登入會員
                StringRequest stringPostRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        boolean status = false;
                        String message = "登入失敗";

//                        Log.d("tag", "結果：" + response);

                        // 解析 Json
                        Gson gson = new Gson();
                        Type objectType = new TypeToken<JsonBean<Login>>() {}.getType();
                        JsonBean<Login> json_data = gson.fromJson(response, objectType);

                        // 取得Login
                        Login login = json_data.getData();

                        if (login != null){

                            // 取得 FormError
                            FormError formError = login.getErrors();

                            // 取得 accountToken & customerId
                            String accountToken = login.getAccountToken();
                            Integer customerId = login.getCustomer_id();


                            if (accountToken != null && !accountToken.isEmpty()){

                                status = true;

                                // 記入 SP
                                sp.edit()
                                .putBoolean("isLogged", true)
                                .putInt("customerId", customerId)
                                .putString("accountToken", accountToken)
                                .apply();

                                new AlertDialog.Builder(LoginActivity.this)
                                        .setTitle("會員登入")
                                        .setMessage("登入成功")
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                Intent intent = new Intent();
                                                intent.setClass(LoginActivity.this, AccountEditActivity.class);
                                                startActivity(intent);
                                                finish();
                                            }
                                        })
                                        .show();
                            }else{

                                // 是否有表單錯誤訊息
                                if (formError != null){
                                    String errorForm = formError.getForm();

                                    if (errorForm != null && !errorForm.isEmpty()){
                                        message = formError.getForm();
                                    }
                                }
                            }
                        }
                        if (!status){
                            new AlertDialog.Builder(LoginActivity.this)
                                    .setTitle("會員登入")
                                    .setMessage(message)
                                    .setPositiveButton("OK", null)
                                    .show();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                        Log.d("login", error.getMessage());
                    }
                }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        // 傳遞參數
                        Map<String,String> map = new HashMap<String,String>();
                        map.put("email",accountText);
                        map.put("password", passwordText);
                        return map;
                    }
                };

                queue.add(stringPostRequest);
            }
        });
    }
}
