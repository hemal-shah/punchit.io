package io.punch_it.punchit;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseUser;

/**
 * Created by ASUS on 05-10-2015.
 */
public class ParseApp extends Application {

    public void onCreate(){
        super.onCreate();

        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);

        Parse.initialize(this, "RcJdXtUZt0Ro3CG4XI75Q5p8naBu3JnGICNPWERe", "gBhf51liBQU2p25UQdjTiEQjNAnf5oDB2YtYlpFQ");

        ParseUser.enableRevocableSessionInBackground();
        ParseUser.enableAutomaticUser();
        ParseACL dACL = new ParseACL();

        dACL.setPublicReadAccess(true);
        ParseACL.setDefaultACL(dACL,true);
    }

}
