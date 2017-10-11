package com.pos.leaders.leaderspossystem;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.SyncStateContract;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.pos.leaders.leaderspossystem.DataBaseAdapter.DepartmentDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.ProductDBAdapter;
import com.pos.leaders.leaderspossystem.Models.Department;
import com.pos.leaders.leaderspossystem.Models.Product;
import com.pos.leaders.leaderspossystem.Tools.CONSTANT;
import com.pos.leaders.leaderspossystem.Tools.SESSION;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import static com.pos.leaders.leaderspossystem.ProductsActivity.lvDepatment;

/**
 * Created by KARAM on 19/12/2016.
 */

public class ImportProductsActivity extends Activity {
    Button btnBrowse,btnSave;
    TextView tvFileName,tvStatus;
    ListView lvDepartment;
    ArrayAdapter<String> LAdapter;
    View previouslySelectedItem = null;
    String selectedDepartment="";

    Map<String,Long> departmentMap=new HashMap<String,Long>();

    List<Product> lsProducts=new ArrayList<Product>();
    ArrayList<String> departmentsName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.dialog_import_products);
        btnBrowse = (Button) findViewById(R.id.dialogImportProducts_btBrowse);
        btnSave = (Button) findViewById(R.id.dialogImportProducts_btSave);
        btnSave.setVisibility(View.GONE);
        tvFileName = (TextView) findViewById(R.id.dialogImportProducts_tvFileName);
        tvStatus = (TextView) findViewById(R.id.dialogImportProducts_tvStatus);
        lvDepartment = (ListView) findViewById(R.id.dialogImportProducts_lvDepartments);



        btnBrowse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent fileintent = new Intent(Intent.ACTION_GET_CONTENT);
                fileintent.setType("file/*");
                try {
                    startActivityForResult(fileintent, CONSTANT.COM_POS_LEADERS_LEADPOS_CHOSEFILE);
                } catch (ActivityNotFoundException e) {
                    Log.e("tag", "No activity can handle picking a file. Showing alternatives.");
                }
            }
        });


        btnSave.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(selectedDepartment.equals("")){
                    Toast.makeText(ImportProductsActivity.this,getBaseContext().getString(R.string.department_not_selected),Toast.LENGTH_SHORT);
                }
                else {
                    final ProgressDialog dialog = new ProgressDialog(ImportProductsActivity.this);
                    new AsyncTask<Void, Void, Void>() {
                        @Override
                        protected Void doInBackground(Void... params) {
                            ProductDBAdapter productDBAdapter = new ProductDBAdapter(getBaseContext());
                            productDBAdapter.open();
                            int count = 0;
                            for (Product p : lsProducts) {
                                p.setDepartmentId(departmentMap.get(selectedDepartment));
                                count++;
                                productDBAdapter.insertEntry(p);
                            }
                            if (lsProducts.size() == count) {//success to add all the products
                            } else {
                                x = lsProducts.size() - count;
                            }
                            return null;
                        }

                        @Override
                        protected void onPostExecute(Void aVoid) {
                            dialog.cancel();
                            if (x == 0) {
                                tvStatus.setText("Success to add all the items");
                            } else {
                                tvStatus.setText(String.format("Added %d,Error With %d Items", lsProducts.size() - x, x));
                            }
                        }

                        @Override
                        protected void onPreExecute() {
                            dialog.setTitle(getBaseContext().getString(R.string.saving_data));
                            dialog.show();
                        }
                    }.execute();
                }
            }
        });
        DepartmentDBAdapter db=new DepartmentDBAdapter(getBaseContext());
        db.open();

        departmentsName = new ArrayList<String>();
        List<Department> listDepartment = db.getAllDepartments();
        //departmentDBAdapter.close();

        for (Department d : listDepartment) {
            departmentsName.add(d.getName());
            departmentMap.put(d.getName(),d.getId());
        }
        LAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,departmentsName);
        lvDepartment.setAdapter(LAdapter);

        lvDepartment.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
        lvDepartment.setItemChecked(1,true);

        lvDepartment.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long arg3) {
                view.setSelected(true);
                selectItemDepartments(view);

                selectedDepartment=lvDepartment.getItemAtPosition(position).toString();


            }
        });
        lvDepartment.setVisibility(View.GONE);


    }
    static int x=0;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Fix no activity available
        if (data == null)
            return;
        switch (requestCode) {
            case CONSTANT.COM_POS_LEADERS_LEADPOS_CHOSEFILE:
                if (resultCode == RESULT_OK) {
                    final ProgressDialog dialog = new ProgressDialog(ImportProductsActivity.this);
                    dialog.setTitle(getBaseContext().getString(R.string.import_wait));
                    final String FilePath = data.getData().getPath();
                    new AsyncTask<Void,Void,Void>(){
                        @Override
                        protected void onPreExecute() {
                            dialog.show();
                        }

                        @Override
                        protected void onPostExecute(Void aVoid) {
                            tvStatus.setText(msg);
                            if(lsProducts.size()!=0) {
                                btnSave.setVisibility(View.VISIBLE);
                                lvDepartment.setVisibility(View.VISIBLE);
                            }


                            dialog.cancel();
                        }

                        @Override
                        protected Void doInBackground(Void... params) {
                            try {
                                lsProducts=readProducts(FilePath);
                                Log.i("",lsProducts.toString());

                            }
                            catch (IOException ioex){
                                Log.e("Read File",ioex.getMessage());
                            }
                            return null;
                        }
                    }.execute();
                }
        }
    }


    static int failImportItems=0;
    static String msg="";
    public List<Product> readProducts(String inputFile) throws IOException {
        List<Product> resultSet = new ArrayList<Product>();
        failImportItems=0;
        File inputWorkbook = new File(inputFile);
        if(inputWorkbook.exists()){
            Workbook w;
            try {
                w = Workbook.getWorkbook(inputWorkbook);
                // Get the first sheet
                Sheet sheet = w.getSheet(0);
                // Loop over column and lines
                Log.i("Row ","id \t name \t barcode \t price");
                for (int row = 1; row < sheet.getRows(); row++) {
                    try {
                        String name,barcode,id,price;
                        id=sheet.getCell(0, row).getContents().replaceAll(" ","");
                        name=sheet.getCell(1, row).getContents();
                        barcode=new BigDecimal(sheet.getCell(2, row).getContents().replaceAll(" ","")).toString();
                        price=new BigDecimal(sheet.getCell(4, row).getContents().replaceAll(" ","")).toString();
                        resultSet.add(new Product(Integer.parseInt(id),name,Double.parseDouble(price),barcode,1, SESSION._USER.getId()));
                    }
                    catch (Exception ex){
                        Log.e("",ex.getMessage());
                        failImportItems++;
                    }


                    /*for(int i=0; i<sheet.getColumns();i++){
                        Cell cell = sheet.getCell(i, row);
                    }

                    if(cell.getContents().equalsIgnoreCase(key)){
                        for (int i = 0; i < sheet.getColumns(); i++) {
                            Cell cel = sheet.getCell(i, row);
                            resultSet.add(cel.getContents());
                        }
                    }*/
                    continue;
                }
                msg = String.format("file have %d products, %d successfully to read, %d errors",sheet.getRows()-1,sheet.getRows()-1-failImportItems,failImportItems);
                //Toast.makeText(getBaseContext(),"fail to add "+failImportItems,Toast.LENGTH_LONG);

            } catch (BiffException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else
        {
            // resultSet.add("File not found..!");
        }
        if(resultSet.size()==0){
            // resultSet.add("Data not found..!");
        }
        return resultSet;
    }

    private void selectItemDepartments(View v){
        if(previouslySelectedItem!=null)
            previouslySelectedItem.setBackgroundColor(getResources().getColor(R.color.transparent));
        previouslySelectedItem=v;
        v.setBackgroundColor(getResources().getColor(R.color.pressed_color));


    }


}
