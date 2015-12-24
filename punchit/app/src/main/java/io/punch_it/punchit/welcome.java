package io.punch_it.punchit;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Intent;
import android.content.pm.PackageInstaller;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.tv.TvInputService;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseSession;
import com.parse.ParseUser;

import java.io.File;
import java.io.InputStream;

public class welcome extends AppCompatActivity {
    Button logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        ImageView iview = (ImageView) findViewById(R.id.imageView);
        ParseUser currentuser = ParseUser.getCurrentUser();
        ParseObject obj1= currentuser;
        String ninja=obj1.get("Ninja_name").toString();
        ParseFile file= (ParseFile) obj1.get("Image");
        try {
            byte[] fileData=file.getData();
            Bitmap bmp=BitmapFactory.decodeByteArray(fileData,0,fileData.length);
            iview.setImageBitmap(bmp);
        } catch (ParseException e) {
            Log.d("MyApp", e.toString());
        }


        TextView txtuser = (TextView) findViewById(R.id.txtuser);
        txtuser.setText("You are logged in as " + ninja);

        logout = (Button) findViewById(R.id.logout);
        FacebookSdk.sdkInitialize(this);

    }

    public void logout(View view) {
        try{

            LoginManager.getInstance().logOut();
            ParseUser.logOut();

            Intent intent = new Intent(welcome.this, LoginSignupActivity.class);
            startActivity(intent);


        }
        catch (Exception e)
        {
            Log.d("MyApp",e.toString());
        }
    }


    }








