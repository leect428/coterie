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
import com.usc.model.User;

import java.util.ArrayList;

public class GroupsAdapter extends RecyclerView.Adapter<GroupsAdapter.GroupsViewHolder> {

    private ArrayList<Group> groups;
    private ArrayList<Group> allGroups;
    private boolean mine;
    private String username;
    private AppCompatActivity activity;

    public static class GroupsViewHolder extends RecyclerView.ViewHolder {
        public ConstraintLayout view;
        public GroupsViewHolder(ConstraintLayout v) {
            super(v);
            view = v;
        }
    }

    public GroupsAdapter(ArrayList<Group> d, ArrayList<Group> g, boolean m, String u, AppCompatActivity a) {
        allGroups = d;
        groups = g;
        mine = m;
        username = u;
        activity = a;
    }

    @Override
    public GroupsAdapter.GroupsViewHolder onCreateViewHolder(ViewGroup parent,
                                                         int viewType) {
        ConstraintLayout v = (ConstraintLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_group, parent, false);

        GroupsViewHolder vh = new GroupsViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(GroupsViewHolder holder, int position) {
        ImageView image = holder.view.findViewById(R.id.group_icon);
        TextView groupName = holder.view.findViewById(R.id.group_name);
        TextView groupDescription = holder.view.findViewById(R.id.group_description);
        Button button = holder.view.findViewById(R.id.group_button);
        final Group g = groups.get(position);

        if (mine) {
            button.setText("Leave");
        } else {
            button.setText("Join");
        }

        if (g.getImage() != null && !g.getImage().equals("")) {
            Picasso.get().load(g.getImage()).fit().centerCrop().into(image);
        }

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(activity, GroupActivity.class);
                i.putExtra("GROUP_NAME", g.getName());
                i.putExtra("GROUP_MEMBERS", g.getMembers().size());
                i.putExtra("GROUP_IMAGE", g.getImage());
                activity.startActivity(i);
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("final-project-a2b32");

                Group g2 = null;
                for(Group t: allGroups){
                    if(t.getName().equals(g.getName())){
                        g2 = t;
                    }
                }
                if (mine) {
                    for (int i = g2.getMembers().size()-1; i >= 0; i--) {
                        if (g2.getMembers().get(i).getUsername().equals(username)) {
                            g2.getMembers().remove(i);
                        }
                    }
                } else {
                    g2.getMembers().add(new User(username, "", ""));
                }

                mDatabase.child("groups").setValue(allGroups);
                Intent i = new Intent(activity, GroupsActivity.class);
                activity.startActivity(i);
                activity.finish();
            }
        });

        groupName.setText(g.getName());
        groupDescription.setText(g.getDescription());
    }

    @Override
    public int getItemCount() {
        return groups.size();
    }
}
