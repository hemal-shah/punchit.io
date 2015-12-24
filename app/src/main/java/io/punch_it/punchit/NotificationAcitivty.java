package io.punch_it.punchit;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by hemal on 12/22/15.
 */
public class NotificationAcitivty extends Fragment {

    public NotificationAcitivty() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.notification_fragment, container, false);
    }
}
