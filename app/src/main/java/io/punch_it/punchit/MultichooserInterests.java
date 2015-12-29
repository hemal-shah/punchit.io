package io.punch_it.punchit;

import android.graphics.Color;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.AbsListView;
import android.widget.ListView;

/**
 * Created by hemal on 12/29/15.
 */
public class MultichooserInterests implements AbsListView.MultiChoiceModeListener {
    TagsDialogFragment host;
    ActionMode actionMode;
    ItemStateChanged interfaceObject;
    ListView lv;

    MultichooserInterests(TagsDialogFragment host, ListView lv) {
        this.host=host;
        this.interfaceObject = host;
        this.lv=lv;
    }

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        MenuInflater inflater = host.getActivity().getMenuInflater();
        inflater.inflate(R.menu.context_dialog, menu);

        mode.setTitle("Tags selected!");
        mode.setSubtitle("(1)");
        actionMode = mode;
        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        return false;
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        boolean result = host.performActions(item);
        updateSubtitle(actionMode);
        return result;
    }

    private void updateSubtitle(ActionMode actionMode) {
        if(actionMode != null)
            actionMode.setSubtitle("("+lv.getCheckedItemCount()+")");
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {
        actionMode = null;
    }

    @Override
    public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
        updateSubtitle(mode);

        if(checked){
            interfaceObject.itemSelected(position);
        }else{
            interfaceObject.itemDeselected(position);
        }
    }

    public interface ItemStateChanged{
        void itemSelected(int position);
        void itemDeselected(int position);
    }

}