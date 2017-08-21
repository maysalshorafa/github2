package com.pos.leaders.leaderspossystem.syncposservice.SetupFragments;

import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.pos.leaders.leaderspossystem.R;

/**
 * Created by KARAM on 10/08/2017.
 */

public class StandAloneSetup extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.stand_alone_setup, container, false);
        Button btStart = (Button) rootView.findViewById(R.id.standAloneSetup_btStart);

        btStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(rootView, "Welcome to SwA", Snackbar.LENGTH_LONG).show();
            }
        });

        return rootView;
    }
}
