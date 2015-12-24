package io.punch_it.punchit;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.view.View;
import android.widget.TextView;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

public class MyCustomAdapter extends ArrayAdapter<InterestItems> {
    private static final String TAG = MyCustomAdapter.class.getSimpleName();
    private ArrayList<InterestItems> list = null;
    private Context context;
    private int layoutResource;
    private ParseUser user = ParseUser.getCurrentUser();

    public MyCustomAdapter(Context context, int layoutResource, ArrayList<InterestItems> list) {
        super(context, layoutResource, list);
        this.context = context;
        this.list = list;
        this.layoutResource = layoutResource;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public long getItemId(int position) {
        /*
        * Returning 0 because the Interest types dont have ids...
        * */

        return 0;

    }

    public class ViewHolder {
        TextView tv_interest_name;
        SwitchCompat switchCompat;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(layoutResource, null, false);

            holder = new ViewHolder();
            holder.tv_interest_name = (TextView) convertView.findViewById(R.id.tv_interestName_custom_row);
            holder.switchCompat = (SwitchCompat) convertView.findViewById(R.id.switch_custom_row);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tv_interest_name.setText(list.get(position).getInterestName());
        holder.switchCompat.setChecked(list.get(position).isInterested());

        //Changing the contents through switch from here..

        holder.switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                String name = list.get(position).getInterestName();
                Log.i(TAG,name);
                ParseQuery<ParseObject> query;
                ParseObject obj, userInterest;
                if (isChecked) {
                    try {
                        query = new ParseQuery<>("Intrestlist");
                        query.whereContains("IntrestText", name);
                        obj = query.getFirst();
                        userInterest = new ParseObject("UserIntrest");
                        userInterest.put("User", user);
                        userInterest.put("HisInterest", obj);
                        userInterest.put("IntrestText", name);
                        userInterest.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                Log.i(TAG, "Saved content....");
                                list.get(position).setIsInterested(true);
                            }
                        });
                    } catch (ParseException e) {
                        Log.i(TAG, "error onCheckedChange MyCustomAdapter.java====="+e.toString());
                    }
                } else {
                    query = new ParseQuery<>("UserIntrest");
                    query.whereContains("IntrestText", name);

                        query.findInBackground(new FindCallback<ParseObject>() {
                            @Override
                            public void done(List<ParseObject> list, ParseException e) {

                                for(ParseObject singleItem : list){
                                    ParseUser user = (ParseUser) singleItem.get("User");
                                    if(ParseUser.getCurrentUser().getObjectId().equals(user.getObjectId())){
                                        singleItem.deleteInBackground();
                                    }else{
                                        Log.e(TAG, "Error! Item not deleted..");
                                    }
                                }
                            }
                        });
                    }
                }
        });

        return convertView;
    }
}