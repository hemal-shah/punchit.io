package io.punch_it.punchit;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
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
import org.json.JSONObject;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class LoginSignupActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView ib_fblogin;
    EditText et_password, et_emailid;
    String userName, passwordtxt;
    TextView tv_or;
    Button bt_forgetPass, bt_login, bt_signUp;
    ParseUser us = ParseUser.getCurrentUser();
    private static final String TAG = LoginSignupActivity.class.getSimpleName();
    public static final List<String> permissions = new ArrayList<String>() {{
        add("public_profile");
        add("email");
        add("user_friends");
    }};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_signup);
        setUpActivity();
    }

    private void setUpActivity() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        ib_fblogin = (ImageView) findViewById(R.id.ib_fblogin);
        bt_forgetPass = (Button) findViewById(R.id.bt_forgetPass);
        bt_login = (Button) findViewById(R.id.bt_login);
        bt_signUp = (Button) findViewById(R.id.bt_signUp);
        et_emailid = (EditText) findViewById(R.id.et_emailId);
        et_password = (EditText) findViewById(R.id.et_password);
        tv_or = (TextView) findViewById(R.id.tv_or);

        bt_forgetPass.setOnClickListener(this);
        bt_login.setOnClickListener(this);
        bt_signUp.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.bt_signUp:
                Intent intent = new Intent(LoginSignupActivity.this, SIgnup.class);
                startActivity(intent);
                break;

            case R.id.bt_login:
                logInMethod();
                break;

            case R.id.bt_forgetPass:
                forget();
                break;
        }
    }

    public void logInMethod() {
        userName = et_emailid.getText().toString();
        passwordtxt = et_password.getText().toString();

        ParseUser.logInInBackground(userName, passwordtxt, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                try {
                    if (user != null) {
                        Intent intent = new Intent(LoginSignupActivity.this, abc.class);
                        intent.putExtra("ActivityName", TAG);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "Error! Username or password incorrect!", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e1) {
                    Log.e(TAG, e1.toString());
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ParseFacebookUtils.onActivityResult(requestCode, resultCode, data);
    }


    public void forget() {

        AlertDialog alertDialog;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Enter Email:");
        final EditText editText = new EditText(this);
        editText.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        builder.setView(editText);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (editText.getText() != null) {
                    ParseUser.requestPasswordResetInBackground(editText.getText().toString(), new RequestPasswordResetCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {
                                Snackbar.make(findViewById(R.id.rl_login_signup), "Email sent!", Snackbar.LENGTH_SHORT).show();
                            } else {
                                Snackbar.make(findViewById(R.id.rl_login_signup), "Try again later!", Snackbar.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.setIcon(R.mipmap.punchit_main);
        builder.setCancelable(true);
        alertDialog = builder.create();
        alertDialog.show();

    }

    public void get_details() {
        GraphRequest request = GraphRequest.newMeRequest(
                AccessToken.getCurrentAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(
                            JSONObject object,
                            GraphResponse response) {
                        try {
                            String data = object.getString("name");
                            String email = object.getString("email");
                            String gender = object.getString("gender");
                            JSONObject obj2 = object.getJSONObject("picture");
                            JSONObject obj3 = obj2.getJSONObject("data");
                            String url = obj3.getString("url");

                            new DownloadImageTask().execute(url);
                            Log.d("MyApp", data);
                            Log.d("MyApp", email);
                            try {
                                String ninjanamecheck = ParseUser.getCurrentUser().get("Ninja_name").toString();

                                Intent intent = new Intent(LoginSignupActivity.this, abc.class);
                                intent.putExtra("url", url);
                                startActivity(intent);
                            } catch (Exception e) {
                                Intent intent = new Intent(LoginSignupActivity.this, MainFiveFragmentDisplay.class);
                                intent.putExtra("url", url);
                                ParseUser usr = ParseUser.getCurrentUser();
                                usr.setEmail(email);
                                usr.put("Gender", gender);
                                usr.put("Full_name", data);
                                usr.setUsername(email);
                                usr.saveEventually();
                                startActivity(intent);
                            }
                        } catch (Exception e) {
                            Log.d("MyApp", e.toString());
                        }
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,link,email,gender,picture.type(large)");
        request.setParameters(parameters);
        request.executeAsync();
    }

    public void fb(View view) {

        try {
            ParseFacebookUtils.initialize(this);
            if (ParseUser.getCurrentUser() != null) {
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
        } catch (Exception e) {

            Log.d("MyApp", e.toString());
        }
    }


    class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        public Bitmap bmImage;
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
            bmImage = mIcon11;
            return mIcon11;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);
            try {
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                result.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] image = stream.toByteArray();
                ParseFile file = new ParseFile("propic.png", image);

                us.put("Image", file);
                us.saveInBackground();
            } catch (Exception e) {
                Log.d("MyApp", e.toString());
            }
        }
    }

}

