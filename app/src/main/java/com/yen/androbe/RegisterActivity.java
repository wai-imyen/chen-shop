package com.yen.androbe;

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
import com.yen.androbe.object.Register;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends BaseActivity {

    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        setToolbar();
        setupNav();

        TextView login = findViewById(R.id.login);
        final EditText account = findViewById(R.id.account);
        final EditText name = findViewById(R.id.name);
        final EditText telephone = findViewById(R.id.telephone);
        final EditText password = findViewById(R.id.password);
        final EditText confirm = findViewById(R.id.confirm);
        Button submit = findViewById(R.id.submit);

        // 取得 SP
        sp = getSharedPreferences("customer", MODE_PRIVATE);

        // 送出註冊表單
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String accountText = account.getText().toString() ;
                final String nameText = name.getText().toString() ;
                final String telephoneText = telephone.getText().toString();
                final String passwordText = helper.encrypt(password.getText().toString()) ; // 加密
                final String confirmText = helper.encrypt(confirm.getText().toString()) ; // 加密

                // 初始化 Request 序列
                RequestQueue queue = Volley.newRequestQueue(RegisterActivity.this);

                // Api 網址
                String url = config.getHost() + "/api/users/register?token=" + config.getToken();

                // Put - 註冊會員
                StringRequest stringPostRequest = new StringRequest(Request.Method.PUT, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        Log.d("tag", "結果：" + response);

                        boolean status = false;
                        String message = "註冊失敗";

                        // 解析 Json
                        Gson gson = new Gson();
                        Type objectType = new TypeToken<JsonBean<Register>>() {}.getType();
                        JsonBean<Register> json_data = gson.fromJson(response, objectType);

                        // 取得 Register
                        Register register = json_data.getData();

                        if (register != null){

                            // 取得 FormError
                            FormError formError = register.getErrors();

                            // 取得 accountToken & customerId
                            String accountToken = register.getAccountToken();
                            Integer customerId = register.getCustomer_id();

                            if (accountToken != null && !accountToken.isEmpty()){

                                status = true;

                                // 記入 SP
                                sp.edit()
                                        .putBoolean("isLogged", true)
                                        .putInt("customerId", customerId)
                                        .putString("accountToken", accountToken)
                                        .apply();

                                new AlertDialog.Builder(RegisterActivity.this)
                                        .setTitle("會員註冊")
                                        .setMessage("您的新帳號已建立，請確認郵件已發送到您所提供的 Eamil 信箱！")
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                Intent intent = new Intent();
                                                intent.setClass(RegisterActivity.this, AccountActivity.class);
                                                startActivity(intent);
                                                finish();
                                            }
                                        })
                                        .show();
                            }else{

                                // 是否有表單錯誤訊息
                                if (formError != null){

                                    String errorForm = formError.getForm();
                                    String errorName = formError.getName();
                                    String errorTelephone = formError.getTelephone();
                                    String errorPassword = formError.getPassword();
                                    String errorConfirm = formError.getConfirm();

                                    if (errorForm != null && !errorForm.isEmpty()){
                                        message = errorForm;
                                    }
                                    else if (errorName != null && !errorName.isEmpty()){
                                        message = errorName;
                                    }
                                    else if (errorTelephone != null && !errorTelephone.isEmpty()){
                                        message = errorTelephone;
                                    }
                                    else if (errorPassword != null && !errorPassword.isEmpty()){
                                        message = errorPassword;
                                    }
                                    else if (errorConfirm != null && !errorConfirm.isEmpty()){
                                        message = errorConfirm;
                                    }
                                }
                            }
                        }

                        if (!status){
                            new AlertDialog.Builder(RegisterActivity.this)
                                    .setTitle("會員註冊")
                                    .setMessage(message)
                                    .setPositiveButton("OK", null)
                                    .show();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                        Log.d("tag", error.getMessage());
                    }
                }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {

                        Map<String,String> map = new HashMap<String,String>();
                        map.put("email",accountText);
                        map.put("name",nameText);
                        map.put("telephone",telephoneText);
                        map.put("password",passwordText);
                        map.put("confirm",confirmText);
//                        map.put("gender","0");
//                        map.put("birthday","2020-02-20");
                        return map;
                    }
                };

                queue.add(stringPostRequest);
            }
        });

        // 點選已有帳號(登入)
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
//                RegisterActivity.this.finish();
            }
        });
    }
}
