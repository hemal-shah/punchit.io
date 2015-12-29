package io.punch_it.punchit;

import android.content.Context;
import android.graphics.Color;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Checkable;
import android.widget.TextView;

/**
 * Created by hemal on 12/30/15.
 */
public class MySelectTagAdapter extends ArrayAdapter<String> implements Checkable{

    String[] interests;
    int resource;
    Context context;
    public boolean mChecked;

    public MySelectTagAdapter(Context context, int resource, String[] objects) {
        super(context, resource, objects);
        this.interests = objects;
        this.context = context;
        this.resource = resource;
    }

    @Override
    public void setChecked(boolean checked) {
        mChecked = checked;
    }

    @Override
    public boolean isChecked() {
        return mChecked;
    }

    @Override
    public void toggle() {
        mChecked = !mChecked;
    }



    public class ViewHolder{
        TextView tv;
    }

    @Override
    public int getCount() {
        return interests.length;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder ;
        if(convertView == null){
            LayoutInflater inflater = LayoutInflater.from(this.context);
            convertView = inflater.inflate(resource, parent, false);
            holder = new ViewHolder();
            holder.tv = (TextView) convertView.findViewById(R.id.tv_dialog_frament);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        if(mChecked){
            convertView.setBackgroundColor(Color.parseColor("#66d3f1"));
        }else {
            convertView.setBackgroundColor(Color.parseColor("#ffffff"));
        }

        holder.tv.setText(interests[position]);
        return convertView;
    }
}
