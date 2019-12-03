package com.usc;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        TextView register = (TextView) findViewById(R.id.login_register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(i);
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                System.out.println(database.getReference().getKey());
                DatabaseReference myRef = database.getReference("message");

                myRef.setValue("Hello, World!");
            }
        });

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        System.out.println(database.getReference().getKey());
        DatabaseReference myRef = database.getReference("message");

        myRef.setValue("Hello!");


    }
}

