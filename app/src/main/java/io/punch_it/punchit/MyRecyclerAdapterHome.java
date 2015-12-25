package io.punch_it.punchit;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Icon;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hemal on 12/24/15.
 */
public class MyRecyclerAdapterHome  extends RecyclerView.Adapter<MyRecyclerAdapterHome.CustomViewHolder> implements View.OnClickListener {

    private ArrayList<HomeFeed> list;
    private Context context;

    public MyRecyclerAdapterHome(Context context, ArrayList<HomeFeed> list){
        this.context = context;
        this.list = list;
    }



    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_row_home, null);
        CustomViewHolder customViewHolder = new CustomViewHolder(view);
        return customViewHolder;
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {
        //TODO set all the received contents here....
        HomeFeed singleFeed = list.get(position);

        holder.imageView.setImageResource(R.mipmap.user_icon);
        holder.tv_home.setText(singleFeed.getUser());
        holder.tv_time_home.setText(singleFeed.getTime());
        holder.tv_question.setText(singleFeed.getQuestion());
        holder.tv_first_post.setText(singleFeed.getFirst_post());
        holder.tv_second_post.setText(singleFeed.getSecond_post());
        holder.tv_user_id.setText(singleFeed.getUser());
        holder.tv_comment.setText(singleFeed.getComment());
        holder.post1.setBackgroundResource(R.mipmap.punchit_main);
        holder.post2.setBackgroundResource(R.mipmap.user_icon);
        holder.iv_share.setOnClickListener(this);
        holder.iv_comment.setOnClickListener(this);
        holder.iv_spam.setOnClickListener(this);
    }

    @Override
    public int getItemCount() {
        return (list != null ? list.size() : 0);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.iv_spam:
                Snackbar.make(v, "Reporting spam...",Snackbar.LENGTH_SHORT).show();
                break;
            case R.id.iv_share:
                Snackbar.make(v, "share it here", Snackbar.LENGTH_SHORT).show();
                break;
            case R.id.iv_comment:
                Snackbar.make(v, "Comments section", Snackbar.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder{

        RoundedImageView imageView;
        TextView tv_home, tv_time_home, tv_question, tv_first_post, tv_second_post, tv_user_id, tv_comment;
        LinearLayout post1, post2;
        ImageView iv_share, iv_comment, iv_spam;

        public CustomViewHolder(View view) {
            super(view);

            this.imageView = (RoundedImageView) view.findViewById(R.id.roundedImageView_home);
            this.tv_home = (TextView) view.findViewById(R.id.tv_home);
            this.tv_time_home = (TextView) view.findViewById(R.id.tv_time_home);
            this.tv_question = (TextView) view.findViewById(R.id.tv_question_home);
            this.tv_first_post = (TextView) view.findViewById(R.id.tv_first_post);
            this.tv_second_post = (TextView) view.findViewById(R.id.tv_second_post);
            this.tv_user_id = (TextView) view.findViewById(R.id.tv_user_id);
            this.tv_comment = (TextView) view.findViewById(R.id.tv_comment);
            this.post1 = (LinearLayout) view.findViewById(R.id.ll_post1);
            this.post2 = (LinearLayout) view.findViewById(R.id.ll_post2);
            this.iv_share = (ImageView) view.findViewById(R.id.iv_share);
            this.iv_comment = (ImageView) view.findViewById(R.id.iv_comment);
            this.iv_spam = (ImageView) view.findViewById(R.id.iv_spam);
        }
    }
}
