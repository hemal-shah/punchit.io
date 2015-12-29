package io.punch_it.punchit;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import java.util.ArrayList;

/**
 * Created by hemal on 12/29/15.
 */

public class TagsDialogFragment extends DialogFragment implements AdapterView.OnItemClickListener, MultichooserInterests.ItemStateChanged {
    String[] interests = ParseCloudApp.getAllInterestsFromParse().split(",");
    ListView list;
    ArrayList<String> selectedItems = new ArrayList<>();
    ArrayAdapter<String> adapter;

    private static final String TAG = TagsDialogFragment.class.getSimpleName();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.interest_dialog_fragment, container, false);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        list = (ListView) view.findViewById(R.id.list_dialog);
        return  view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        adapter = new MySelectTagAdapter(getActivity(), R.layout.dialog_for_interest_new_punch, interests);
        list.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        list.setMultiChoiceModeListener(new MultichooserInterests(this, list));
        list.setOnItemClickListener(this);
        list.setItemsCanFocus(false);
        list.setAdapter(adapter);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        view.setSelected(true);
        list.setItemChecked(position, true);
    }

    public boolean performActions(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.done:
                SharedPreferences sp = getActivity().getSharedPreferences("UserItemSelected", 0);
                SharedPreferences.Editor editor = sp.edit();
                StringBuilder sb = new StringBuilder();
                for(String s : selectedItems){
                    sb.append(s).append(",");
                }
                editor.putString("SelectedItems", sb.toString());
                editor.apply();
                dismiss();
                return true;
        }
        return false;
    }

    @Override
    public void itemSelected(int position) {
        selectedItems.add(interests[position]);
    }

    @Override
    public void itemDeselected(int position) {
        selectedItems.remove(interests[position]);
    }
}
