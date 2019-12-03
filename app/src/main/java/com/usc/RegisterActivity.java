package com.usc;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.system.Os;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.sql.*;

public class RegisterActivity extends AppCompatActivity {
    private EditText username;
    private EditText password;
    private EditText phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        phone = (EditText) findViewById(R.id.phone_number);

        TextView login = (TextView) findViewById(R.id.register_login);
        Button register = (Button) findViewById(R.id.register_button);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(i);
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });
    }

    private void register() {
        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    Class.forName("com.mysql.jdbc.Driver");
                    Os.setenv("GOOGLE_APPLICATIION_CREDENTIALS", "woof", true);

                    Connection con = DriverManager.getConnection("jdbc:mysql://google/FinalProject?cloudSqlInstance=quick-howl-255400:us-central1:sql-db-1&socketFactory=com.google.cloud.sql.mysql.SocketFactory&useSSL=false&user=lynnebai&password=admin");
                    PreparedStatement stmt = con.prepareStatement("insert into users (username, password, user_tag) values (?,?,?)");
                    stmt.setString(1, username.getText().toString());
                    stmt.setString(2, password.getText().toString());
                    stmt.setString(3, phone.getText().toString());

                    stmt.executeUpdate();
                } catch (Exception e) {
                    e.printStackTrace();;
                }
            }
        });
        thread.start();
    }
}
