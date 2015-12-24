package io.punch_it.punchit;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by hemal on 12/22/15.
 */
public class MainPunch extends AppCompatActivity {

    private final static String TAG = MainPunch.class.getSimpleName();
    ActionBar actionBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_punch);
        setUpActivity();
    }

    private void setUpActivity() {
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar_main_punch));
        actionBar = getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }
    }
}