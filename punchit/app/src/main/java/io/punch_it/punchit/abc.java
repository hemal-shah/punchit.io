package io.punch_it.punchit;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;


public class abc extends Activity {

    ListView lView;
    ArrayList<String> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listview);




        /*LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        RelativeLayout parent = (RelativeLayout) inflater.inflate(R.layout.my_custom_list_layout,
                null);
        Button a=(Button) parent.findViewById(R.id.addbtn);
        Button d= (Button) parent.findViewById(R.id.deletebtn);*/


        list = new ArrayList<String>();

        final MyCustomAdapter adapter = new MyCustomAdapter(list, this);


        lView = (ListView) findViewById(R.id.listview1);
        lView.setAdapter(adapter);

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Interestlist");
        try {

            List<ParseObject> InterestList = query.find();

            for (int i = 0; i < InterestList.size(); i++) {
                final ParseObject obj = (ParseObject) InterestList.get(i);
                list.add(obj.get("Interest").toString());
            }
        }
        catch (Exception e)
        {
            Log.d("MyApp",e.toString());

        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.yourentry, menu);
        return  true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_cart:
                Intent intentHome = new Intent(this, fbfriend.class);
                startActivity(intentHome);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}