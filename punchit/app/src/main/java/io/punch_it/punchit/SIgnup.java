package io.punch_it.punchit;

import android.content.Intent;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import java.lang.reflect.Array;
import java.util.Objects;

public class SIgnup extends AppCompatActivity {

    RadioButton r1,r2;
    Button signup;
    EditText fname,nname,password,password1,email;
    String fnametxt,nnametxt,passwordtxt,password1txt,emailtxt;

    String gender;

    private int countWords(String s) {
        String trim = s.trim();
        if (trim.isEmpty())
            return 0;
        return trim.split("\\s+").length; // separate string around spaces
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        signup = (Button) findViewById(R.id.button);
        r1 = (RadioButton) findViewById(R.id.radioButton);
        r2 = (RadioButton) findViewById(R.id.radioButton1);
        fname = (EditText) findViewById(R.id.fullname);
        nname = (EditText) findViewById(R.id.ninjaname);
        password = (EditText) findViewById(R.id.password);
        password1 = (EditText) findViewById(R.id.password1);
        email = (EditText) findViewById(R.id.email);

        fname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String[] name = fnametxt.split(" ");
                if (name.length > 2) {
                    fnametxt = fname.getText().toString();
                    int a = countWords(fnametxt);
                    if (a > 2) {
                        Toast.makeText(getApplicationContext(), "only two words allowed", Toast.LENGTH_SHORT).show();
                        signup.setEnabled(false);
                    } else {
                        signup.setEnabled(true);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                String[] name = fnametxt.split(" ");
                if (name.length > 2) {
                    fnametxt = fname.getText().toString();
                    int a = countWords(fnametxt);
                    if (a > 2) {
                        Toast.makeText(getApplicationContext(), "only two words allowed", Toast.LENGTH_SHORT).show();
                        signup.setEnabled(false);
                    } else {
                        signup.setEnabled(true);
                    }
                }
            }
        });

        fname.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                fnametxt = fname.getText().toString();
                int a = countWords(fnametxt);
                if (a > 2) {
                    Toast.makeText(getApplicationContext(), "only two words allowed", Toast.LENGTH_SHORT).show();
                    signup.setEnabled(false);
                } else {
                    signup.setEnabled(true);
                }

            }
        });
    }



 public void radiobutton(View view) {
        boolean checked = ((RadioButton) view ).isChecked();

        switch(view.getId()) {
            case R.id.radioButton:
                if (checked){
                    gender="male";
                    r2.setChecked(false);
                    break;
                }

            case R.id.radioButton1:
                if (checked){
                    r1.setChecked(false);
                    gender="female";
                    break;
                }

        }

    }

    public void signup(View view)
    {
        nnametxt = nname.getText().toString();
        passwordtxt = password.getText().toString();
        password1txt = password1.getText().toString();
        emailtxt = email.getText().toString();
        fnametxt = fname.getText().toString();

       try{
           if (fnametxt.equals("")|| nnametxt.trim().equals("") || passwordtxt.equals("")  || emailtxt.equals(""))

           {
               Toast.makeText(getApplicationContext(), "check and fill all feilds ", Toast.LENGTH_SHORT).show();
           }

           else {
               final ParseUser user = new ParseUser();
               user.setUsername(emailtxt);
               user.put("Full_name", fnametxt);
               user.setPassword(password1txt);
               user.setEmail(emailtxt);
               user.put("Ninja_name",nnametxt);
               user.put("Gender", gender);



                if (passwordtxt.equals(password1txt)) {
                   user.signUpInBackground(new SignUpCallback() {
                       @Override
                       public void done(ParseException e) {
                           if (e == null) {
                               user.saveInBackground();
                               Toast.makeText(getApplicationContext(), "Sign up successfully!!", Toast.LENGTH_SHORT).show();
                               Intent intent = new Intent(SIgnup.this, demo.class);
                               startActivity(intent);
                           } else {
                               Toast.makeText(getApplicationContext(), "Sign up Error!!", Toast.LENGTH_SHORT).show();
                           }
                       }
                   });
               } else {
                   Toast.makeText(getApplicationContext(), "Make sure password is same!!", Toast.LENGTH_SHORT).show();
               }

           }
       }
       catch (Exception e)
       {
           Toast.makeText(getApplicationContext(), "Fill aal the Fields.!!", Toast.LENGTH_SHORT).show();
       }
    }
}


