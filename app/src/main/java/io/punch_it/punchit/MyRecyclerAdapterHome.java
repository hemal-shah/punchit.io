package io.punch_it.punchit;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MyRecyclerAdapterHome extends RecyclerView.Adapter<MyRecyclerAdapterHome.CustomViewHolder> {

    private ArrayList<HomeFeed> list;
    private FeedButtonEvents mFeedButtonEvents;
    CustomViewHolder customViewHolder;
    private Context context;

    private static final String TAG = MyRecyclerAdapterHome.class.getSimpleName();

    public MyRecyclerAdapterHome(Context context, ArrayList<HomeFeed> list) {
        this.context = context;
        this.list = list;
    }

    public void setClickListener(FeedButtonEvents feedButtonEvents) {
        this.mFeedButtonEvents = feedButtonEvents;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_row_home, parent, false);
        customViewHolder = new CustomViewHolder(view);
        return customViewHolder;
    }

    public String getTimeInFormat(Date d) {
        int hours = d.getHours();
        int min = d.getMinutes();
        int month = d.getMonth();
        int day = d.getDay();

        Calendar current = Calendar.getInstance();
        int currentHours = current.get(Calendar.HOUR_OF_DAY);
        int currentMin = current.get(Calendar.MINUTE);
        int currentMonth = current.get(Calendar.MONTH);
        int currentDay = current.get(Calendar.DAY_OF_WEEK);

        String time;

        if (Math.abs(currentMonth - month) == 0) {
            if (Math.abs(currentDay - day) > 7) {
                time = (Math.abs(currentDay - day) / 7) + "W";
            } else if (Math.abs(currentDay - day) > 0) {
                time = Math.abs(currentDay - day) + "d";
            } else if (Math.abs(currentHours - hours) > 0) {
                time = Math.abs(currentHours - hours) + "h";
            } else {
                time = Math.abs(currentMin - min) + "m";
            }
        } else {
            time = Math.abs(currentMonth - month) + "M";
        }

        return time;
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, final int position) {
        //TODO set all the received contents here....
        HomeFeed singleFeed = list.get(position);

        String time = getTimeInFormat(singleFeed.getDate());

        try {
            new DownloadImageTask(holder.imageView)
                    .execute(singleFeed.getProfilePicture().getUrl());
        } catch (Exception e) {
            Log.d("MyApp", e.toString());
        }

        holder.tv_home.setText(singleFeed.getUser());
        holder.tv_time_home.setText(time);
        holder.tv_question.setText(singleFeed.getQuestion());
        holder.tv_first_post.setText(singleFeed.getFirst_post());
        holder.tv_second_post.setText(singleFeed.getSecond_post());
        holder.tv_likes_1.setText(String.valueOf(singleFeed.getLikesIn1()));
        holder.tv_likes_2.setText(String.valueOf(singleFeed.getLikesIn2()));
        loadImages(singleFeed.getImage1(), holder.iv_post1);
        loadImages(singleFeed.getImage2(), holder.iv_post2);

        holder.iv_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v, "share it here", Snackbar.LENGTH_SHORT).show();
                if (mFeedButtonEvents != null)
                    mFeedButtonEvents.shareFeed(position);
            }
        });

        holder.iv_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v, "Comments section", Snackbar.LENGTH_SHORT).show();
                if (mFeedButtonEvents != null)
                    mFeedButtonEvents.commentPage(position);
            }
        });

        holder.iv_spam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mFeedButtonEvents != null)
                    mFeedButtonEvents.reportSpam(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return (list != null ? list.size() : 0);
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {

        RoundedImageView imageView;
        TextView tv_home, tv_time_home, tv_question, tv_first_post, tv_second_post, tv_likes_1, tv_likes_2;
        ImageView iv_post1, iv_post2, iv_share, iv_comment, iv_spam;

        public CustomViewHolder(View view) {
            super(view);

            this.imageView = (RoundedImageView) view.findViewById(R.id.roundedImageView_home);
            this.tv_home = (TextView) view.findViewById(R.id.tv_home);
            this.tv_time_home = (TextView) view.findViewById(R.id.tv_time_home);
            this.tv_question = (TextView) view.findViewById(R.id.tv_question_home);
            this.tv_first_post = (TextView) view.findViewById(R.id.tv_first_post);
            this.tv_second_post = (TextView) view.findViewById(R.id.tv_second_post);
            this.tv_likes_1 = (TextView) view.findViewById(R.id.tv_likes_1);
            this.tv_likes_2 = (TextView) view.findViewById(R.id.tv_likes_2);
            this.iv_share = (ImageView) view.findViewById(R.id.iv_share);
            this.iv_comment = (ImageView) view.findViewById(R.id.iv_comment);
            this.iv_post1 = (ImageView) view.findViewById(R.id.iv_post1);
            this.iv_post2 = (ImageView) view.findViewById(R.id.iv_post2);
            this.iv_spam = (ImageView) view.findViewById(R.id.iv_spam);
        }
    }

    public interface FeedButtonEvents {
        void reportSpam(int position);

        void shareFeed(int position);

        void commentPage(int position);
    }


    private void loadImages(ParseFile thumbnail, final ImageView img) {

        if (thumbnail != null) {
            thumbnail.getDataInBackground(new GetDataCallback() {
                @Override
                public void done(byte[] data, ParseException e) {
                    if (e == null) {
                        Bitmap bmp = BitmapFactory.decodeByteArray(data, 0, data.length);
                        img.setImageBitmap(bmp);
                    } else {
                    }
                }
            });
        } else {
            img.setImageResource(R.mipmap.punchit_main);
        }
    }


    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
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
            return mIcon11;
        }

    }
}