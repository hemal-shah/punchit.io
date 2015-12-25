package io.punch_it.punchit;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hemal on 12/22/15.
 */
public class Home extends Fragment {


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
        Log.i(TAG, "oncreate...");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView");
        View v = inflater.inflate(R.layout.home_fragment, container, false);
        mRecyclerView = (RecyclerView) v.findViewById(R.id.recyclerView_home);
        Log.i(TAG, "recyclerview initiated...");
        HomeFeed[] obj = new HomeFeed[5];
        Log.i(TAG, "obj array initiated..");
        for(int i=0; i<5; i++){
            obj[i] = new HomeFeed("Hemal",i+" min", "Which is better name?", "Hemal", "Hemal", "#Please #share.");
            Log.i(TAG, obj[i].toString());
            feedsList.add(obj[i]);
            Log.i(TAG, "object added:=="+i);
        }
        SpacesHomeFeed spacesHomeFeed = new SpacesHomeFeed(16);
        adapter = new MyRecyclerAdapterHome(getContext(), feedsList);
        mRecyclerView.addItemDecoration(spacesHomeFeed);
        Log.i(TAG, "addItemDecoration done");
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        Log.i(TAG, "setlayoutmanager done");
        mRecyclerView.setAdapter(adapter);
        Log.i(TAG, "adapter done...");

        return v;
    }

}
