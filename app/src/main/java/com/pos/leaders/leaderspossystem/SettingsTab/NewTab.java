package com.pos.leaders.leaderspossystem.SettingsTab;

/**
 * Created by Win8.1 on 5/23/2018.
 */
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.pos.leaders.leaderspossystem.R;

public class NewTab extends Fragment  {
    EditText et1 , et2 ,et3 ;
    Button ok;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.new_tab_fragment, container, false);
        ok = (Button)v.findViewById(R.id.ok);
        et1 = (EditText)v.findViewById(R.id.add_first_value);
        et2 =(EditText)v.findViewById(R.id.second_value);
        et3=(EditText)v.findViewById(R.id.third_value);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 Toast.makeText(getContext(), "New Tab", Toast.LENGTH_LONG).show();
            }
        });

        return v;
    }
}
