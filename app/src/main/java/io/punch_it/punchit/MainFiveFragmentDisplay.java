package io.punch_it.punchit;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hemal on 12/22/15.
 */
public class MainFiveFragmentDisplay extends AppCompatActivity implements View.OnClickListener {

    final private static String TAG = MainFiveFragmentDisplay.class.getSimpleName();
    private int[] tabIcons = {
            R.mipmap.home_white,
            R.mipmap.search_white,
            R.mipmap.notify_white,
            R.mipmap.settings_white
    };

    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_five_fragment);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        fab = (FloatingActionButton) findViewById(R.id.fab_main_five_fragment);
        fab.setOnClickListener(this);
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        setupViewPager(viewPager);

        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout.getTabAt(2).setIcon(tabIcons[2]);
        tabLayout.getTabAt(3).setIcon(tabIcons[3]);
    }

    private void setupViewPager(ViewPager viewPager) {

        Log.i(TAG, "3");
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new Home(), "HOME");
        adapter.addFrag(new Search(), "SEARCH");
        adapter.addFrag(new NotificationAcitivty(), "NOTIFICATION");
        adapter.addFrag(new SettingsActivity(), "SETTINGS");
        viewPager.setAdapter(adapter);

        Log.i(TAG, "4");
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch(id){
            case R.id.fab_main_five_fragment:
                startActivity(new Intent(MainFiveFragmentDisplay.this, MainPunch.class));
                break;
            default:
                break;
        }
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return null;
        }
    }
}
