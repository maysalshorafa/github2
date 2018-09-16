package com.pos.leaders.leaderspossystem;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.Toast;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.ProductDBAdapter;
import com.pos.leaders.leaderspossystem.Models.Product;
import com.pos.leaders.leaderspossystem.Offers.Action;
import com.pos.leaders.leaderspossystem.Offers.ResourceType;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GiftProductActivity extends AppCompatActivity {

    List<Long> resourceList;
    int giftQuantity;
    ResourceType resourceType;

    List<Product> products = null;

    List<Product> selectedProduct = new ArrayList<>();

    ProductMultiSelectListAdapter adapter;
    RecyclerView recyclerView;

    Button btOk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gift_product);

        recyclerView = (RecyclerView) findViewById(R.id.multiSelect_rvMain);
        btOk = (Button) findViewById(R.id.multiSelect_btOk);



        //check extras
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            resourceList = new ArrayList<Long>();
            String resList = extras.getString(Action.RESOURCES_LIST.getValue());
            try {
                resourceList = Arrays.asList(new ObjectMapper().readValue(resList, Long[].class));
            } catch (IOException e) {
                //fail on convert
                onBackPressed();
            }

            giftQuantity = (int) extras.get(Action.QUANTITY.getValue());
            resourceType = ResourceType.valueOf(extras.getString(Action.RESOURCE_TYPE.getValue()));
        } else {
            onBackPressed();
        }

        ProductDBAdapter productDBAdapter = new ProductDBAdapter(this);
        productDBAdapter.open();

        switch (resourceType) {
            case CATEGORY:
                for(long rID:resourceList){
                    products.addAll(productDBAdapter.getAllProductsByCategory(rID));
                }
                break;
            case PRODUCT:
                for(long pID:resourceList) {
                    products.add(productDBAdapter.getProductByID(pID));
                }
                break;
        }

        productDBAdapter.close();


        adapter = new ProductMultiSelectListAdapter(products, selectedProduct, this);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                multiSelect(position);
            }


            @Override
            public void onItemLongClick(View view, int position) {

            }

        }));

    }

    public void multiSelect(int position) {
            if (selectedProduct.contains(products.get(position)))
                selectedProduct.remove(products.get(position));
            else {
                if(selectedProduct.size()<giftQuantity)
                    selectedProduct.add(products.get(position));
                else {
                    Toast.makeText(this, "selec", Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            refreshAdapter();
    }

    public void refreshAdapter() {
        adapter.selectedProducts=this.selectedProduct;
        adapter.products = this.products;
        adapter.notifyDataSetChanged();
    }

}
