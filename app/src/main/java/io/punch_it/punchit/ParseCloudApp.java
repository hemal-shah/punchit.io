package io.punch_it.punchit;

import android.util.Log;

import com.parse.Parse;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by hemal on 12/29/15.
 */
public class ParseCloudApp {

    private static final String TAG = ParseCloudApp.class.getSimpleName();

    ParseCloudApp(){

    }

    public static String getAllInterestsFromParse(){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Intrestlist");
        StringBuilder sb = new StringBuilder();
        try {
            List<ParseObject> parseObjects = query.find();
            for(ParseObject parseObject : parseObjects){
                sb.append(parseObject.get("IntrestText").toString()).append(",");
            }
        }catch (ParseException e){
            e.printStackTrace();
        }
        return sb.toString();
    }

    public static String getInterestItems(){
        StringBuilder sb = new StringBuilder();

        try {
            @SuppressWarnings("unchecked")
            ArrayList<String> list = (ArrayList<String>) ParseCloud.callFunction("GetUserIntrest", new HashMap<String, Object>());

            for(String string:list){
                sb.append(string).append(",");
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Log.i(TAG, "Returning from ParseCloudApp:"+sb.toString());
        return sb.toString();
    }

}
