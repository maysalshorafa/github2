package com.pos.leaders.leaderspossystem;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import com.pos.leaders.leaderspossystem.Tools.QuickPricePad;

/**
 * Created by TOSHIBA on 08/02/2017.
 */

public class QuickPricePadFragment extends Fragment implements View.OnLongClickListener{


    public QuickPricePadFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_quick_price_button, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        int[] ids = {R.id.fragmentQuickPriceButton_bt00, R.id.fragmentQuickPriceButton_bt01, R.id.fragmentQuickPriceButton_bt02,
                R.id.fragmentQuickPriceButton_bt10, R.id.fragmentQuickPriceButton_bt11, R.id.fragmentQuickPriceButton_bt12,
                R.id.fragmentQuickPriceButton_bt20, R.id.fragmentQuickPriceButton_bt21, R.id.fragmentQuickPriceButton_bt22};
        for (int i:ids){
            Button b = (Button) view.findViewById(i);

            b.setOnLongClickListener(this);

            b.setText(QuickPricePad.read(getContext(), b.getId()) + "");
        }
    }

    @Override
    public boolean onLongClick(View v) {
        quickPriceButtonLongPress(v);
        return true;
    }

    public void quickPriceButtonLongPress(View view){
        final View _view = view;
        final Dialog quickPriceDialog = new Dialog(getContext());
        quickPriceDialog.setTitle(R.string.set_price);
        quickPriceDialog.setContentView(R.layout.cash_payment_dialog);
        quickPriceDialog.show();
        final Button BTOk = (Button) quickPriceDialog.findViewById(R.id.cashPaymentDialog_BTOk);
        final Button BTCancel = (Button) quickPriceDialog.findViewById(R.id.cashPaymentDialog_BTCancel);
        final EditText ETCash = (EditText) quickPriceDialog.findViewById(R.id.cashPaymentDialog_TECash);
        final Switch sw = (Switch) quickPriceDialog.findViewById(R.id.cashPaymentDialog_SwitchProportion);
        sw.setVisibility(View.GONE);
        BTOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                QuickPricePad.write(getContext(), _view.getId(), Float.parseFloat(ETCash.getText().toString()));
                ((Button)_view).setText(Float.parseFloat(ETCash.getText().toString())+"");
                quickPriceDialog.cancel();
            }
        });
        BTCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quickPriceDialog.cancel();
            }
        });
        ETCash.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                BTOk.callOnClick();
                return false;
            }
        });
    }


}
