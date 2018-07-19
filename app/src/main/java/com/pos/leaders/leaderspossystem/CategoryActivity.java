package com.pos.leaders.leaderspossystem;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;

import com.pos.leaders.leaderspossystem.DataBaseAdapter.CategoryDBAdapter;
import com.pos.leaders.leaderspossystem.Models.Category;
import com.pos.leaders.leaderspossystem.Tools.CategoryGridViewAdapter;
import com.pos.leaders.leaderspossystem.Tools.TitleBar;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Karam on 18/10/2016.
 */

public class CategoryActivity extends AppCompatActivity {

    CategoryDBAdapter categoryDBAdapter;
    EditText etCategoryName;
    Button btAddCategory, btnCancel;
    List<Category> listCategory, filter_categoryList;
    GridView gvCategory;

    CategoryGridViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        // Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.category_mangment);

        TitleBar.setTitleBar(this);

        // Get Refferences of Views
        gvCategory = (GridView) findViewById(R.id.workerManagement_GVDEpartment);
        etCategoryName = (EditText) findViewById(R.id.ETdepartmentName);
        btAddCategory = (Button) findViewById(R.id.BTAddDepartment);
        btnCancel = (Button) findViewById(R.id.departmentActivity_btnCancel);
        makeList();

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        gvCategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                final String[] items = {
                        getString(R.string.edit),
                        getString(R.string.delete)};
                AlertDialog.Builder builder = new AlertDialog.Builder(CategoryActivity.this);
                builder.setTitle(getBaseContext().getString(R.string.make_your_selection));
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        Intent intent;
                        switch (item) {
                            case 0:
                                intent = new Intent(CategoryActivity.this, AddNewCategory.class);
                                intent.putExtra("categoryID", listCategory.get(position).getCategoryId());
                                startActivity(intent);
                                break;
                            case 1:
                                new AlertDialog.Builder(CategoryActivity.this)
                                        .setMessage(getString(R.string.delete))
                                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                categoryDBAdapter.deleteEntry(listCategory.get(position).getCategoryId());
                                                listCategory.remove(listCategory.get(position));
                                                gvCategory.setAdapter(adapter);
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

        btAddCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CategoryActivity.this, AddNewCategory.class);
                startActivity(intent);
            }
        });
        etCategoryName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                etCategoryName.setFocusable(true);
            }
        });
        etCategoryName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                gvCategory.setTextFilterEnabled(true);

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filter_categoryList = new ArrayList<Category>();
                String word = etCategoryName.getText().toString();

                if (!word.equals("")) {
                    for (Category d : listCategory) {

                        if (d.getName().toLowerCase().contains(word.toLowerCase())) {
                            filter_categoryList.add(d);

                        }
                    }
                } else {
                    filter_categoryList = listCategory;
                }
                adapter = new CategoryGridViewAdapter(getApplicationContext(), filter_categoryList);

                gvCategory.setAdapter(adapter);
            }
        });
    }

    private void makeList() {
        // Category Data adapter
        categoryDBAdapter = new CategoryDBAdapter(this);
        categoryDBAdapter.open();

        filter_categoryList = categoryDBAdapter.getAllDepartments();
        listCategory = filter_categoryList;
        adapter = new CategoryGridViewAdapter(this, listCategory);
        gvCategory.setAdapter(adapter);
    }
}