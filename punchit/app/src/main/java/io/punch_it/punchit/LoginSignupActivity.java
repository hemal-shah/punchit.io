package io.punch_it.punchit;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.RequestPasswordResetCallback;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LoginSignupActivity extends AppCompatActivity {


    Button loginButton;
    Button signupButton;
    String usernametxt;
    String passwordtxt;
    EditText password;
    EditText username;
    EditText email;
    Button fb;
    ParseUser us=ParseUser.getCurrentUser();

    public static final List<String> permissions = new ArrayList<String>() {{
        add("public_profile");
        add("email");
        add("user_friends");
    }};

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ParseFacebookUtils.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_signup);



        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
      //  email = (EditText) findViewById(R.id.email);
        loginButton = (Button) findViewById(R.id.login);
        signupButton = (Button) findViewById(R.id.signup);
        fb = (Button) findViewById(R.id.fblogin);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                usernametxt = username.getText().toString();
                passwordtxt = password.getText().toString();

                ParseUser.logInInBackground(usernametxt, passwordtxt, new LogInCallback() {
                    @Override
                    public void done(ParseUser user, ParseException e) {
                        if (user != null) {
                            Intent intent = new Intent(LoginSignupActivity.this, abc.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(getApplicationContext(), "This user doesn't exist..Please sign up!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

        });

      signupButton.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {

              Intent intent = new Intent(LoginSignupActivity.this, SIgnup.class);
              startActivity(intent);
              finish();

          }

      });
    }

    public void forget(View view) {
     AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setMessage("Enter your Email:");
// Create TextView

        final EditText input1 = new EditText(this);
        alert.setView(input1);
        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                ParseUser.requestPasswordResetInBackground(input1.getText().toString(), new RequestPasswordResetCallback() {
                    public void done(ParseException e) {
                        if (e == null) {
                            Toast.makeText(getApplicationContext(), "Email successfully sent!!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "Eroorrr!!!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.
            }
        });
        alert.show();
    }


  public Bitmap img;
public void get_details(){
    GraphRequest request = GraphRequest.newMeRequest(
            AccessToken.getCurrentAccessToken(),
            new GraphRequest.GraphJSONObjectCallback() {
                @Override
                public void onCompleted(
                        JSONObject object,
                        GraphResponse response) {
                    try {
                        String data = object.getString("name");
                        String email=object.getString("email");
                        String gender=object.getString("gender");
                        JSONObject obj2=object.getJSONObject("picture");
                        JSONObject obj3=obj2.getJSONObject("data");
                        String url=obj3.getString("url");

                        new DownloadImageTask().execute(url);
                        Log.d("MyApp",data);
                            Log.d("MyApp", email);
                        try {
                            String ninjanamecheck = ParseUser.getCurrentUser().get("Ninja_name").toString();

                            Intent intent = new Intent(LoginSignupActivity.this, abc.class);
                            intent.putExtra("url", url);
                            startActivity(intent);
                        }
                        catch (Exception e) {
                            Intent intent = new Intent(LoginSignupActivity.this, Fbninja.class);
                            intent.putExtra("url", url);
                            ParseUser usr=ParseUser.getCurrentUser();
                            usr.setEmail(email.toString());
                            usr.put("Gender", gender.toString());
                            usr.put("Full_name", data.toString());
                            usr.setUsername(email.toString());
                            usr.saveEventually();
                            startActivity(intent);
                        }
                    }
                    catch(Exception e)
                    {
                        Log.d("MyApp",e.toString());
                    }
                }
            });
    Bundle parameters = new Bundle();
    parameters.putString("fields", "id,name,link,email,gender,picture.type(large)");
    request.setParameters(parameters);
    request.executeAsync();
}
    public void fb(View view) {

                try
                {
                    ParseFacebookUtils.initialize(this);
                    if(ParseUser.getCurrentUser() != null)
                    {
                        ParseFacebookUtils.logInWithReadPermissionsInBackground(this, permissions, new LogInCallback() {
                            @Override
                            public void done(ParseUser user, ParseException err) {
                                if (user == null) {
                                    Log.d("MyApp", "Uh oh. The user cancelled the Facebook login.");
                                } else if (user.isNew()) {
                                    Log.d("MyApp", "User signed up and logged in through Facebook!");
                                    get_details();

                                } else {
                                    Log.d("MyApp", "User logged in through Facebook!");
                                    get_details();

                                }
                            }
                        });
                    }
                }
                catch (Exception e)
                {

                   Log.d("MyApp",e.toString());
                }
        }

    class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        public Bitmap bmImage;

        // public DownloadImageTask(Bitmap bmImage) {
        //   this.bmImage = bmImage;
        //   }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();

        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            bmImage=mIcon11;
            return mIcon11;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);
           try
           {
               ByteArrayOutputStream stream = new ByteArrayOutputStream();
               result.compress(Bitmap.CompressFormat.PNG,100,stream);
               byte[] image = stream.toByteArray();
               ParseFile  file= new ParseFile("propic.png",image);

               us.put("Image",file);
                us.saveInBackground();
           }
           catch (Exception e)
           {
                Log.d("MyApp",e.toString());
           }
        }
    }


    }

