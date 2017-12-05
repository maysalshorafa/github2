package com.pos.leaders.leaderspossystem.CustomerAndClub;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.pos.leaders.leaderspossystem.DataBaseAdapter.CustomerDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.ClubAdapter;
import com.pos.leaders.leaderspossystem.Models.Customer;
import com.pos.leaders.leaderspossystem.Models.Group;
import com.pos.leaders.leaderspossystem.R;
import com.pos.leaders.leaderspossystem.Tools.CustomerCatalogGridViewAdapter;
import com.pos.leaders.leaderspossystem.Tools.TitleBar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Coustmer_Group extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    android.support.v7.app.ActionBar actionBar;
    TextView tvParcent, tvPoint, tvAmount;
    EditText etGroupName, etParcent, etAmount, etPoint, etDescription;
    Spinner clubType;

    Button btAddGroup, btCancel;
    ClubAdapter groupAdapter = new ClubAdapter(this);
    Group group;
    List<Customer> custmers;
    CustomerDBAdapter customerDBAdapter;
    GridView gvcustmer;
    Customer customer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        // Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_coustmer__group);

        TitleBar.setTitleBar(this);
        groupAdapter.open();
        etGroupName = (EditText) findViewById(R.id.group_name);
        clubType = (Spinner) findViewById(R.id.typeSpinner);
        clubType.setOnItemSelectedListener(this);
        etParcent = (EditText) findViewById(R.id.et_parcent);
        etAmount = (EditText) findViewById(R.id.et_amount);
        etPoint = (EditText) findViewById(R.id.et_point);
        etDescription = (EditText) findViewById(R.id.ET_description);
        tvParcent = (TextView) findViewById(R.id.tvParcent);
        tvPoint = (TextView) findViewById(R.id.tvPoint);
        tvAmount = (TextView) findViewById(R.id.tvAmount);
        btCancel = (Button) findViewById(R.id.addGroup_BTCancel);
        btAddGroup = (Button) findViewById(R.id.add_group);
        gvcustmer = (GridView) findViewById(R.id.custmerManagement_GVCustmerClub);
        group = null;
        ArrayList<Integer> idForClubType = new ArrayList<Integer>();
        ArrayList<String> hintForClubType = new ArrayList<String>();
        hintForClubType.add(getString(R.string.club_general_type));
        hintForClubType.add(getString(R.string.club_parcent));
        hintForClubType.add(getString(R.string.club_point_amount));
        idForClubType.add(0);
        idForClubType.add(1);
        idForClubType.add(2);

        String[] spinnerArray = new String[idForClubType.size()];
        HashMap<Integer, Integer> spinnerMap = new HashMap<>();
        for (int i = 0; i < idForClubType.size(); i++) {
            spinnerMap.put(i, idForClubType.get(i));
            spinnerArray[i] = hintForClubType.get(i);
        }
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinnerArray);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        clubType.setAdapter(adapter1);
        String name = clubType.getSelectedItem().toString();
        final Integer id = spinnerMap.get(clubType.getSelectedItemPosition());

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            long i = (long) bundle.get("id");
            group = groupAdapter.getGroupByID(i);
            customerDBAdapter = new CustomerDBAdapter(this);
            customerDBAdapter.open();
            custmers = customerDBAdapter.getAllCustomerInClub(group.getId());

            final CustomerCatalogGridViewAdapter adapter = new CustomerCatalogGridViewAdapter(this, custmers);
            gvcustmer.setAdapter(adapter);
            etGroupName.setText(group.getname());
            etDescription.setText(group.getDescription());
            etParcent.setText("" + group.getParcent());
            etAmount.setText("" + group.getAmount());
            etPoint.setText("" + group.getPoint());
            clubType.setSelection(group.getType());
            btAddGroup.setText(getResources().getText(R.string.edit));
        }


        btAddGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String _groupName = etGroupName.getText().toString();
                Intent intent;
                if (group == null) {
                    if (_groupName != "") {
                        if (groupAdapter.availableGrouprName(_groupName)) {
                            if (etGroupName.getText().toString().equals("")) {
                                Toast.makeText(getApplicationContext(), getString(R.string.please_insert_club_name), Toast.LENGTH_LONG).show();
                            } else if (etDescription.getText().toString().equals("")) {
                                Toast.makeText(getApplicationContext(), getString(R.string.please_insert_club_description), Toast.LENGTH_LONG).show();
                            } else if (etParcent.getText().toString().equals("")) {
                                Toast.makeText(getApplicationContext(), getString(R.string.please_insert_club_percentage), Toast.LENGTH_LONG).show();
                            } else {
                                long i = groupAdapter.insertEntry(etGroupName.getText().toString(), etDescription.getText().toString(), id, Float.valueOf(etParcent.getText().toString()), Integer.parseInt(etAmount.getText().toString()), Integer.parseInt(etPoint.getText().toString()));

                                if (i > 0) {
                                    Log.i("success", "adding new Group");

                                    Toast.makeText(getApplicationContext(), getString(R.string.success_adding_new_club), Toast.LENGTH_LONG).show();
                                    try {
                                        Thread.sleep(500);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    onBackPressed();
                                } else {
                                    Log.e("error", "can`t add group");
                                    Toast.makeText(getApplicationContext(), getString(R.string.can_not_add_club_please_try_again), Toast.LENGTH_SHORT).show();
                                }
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), getString(R.string.club_name_is_not_available_try_to_use_another_club_name), Toast.LENGTH_LONG).show();
                        }
                    }
                } else {
                    // Edit mode
                    if (_groupName != "") {
                        if (etGroupName.getText().toString().equals("")) {
                            Toast.makeText(getApplicationContext(), getString(R.string.please_insert_club_name), Toast.LENGTH_LONG).show();
                        } else if (etDescription.getText().toString().equals("")) {
                            Toast.makeText(getApplicationContext(), getString(R.string.please_insert_club_description), Toast.LENGTH_LONG).show();
                        } else if (etParcent.getText().toString().equals("")) {
                            Toast.makeText(getApplicationContext(), getString(R.string.please_insert_club_percentage), Toast.LENGTH_LONG).show();
                        } else {
                            try {
                                etAmount.setVisibility(View.VISIBLE);
                                etPoint.setVisibility(View.VISIBLE);
                                etParcent.setVisibility(View.VISIBLE);
                                etGroupName.setText(group.getname());
                                etDescription.setText(group.getDescription());
                                etParcent.setText("" + group.getParcent());
                                etAmount.setText("" + group.getAmount());
                                etPoint.setText("" + group.getPoint());
                                clubType.setSelection(group.getType());
                                Log.i("success Edit", group.toString());
                                onBackPressed();
                            } catch (Exception ex) {
                                Log.e("error can`t edit 0club", ex.getMessage().toString());
                                Toast.makeText(getApplicationContext(), getString(R.string.can_not_edit_club_please_try_again), Toast.LENGTH_SHORT).show();
                            }
                        }

                    }
                }
            }
        });

        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (id == 1) {
            tvParcent.setVisibility(View.VISIBLE);
            etParcent.setVisibility(View.VISIBLE);
            tvAmount.setVisibility(View.GONE);
            tvPoint.setVisibility(View.GONE);
            etAmount.setVisibility(View.GONE);
            etPoint.setVisibility(View.GONE);
            etPoint.setText("0");
            etAmount.setText("0");

        }
        if (id == 2) {
            etAmount.setVisibility(View.VISIBLE);
            etPoint.setVisibility(View.VISIBLE);
            tvPoint.setVisibility(View.VISIBLE);
            tvAmount.setVisibility(View.VISIBLE);
            etParcent.setVisibility(View.GONE);
            tvParcent.setVisibility(View.GONE);
            etParcent.setText("0.0");

        }
        if (id == 3) {
            etAmount.setVisibility(View.GONE);
            etPoint.setVisibility(View.GONE);
            etParcent.setVisibility(View.GONE);
            tvParcent.setVisibility(View.GONE);
            tvAmount.setVisibility(View.GONE);
            tvPoint.setVisibility(View.GONE);
            etParcent.setText("0.0");
            etPoint.setText("0");
            etAmount.setText("0");
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
