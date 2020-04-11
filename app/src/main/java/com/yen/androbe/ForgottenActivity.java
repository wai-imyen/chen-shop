package com.yen.androbe;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.yen.androbe.object.Forgotten;
import com.yen.androbe.object.FormError;
import com.yen.androbe.object.JsonBean;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class ForgottenActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotten);

        // 初始化
        setToolbar();
        setupNav();

        final EditText account = findViewById(R.id.account);
        Button submit = findViewById(R.id.submit);

        account.setText("yen.chen@codepulse.com.tw");

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String accounteText = account.getText().toString();

                // 初始化 Request 序列
                RequestQueue queue = Volley.newRequestQueue(ForgottenActivity.this);

                // Api 網址
                String url = config.getHost() + "/api/users/forgotten?token=" + config.getToken();

                // Post
                StringRequest stringPostRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

//                        Log.d("tag", response);

                        // 解析 Josn
                        Gson gson = new Gson();
                        Type objectType = new TypeToken<JsonBean<Forgotten>>() {}.getType();
                        JsonBean<Forgotten> jsonBean = gson.fromJson(response, objectType);

                        // Forgotten
                        Forgotten forgotten = jsonBean.getData();

                        if (jsonBean.getCode().equals("0")){
                            if (forgotten != null){
                                //FormError
                                FormError formError = forgotten.getErrors();

                                String message = "資料不正確";
                                String formErrorText = formError.getForm();

                                if (formErrorText != null && !formErrorText.isEmpty()){
                                    message = formError.getForm();
                                }
                                new AlertDialog.Builder(ForgottenActivity.this)
                                        .setTitle("忘記密碼")
                                        .setMessage(message)
                                        .setPositiveButton("OK", null)
                                        .show();
                            }else{
                                new AlertDialog.Builder(ForgottenActivity.this)
                                        .setTitle("忘記密碼")
                                        .setMessage("成功：新密碼已發送到您的 Email 信箱！")
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                Intent intent = new Intent();
                                                intent.setClass(ForgottenActivity.this, LoginActivity.class);
                                                startActivity(intent);
                                                finish();
                                            }
                                        })
                                        .show();
                            }
                        }else{
                            new AlertDialog.Builder(ForgottenActivity.this)
                                    .setTitle("忘記密碼")
                                    .setMessage("發生錯誤")
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
                        map.put("email",accounteText);
                        return map;
                    }
                };

                queue.add(stringPostRequest);
            }
        });
    }
}
