package com.yen.androbe;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends BaseActivity {

    private EditText account;
    private EditText password;
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setToolbar();
        setupNav();

        account = findViewById(R.id.account);
        password = findViewById(R.id.password);
        Button submit = findViewById(R.id.submit);
        Button logout = findViewById(R.id.logout);
        Button show = findViewById(R.id.show);
        Button register = findViewById(R.id.register);

        sp = getSharedPreferences("customer", MODE_PRIVATE);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                LoginActivity.this.finish();
            }
        });

        show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String login_account = sp.getString("account", "");
                String message;
                if (login_account == ""){
                    message = "尚未登入！";
                }else{
                    message = "登入帳號: " + login_account;
                }
                new AlertDialog.Builder(LoginActivity.this)
                        .setTitle("Customer")
                        .setMessage(message).
                        setPositiveButton("OK", null)
                        .show();
//                Toast.makeText(getApplicationContext(), message , Toast.LENGTH_LONG).show();
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sp.edit().clear().commit();
                new AlertDialog.Builder(LoginActivity.this)
                        .setTitle("Logout")
                        .setMessage("登出").
                        setPositiveButton("OK", null)
                        .show();
//                Toast.makeText(getApplicationContext(), "登出！" , Toast.LENGTH_LONG).show();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String accountText = account.getText().toString() ;
                if (accountText != ""){
                    sp.edit()
                            .putBoolean("is_logged", true)
                            .putString("account", accountText)
                            .commit();
                    String login_account = sp.getString("account", "");
                    new AlertDialog.Builder(LoginActivity.this)
                            .setTitle("Login")
                            .setMessage("登入成功").
                            setPositiveButton("OK", null)
                            .show();
                }
            }
        });
    }
}
