package com.pos.leaders.leaderspossystem;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.pos.leaders.leaderspossystem.DataBaseAdapter.OfferDBAdapter;
import com.pos.leaders.leaderspossystem.Models.Offer;
import com.pos.leaders.leaderspossystem.Tools.OfferManagementListViewAdapter;
import com.pos.leaders.leaderspossystem.Tools.TitleBar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class OfferManagementActivity extends AppCompatActivity {
    ListView lvOffers;
    OfferDBAdapter offerDBAdapter;
    private static final int DIALOG_FROM_DATE = 825;
    private static final int DIALOG_TO_DATE = 324;
    EditText etSearch;
    OfferManagementListViewAdapter adapter;
    View previousView = null;
    List<Offer> _offerList;
    private final static int DAY_MINUS_ONE_SECOND = 86399999;
    List<Offer> All_Offers;
    public static int Offer_Management_Edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        // Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_offer_management2);
        TitleBar.setTitleBar(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        lvOffers = (ListView) findViewById(R.id.offerManagement_LVOffers);
        etSearch = (EditText) findViewById(R.id.etSearch);
        etSearch.setText("");
        etSearch.setHint("Search..");
        etSearch.setFocusable(true);
        etSearch.requestFocus();
        offerDBAdapter=new OfferDBAdapter(this);
        offerDBAdapter.open();
    }
    private void setDate() {
        _offerList = offerDBAdapter.getListOfValidOffers();
        All_Offers = _offerList;
        adapter = new OfferManagementListViewAdapter(this, R.layout.list_adapter_row_offer_management, _offerList);

        lvOffers.setAdapter(adapter);


        lvOffers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                Offer_Management_Edit=0;
                final String[] items = {
                     //   getString(R.string.edit_offer),
                        getString(R.string.delete)
                };
                AlertDialog.Builder builder = new AlertDialog.Builder(OfferManagementActivity.this);
                builder.setTitle(getBaseContext().getString(R.string.make_your_selection));
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        Intent intent;
                        switch (item) {
                           /** case 0:
                                Offer_Management_Edit=15;
                                intent = new Intent(OfferManagementActivity.this, CreateOfferActivity.class);
                                intent.putExtra("offerId", _offerList.get(position).getOfferId());
                                startActivity(intent);
                                break;**/

                            case 0:
                                new AlertDialog.Builder(OfferManagementActivity.this)
                                        .setTitle(getString(R.string.delete_offer))
                                        .setMessage(getString(R.string.delete))
                                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                offerDBAdapter.deleteEntry(_offerList.get(position).getOfferId());
                                                _offerList.remove(_offerList.get(position));
                                                lvOffers.setAdapter(adapter);
                                                adapter.notifyDataSetChanged();
                                            }
                                        })
                                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                // do nothing
                                            }
                                        })
                                        .setIcon(android.R.drawable.ic_dialog_alert)
                                        .show();
                                break;


                        }
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
                    }
                });
                //endregion Print Button

                //region Replacement Note Button
        etSearch.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                etSearch.setFocusable(true);
            }
        });
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                lvOffers.setTextFilterEnabled(true);

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                _offerList = new ArrayList<Offer>();
                String word = etSearch.getText().toString();
                String actionName = "";
                if (!word.equals("")) {
                    for (Offer o : All_Offers) {
                        String data=o.getDataAsJson().toString();
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(data);
                            JSONObject action = new JSONObject(jsonObject.get("action").toString());
                            actionName = action.getString("name");
                            Log.d("action",actionName);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        if (o.getName().toLowerCase().contains(word.toLowerCase()) ||(o.getResourceType() + "").contains(word.toLowerCase())
                                || (o.getCreatedAt() + "").contains(word.toLowerCase())|| (o.getStartDate() + "").contains(word.toLowerCase())|| actionName.contains(word.toLowerCase())|| (o.getEndDate() + "").contains(word.toLowerCase())) {
                            _offerList.add(o);

                        }
                    }
                } else {
                    _offerList = All_Offers;
                }
                adapter = new OfferManagementListViewAdapter(getApplicationContext(), R.layout.list_adapter_row_offer_management, _offerList);

                lvOffers.setAdapter(adapter);
            }
        });

    }


    @Override
    protected void onResume() {
        super.onResume();
        setDate();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) // Press Back Icon
        {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
