package io.punch_it.punchit;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;

public class MainPunch extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = MainPunch.class.getSimpleName();
    private static final int PICK_IMAGE_1 = 1, PICK_IMAGE_2 = 2;
    EditText et_title, et_1, et_2;
    Button add_community;
    FloatingActionButton fab_submit;
    ImageView iv_1, iv_2;
    ActionBar actionBar;
    ProgressBar progressBar;
    Bitmap bitmap_post1, bitmap_post2;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_punch);
        setUpActivity();
    }

    private void setUpActivity() {
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar_main_punch));
        actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setTitle("New Punch");
        }
        progressBar = (ProgressBar) findViewById(R.id.pb_main_punch);
        progressBar.setVisibility(View.INVISIBLE);
        iv_1 = (ImageView) findViewById(R.id.iv_post1_new_punch);
        iv_2 = (ImageView) findViewById(R.id.iv_post2_new_punch);
        et_1 = (EditText) findViewById(R.id.et_post1_new_punch);
        et_2 = (EditText) findViewById(R.id.et_post2_new_punch);
        add_community = (Button) findViewById(R.id.bt_summary_new_punch);
        et_title = (EditText) findViewById(R.id.et_title_new_punch);
        fab_submit = (FloatingActionButton) findViewById(R.id.fab_new_punch);
        iv_1.setOnClickListener(this);
        add_community.setOnClickListener(this);
        iv_2.setOnClickListener(this);
        fab_submit.setOnClickListener(this);
    }

    public void chooseImage(int id) {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(Intent.createChooser(photoPickerIntent, "Set Image"), (id == 1) ? PICK_IMAGE_1 : PICK_IMAGE_2);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == PICK_IMAGE_1 || requestCode == PICK_IMAGE_2) {
                Uri imageUri = data.getData();
                InputStream inputStream = null;
                try {
                    inputStream = getContentResolver().openInputStream(imageUri);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                Bitmap selectedImage = BitmapFactory.decodeStream(inputStream);
                if (requestCode == PICK_IMAGE_1) {
                    iv_1.setImageBitmap(selectedImage);
                    bitmap_post1 = selectedImage;
                } else if (requestCode == PICK_IMAGE_2) {
                    iv_2.setImageBitmap(selectedImage);
                    bitmap_post2 = selectedImage;
                }
            }
        }

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.iv_post1_new_punch:
                chooseImage(1);
                break;
            case R.id.iv_post2_new_punch:
                chooseImage(2);
                break;
            case R.id.bt_summary_new_punch:
                addCommunities();
                break;
            case R.id.fab_new_punch:
                submitPunch();
                break;
            default:
                break;
        }
    }

    private void addCommunities() {
        FragmentManager manager = getSupportFragmentManager();
        TagsDialogFragment dialog = new TagsDialogFragment();
        dialog.setCancelable(true);
        dialog.show(manager, "Tag communities.");

    }


    public static byte[] bitmapToByteArray(Bitmap bmp)
    {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }

    private void submitPunch() {
        String post1 = et_1.getText().toString();
        String post2 = et_2.getText().toString();
        String title = et_title.getText().toString();

        SharedPreferences sp = getSharedPreferences("UserItemSelected", 0);
        String text = sp.getString("SelectedItems","Nothing selected!");

        if(post1.equals("") || post2.equals("") || title.equals("") || bitmap_post1==null || bitmap_post2==null){
            Snackbar.make(findViewById(R.id.rl_new_punch), "Please fill in all details!", Snackbar.LENGTH_SHORT).show();
        }else{
            byte[] image1 = bitmapToByteArray(bitmap_post1);
            byte[] image2 = bitmapToByteArray(bitmap_post2);

            progressBar.setVisibility(View.VISIBLE);
            ParseObject putValue = new ParseObject("Posts");
            putValue.put("Title", title);
            putValue.put("TargetIntrests", Arrays.asList(text.split(",")));
            putValue.put("By", ParseUser.getCurrentUser());
            ParseFile file1 = new ParseFile("img1.png", image1);
            ParseFile file2 = new ParseFile("img2.png", image2);
            putValue.put("Image1", file1);
            putValue.put("Image2", file2);
            putValue.put("Punchers1", new ArrayList<>());
            putValue.put("Punchers2", new ArrayList<>());
            putValue.put("Image1Title", post1);
            putValue.put("Image2Title", post2);

            putValue.saveInBackground();
            progressBar.setVisibility(View.INVISIBLE);
            Snackbar.make(findViewById(R.id.rl_new_punch), "New punch added!", Snackbar.LENGTH_SHORT).show();
            MainPunch.this.finish();
        }
    }
}

