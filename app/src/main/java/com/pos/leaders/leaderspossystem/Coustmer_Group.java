package com.pos.leaders.leaderspossystem;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
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
import com.pos.leaders.leaderspossystem.DataBaseAdapter.GroupAdapter;
import com.pos.leaders.leaderspossystem.Models.Customer_M;
import com.pos.leaders.leaderspossystem.Models.Group;
import com.pos.leaders.leaderspossystem.Tools.CustmerCatalogGridViewAdapter;
import com.pos.leaders.leaderspossystem.Tools.SESSION;
import com.pos.leaders.leaderspossystem.Tools.TitleBar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class Coustmer_Group extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    android.support.v7.app.ActionBar actionBar;
TextView tvParcent , tvPoint ,tvAmount;
    EditText etGroupName , etParcent , etAmount ,etPoint ,etDescription;
    Spinner clubType;

    Button btAddGroup ,btCancel  ;
     GroupAdapter groupAdapter ;
    Group group;
    List<Customer_M> custmers;
    CustomerDBAdapter customerDBAdapter;
    GridView gvcustmer;
    Customer_M customer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        // Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_coustmer__group);

        TitleBar.setTitleBar(this);

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
        btCancel=(Button)findViewById(R.id.addGroup_BTCancel);
        btAddGroup = (Button) findViewById(R.id.add_group);
        group = null;
        ArrayList<Integer>idForClubType = new ArrayList<Integer>();
        ArrayList<String> hintForClubType = new ArrayList<String>();
        hintForClubType.add("With Parcent Type");
        hintForClubType.add("With Amount And Point ");
        hintForClubType.add("General Type");
        idForClubType.add(0);
        idForClubType.add(1);
        idForClubType.add(2);

        String[] spinnerArray = new String[idForClubType.size()];
        HashMap<Integer, Integer> spinnerMap = new HashMap<>();
        for (int i = 0; i < idForClubType.size(); i++)
        {
            spinnerMap.put(i,idForClubType.get(i));
            spinnerArray[i] = hintForClubType.get(i);
        }
        ArrayAdapter<String> adapter1 =new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, spinnerArray);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        clubType.setAdapter(adapter1);
        String name = clubType.getSelectedItem().toString();
        final Integer id = spinnerMap.get(clubType.getSelectedItemPosition());
     /**   ArrayList<Integer> types = new ArrayList<Integer>();

        // here we add the values to the Spiiner array
        types.add(0);
        types.add(1);
        types.add(2);
        ArrayAdapter type_adapter = new ArrayAdapter(this, R.layout.spinner_row_layout,R.id.spinnerTxt,
                types);
        clubType.setAdapter(type_adapter);**/
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            long i = (long) bundle.get("id");
            group = groupAdapter.getGroupByID(i);
            customerDBAdapter= new CustomerDBAdapter(this);
            customerDBAdapter.open();
            custmers = customerDBAdapter.getAllCustmerInClub(group.getId());

            final CustmerCatalogGridViewAdapter adapter = new CustmerCatalogGridViewAdapter(this, custmers);
            gvcustmer.setAdapter(adapter);
            etGroupName.setText(group.getname());
            etDescription.setText(group.getDescription());
            etParcent.setText(""+group.getParcent());
            etAmount.setText(""+group.getAmount());
            etParcent.setText(""+group.getParcent());
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
        Toast.makeText(getApplicationContext(), "Please insert Group Name", Toast.LENGTH_LONG).show();
        } else if (etDescription.getText().toString().equals("")) {
        Toast.makeText(getApplicationContext(), "Please insert Description", Toast.LENGTH_LONG).show();
        }
        else if (etParcent.getText().toString().equals("")) {
        Toast.makeText(getApplicationContext(), "Please insert email", Toast.LENGTH_LONG).show();
        }


        else {
            long i = groupAdapter.insertEntry(etGroupName.getText().toString(),etDescription.getText().toString(),id,Float.valueOf(etParcent.getText().toString()),Integer.parseInt(etAmount.getText().toString()),Integer.parseInt(etPoint.getText().toString()));

            if (i > 0) {
        Log.i("success", "adding new Group");
        intent = new Intent(Coustmer_Group.this, ClubMangmentActivity.class);
        startActivity(intent);
        //// TODO: 17/10/2016 sucess to add entity
        } else {
        Log.e("error", "can`t add group");
        Toast.makeText(getApplicationContext(), "Can`t add group please try again", Toast.LENGTH_LONG).show();
        //// TODO: 17/10/2016 error with adding entity
        }
        }
        } else {
        Toast.makeText(getApplicationContext(), "Group name is not available, try to use another Group name", Toast.LENGTH_LONG).show();
        }
        }
        } else {
        // Edit mode
        if (_groupName != "") {
            if (etGroupName.getText().toString().equals("")) {
        Toast.makeText(getApplicationContext(), "Please insert Group Name", Toast.LENGTH_LONG).show();
        } else if (etDescription.getText().toString().equals("")) {
        Toast.makeText(getApplicationContext(), "Please insert Description", Toast.LENGTH_LONG).show();
        }
        else {
        try {
            etAmount.setVisibility(View.VISIBLE);
            etPoint.setVisibility(View.VISIBLE);
            etParcent.setVisibility(View.VISIBLE);
        etGroupName.setText(group.getname());
        etDescription.setText(group.getDescription());
        etParcent.setText(""+group.getParcent());
        etAmount.setText(""+group.getAmount());
        etPoint.setText(""+group.getPoint());
            clubType.setSelection(group.getType());
            Log.i("success Edit", group.toString());
        intent = new Intent(Coustmer_Group.this, ClubMangmentActivity.class);
        startActivity(intent);
        } catch (Exception ex) {
        Log.e("error can`t edit group", ex.getMessage().toString());
        Toast.makeText(getApplicationContext(), "Can`t edit group please try again", Toast.LENGTH_SHORT).show();
        }
        }

        }
        }}
        });

        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //// TODO: 22/10/2016 cancel and return to previous activity
                Intent intent = new Intent(Coustmer_Group.this, MainActivity.class);
                //	intent.putExtra("permissions_name",user.getPermtionName());

                //userDBAdapter.close();
                startActivity(intent);
            }
        });



    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (id==0){
            tvParcent.setVisibility(View.VISIBLE);
            etParcent.setVisibility(View.VISIBLE);
            tvAmount.setVisibility(View.GONE);
            tvPoint.setVisibility(View.GONE);
            etAmount.setVisibility(View.GONE);
            etPoint.setVisibility(View.GONE);
            etPoint.setText("0");
            etAmount.setText("0");

        }
        if (id==1) {
            etAmount.setVisibility(View.VISIBLE);
            etPoint.setVisibility(View.VISIBLE);
            tvPoint.setVisibility(View.VISIBLE);
            tvAmount.setVisibility(View.VISIBLE);
            etParcent.setVisibility(View.GONE);
            tvParcent.setVisibility(View.GONE);
            etParcent.setText("0.0");

        }
        if (id==2) {
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
