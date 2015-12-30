package io.punch_it.punchit;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hemal on 12/22/15.
 */
public class MainFiveFragmentDisplay extends AppCompatActivity implements View.OnClickListener {

    final private static String TAG = MainFiveFragmentDisplay.class.getSimpleName();
    private int[] tabIcons = {
            R.mipmap.home_white,
            R.mipmap.notify_white,
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
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new Home(), "HOME");
        adapter.addFrag(new NotificationAcitivty(), "NOTIFICATION");
        viewPager.setAdapter(adapter);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_fragment, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        switch (id){
            case R.id.settings:
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
            case R.id.log_out:
                logOutUser();
                return true;
            case R.id.user_profile:
                startActivity(new Intent(this, UserProfile.class));
                return true;
        }
        return false;
    }

    private void logOutUser() {
        ParseUser.logOut();
        if(ParseUser.getCurrentUser() == null){
            startActivity(new Intent(this, LoginSignupActivity.class));
            MainFiveFragmentDisplay.this.finish();
        }
    }
}
