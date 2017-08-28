package com.pos.leaders.leaderspossystem;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class TouchPadFragment extends Fragment {


    public TouchPadFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_touch_pad, container, false);
        return inflater.inflate(R.layout.calculator_pad, container, false);
    }

    public void click(View view){

    }
}
