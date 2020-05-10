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

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yen.androbe.object.Forgotten;
import com.yen.androbe.object.FormError;
import com.yen.androbe.object.JsonBean;
import com.yen.androbe.object.Password;
import com.yen.androbe.object.Register;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class PasswordActivity extends BaseActivity {

    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);

        // 初始化
        setToolbar();
        setupNav();

        final EditText password = findViewById(R.id.password);
        final EditText new_password = findViewById(R.id.new_password);
        final EditText confirm_password = findViewById(R.id.confirm_password);
        Button submit = findViewById(R.id.submit);

        // 取得 SP
        sp = getSharedPreferences("customer", MODE_PRIVATE);

        final String accountToken = sp.getString("accountToken", "");
        final Integer customerId = sp.getInt("customerId", 0);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String input_password = helper.encrypt(password.getText().toString()) ;
                final String input_new_password = helper.encrypt(new_password.getText().toString());
                final String input_confirm_password = helper.encrypt(confirm_password.getText().toString());

                // 初始化 Request 序列
                RequestQueue queue = Volley.newRequestQueue(PasswordActivity.this);

                // Api 網址
                String url = config.getHost() + "/api/users/" +  customerId + "/password" + "?token=" + accountToken;

                // Post
                StringRequest stringPostRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("tag", "結果：" + response);

                        boolean status = true;
                        String message = "修改成功";

                        // 解析 Json
                        Gson gson = new Gson();
                        Type objectType = new TypeToken<JsonBean<Password>>() {}.getType();
                        JsonBean<Password> jsonBean = gson.fromJson(response, objectType);

                        if (jsonBean.getCode().equals("0")){

                            // 取得 Password
                            Password password = jsonBean.getData();

                            if (password != null){
                                // 取得 FormError
                                FormError formError = password.getErrors();

                                // 是否有表單錯誤訊息
                                if (formError != null){

                                    status = false;

                                    String errorForm = formError.getForm();
                                    String errorPassword = formError.getPassword();
                                    String errorConfirm = formError.getConfirm();
                                    String errorNewPassword = formError.getNew_password();

                                    Log.d("tag", "errorPassword：" + errorPassword);

                                    if (errorForm != null && !errorForm.isEmpty()){
                                        message = errorForm;
                                    }
                                    else if (errorPassword != null && !errorPassword.isEmpty()){
                                        message = errorPassword;
                                    }
                                    else if (errorNewPassword != null && !errorNewPassword.isEmpty()){
                                        message = errorNewPassword;
                                    }
                                    else if (errorConfirm != null && !errorConfirm.isEmpty()){
                                        message = errorConfirm;
                                    }
                                }
                            }
                        }else{
                            status = false;
                        }

                        // 結果
                        final boolean finalStatus = status;
                        new AlertDialog.Builder(PasswordActivity.this)
                                .setTitle("修改密碼")
                                .setMessage(message)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        if (finalStatus){
                                            Intent intent = new Intent();
                                            intent.setClass(PasswordActivity.this, AccountActivity.class);
                                            startActivity(intent);
                                            finish();
                                        }
                                    }
                                })
                                .show();
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
                        map.put("origin",input_password);
                        map.put("new",input_new_password);
                        map.put("confirm",input_confirm_password);
                        return map;
                    }
                };

                queue.add(stringPostRequest);
            }
        });
    }
}
