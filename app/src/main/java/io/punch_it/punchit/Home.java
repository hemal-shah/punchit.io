package io.punch_it.punchit;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class Home extends Fragment implements MyRecyclerAdapterHome.FeedButtonEvents {


    private final static String TAG = Home.class.getSimpleName();
    private RecyclerView mRecyclerView;
    private ArrayList<HomeFeed> feedsList = new ArrayList<>();
    private MyRecyclerAdapterHome adapter;
    private SpacesHomeFeed spacesHomeFeed;
    private ProgressDialog progressDialog;

    HomeFeed object;
    String[] interest;
    SharedPreferences sp;

    public Home() {
        //Required for now
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sp = getActivity().getSharedPreferences("UserFirstTime", Context.MODE_PRIVATE);
        interest = sp.getString("UserInterest", "").split(",");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.home_fragment, container, false);
        mRecyclerView = (RecyclerView) v.findViewById(R.id.recyclerView_home);

        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final ParseQuery<ParseObject> query = ParseQuery.getQuery("Posts");
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setIndeterminate(true);
        progressDialog.show();
        query.setLimit(10);
        query.whereContainedIn("TargetIntrests", Arrays.asList(interest));
        Log.i(TAG, Arrays.asList(interest).toString());
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> feedList, ParseException e) {
                if (e == null) {
                    Log.i(TAG, String.valueOf(feedList.size()));
                    for (ParseObject singleFeed : feedList) {
                        String question = singleFeed.get("Title").toString();
                        Date date = singleFeed.getCreatedAt();
                        ParseUser user = (ParseUser) singleFeed.get("By");
                        String name = null;
                        try {
                            name = user.fetchIfNeeded().getString("Ninja_name");
                        } catch (ParseException e1) {
                            e1.printStackTrace();
                        }
                        String post1 = singleFeed.get("Image1Title").toString();
                        String post2 = singleFeed.get("Image2Title").toString();

                        @SuppressWarnings("unchecked")
                        ArrayList<String> numberOfLikes1 = (ArrayList<String>) singleFeed.get("Punchers1");

                        @SuppressWarnings("unchecked")
                        ArrayList<String> numberOfLikes2 = (ArrayList<String>) singleFeed.get("Punchers2");

                        int like1 = (numberOfLikes1 != null) ? numberOfLikes1.size() : 0;
                        int like2 = (numberOfLikes2 != null) ? numberOfLikes2.size() : 0;

                        ParseFile image1 = (ParseFile) singleFeed.get("Image1");
                        ParseFile image2 = (ParseFile) singleFeed.get("Image2");
                        ParseFile ProfilePicture = (ParseFile) user.get("ProfilePicture");
                        object = new HomeFeed(name, question, post1, post2, "Sample comment", date, like1, like2,image1,image2,ProfilePicture);
                        feedsList.add(object);
                    }
                } else {
                    Snackbar.make(getView(), "Network Problems!", Snackbar.LENGTH_SHORT).show();
                }
                progressDialog.hide();
            }

        });

        spacesHomeFeed = new SpacesHomeFeed(16);
        adapter = new MyRecyclerAdapterHome(getActivity(), feedsList);
        adapter.setClickListener(this);
        mRecyclerView.addItemDecoration(spacesHomeFeed);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(adapter);
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
}
