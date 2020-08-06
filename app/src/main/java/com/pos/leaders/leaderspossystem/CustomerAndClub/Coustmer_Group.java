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
import com.pos.leaders.leaderspossystem.Tools.SETTINGS;
import com.pos.leaders.leaderspossystem.Tools.TitleBar;

import java.util.ArrayList;
import java.util.List;

public class Coustmer_Group extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    TextView tvPercent, tvPoint, tvAmount,tvValueOfPoint;
    EditText etClubName, etPercent, etAmount, etPoint, etDescription,etValueOfPoint;
    Spinner clubType,SpClubBranch;
    Button btAddGroup, btCancel;
    ClubAdapter clubAdapter = new ClubAdapter(this);
    Club club;
    List<Customer> customers;
    CustomerDBAdapter customerDBAdapter;
    GridView gvCustomer;
    int id=0;
    float discount=0;
    int point,branchId=0;
    double valueOfPoint=0 ,amount=0;
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
        etValueOfPoint=(EditText)findViewById(R.id.et_ValueOfPoint) ;
        etDescription = (EditText) findViewById(R.id.ET_description);
        tvPercent = (TextView) findViewById(R.id.tvParcent);
        tvPoint = (TextView) findViewById(R.id.tvPoint);
        tvAmount = (TextView) findViewById(R.id.tvAmount);
        tvValueOfPoint=(TextView)findViewById(R.id.tvValueOfPoint);
        btCancel = (Button) findViewById(R.id.addGroup_BTCancel);
        btAddGroup = (Button) findViewById(R.id.add_group);
        gvCustomer = (GridView) findViewById(R.id.custmerManagement_GVCustmerClub);
        SpClubBranch = (Spinner)findViewById(R.id.SpClubBranch);
        final List<String> clubBranch = new ArrayList<String>();
        clubBranch.add(getString(R.string.all));
        clubBranch.add(getString(R.string.pos_branch));
        final ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, clubBranch);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        SpClubBranch.setAdapter(dataAdapter);
        club = null;
        etAmount.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        etValueOfPoint.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
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

        final Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            long i = (long) bundle.get("id");
            club = clubAdapter.getGroupByID(i);
            customerDBAdapter = new CustomerDBAdapter(this);
            customerDBAdapter.open();
            customers = customerDBAdapter.getAllCustomerInClub(club.getClubId());
            final CustomerCatalogGridViewAdapter adapter = new CustomerCatalogGridViewAdapter(this, customers);
            gvCustomer.setAdapter(adapter);
            etClubName.setText(club.getName());
            etDescription.setText(club.getDescription());
            etPercent.setText("" + club.getPercent());
            etAmount.setText("" + club.getAmount());
            etPoint.setText("" + club.getPoint());
            etValueOfPoint.setText(""+club.getValueOfPoint());
            clubType.setSelection(club.getType());

        }
        if(ClubManagementActivity.Club_Management_View==9){
            etAmount.setEnabled(false);
            etClubName.setEnabled(false);
            etDescription.setEnabled(false);
            etPercent.setEnabled(false);
            etPoint.setEnabled(false);
            etValueOfPoint.setEnabled(false);
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
                                amount= Double.parseDouble(etAmount.getText().toString());
                                valueOfPoint=Double.parseDouble(etValueOfPoint.getText().toString());
                            }

                                if( !etDescription.getText().toString().equals("")&&  !etClubName.getText().toString().equals("")){
                                    if(SpClubBranch.getSelectedItem().toString().equals(getString(R.string.all))){
                                        branchId=0;
                                    }else {
                                        branchId= SETTINGS.branchId;
                                    }
                                    Log.d("valueOfPoint",valueOfPoint+"");
                            long i = clubAdapter.insertEntry(etClubName.getText().toString(), etDescription.getText().toString(), id,discount, amount, point,branchId,valueOfPoint);

                            if (i > 0) {
                                Log.d("success", "adding new Club");

                                Toast.makeText(getApplicationContext(), getString(R.string.success_adding_new_club), Toast.LENGTH_LONG).show();
                                try {
                                    Thread.sleep(500);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                onBackPressed();
                            }
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
                                if(SpClubBranch.getSelectedItem().toString().equals(getString(R.string.all))){
                                    branchId=0;
                                }else {
                                    branchId= SETTINGS.branchId;
                                }
                                if(club.getType()==0){
                                    club.setName(etClubName.getText().toString());
                                    club.setDescription(etDescription.getText().toString());
                                    club.setBranchId(branchId);

                                }
                                if(club.getType()==1){
                                    club.setName(etClubName.getText().toString());
                                    club.setDescription(etDescription.getText().toString());
                                    club.setPercent(Float.parseFloat(etPercent.getText().toString()));
                                    club.setBranchId(branchId);

                                }
                                if(club.getType()==2){
                                    club.setName(etClubName.getText().toString());
                                    club.setDescription(etDescription.getText().toString());
                                    club.setAmount(Double.parseDouble(etAmount.getText().toString()));
                                    club.setValueOfPoint(Double.parseDouble(etValueOfPoint.getText().toString()));
                                    club.setPoint(Integer.parseInt(etPoint.getText().toString()));
                                    club.setBranchId(branchId);

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
                Intent intent = new Intent(Coustmer_Group.this, com.pos.leaders.leaderspossystem.CustomerAndClub.Customer.class);
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
            tvValueOfPoint.setVisibility(View.GONE);
            tvPoint.setVisibility(View.GONE);
            etAmount.setVisibility(View.GONE);
            etPoint.setVisibility(View.GONE);
            etValueOfPoint.setVisibility(View.GONE);
            point=0;
            amount=0;
            valueOfPoint=0;

        }
        if (id == 2) {
            etAmount.setVisibility(View.VISIBLE);
            etPoint.setVisibility(View.VISIBLE);
            tvPoint.setVisibility(View.VISIBLE);
            tvAmount.setVisibility(View.VISIBLE);
            tvValueOfPoint.setVisibility(View.VISIBLE);
            etPercent.setVisibility(View.GONE);
            tvPercent.setVisibility(View.GONE);
            etValueOfPoint.setVisibility(View.VISIBLE);
            discount=0;


        }
        if (id == 0) {
            point=0;
            amount=0;
            discount=0;
            valueOfPoint=0;
            etAmount.setVisibility(View.GONE);
            etPoint.setVisibility(View.GONE);
            etPercent.setVisibility(View.GONE);
            tvPercent.setVisibility(View.GONE);
            tvAmount.setVisibility(View.GONE);
            tvPoint.setVisibility(View.GONE);
            tvValueOfPoint.setVisibility(View.GONE);
            etValueOfPoint.setVisibility(View.GONE);
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}


