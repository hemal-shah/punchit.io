package io.punch_it.punchit;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FunctionCallback;
import com.parse.Parse;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;


public class abc extends AppCompatActivity implements View.OnClickListener {

    final private static String TAG = abc.class.getSimpleName();
    ListView lView = null;
    ArrayList<InterestItems> list = new ArrayList<>();
    FloatingActionButton fab;
    MyCustomAdapter adapter = null;
    EditText et ;
    InterestItems singleInterest;
    boolean isFirstTime;
    SharedPreferences sp;
    SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listview);
        setUpActivity();

        sp = getSharedPreferences("UserFirstTime", Context.MODE_APPEND);
        editor = sp.edit();
        isFirstTime = sp.getBoolean("FirstTimeLoggedIn", true);
        if (!isFirstTime) {
            String interest = ParseCloudApp.getInterestItems();
            editor.putString("UserInterest", interest);
            Log.i(TAG, interest);
            editor.apply();
            startActivity(new Intent(abc.this, MainFiveFragmentDisplay.class));
            abc.this.finish();
        }


        final ParseQuery<ParseObject> query = ParseQuery.getQuery("Intrestlist");
        new Handler().post(new Runnable() {
                               @Override
                               public void run() {
                                   try {
                                       List<ParseObject> InterestList = query.find();
                                       for (ParseObject singleItem : InterestList) {
                                           boolean interested = false;
                                           String name = singleItem.get("IntrestText").toString();
                                           List<String> interest = Arrays.asList(sp.getString("UserInterest", "").split(","));
                                           if (interest.contains(name)){
                                               interested = true;
                                           }
                                           singleInterest = new InterestItems(name, interested);
                                           list.add(singleInterest);
                                           //TODO check if following gives error at runtime...
                                       }
                                       adapter = new MyCustomAdapter(abc.this, R.layout.my_custom_list_layout, list);
                                       lView.setAdapter(adapter);
                                       editor.putBoolean("FirstTimeLoggedIn", false);
                                       editor.apply();
                                   } catch (ParseException e) {
                                       Snackbar.make(findViewById(R.id.ll_listview), e.toString(), Snackbar.LENGTH_SHORT).show();
                                   }
                               }
                           }
        );

        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                abc.this.adapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void setUpActivity() {
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar_listview));
        fab = (FloatingActionButton) findViewById(R.id.fab_listView);
        fab.setOnClickListener(this);
        et = (EditText) findViewById(R.id.inputSearch);
        lView = (ListView) findViewById(R.id.listview);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_list_view, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        switch (id) {
            case R.id.menu_next_list_view:
                startActivity(new Intent(abc.this, MainFiveFragmentDisplay.class));
                abc.this.finish();
                break;
            default:
                break;
        }
        return false;
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {
            case R.id.fab_listView:
                addItemToInterestList();
                break;
            default:
                break;
        }
    }

    private void addItemToInterestList() {
        AlertDialog alertDialog;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final EditText editText = new EditText(this);
        builder.setTitle("Interest Name:");
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setCancelable(true);
        editText.setInputType(InputType.TYPE_CLASS_TEXT);
        editText.setHeight(50);
        editText.setWidth(150);
        builder.setView(editText);
        builder.setPositiveButton("ADD!", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String name = editText.getText().toString();
                if (name.equals("")) {
                    Toast.makeText(getApplicationContext(), "Please enter type.", Toast.LENGTH_SHORT).show();
                } else {
                    //field is not empty!
                    list.add(new InterestItems(name, true));
                    adapter.notifyDataSetChanged();
                    addItemToParseBase(name);
                    dialog.dismiss();
                }
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        alertDialog = builder.create();
        alertDialog.show();
    }

    private void addItemToParseBase(String name) {
        ParseQuery<ParseObject> query;
        ParseObject obj, userInterest;

        obj = new ParseObject("Intrestlist");
        obj.put("IntrestText", name);
        try {
            obj.save();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        userInterest = new ParseObject("UserIntrest");
        userInterest.put("user", ParseUser.getCurrentUser());
        userInterest.put("HisInterest", obj);
        userInterest.put("IntrestText", name);
        userInterest.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                Log.i(TAG, "Saved Content.");
            }
        });
    }
}