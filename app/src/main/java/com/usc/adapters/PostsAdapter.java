package com.usc.adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.usc.GroupActivity;
import com.usc.GroupsActivity;
import com.usc.R;
import com.usc.model.Group;
import com.usc.model.Post;
import com.usc.model.User;

import java.util.ArrayList;
import java.util.Date;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.PostsViewHolder> {

    private ArrayList<Post> posts;
    private AppCompatActivity activity;

    public static class PostsViewHolder extends RecyclerView.ViewHolder {
        public ConstraintLayout view;
        public PostsViewHolder(ConstraintLayout v) {
            super(v);
            view = v;
        }
    }

    public PostsAdapter(ArrayList<Post> p, AppCompatActivity a) {
        posts = p;
        activity = a;
    }

    @Override
    public PostsAdapter.PostsViewHolder onCreateViewHolder(ViewGroup parent,
                                                             int viewType) {
        ConstraintLayout v = (ConstraintLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_post, parent, false);

        PostsViewHolder vh = new PostsViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(PostsViewHolder holder, int position) {
        TextView postBody = holder.view.findViewById(R.id.post_body);
        TextView postTime = holder.view.findViewById(R.id.post_time);
        TextView username = holder.view.findViewById(R.id.username);
        final ImageView image = holder.view.findViewById(R.id.user_icon);
        final Post p = posts.get(position);
        final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("final-project-a2b32");


        mDatabase.child("users").child(p.getUser().getUsername()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User u = dataSnapshot.getValue(User.class);
                if (u.getImage() != null && !u.getImage().equals("")) {
                    Picasso.get().load(u.getImage()).fit().centerCrop().into(image);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        postBody.setText(p.getText());
        postTime.setText(new Date(p.getTime()).toString());
        username.setText(p.getUser().getUsername());
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }
}
