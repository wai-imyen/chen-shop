package com.yen.androbe;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class AccountEditActivity extends BaseActivity {

    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_edit);

        setToolbar();
        setupNav();

        Button logout = findViewById(R.id.logout);
        TextView email = findViewById(R.id.textEmail);

        sp = getSharedPreferences("customer", MODE_PRIVATE);

        String login_account = sp.getString("account", "");
        email.setText("帳號：" + login_account);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sp.edit().clear().commit();
                new AlertDialog.Builder(AccountEditActivity.this)
                        .setTitle("Logout")
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
//                Toast.makeText(getApplicationContext(), "登出！" , Toast.LENGTH_LONG).show();
            }
        });
    }
}
