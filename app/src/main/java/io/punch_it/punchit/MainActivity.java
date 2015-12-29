package io.punch_it.punchit;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseUser;

public class MainActivity extends AppCompatActivity {

    final private static String TAG = MainActivity.class.getSimpleName();
    ActionBar actionBar;
    ParseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        setUpActivity();

        currentUser = ParseUser.getCurrentUser();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if (ParseAnonymousUtils.isLinked(currentUser)) {
                    startActivity(new Intent(MainActivity.this, LoginSignupActivity.class));
                    MainActivity.this.finish();
                } else {
                    if (currentUser != null) {
                        //TODO optimize sign in...
                        Intent intent = new Intent(MainActivity.this, abc.class);
                        intent.putExtra("ActivityName", TAG);
                        startActivity(intent);
                        MainActivity.this.finish();
                    } else {
                        startActivity(new Intent(MainActivity.this, LoginSignupActivity.class));
                        MainActivity.this.finish();
                    }
                }
            }
        }, 1000);

    }

    private void setUpActivity() {
        actionBar = getSupportActionBar();
        if (actionBar != null)
            actionBar.hide();
    }
}
