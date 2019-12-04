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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.usc.adapters.GroupsAdapter;
import com.usc.model.Group;
import com.usc.model.User;

import java.util.ArrayList;

public class GroupsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groups);
        final TextView create = (TextView) findViewById(R.id.create_community);
        final TextView your = (TextView) findViewById(R.id.your_communities);
        final TextView all = (TextView) findViewById(R.id.all_communities);
        final RecyclerView myGroups = (RecyclerView) findViewById(R.id.my_groups);
        final RecyclerView otherGroups = (RecyclerView) findViewById(R.id.other_groups);
        final TextView myAccount = (TextView) findViewById(R.id.my_account);

        final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("final-project-a2b32");
        SharedPreferences sharedPref = getSharedPreferences("mysettings", Context.MODE_PRIVATE);
        final String username = sharedPref.getString("USERNAME", "");

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(GroupsActivity.this, CreateGroupActivity.class);
                startActivity(i);
            }
        });

        myAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(GroupsActivity.this, AccountActivity.class);
                startActivity(i);
            }
        });

        mDatabase.child("groups").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<Group> groups = new ArrayList<>();
                if (dataSnapshot.exists()) {
                    GenericTypeIndicator<ArrayList<Group>> type =new GenericTypeIndicator<ArrayList<Group>>(){};
                    groups = dataSnapshot.getValue(type);
                }

                ArrayList<Group> mine = new ArrayList<Group>();
                ArrayList<Group> other = new ArrayList<Group>();

                for (Group g: groups) {
                    boolean in = false;
                    for (User u : g.getMembers()) {
                        if (u.getUsername().equals(username)) {
                            in = true;
                        }
                    }
                    if (in) {
                        mine.add(g);
                    } else {
                        other.add(g);
                    }
                }

                GroupsAdapter myAdapter = new GroupsAdapter(mine, true, username, GroupsActivity.this);
                myGroups.setAdapter(myAdapter);
                myGroups.setLayoutManager(new LinearLayoutManager(GroupsActivity.this));

                GroupsAdapter otherAdapter = new GroupsAdapter(other, false, username, GroupsActivity.this);
                otherGroups.setAdapter(otherAdapter);
                otherGroups.setLayoutManager(new LinearLayoutManager(GroupsActivity.this));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println(databaseError.getDetails());
            }
        });

        all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                otherGroups.setVisibility(View.VISIBLE);
                myGroups.setVisibility(View.GONE);
            }
        });

        your.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myGroups.setVisibility(View.VISIBLE);
                otherGroups.setVisibility(View.GONE);
            }
        });

    }
}
