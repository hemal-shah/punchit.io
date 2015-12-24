package io.punch_it.punchit;

import android.app.Application;
import android.util.Log;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseUser;

/**
 * Created by ASUS on 05-10-2015.
 */
public class ParseApp extends Application {

    static final String APPLICATION_ID = "Y4Txek5e5lKnGzkArbcNMVKqMHyaTk3XR6COOpg4";
    static final String CLIENT_KEY = "EBXwzrnLMWFP0new7t79tV56RyIgoz3WPeuuA6lE";

    public void onCreate(){
        super.onCreate();
        // Enable Local Datastore
        Log.i("Application created","initialization");
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, APPLICATION_ID, CLIENT_KEY);
//        ParseUser.enableRevocableSessionInBackground();
        ParseUser.enableAutomaticUser();
        ParseACL dACL = new ParseACL();

        dACL.setPublicReadAccess(true);
        ParseACL.setDefaultACL(dACL,true);
    }

}
