package com.pos.leaders.leaderspossystem;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.pos.leaders.leaderspossystem.DataBaseAdapter.CustomerAssetDB;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.EmployeeDBAdapter;
import com.pos.leaders.leaderspossystem.Models.CustomerAssistant;
import com.pos.leaders.leaderspossystem.Tools.DateConverter;
import com.pos.leaders.leaderspossystem.Tools.SalesManDetailsGridViewAdapter;
import com.pos.leaders.leaderspossystem.Tools.TitleBar;
import com.pos.leaders.leaderspossystem.Tools.Util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SalesAssistantDetailesSalesMangmentActivity extends AppCompatActivity {
   SalesManDetailsGridViewAdapter adapter;
    List<CustomerAssistant> customerAssests;
    CustomerAssetDB customerAssetDB;
    ListView gvSalesMan;
    EditText etSearch ;
    TextView etUserName, etAmount;
  List <CustomerAssistant>salesMan;
    List<CustomerAssistant>All_custmerAssestint;
    boolean userScrolled=true;
    Date to , from ;
    Button print , cancel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        // Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_sales_assistant_detailes_sales_mangment );

        TitleBar.setTitleBar(this);
        Bundle bundle = getIntent().getExtras();
        long userId = (long) bundle.get("employeeId");
        EmployeeDBAdapter userDBAdapter=new EmployeeDBAdapter(this);
        userDBAdapter.open();
        String userName=userDBAdapter.getEmployeesName(userId);

        etUserName=(TextView) findViewById(R.id.etUserName);
        etAmount=(TextView)findViewById(R.id.etAmount);
        etUserName.setText(": "+userName);
        etSearch = (EditText) findViewById(R.id.etSearch);
        print = (Button)findViewById(R.id.salesManManagement_BTPrintReport);
        cancel = (Button)findViewById(R.id.salesManManagement_BTCancel);
        gvSalesMan = (ListView) findViewById(R.id.Management_GVSalesManSalesDetails);
        customerAssetDB= new CustomerAssetDB(this);
        customerAssetDB.open();
        from= DateConverter.stringToDate(DateConverter.toDate(new Date()));
        to = DateConverter.stringToDate(DateConverter.currentDateTime());
        customerAssests = customerAssetDB.getBetweenTwoDates(userId,from.getTime(),to.getTime());
        double amount=customerAssetDB.getTotalAmountForAssistant(userId,from.getTime(),to.getTime());
        etAmount.setText(": "+Util.makePrice(amount));
        All_custmerAssestint = customerAssests;
        All_custmerAssestint = customerAssests;
        LayoutInflater inflater = getLayoutInflater();
        ViewGroup header = (ViewGroup)inflater.inflate(R.layout.list_adapter_customer_assistant_header, gvSalesMan, false);
        gvSalesMan.addHeaderView(header, null, false);
        adapter = new SalesManDetailsGridViewAdapter(this, R.layout.grid_view_item_sales_man,customerAssests);
        gvSalesMan.setAdapter(adapter);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        etSearch.setText("");
        etSearch.setHint("Search..");

        etSearch.setFocusable(true);
        etSearch.requestFocus();
        gvSalesMan.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
        gvSalesMan.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    userScrolled = true;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (userScrolled && firstVisibleItem + visibleItemCount == totalItemCount) {

                    userScrolled = false;
                    //  loadMoreProduct();
                }
            }
        });


        etSearch.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                etSearch.setFocusable(true);
            }
        });
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                gvSalesMan.setTextFilterEnabled(true);

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                customerAssests = new ArrayList<CustomerAssistant>();
                String word = etSearch.getText().toString();

                if (!word.equals("")) {
                    for (CustomerAssistant c : All_custmerAssestint) {

                        if (c.getSalesCase().toLowerCase().contains(word.toLowerCase())||(c.getAmount()+"").contains(word.toLowerCase())||(c.getOrderId()+"").contains(word.toLowerCase())
                                ||(c.getCreatedAt()+"").contains(word.toLowerCase())) {
                            customerAssests.add(c);

                        }
                    }
                }

                   else {
                        customerAssests = All_custmerAssestint;
                    }
                adapter = new SalesManDetailsGridViewAdapter(getApplicationContext(),customerAssests);

                gvSalesMan.setAdapter(adapter);

            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.salesManReport(SalesAssistantDetailesSalesMangmentActivity.this,customerAssests);

            }
        });



    }

}
