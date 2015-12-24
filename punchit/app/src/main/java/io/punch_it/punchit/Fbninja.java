package io.punch_it.punchit;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.parse.Parse;
import com.parse.ParseUser;

public class Fbninja extends AppCompatActivity {


    EditText ninjaname;
    Button next;
    String ninjanametxt;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fbninja);


        ninjaname = (EditText) findViewById(R.id.ninjaname);
        next = (Button) findViewById(R.id.nextbtn);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ninjanametxt = ninjaname.getText().toString();

                ParseUser user = ParseUser.getCurrentUser();
                user.put("Ninja_name", ninjanametxt);

                user.saveEventually();
                String url = getIntent().getStringExtra("url");
                Intent intent = new Intent(Fbninja.this , abc.class);
                intent.putExtra("url",url);
                startActivity(intent);

            }
        });

    }


}