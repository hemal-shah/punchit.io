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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;


public class SIgnup extends AppCompatActivity implements View.OnClickListener, TextWatcher {

    private static final int PICK_IMAGE = 1001;
    EditText et_name, et_email, et_ninjaName, et_pass1, et_pass2;
    RadioButton gender;
    String name, email, pass1, pass2, genderText, ninjaName;
    RadioGroup genderGroup;
    Button finalButton;
    ImageView chooseImage;
    private int char_limit = 20;

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

        Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] {pickIntent});

        startActivityForResult(chooserIntent, PICK_IMAGE);
    }

    private void finallySignIn() {
        name = et_name.getText().toString();
        email = et_email.getText().toString();
        pass1 = et_pass1.getText().toString();
        pass2 = et_pass2.getText().toString();
        ninjaName = et_ninjaName.getText().toString();
        genderText = gender.getText().toString();


        try {
            if (email.equals("") || pass1.trim().equals("") || pass2.equals("") || ninjaName.equals("")) {
                Snackbar.make(findViewById(R.id.rl_signUp), "Fill all details.", Snackbar.LENGTH_SHORT).show();
            } else {

                if (pass1.equals(pass2)) {
                    final ParseUser user = new ParseUser();
                    user.setUsername(email);
                    user.put("Full_name", name);
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
                                Intent intent = new Intent(SIgnup.this, abc.class);
                                startActivity(intent);
                                SIgnup.this.finish();
                            } else {
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

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            if (width > height) {
                inSampleSize = Math.round((float)height / (float)reqHeight);
            } else {
                inSampleSize = Math.round((float)width / (float)reqWidth);
            }
        }
        return inSampleSize;
    }

    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId, int reqWidth, int reqHeight) {

        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null) {
                Uri selectedImage = data.getData();
                String[] filePathColumn = { MediaStore.Images.Media.DATA };
                Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                if(cursor != null && cursor.moveToNext()) {
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String imgDecodableString = cursor.getString(columnIndex);
                    cursor.close();
                    chooseImage.setImageBitmap(decodeSampledBitmapFromResource(getResources(), Integer.parseInt(imgDecodableString), 100, 100));
                }else{
                    Snackbar.make(findViewById(R.id.rl_signUp), "Problem!", Snackbar.LENGTH_SHORT).show();
                }
            } else {
                Snackbar.make(findViewById(R.id.rl_signUp), "Error in Image Selection.", Snackbar.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Snackbar.make(findViewById(R.id.rl_signUp), "Something went wrong!", Snackbar.LENGTH_SHORT).show();
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