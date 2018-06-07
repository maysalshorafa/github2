package com.pos.leaders.leaderspossystem.SettingsTab;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.pos.leaders.leaderspossystem.R;
import com.pos.leaders.leaderspossystem.SetUpManagement;
import com.pos.leaders.leaderspossystem.Tools.SETTINGS;

/**
 * Created by Win8.1 on 3/25/2018.
 */

public class PosSettings extends Fragment {
    CheckBox currencyCheckBox , creditCardCheckBox ,cbPinPad, customerMeasurementCheckBox ;
    TextView floatPointNo , printerTypeTv ;
    public static final String LEAD_POS_RESULT_INTENT_SETTING_ENABLE_EDIT = "LEAD_POS_RESULT_INTENT_SETTING_ENABLE_EDIT";
    Button btnEditPosSetting;
    ImageView currencyImage, customerMeasurementImage, creditCardImage,ivPinpad;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.pos_settings_fragment, container, false);
        btnEditPosSetting = (Button)v.findViewById(R.id.settings_editPosSetting);
        currencyCheckBox = (CheckBox) v.findViewById(R.id.setUpManagementCurrencyCheckBox);
        creditCardCheckBox = (CheckBox) v.findViewById(R.id.setUpManagementCreditCardCheckBox);
        cbPinPad = (CheckBox) v.findViewById(R.id.setUpManagementCreditCardPinPadCheckBox);
        customerMeasurementCheckBox = (CheckBox) v.findViewById(R.id.setUpManagementCustomerMeasurementCheckBox);
        floatPointNo = (TextView)v.findViewById(R.id.noOfFloatPoint);
        printerTypeTv = (TextView)v.findViewById(R.id.printerType);
        currencyImage = (ImageView) v.findViewById(R.id.currencyImage);
        creditCardImage = (ImageView) v.findViewById(R.id.creditCardImage);
        ivPinpad = (ImageView) v.findViewById(R.id.creditCardPinPadImage);
        customerMeasurementImage = (ImageView) v.findViewById(R.id.customerMeasurementImage);
        floatPointNo.setText(SETTINGS.decimalNumbers+" ");
        printerTypeTv.setText(SETTINGS.printer.toString());
        if(SETTINGS.enableCurrencies){
            currencyCheckBox.setChecked(true);
        }
        if(SETTINGS.creditCardEnable){
            creditCardCheckBox.setChecked(true);
        }
        if(SETTINGS.pinpadEnable){
            cbPinPad.setChecked(true);
        }
        if(SETTINGS.enableCustomerMeasurement){
            customerMeasurementCheckBox.setChecked(true);
        }
        btnEditPosSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), SetUpManagement.class);
                i.putExtra(LEAD_POS_RESULT_INTENT_SETTING_ENABLE_EDIT,true);
                startActivity(i);
            }
        });
        currencyImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(SETTINGS.enableCurrencies){
                    Toast.makeText(getContext(), getString(R.string.currency_is_activated_in_pos), Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(getContext(), getString(R.string.currency_is_not_activated_in_pos), Toast.LENGTH_LONG).show();
                }
            }
        });
        creditCardImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(SETTINGS.creditCardEnable){
                    Toast.makeText(getContext(), getString(R.string.credit_card_is_activated_in_pos), Toast.LENGTH_LONG).show();

                }
            else {
                    Toast.makeText(getContext(),getString(R.string.credit_card_is_not_activated_in_pos), Toast.LENGTH_LONG).show();

                }
            }
        });
        ivPinpad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(SETTINGS.pinpadEnable){
                    Toast.makeText(getContext(), "PinPad is activated", Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(getContext(), "PinPad is not activated", Toast.LENGTH_LONG).show();

                }
            }
        });
        customerMeasurementImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(SETTINGS.enableCustomerMeasurement){
                    Toast.makeText(getContext(), getString(R.string.customer_measurement_is_activated_in_pos), Toast.LENGTH_LONG).show();

                }else {
                    Toast.makeText(getContext(), getString(R.string.customer_measurement_is_not_activated_in_pos), Toast.LENGTH_LONG).show();

                }
            }
        });
        return v;
    }
}
