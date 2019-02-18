package com.pos.leaders.leaderspossystem;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;

import com.pos.leaders.leaderspossystem.DataBaseAdapter.CategoryDBAdapter;
import com.pos.leaders.leaderspossystem.Models.Category;
import com.pos.leaders.leaderspossystem.Tools.CategoryGridViewAdapter;
import com.pos.leaders.leaderspossystem.Tools.TitleBar;

import java.util.ArrayList;
import java.util.List;

public class CategoryOfferActivity extends AppCompatActivity {
    EditText categoryOfferName;
    Button saveCategory , cancelAddCategoryOffer , addCategoryProduct , addProduct;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        // Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_category_offer);
        TitleBar.setTitleBar(this);
        categoryOfferName = (EditText)findViewById(R.id.etCategoryOfferName);
        saveCategory = (Button)findViewById(R.id.addNewCategoryOffer);
        cancelAddCategoryOffer = (Button)findViewById(R.id.cancelAddCategoryOffer);
        addCategoryProduct = (Button)findViewById(R.id.addCategoryProduct);
        addProduct=(Button)findViewById(R.id.addProduct);
        addCategoryProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            //addProductFromCategory
                addProductFromCategory();
            }
        });

        addProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        saveCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        cancelAddCategoryOffer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    private void addProductFromCategory() {
        final Dialog addProductFromCategoryDialog = new Dialog(CategoryOfferActivity.this);
        addProductFromCategoryDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        addProductFromCategoryDialog.show();
        addProductFromCategoryDialog.setContentView(R.layout.add_category_offer_product_from_category);
       EditText categoryName = (EditText) addProductFromCategoryDialog.findViewById(R.id.categoryName);
        final GridView gvCategory = (GridView) addProductFromCategoryDialog.findViewById(R.id.gvCategory);
        gvCategory.setNumColumns(3);
       Button btn_cancel = (Button) addProductFromCategoryDialog.findViewById(R.id.btn_cancel);
        Button btn_add = (Button) addProductFromCategoryDialog.findViewById(R.id.btn_add);
        List<Category>filter_categoryList = new ArrayList<>();

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addProductFromCategoryDialog.dismiss();
            }
        });
        CategoryDBAdapter  categoryDBAdapter = new CategoryDBAdapter(this);
        categoryDBAdapter.open();

        filter_categoryList = categoryDBAdapter.getAllDepartments();
        listCategory = filter_categoryList;
        adapter = new CategoryGridViewAdapter(this, listCategory);
        gvCategory.setAdapter(adapter);

    }
}
