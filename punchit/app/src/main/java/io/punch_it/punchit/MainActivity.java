package io.punch_it.punchit;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;

import com.parse.Parse;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseFacebookUtils;
import com.parse.ParseObject;
import com.parse.ParseUser;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        if(ParseAnonymousUtils.isLinked(ParseUser.getCurrentUser())){
            Intent intent = new Intent(MainActivity.this,LoginSignupActivity.class);
            startActivity(intent);
            finish();
        }

        else{
            ParseUser currentuser = ParseUser.getCurrentUser();
            if(currentuser != null){
                Intent intent = new Intent(MainActivity.this,abc.class);
                startActivity(intent);
                finish();
            }
            else{
                Intent intent = new Intent(MainActivity.this,LoginSignupActivity.class);
                startActivity(intent);
                finish();
            }

        }



    }
}
