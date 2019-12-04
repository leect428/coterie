package com.usc;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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
import com.usc.adapters.PostsAdapter;
import com.usc.model.Group;
import com.usc.model.Post;
import com.usc.model.User;

import java.util.ArrayList;

public class GroupActivity extends AppCompatActivity {

    private ArrayList<Post> posts = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final String groupName = getIntent().getExtras().getString("GROUP_NAME");
        final int groupMembers = getIntent().getExtras().getInt("GROUP_MEMBERS");
        setContentView(R.layout.activity_group);
        final Button createPost = (Button) findViewById(R.id.create_post);
        final TextView numPosts = (TextView) findViewById(R.id.posts);
        final TextView members = (TextView) findViewById(R.id.members);
        final TextView name = (TextView) findViewById(R.id.group_name);
        final RecyclerView postsR = (RecyclerView) findViewById(R.id.post_recycler);

        final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("final-project-a2b32");
        SharedPreferences sharedPref = getSharedPreferences("mysettings", Context.MODE_PRIVATE);
        final String username = sharedPref.getString("USERNAME", "");
        mDatabase.child("posts").child(groupName).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    GenericTypeIndicator<ArrayList<Post>> type = new GenericTypeIndicator<ArrayList<Post>>() {
                    };
                    posts = dataSnapshot.getValue(type);
                }

                members.setText(groupMembers + "MEMBERS");
                numPosts.setText(posts.size() + " POSTS");

                PostsAdapter adapter = new PostsAdapter(posts, GroupActivity.this);
                postsR.setAdapter(adapter);
                postsR.setLayoutManager(new LinearLayoutManager(GroupActivity.this));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        createPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(GroupActivity.this, CreatePostActivity.class);
                i.putExtra("GROUP_NAME", groupName);
                i.putExtra("GROUP_MEMBERS", groupMembers);
                startActivity(i);
            }
        });

        name.setText(groupName);
    }
}
