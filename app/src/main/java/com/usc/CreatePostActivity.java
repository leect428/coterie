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
import com.usc.model.Post;
import com.usc.model.User;

import java.util.ArrayList;

public class CreatePostActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_post_activity);
        final String groupName = getIntent().getExtras().getString("GROUP_NAME");
        final int groupMembers = getIntent().getExtras().getInt("GROUP_MEMBERS");
        final Button create = (Button) findViewById(R.id.create_post_button);
        final EditText post = (EditText) findViewById(R.id.post_text);
        final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("final-project-a2b32");
        SharedPreferences sharedPref = getSharedPreferences("mysettings", Context.MODE_PRIVATE);
        final String username = sharedPref.getString("USERNAME", "");

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatabase.child("posts").child(groupName).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        ArrayList<Post>  posts = new ArrayList<Post>();
                        if (dataSnapshot.exists()) {
                            posts = (ArrayList<Post>) dataSnapshot.getValue();
                        }
                        Post p = new Post(post.getText().toString(), System.currentTimeMillis(), new User(username, "", ""));

                        posts.add(p);
                        mDatabase.child("posts").child(groupName).setValue(posts);

                        Intent i = new Intent(CreatePostActivity.this, GroupActivity.class);
                        i.putExtra("GROUP_NAME", groupName);
                        i.putExtra("GROUP_MEMBERS", groupMembers);
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
