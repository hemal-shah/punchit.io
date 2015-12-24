package io.punch_it.punchit;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.ParseFile;
import com.parse.ParseUser;


public class demo extends Activity {


    ImageView targetImage;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.demo);
//        Button buttonLoadImage = (Button)findViewById(R.id.loadimage);
//
//        targetImage = (ImageView)findViewById(R.id.targetimage);
//
//        buttonLoadImage.setOnClickListener(new Button.OnClickListener(){
//
//            @Override
//            public void onClick(View arg0) {
//                // TODO Auto-generated method stub
//                Intent intent = new Intent(Intent.ACTION_PICK,
//                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                startActivityForResult(intent, 0);
//            }});
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        // TODO Auto-generated method stub
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (resultCode == RESULT_OK){
//            Uri targetUri = data.getData();
//
//            Bitmap bitmap;
//            try {
//                bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(targetUri));
//                ByteArrayOutputStream stream = new ByteArrayOutputStream();
//                bitmap.compress(Bitmap.CompressFormat.PNG,100,stream);
//                byte[] image = stream.toByteArray();
//                ParseFile file= new ParseFile("propic.png",image);
//                ParseUser us = ParseUser.getCurrentUser();
//                us.put("Image", file);
//                us.saveInBackground();
//                targetImage.setImageBitmap(bitmap);
//
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//    public void next(View view) {
//        Intent intent = new Intent(demo.this , abc.class);
//        startActivity(intent);
    }
}