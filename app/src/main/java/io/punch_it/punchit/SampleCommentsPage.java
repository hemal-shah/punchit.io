package io.punch_it.punchit;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

/**
 * Created by hemal on 12/25/15.
 */
public class SampleCommentsPage extends AppCompatActivity{

    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comments_activity);
        tv = (TextView) findViewById(R.id.tv_comment);

        String data = getIntent().getExtras().getString("Position");
        tv.setText(data);
    }
}
