package com.usc;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.system.Os;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.usc.model.User;

import java.sql.*;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        final TextView loginError = (TextView) findViewById(R.id.login_error);
        final EditText username = (EditText) findViewById(R.id.username);
        final EditText password = (EditText) findViewById(R.id.password);
        final EditText confirm = (EditText) findViewById(R.id.confirm_password);
        final EditText phone = (EditText) findViewById(R.id.phone_number);
        final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("final-project-a2b32");

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
                if (!password.getText().toString().equals(confirm.getText().toString())) {
                    loginError.setText("Passwords Do Not Match");
                    loginError.setVisibility(View.VISIBLE);
                    return;
                }
                mDatabase.child("users").child(username.getText().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            loginError.setVisibility(View.VISIBLE);
                            loginError.setText("Username Taken");
                        } else {
                            User u = new User(username.getText().toString(), password.getText().toString(), phone.toString());
                            mDatabase.child("users").child(username.getText().toString()).setValue(u);
                            SharedPreferences sharedPref = getSharedPreferences("mysettings", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPref.edit();
                            editor.putString("USERNAME", username.getText().toString());
                            editor.commit();

                            Intent i = new Intent(RegisterActivity.this, GroupsActivity.class);
                            startActivity(i);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        System.out.println(databaseError.getDetails());
                    }
                });
            }
        });
    }
}
