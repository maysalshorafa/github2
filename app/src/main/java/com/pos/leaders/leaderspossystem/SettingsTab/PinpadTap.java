package com.pos.leaders.leaderspossystem.SettingsTab;

/**
 * Created by Win8.1 on 5/23/2018.
 */
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.pos.leaders.leaderspossystem.R;

public class PinpadTap extends Fragment  {
    EditText et_ip, et_username, et_password;
    Button bt_save;
    public static final String PINPAD_PREFERENCES = "pinpad_settings";
    public static final String PINPAD_IP = "ip";
    public static final String PINPAD_USERNAME = "username";
    public static final String PINPAD_PASSWORD = "password";
    SharedPreferences pinpadSP;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.new_tab_fragment, container, false);
        bt_save = (Button)v.findViewById(R.id.pinpad_save);
        et_ip = (EditText)v.findViewById(R.id.pinpad_ip);
        et_username =(EditText)v.findViewById(R.id.pinpad_username);
        et_password =(EditText)v.findViewById(R.id.pinpad_password);

        pinpadSP = getContext().getSharedPreferences(PINPAD_PREFERENCES, 0);

        if (pinpadSP.contains(PINPAD_IP)||pinpadSP.contains(PINPAD_USERNAME)) {
            et_ip.setText(pinpadSP.getString(PINPAD_IP, null));
            et_username.setText(pinpadSP.getString(PINPAD_USERNAME, null));
            et_password.setText(pinpadSP.getString(PINPAD_PASSWORD, null));
        }


        bt_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = pinpadSP.edit();
                editor.putString(PINPAD_IP, et_ip.getText().toString());
                editor.putString(PINPAD_USERNAME, et_username.getText().toString());
                editor.putString(PINPAD_PASSWORD, et_password.getText().toString());
                editor.apply();

                Toast.makeText(getContext(), "Done!", Toast.LENGTH_SHORT).show();
            }
        });

        return v;
    }
}
