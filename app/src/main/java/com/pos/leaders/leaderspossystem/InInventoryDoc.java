package com.pos.leaders.leaderspossystem;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.pos.leaders.leaderspossystem.DataBaseAdapter.ProductDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.ProviderDbAdapter;
import com.pos.leaders.leaderspossystem.Models.Product;
import com.pos.leaders.leaderspossystem.Models.Provider;
import com.pos.leaders.leaderspossystem.Tools.ProductCatalogGridViewAdapter;
import com.pos.leaders.leaderspossystem.Tools.ProviderCatalogGridViewAdapter;
import com.pos.leaders.leaderspossystem.Tools.TitleBar;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class InInventoryDoc extends AppCompatActivity {
    TextView providerName;
    ImageView chooseProvider;
    Button inventoryIn , inventoryOut;
    boolean userScrolled =false;
    List<Provider> providerList = new ArrayList<>();
    List<Provider> AllProviderList = new ArrayList<>();
    public static Provider provider;
    Product product =new Product();
    Spinner SpProductBranch;
    ListView finalProductListView;
    GridView gvProduct;
    List<Product>filter_productList=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        // Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_in_inventory_doc);
        TitleBar.setTitleBar(this);
        chooseProvider = (ImageView)findViewById(R.id.chooseProvider);
        providerName = (TextView)findViewById(R.id.providerName);
        inventoryIn=(Button)findViewById(R.id.addProductToInventory);
        inventoryOut=(Button)findViewById(R.id.outProductFromInventory);
        SpProductBranch = (Spinner)findViewById(R.id.SpInventoryBranch);
        gvProduct =(GridView)findViewById(R.id.inOutInventoryGvProducts);
        filter_productList=new ArrayList<>();
        final ProductDBAdapter productDBAdapter = new ProductDBAdapter(this);
        productDBAdapter.open();
        filter_productList = productDBAdapter.getAllProducts();
        ProductCatalogGridViewAdapter adapter = new ProductCatalogGridViewAdapter(this, filter_productList);
        gvProduct.setAdapter(adapter);
        gvProduct.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                try {
                    addToCart(filter_productList.get(position));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
        chooseProvider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callProviderDialog();


            }
        });
        inventoryIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });
    }
    private void callProviderDialog() {
        ProviderDbAdapter providerDbAdapter =new ProviderDbAdapter(this);
        providerDbAdapter.open();
        final Dialog providerDialog = new Dialog(InInventoryDoc.this);
        providerDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        providerDialog.show();
        providerDialog.setContentView(R.layout.pop_up);
        final EditText provider_id = (EditText) providerDialog.findViewById(R.id.customer_name);
        final GridView gvProvider = (GridView) providerDialog.findViewById(R.id.popUp_gvCustomer);
        gvProvider.setNumColumns(3);

        Button btn_cancel = (Button) providerDialog.findViewById(R.id.btn_cancel);

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                providerDialog.dismiss();
            }
        });

        provider_id.setText("");
        provider_id.setHint("Search..");

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        gvProvider.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
        gvProvider.setOnScrollListener(new AbsListView.OnScrollListener() {
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

        providerList = providerDbAdapter.getAllCustomer();
        AllProviderList = providerList;

        ProviderCatalogGridViewAdapter providerCatalogGridViewAdapter = new ProviderCatalogGridViewAdapter(getApplicationContext(), providerList);

        gvProvider.setAdapter(providerCatalogGridViewAdapter);

        gvProvider.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                provider= providerList.get(position);
                providerName.setText(provider.getFullName());
                if(provider!=null) {
                    StartOrderDocumentConnection startOrderDocumentConnection = new StartOrderDocumentConnection();
                    startOrderDocumentConnection.execute(String.valueOf(provider.getProviderId()));
                }
                providerDialog.dismiss();
            }
        });

        provider_id.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                gvProvider.setTextFilterEnabled(true);

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                providerList = new ArrayList<Provider>();
                String word = provider_id.getText().toString();

                if (!word.equals("")) {
                    for (Provider c : AllProviderList) {

                        if (c.getFirstName().toLowerCase().contains(word.toLowerCase()) ||
                                c.getLastName().toLowerCase().contains(word.toLowerCase())||
                                c.getProviderIdentity().toLowerCase().contains(word.toLowerCase())||
                                c.getPhoneNumber().toLowerCase().contains(word.toLowerCase())) {
                            providerList.add(c);

                        }
                    }
                } else {
                    providerList = AllProviderList;
                }
                ProviderCatalogGridViewAdapter adapter = new ProviderCatalogGridViewAdapter(getApplicationContext(), providerList);
                gvProvider.setAdapter(adapter);
                // Log.i("products", productList.toString());


            }
        });


    }

}
