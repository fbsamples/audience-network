package com.fb.audiencenetwork.scrollapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;


import com.facebook.ads.*;

public class MainActivity extends AppCompatActivity implements NasaRequester.NasaResponse {
    private ArrayList<NasaPost> mPosts;
    private NasaRequester mImageRequester;

    private RecyclerAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;

    private NativeAdsManager mAds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPosts = new ArrayList<>();
        mImageRequester = new NasaRequester(this);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        LoadAds();

        mAdapter = new RecyclerAdapter(mPosts, this, mAds);
        mRecyclerView.setAdapter(mAdapter);

        setRecyclerViewScrollListener();
        // setRecyclerViewItemTouchListener();

    }

    private void requestPost() {
        try {
            mImageRequester.getPost();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void receivedPost(final NasaPost post) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mPosts.add(post);
                mAdapter.notifyItemInserted(mPosts.size());
            }
        });
    }

    private int getLastVisibleItemPosition() {
        return mLayoutManager.findLastVisibleItemPosition();
    }

    private void setRecyclerViewScrollListener() {
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                int totalItemCount = mRecyclerView.getLayoutManager().getItemCount();
                if (!mImageRequester.isLoadingData() && totalItemCount == getLastVisibleItemPosition() + 1) {
                    requestPost();
                }
            }
        });
    }

    private void setRecyclerViewItemTouchListener() {
        ItemTouchHelper.SimpleCallback itemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder viewHolder1) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                //3
                int position = viewHolder.getAdapterPosition();
                mPosts.remove(position);
                mRecyclerView.getAdapter().notifyItemRemoved(position);
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(itemTouchCallback);
        itemTouchHelper.attachToRecyclerView(mRecyclerView);
    }

    public void LoadAds() {
        String placement_id = "YOUR_PLACEMENT_ID";
        mAds = new NativeAdsManager(this, placement_id, 1);
        mAds.loadAds();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mPosts.size() == 0) {
            requestPost();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
    }
}
