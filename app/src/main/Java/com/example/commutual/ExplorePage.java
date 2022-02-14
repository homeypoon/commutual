package com.example.commutual;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ExplorePage extends AppCompatActivity {

    private RecyclerView postRecyclerView;
    private PostRecyclerViewAdapter postRecyclerViewAdapter;
    private List<AccountabilityPost> postList;
    private DividerItemDecoration mDividerItemDecoration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.explore_page);

        postList = new ArrayList<>();
        postRecyclerView = (RecyclerView) findViewById(R.id.explore_recycler_view);

        postRecyclerViewAdapter = new PostRecyclerViewAdapter(postList);
        postRecyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        postRecyclerView.setLayoutManager(layoutManager);
        postRecyclerView.setItemAnimator(new DefaultItemAnimator());
        postRecyclerView.setAdapter(postRecyclerViewAdapter);

        mDividerItemDecoration = new DividerItemDecoration(postRecyclerView.getContext(),
                layoutManager.getOrientation());
        postRecyclerView.addItemDecoration(mDividerItemDecoration);

        postRecyclerViewAdapter.setOnPostClickListener(new PostClickListener<AccountabilityPost>() {
            @Override
            public void onClick(View view, AccountabilityPost data, int position) {
                Log.d("Explore Page", "clicked on item " + position);
                Toast.makeText(getApplicationContext(),"text",Toast.LENGTH_SHORT).show();
            }
        });

        prepareItems();

    }

    private void prepareItems() {
        for (int i = 0; i < 20; i++) {
            AccountabilityPost accountabilityPost = new AccountabilityPost("Post Name " + i, "Post Description " + i, "Username " + i);
            postList.add(accountabilityPost);


        }
    }

}
