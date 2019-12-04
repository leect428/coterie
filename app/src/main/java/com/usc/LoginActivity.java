package com.usc;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Button login = (Button) findViewById(R.id.login_button);
        final TextView register = (TextView) findViewById(R.id.login_register);
        final TextView loginError = (TextView) findViewById(R.id.login_error);
        final EditText username = (EditText) findViewById(R.id.username);
        final EditText passsword = (EditText) findViewById(R.id.password);
        final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("final-project-a2b32");

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatabase.child("users").child(username.getText().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            User u = dataSnapshot.getValue(User.class);
                            if (u.getPassword().equals(passsword.getText().toString())) {

                                SharedPreferences sharedPref = getSharedPreferences("mysettings", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPref.edit();
                                editor.putString("USERNAME", username.getText().toString());
                                editor.commit();

                                Intent i = new Intent(LoginActivity.this, GroupsActivity.class);
                                startActivity(i);
                            } else {
                                loginError.setVisibility(View.VISIBLE);
                                loginError.setText("Incorrect Password");
                            }
                        } else {
                            loginError.setVisibility(View.VISIBLE);
                            loginError.setText("Username Does Not Exist");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(i);
            }
        });
    }

}
