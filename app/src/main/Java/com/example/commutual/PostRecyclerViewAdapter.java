package com.example.commutual;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PostRecyclerViewAdapter extends RecyclerView.Adapter<PostRecyclerViewAdapter.MyViewHolder> {

    private List<AccountabilityPost> postList;
    private PostClickListener postClickListener;

    PostRecyclerViewAdapter(List<AccountabilityPost> mAccountabilityPostList) {
        this.postList = mAccountabilityPostList;
    }

    @Override
    public PostRecyclerViewAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.accountability_post, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PostRecyclerViewAdapter.MyViewHolder holder, int position) {
        final AccountabilityPost accountabilityPost = postList.get(holder.getAdapterPosition());
        holder.postName.setText(accountabilityPost.getPostName());
        holder.postDescription.setText(accountabilityPost.getPostDescription());;
        holder.username.setText(accountabilityPost.getUsername());

        holder.postLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postClickListener.onClick(view, accountabilityPost, holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    public void setOnPostClickListener(PostClickListener postClickListener) {
        this.postClickListener = postClickListener;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView postName, postDescription, username;
        private LinearLayout postLayout;

        public MyViewHolder(View postView) {
            super(postView);
            postName = postView.findViewById(R.id.post_name);
            postDescription = postView.findViewById(R.id.post_description);
            username = postView.findViewById(R.id.username);
            postLayout = postView.findViewById(R.id.accountability_post_layout);
        }
    }

}
