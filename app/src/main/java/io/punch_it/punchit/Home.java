package io.punch_it.punchit;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class Home extends Fragment implements MyRecyclerAdapterHome.FeedButtonEvents, SwipeRefreshLayout.OnRefreshListener {


    private final static String TAG = Home.class.getSimpleName();
    private RecyclerView mRecyclerView;
    private ArrayList<HomeFeed> feedsList = new ArrayList<>();
    private MyRecyclerAdapterHome adapter;
    private SpacesHomeFeed spacesHomeFeed;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ArrayList<String> objectIds = new ArrayList<>();
    List<String> interests;
    String question, name, post1, post2;
    int like1, like2;
    ParseFile image1, image2, ProfilePicture;

    public Home() {
        //Required for creating instance of class in main five fragments display...
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        interests = Arrays.asList(ParseCloudApp.getInterestItems().split(","));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.home_fragment, container, false);
        mRecyclerView = (RecyclerView) v.findViewById(R.id.recyclerView_home);
        swipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);
        spacesHomeFeed = new SpacesHomeFeed(16);
        adapter = new MyRecyclerAdapterHome(getActivity(), feedsList);
        adapter.setClickListener(this);
        mRecyclerView.addItemDecoration(spacesHomeFeed);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(adapter);
        return v;
    }

    public void fetchContents() {
        swipeRefreshLayout.setRefreshing(true);

        final ParseQuery<ParseObject> query = ParseQuery.getQuery("Posts");
        query.setLimit(5);
        query.whereContainedIn("TargetIntrests", interests);
        query.whereNotContainedIn("objectId", objectIds);
        query.orderByDescending("createdAt");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> feedList, ParseException e) {
                if (e == null) {
                    for (ParseObject singleFeed : feedList) {
                        try {
                            objectIds.add(singleFeed.getObjectId());
                            question = singleFeed.get("Title").toString();
                            Date date = singleFeed.getCreatedAt();
                            ParseUser user = (ParseUser) singleFeed.get("By");
                            name = user.fetchIfNeeded().getString("Ninja_name");
                            post1 = singleFeed.get("Image1Title").toString();
                            post2 = singleFeed.get("Image2Title").toString();

                            @SuppressWarnings("unchecked")
                            ArrayList<String> numberOfLikes1 = (ArrayList<String>) singleFeed.get("Punchers1");

                            @SuppressWarnings("unchecked")
                            ArrayList<String> numberOfLikes2 = (ArrayList<String>) singleFeed.get("Punchers2");

                            like1 = (numberOfLikes1 != null) ? numberOfLikes1.size() : 0;
                            like2 = (numberOfLikes2 != null) ? numberOfLikes2.size() : 0;

                            image1 = (ParseFile) singleFeed.get("Image1");
                            image2 = (ParseFile) singleFeed.get("Image2");
                            ProfilePicture = (ParseFile) user.fetchIfNeeded().get("ProfilePicture");
                            user.pinInBackground();
                            feedsList.add(0, new HomeFeed(name, question, post1, post2, date, like1, like2, image1, image2, ProfilePicture));
                            singleFeed.pinInBackground();
                        } catch (ParseException e1) {
                            e1.printStackTrace();
                        }
                        adapter.notifyDataSetChanged();
                    }
                } else {
                    Snackbar.make(getView(), "Network Problems!", Snackbar.LENGTH_SHORT).show();
                }
            }

        });

        //completing the refresh
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ParseQuery<ParseObject> queryOfLocalData = new ParseQuery<>("Posts");
        queryOfLocalData.fromLocalDatastore();
        queryOfLocalData.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> feedList, ParseException e) {
                if (e == null) {
                    for (ParseObject singleFeed : feedList) {
                        try {
                            question = singleFeed.get("Title").toString();
                            Date date = singleFeed.getCreatedAt();
                            ParseUser user = (ParseUser) singleFeed.get("By");
                            name = user.fetchIfNeeded().getString("Ninja_name");
                            post1 = singleFeed.get("Image1Title").toString();
                            post2 = singleFeed.get("Image2Title").toString();

                            @SuppressWarnings("unchecked")
                            ArrayList<String> numberOfLikes1 = (ArrayList<String>) singleFeed.get("Punchers1");

                            @SuppressWarnings("unchecked")
                            ArrayList<String> numberOfLikes2 = (ArrayList<String>) singleFeed.get("Punchers2");

                            like1 = (numberOfLikes1 != null) ? numberOfLikes1.size() : 0;
                            like2 = (numberOfLikes2 != null) ? numberOfLikes2.size() : 0;

                            image1 = (ParseFile) singleFeed.get("Image1");
                            image2 = (ParseFile) singleFeed.get("Image2");
                            ProfilePicture = (ParseFile) user.fetchIfNeeded().get("ProfilePicture");
                            feedsList.add(0, new HomeFeed(name, question, post1, post2, date, like1, like2, image1, image2, ProfilePicture));
                        } catch (ParseException e1) {
                            e1.printStackTrace();
                        }
                        adapter.notifyDataSetChanged();
                    }
                } else {
                    Snackbar.make(getView(), "Network Problems!", Snackbar.LENGTH_SHORT).show();
                }
            }
        });

        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
                fetchContents();
            }
        });


    }

    @Override
    public void reportSpam(int position) {
        Snackbar.make(mRecyclerView, "Reporting Spam!", Snackbar.LENGTH_SHORT).show();
        if (feedsList != null)
            feedsList.remove(position);
        adapter.notifyItemRemoved(position);
        Snackbar.make(mRecyclerView, "Reported!", Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void shareFeed(int position) {
        Intent intent = new Intent(getContext(), SampleCommentsPage.class);
        intent.putExtra("Position", position + " clicked for sharing.....");
        startActivity(intent);
    }

    @Override
    public void commentPage(int position) {
        Intent intent = new Intent(getContext(), SampleCommentsPage.class);
        intent.putExtra("Position", position + " clicked for comments.....");
        startActivity(intent);
    }

    @Override
    public void onRefresh() {
        fetchContents();
    }
}
