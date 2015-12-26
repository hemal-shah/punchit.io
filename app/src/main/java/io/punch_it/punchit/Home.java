package io.punch_it.punchit;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;

public class Home extends Fragment implements MyRecyclerAdapterHome.FeedButtonEvents {


    private final static String TAG = Home.class.getSimpleName();
    private RecyclerView mRecyclerView;
    private ArrayList<HomeFeed> feedsList = new ArrayList<>();
    private MyRecyclerAdapterHome adapter;


    public Home(){
        //Required for now
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.home_fragment, container, false);
        mRecyclerView = (RecyclerView) v.findViewById(R.id.recyclerView_home);

        new Handler().post(new Runnable() {
            @Override
            public void run() {

            }
        });

        SpacesHomeFeed spacesHomeFeed = new SpacesHomeFeed(16);
        adapter = new MyRecyclerAdapterHome(getActivity(), feedsList);
        adapter.setClickListener(this);
        mRecyclerView.addItemDecoration(spacesHomeFeed);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(adapter);
        return v;
    }

    @Override
    public void reportSpam(int position) {
        Snackbar.make(mRecyclerView, "Reporting Spam!", Snackbar.LENGTH_SHORT).show();
        if(feedsList != null)
            feedsList.remove(position);
        adapter.notifyItemRemoved(position);
        Snackbar.make(mRecyclerView, "Reported!", Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void shareFeed(int position) {
        Intent intent = new Intent(getContext(), SampleCommentsPage.class);
        intent.putExtra("Position", position+" clicked for sharing.....");
        startActivity(intent);
    }

    @Override
    public void commentPage(int position) {
        Intent intent = new Intent(getContext(), SampleCommentsPage.class);
        intent.putExtra("Position", position+" clicked for comments.....");
        startActivity(intent);
    }
}
