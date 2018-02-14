package com.pos.leaders.leaderspossystem.CustomerAndClub;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
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

import com.pos.leaders.leaderspossystem.DataBaseAdapter.ClubAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.CustomerDBAdapter;
import com.pos.leaders.leaderspossystem.Models.Club;
import com.pos.leaders.leaderspossystem.Models.Customer;
import com.pos.leaders.leaderspossystem.R;
import com.pos.leaders.leaderspossystem.Tools.CustomerCatalogGridViewAdapter;
import com.pos.leaders.leaderspossystem.Tools.TitleBar;

import java.util.ArrayList;
import java.util.List;

public class Coustmer_Group extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    TextView tvPercent, tvPoint, tvAmount;
    EditText etClubName, etPercent, etAmount, etPoint, etDescription;
    Spinner clubType;
    Button btAddGroup, btCancel;
    ClubAdapter clubAdapter = new ClubAdapter(this);
    Club club;
    List<Customer> customers;
    CustomerDBAdapter customerDBAdapter;
    GridView gvCustomer;
    int id=0;
    float discount=0;
    int point ,amount=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        // Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_coustmer__group);

        TitleBar.setTitleBar(this);
        clubAdapter.open();
        etClubName = (EditText) findViewById(R.id.group_name);
        clubType = (Spinner) findViewById(R.id.typeSpinner);
        clubType.setOnItemSelectedListener(this);
        etPercent = (EditText) findViewById(R.id.et_parcent);
        etAmount = (EditText) findViewById(R.id.et_amount);
        etPoint = (EditText) findViewById(R.id.et_point);
        etDescription = (EditText) findViewById(R.id.ET_description);
        tvPercent = (TextView) findViewById(R.id.tvParcent);
        tvPoint = (TextView) findViewById(R.id.tvPoint);
        tvAmount = (TextView) findViewById(R.id.tvAmount);
        btCancel = (Button) findViewById(R.id.addGroup_BTCancel);
        btAddGroup = (Button) findViewById(R.id.add_group);
        gvCustomer = (GridView) findViewById(R.id.custmerManagement_GVCustmerClub);
        club = null;
        etAmount.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        etPoint.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        etPercent.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        final ArrayList<Integer> idForClubType = new ArrayList<Integer>();
        final ArrayList<String> hintForClubType = new ArrayList<String>();
        hintForClubType.add(getString(R.string.club_general_type));
        hintForClubType.add(getString(R.string.club_parcent));
        hintForClubType.add(getString(R.string.club_point_amount));
        idForClubType.add(0);
        idForClubType.add(1);
        idForClubType.add(2);

        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, hintForClubType);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        clubType.setAdapter(adapter1);
        String name = clubType.getSelectedItem().toString();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            long i = (long) bundle.get("id");
            club = clubAdapter.getGroupByID(i);
            customerDBAdapter = new CustomerDBAdapter(this);
            customerDBAdapter.open();
            customers = customerDBAdapter.getAllCustomerInClub(club.getId());
            final CustomerCatalogGridViewAdapter adapter = new CustomerCatalogGridViewAdapter(this, customers);
            gvCustomer.setAdapter(adapter);
            etClubName.setText(club.getname());
            etDescription.setText(club.getDescription());
            etPercent.setText("" + club.getParcent());
            etAmount.setText("" + club.getAmount());
            etPoint.setText("" + club.getPoint());
            clubType.setSelection(club.getType());

        }
        if(ClubManagementActivity.Club_Management_View==9){
            etAmount.setEnabled(false);
            etClubName.setEnabled(false);
            etDescription.setEnabled(false);
            etPercent.setEnabled(false);
            etPoint.setEnabled(false);
            clubType.setEnabled(false);
            clubType.setClickable(false);
            btAddGroup.setVisibility(View.GONE);
            ClubManagementActivity.Club_Management_View=0;
        }
        if(ClubManagementActivity.Club_Management_Edit==10){
            clubType.setEnabled(false);
            clubType.setClickable(false);
            btAddGroup.setText(getString(R.string.edit));
            ClubManagementActivity.Club_Management_Edit=0;
        }

        btAddGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String _groupName = etClubName.getText().toString();
                Intent intent;
                if (club == null) {
                    if (_groupName != "") {
                        if (clubAdapter.availableGrouprName(_groupName)) {
                            if (etClubName.getText().toString().equals("")) {
                                Toast.makeText(getApplicationContext(), getString(R.string.please_insert_club_name), Toast.LENGTH_LONG).show();
                            } else if (etDescription.getText().toString().equals("")) {
                                Toast.makeText(getApplicationContext(), getString(R.string.please_insert_club_description), Toast.LENGTH_LONG).show();
                            }else {

                                for (int i =hintForClubType.size()-1;i>0; i--) {

                                    if (hintForClubType.get(i) == clubType.getSelectedItem().toString()) {
                                        id=idForClubType.get(i);
                                    }
                                }
                            }

                            if(id==1){
                                discount= Float.parseFloat(etPercent.getText().toString());

                            }
                            if (id==2){
                                point= Integer.parseInt(etPoint.getText().toString());
                                amount= Integer.parseInt(etAmount.getText().toString());
                            }


                            long i = clubAdapter.insertEntry(etClubName.getText().toString(), etDescription.getText().toString(), id,discount, amount, point);

                            if (i > 0) {
                                Log.i("success", "adding new Club");

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

                        } else {
                            Toast.makeText(getApplicationContext(), getString(R.string.club_name_is_not_available_try_to_use_another_club_name), Toast.LENGTH_LONG).show();
                        }
                    }
                } else {
                    // Edit mode
                    if (_groupName != "") {
                        if (etClubName.getText().toString().equals("")) {
                            Toast.makeText(getApplicationContext(), getString(R.string.please_insert_club_name), Toast.LENGTH_LONG).show();
                        } else if (etDescription.getText().toString().equals("")) {
                            Toast.makeText(getApplicationContext(), getString(R.string.please_insert_club_description), Toast.LENGTH_LONG).show();
                        } else {
                            try {
                                if(club.getType()==0){
                                    club.setname(etClubName.getText().toString());
                                    club.setDescription(etDescription.getText().toString());

                                }
                                if(club.getType()==1){
                                    club.setname(etClubName.getText().toString());
                                    club.setDescription(etDescription.getText().toString());
                                    club.setParcent(Float.parseFloat(etPercent.getText().toString()));
                                }
                                if(club.getType()==2){
                                    club.setname(etClubName.getText().toString());
                                    club.setDescription(etDescription.getText().toString());
                                    club.setAmount(Integer.parseInt(etAmount.getText().toString()));
                                    club.setPoint(Integer.parseInt(etPoint.getText().toString()));

                                }
                                clubAdapter.updateEntry(club);
                                Log.i("success Edit", club.toString());
                                onBackPressed();
                            } catch (Exception ex) {
                                Log.e("error can`t edit club", ex.getMessage().toString());
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
                Intent intent = new Intent(Coustmer_Group.this, ClubManagementActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);            }
        });

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (id == 1) {
            tvPercent.setVisibility(View.VISIBLE);
            etPercent.setVisibility(View.VISIBLE);
            tvAmount.setVisibility(View.GONE);
            tvPoint.setVisibility(View.GONE);
            etAmount.setVisibility(View.GONE);
            etPoint.setVisibility(View.GONE);
            point=0;
            amount=0;

        }
        if (id == 2) {
            etAmount.setVisibility(View.VISIBLE);
            etPoint.setVisibility(View.VISIBLE);
            tvPoint.setVisibility(View.VISIBLE);
            tvAmount.setVisibility(View.VISIBLE);
            etPercent.setVisibility(View.GONE);
            tvPercent.setVisibility(View.GONE);
            discount=0;


        }
        if (id == 0) {
            point=0;
            amount=0;
            discount=0;
            etAmount.setVisibility(View.GONE);
            etPoint.setVisibility(View.GONE);
            etPercent.setVisibility(View.GONE);
            tvPercent.setVisibility(View.GONE);
            tvAmount.setVisibility(View.GONE);
            tvPoint.setVisibility(View.GONE);
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}


