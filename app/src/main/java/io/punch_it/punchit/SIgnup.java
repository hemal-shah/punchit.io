package io.punch_it.punchit;

import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;


public class SIgnup extends AppCompatActivity implements View.OnClickListener, TextWatcher {

    private static final int PICK_IMAGE = 1001;
    EditText et_name, et_email, et_ninjaName, et_pass1, et_pass2;
    RadioButton gender;
    String name, email, pass1, pass2, genderText, ninjaName;
    RadioGroup genderGroup;
    Button finalButton;
    Bitmap bitmap;
    ImageView chooseImage;
    private int char_limit = 20;
    private static final String TAG = SIgnup.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        setUpActivity();
    }

    private void setUpActivity() {
        et_name = (EditText) findViewById(R.id.et_name_signUp);
        et_email = (EditText) findViewById(R.id.et_emailId_signUp);
        et_ninjaName = (EditText) findViewById(R.id.et_ninjaName_signUp);
        et_pass1 = (EditText) findViewById(R.id.et_pass1_signUp);
        et_pass2 = (EditText) findViewById(R.id.et_pass2_signUp);
        chooseImage = (ImageView) findViewById(R.id.iv_signUp);
        finalButton = (Button) findViewById(R.id.bt_signUp_signUp);
        genderGroup = (RadioGroup) findViewById(R.id.rg_gender);
        gender = (RadioButton) findViewById(genderGroup.getCheckedRadioButtonId());

        et_name.addTextChangedListener(this);
        chooseImage.setOnClickListener(this);
        finalButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.bt_signUp_signUp:
                finallySignIn();
                break;
            case R.id.iv_signUp:
                imageChooser();
                break;
            default:
                break;
        }
    }

    private void imageChooser() {
        Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
        getIntent.setType("image/*");

        Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType("image/*");

        startActivityForResult(Intent.createChooser(getIntent, "Select Image"), PICK_IMAGE);
    }

    private void finallySignIn() {
        name = et_name.getText().toString();
        email = et_email.getText().toString();
        pass1 = et_pass1.getText().toString();
        pass2 = et_pass2.getText().toString();
        ninjaName = et_ninjaName.getText().toString();
        genderText = gender.getText().toString();


        try {
            if (email.equals("") || pass1.trim().equals("") || pass2.equals("") || ninjaName.equals("")||bitmap==null) {
                Snackbar.make(findViewById(R.id.rl_signUp), "Fill all details.", Snackbar.LENGTH_SHORT).show();
            } else {

                if (pass1.equals(pass2)) {
                    final ParseUser user = new ParseUser();
                    user.setUsername(email);
                    user.put("Name", name);
                    user.setPassword(pass1);
                    user.setEmail(email);
                    user.put("Ninja_name", ninjaName);
                    user.put("Gender", genderText);

                    user.signUpInBackground(new SignUpCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {
                                user.saveInBackground();
                                Snackbar.make(findViewById(R.id.rl_signUp), "Successfully signed in!", Snackbar.LENGTH_SHORT).show();
                                saveBitmapToParse(bitmap);
                                Intent intent = new Intent(SIgnup.this, abc.class);
                                startActivity(intent);
                                SIgnup.this.finish();
                            } else {
                                Log.i(TAG, e.toString());
                                Snackbar.make(findViewById(R.id.rl_signUp), "Sign-Up Error!", Snackbar.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    Snackbar.make(findViewById(R.id.rl_signUp), "Passwords don't match.", Snackbar.LENGTH_SHORT).show();
                }

            }
        } catch (Exception e) {
            Snackbar.make(findViewById(R.id.rl_signUp), "Some error occurred.", Snackbar.LENGTH_SHORT).show();
        }

    }

    private void decodeImage(final String path) {
        int targetW = 100;
        int targetH = 100;

        final BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        int scaleFactor = Math.min(photoW / targetW, photoH / targetH);

        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;
        bitmap = BitmapFactory.decodeFile(path, bmOptions);
        chooseImage.setImageBitmap(bitmap);
    }

    private void saveBitmapToParse(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] image = stream.toByteArray();
        ParseFile file = new ParseFile("propic.png", image);
        ParseUser user = ParseUser.getCurrentUser();
        user.put("ProfilePicture", file);
        user.saveInBackground();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == PICK_IMAGE){
            if(resultCode == RESULT_OK){
                String yourSelectedImage;
                Uri selectedImage = data.getData();
                String[] filePath = { MediaStore.Images.Media.DATA };
                Cursor c = getContentResolver().query(selectedImage, filePath,
                        null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                yourSelectedImage = c.getString(columnIndex);
                c.close();
                if (yourSelectedImage!= null) {
                    Log.v("Image", yourSelectedImage);
                    decodeImage(yourSelectedImage);
                }

            }
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if(count > char_limit){
            Snackbar.make(findViewById(R.id.rl_signUp), "Character limit:20", Snackbar.LENGTH_SHORT).show();
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}