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
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.facebook.internal.InternalSettings;
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
    private ArrayList<InterestItems> list = null, filteredData;
    private Context context;
    private int layoutResource;
    private ParseUser user = ParseUser.getCurrentUser();

    public MyCustomAdapter(Context context, int layoutResource, ArrayList<InterestItems> list) {
        super(context, layoutResource, list);
        this.context = context;
        this.list = list;
        this.filteredData = list;
        this.layoutResource = layoutResource;
    }

    @Override
    public int getCount() {
        return filteredData.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();

                if(constraint == null || constraint.length() == 0){
                    results.values = list;
                    results.count = list.size();
                }else{
                    ArrayList<InterestItems> filterResultsData = new ArrayList<>();
                    for(InterestItems data:list){
                        if(data.getInterestName().toLowerCase().startsWith(constraint.toString().toLowerCase())){
                            Log.i(TAG, "Match found..");
                            filterResultsData.add(data);
                        }
                    }
                    results.values = filterResultsData;
                    results.count = filterResultsData.size();
                }

                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

                filteredData = (ArrayList<InterestItems>) results.values ;
                Log.i(TAG, filteredData.toString());
                notifyDataSetChanged();
            }
        };
    }



    @Override
    public long getItemId(int position) {
        /*
        * Returning 0 because the Interest types dont have ids...
        * */

        return 0;

    }

    public InterestItems getItem(int position){
        return filteredData.get(position);
    }

    public class ViewHolder {
        TextView tv_interest_name;
        SwitchCompat switchCompat;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        final InterestItems temp;
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

        temp = getItem(position);
        holder.tv_interest_name.setText(temp.getInterestName());
        holder.switchCompat.setChecked(temp.isInterested());

        //Changing the contents through switch from here..

        holder.switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                String name = temp.getInterestName();
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
                                temp.setIsInterested(true);
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