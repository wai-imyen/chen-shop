package com.yen.androbe;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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
//        Button show = findViewById(R.id.show);
        Button register = findViewById(R.id.register);
        TextView login = findViewById(R.id.forgotten);

        sp = getSharedPreferences("customer", MODE_PRIVATE);

        String login_account = sp.getString("account", "");
        if ( ! login_account.isEmpty()){
            Intent intent = new Intent();
            intent.setClass(LoginActivity.this, AccountEditActivity.class);
            startActivity(intent);
            finish();
        }

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
//                LoginActivity.this.finish();
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(LoginActivity.this, ForgottenActivity.class);
                startActivity(intent);
            }
        });

//        show.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String login_account = sp.getString("account", "");
//                String message;
//                if (login_account == ""){
//                    message = "尚未登入！";
//                }else{
//                    message = "登入帳號: " + login_account;
//                }
//                new AlertDialog.Builder(LoginActivity.this)
//                        .setTitle("Customer")
//                        .setMessage(message).
//                        setPositiveButton("OK", null)
//                        .show();
////                Toast.makeText(getApplicationContext(), message , Toast.LENGTH_LONG).show();
//            }
//        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String accountText = account.getText().toString() ;
                if ( ! accountText.isEmpty()){
                    sp.edit()
                            .putBoolean("is_logged", true)
                            .putString("account", accountText)
                            .commit();
                    new AlertDialog.Builder(LoginActivity.this)
                            .setTitle("Login")
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
                    new AlertDialog.Builder(LoginActivity.this)
                            .setTitle("Login")
                            .setMessage("登入失敗")
                            .setPositiveButton("OK", null)
                            .show();
                }
            }
        });
    }
}
