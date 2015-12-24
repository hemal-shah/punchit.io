package io.punch_it.punchit;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ListAdapter;

import android.view.View;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import io.punch_it.punchit.R;

public class MyCustomAdapter extends BaseAdapter implements ListAdapter {
    private ArrayList<String> list = new ArrayList<String>();
    private Context context;

    private ParseUser user = ParseUser.getCurrentUser();

    public MyCustomAdapter(ArrayList<String> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int pos) {
        return list.get(pos);
    }

    @Override
    public long getItemId(int pos) {
        return 0;
        //just return 0 if your list items do not have an Id variable.
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {


        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.my_custom_list_layout, null);
        }

        //Handle TextView and display string from your list
        TextView listItemText = (TextView)view.findViewById(R.id.list_item_string);
        listItemText.setText(list.get(position));

        Log.d("Myapp",list.get(position));

        final Button a=(Button) view.findViewById(R.id.addbtn);
        final Button d= (Button) view.findViewById(R.id.deletebtn);


        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Userinterest");
        try
        {
            List<ParseObject> parseObjects = query.find();
            for(int i=0;i<=parseObjects.size();i++)
            {
                ParseObject obj = parseObjects.get(i);
                ParseUser usr = (ParseUser)obj.get("User");
                if(usr.getObjectId().toString().equals(ParseUser.getCurrentUser().getObjectId()))
                {
                    Log.d("Myapp",obj.get("text").toString());
                   if(obj.get("text").toString().equals(list.get(position)))
                   {
                       a.setEnabled(false);
                       d.setEnabled(true);
                       break;
                   }
                   else
                   {
                       a.setEnabled(true);
                       d.setEnabled(false);
                   }
                }
            }
        }
        catch (Exception e)
        {
            Log.d("Myapp",e.toString());
        }

        a.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setEnabled(false);
                d.setEnabled(true);
                String str= list.get(position);
                ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Interestlist");
                query.whereContains("Interest", str);

                try {
                    Log.d("MyApp", str);
                    ParseObject obj = (ParseObject)query.getFirst();
                    ParseObject userintrest=new ParseObject("Userinterest");
                    userintrest.put("User",user);
                    userintrest.put("Hisintrest",obj);
                    userintrest.put("text", str);

                    userintrest.saveEventually();

                }
                catch (Exception e)
                {
                    Log.d("MyApp",e.toString());
                }
            }
        });

        d.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setEnabled(false);
                a.setEnabled(true);
                String str=list.get(position).toString();
                ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Userinterest");
                query.whereContains("text", str);

                try {

                    Log.d("MyApp", str.toString());
                    query.findInBackground(new FindCallback<ParseObject>() {
                        @Override
                        public void done(List<ParseObject> list, ParseException e) {
                            for(int i=0;i<list.size();i++)
                            {
                                ParseObject obj=list.get(i);
                                ParseUser usr=(ParseUser)obj.get("User");
                                if (usr.getObjectId().equals(user.getObjectId()) == true)
                                {
                                    obj.deleteInBackground();
                                    Log.d("MyApp", "Item removed");
                                }
                                else
                                {
                                    Log.d("MyApp","Item not removed");
                                }
                            }
                        }
                    });
                }
                catch (Exception e)
                {
                    Log.d("MyApp",e.toString());
                }
            }
        });



        return view;
    }
}