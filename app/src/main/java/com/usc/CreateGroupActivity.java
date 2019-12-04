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
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.usc.adapters.GroupsAdapter;
import com.usc.model.Group;
import com.usc.model.User;

import java.util.ArrayList;

public class CreateGroupActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_group_activity);
        final Button create = (Button) findViewById(R.id.create_group_button);
        final EditText name = (EditText) findViewById(R.id.group_name);
        final EditText description = (EditText) findViewById(R.id.group_description);
        final EditText image = (EditText) findViewById(R.id.group_image);
        final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("final-project-a2b32");

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatabase.child("groups").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        ArrayList<Group>  groups = new ArrayList<Group>();
                        if (dataSnapshot.exists()) {
                            groups = (ArrayList<Group>) dataSnapshot.getValue();
                        }
                        Group g = new Group();
                        g.setDescription(description.getText().toString());
                        g.setName(name.getText().toString());
                        g.setImage(image.getText().toString());
                        SharedPreferences sharedPref = getSharedPreferences("mysettings", Context.MODE_PRIVATE);

                        User u = new User(sharedPref.getString("USERNAME", ""), "", "");
                        g.addMember(u);
                        groups.add(g);
                        mDatabase.child("groups").setValue(groups);

                        Intent i = new Intent(CreateGroupActivity.this, GroupsActivity.class);
                        startActivity(i);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
            }
        });
    }
}
