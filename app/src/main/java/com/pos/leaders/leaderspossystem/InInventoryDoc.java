package com.pos.leaders.leaderspossystem;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itextpdf.text.DocumentException;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.InventoryDbAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.ProductDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.ProductInventoryDbAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.ProviderDbAdapter;
import com.pos.leaders.leaderspossystem.Models.BoInvoice;
import com.pos.leaders.leaderspossystem.Models.InInventory;
import com.pos.leaders.leaderspossystem.Models.Inventory;
import com.pos.leaders.leaderspossystem.Models.Product;
import com.pos.leaders.leaderspossystem.Models.ProductInventory;
import com.pos.leaders.leaderspossystem.Models.Provider;
import com.pos.leaders.leaderspossystem.Printer.InvoiceImg;
import com.pos.leaders.leaderspossystem.Tools.DocumentControl;
import com.pos.leaders.leaderspossystem.Tools.InventoryProductDetailsListViewAdapter;
import com.pos.leaders.leaderspossystem.Tools.ProductInventoryCatalogGridViewAdapter;
import com.pos.leaders.leaderspossystem.Tools.ProviderCatalogGridViewAdapter;
import com.pos.leaders.leaderspossystem.Tools.SESSION;
import com.pos.leaders.leaderspossystem.Tools.SETTINGS;
import com.pos.leaders.leaderspossystem.Tools.TitleBar;
import com.pos.leaders.leaderspossystem.Tools.Util;
import com.pos.leaders.leaderspossystem.syncposservice.Enums.ApiURL;
import com.pos.leaders.leaderspossystem.syncposservice.Enums.MessageKey;
import com.pos.leaders.leaderspossystem.syncposservice.MessageTransmit;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InInventoryDoc extends AppCompatActivity {
    TextView providerName;
    ImageView chooseProvider;
    Button inventoryIn , inventoryOut , btnSave;
    boolean userScrolled =false;
    List<Provider> providerList = new ArrayList<>();
    List<Provider> AllProviderList = new ArrayList<>();
    public static Provider provider;
    Product product =new Product();
    Spinner SpProductBranch;
    ListView finalProductListView;
    GridView gvProduct;
    List<ProductInventory>filter_productList=new ArrayList<>();
    List<ProductInventory>searchFilter_productList=new ArrayList<>();
    List<ProductInventory>finalListViewProduct;
    List<Product> productList = new ArrayList();
    int productLoadItemOffset=0;
    int productCountLoad=80;
    InventoryProductDetailsListViewAdapter adapter ;
    View selectedIteminCartList;
    TextView  orderCount;
    ProductInventory selectedOrderOnCart = null;
    InventoryProductDetailsListViewAdapter adapter1;
    Inventory inventory;
    JSONObject invoiceJsonObject=new JSONObject();
    String invoiceNum;
    boolean caseInventory=false;
    String inventoryType="outInventory";
    HashMap<String,Integer>productHashMap=new HashMap<String, Integer>();
    Context context;
    final String SAMPLE_FILE = "inventoryReport.pdf";
    ProductInventoryDbAdapter productInventoryDbAdapter;
    ProductDBAdapter productDBAdapter;
    EditText ETSearch;
    ProductInventoryCatalogGridViewAdapter finalAdapter;
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
        btnSave=(Button)findViewById(R.id.printInventory);
        gvProduct =(GridView)findViewById(R.id.inOutInventoryGvProducts);
        finalProductListView=(ListView)findViewById(R.id.inOutInventoryFinalProductListView);
        ETSearch=(EditText)findViewById(R.id.InInventory_ETSearch);
        filter_productList=new ArrayList<>();
        finalListViewProduct=new ArrayList<>();
        productHashMap=new HashMap<>();
        productInventoryDbAdapter=new ProductInventoryDbAdapter(InInventoryDoc.this);
        productInventoryDbAdapter.open();
        productDBAdapter=new ProductDBAdapter(InInventoryDoc.this);
        productDBAdapter.open();
        searchFilter_productList = productInventoryDbAdapter.getTopProducts(productLoadItemOffset, productCountLoad);
        filter_productList=searchFilter_productList;
        finalAdapter = new ProductInventoryCatalogGridViewAdapter(this, searchFilter_productList);
        gvProduct.setAdapter(finalAdapter);
        context=this;
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(caseInventory){
                    inventoryType="inInventory";
                    InventoryDbAdapter inventoryDbAdapter =new InventoryDbAdapter(InInventoryDoc.this);
                    inventoryDbAdapter.open();
                    Inventory in=null;
                    try {
                        in = inventoryDbAdapter.getLastRow();
                        if(in==null){
                            Util.addPosSetting(InInventoryDoc.this);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    for(int i= 0 ;i<finalListViewProduct.size();i++){
                        productHashMap.put(String.valueOf(finalListViewProduct.get(i).getProductId()),finalListViewProduct.get(i).getTempCount());
                    }
                    inventory = new Inventory(in.getName(),in.getInventoryId(),in.getProductsIdWithQuantityList(),in.getBranchId(),in.getHide());
                    //  if(provider!=null){
                    final Inventory finalIn = in;
                    new AsyncTask<Void, Void, Void>(){
                        @Override
                        protected void onPreExecute() {
                            super.onPreExecute();
                        }
                        @Override
                        protected void onPostExecute(Void aVoid) {
                            printInventory();

                        }
                        @Override
                        protected Void doInBackground(Void... voids) {
                            MessageTransmit transmit = new MessageTransmit(SETTINGS.BO_SERVER_URL);
                            JSONObject providerData = new JSONObject();
                            try {
                                ObjectMapper mapper = new ObjectMapper();
                                if(provider==null){
                                    providerData.put("providerId",0);

                                }else {
                                    providerData.put("providerId", provider.getProviderId());

                                }
                                Log.d("productHashMap",productHashMap.toString());
                                InInventory documents = new InInventory("InInventory", new Timestamp(System.currentTimeMillis()), inventory.getInventoryId(), productHashMap, SESSION._EMPLOYEE.getEmployeeId());
                                String doc = mapper.writeValueAsString(documents);
                                Log.d("doc",doc.toString());

                                JSONObject docJson= new JSONObject(doc);
                                String type = docJson.getString("type");
                                docJson.remove("type");
                                docJson.put("@type",type);
                                docJson.put("provider",providerData);
                                Log.d("Document vale", docJson.toString());
                                BoInvoice invoice;
                                    invoice = new BoInvoice(DocumentType.IN_INVENTORY,docJson,"");

                                Log.d("Invoice log",invoice.toString());
                                String res=transmit.authPost(ApiURL.Documents,invoice.toString(), SESSION.token);
                                JSONObject jsonObject = new JSONObject(res);
                                String msgData = jsonObject.getString(MessageKey.responseBody);
                                invoiceJsonObject=jsonObject;
                                JSONObject msgDataJson = new JSONObject(msgData);
                                JSONObject jsonObject1=msgDataJson.getJSONObject("documentsData");
                                invoiceNum = msgDataJson.getString("docNum");
                                Log.d("Invoice log res", res+"");
                                Log.d("Invoice Num", invoiceNum);

                            } catch (IOException e) {
                                e.printStackTrace();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            return null;
                        }
                    }.execute();
                }else {
                    inventoryType = "outInventory";
                    for(int i= 0 ;i<finalListViewProduct.size();i++){
                        productHashMap.put(String.valueOf(finalListViewProduct.get(i).getProductId()),finalListViewProduct.get(i).getTempCount());
                    }
                    InventoryDbAdapter inventoryDbAdapter =new InventoryDbAdapter(InInventoryDoc.this);
                    inventoryDbAdapter.open();
                    Inventory in=null;
                    try {
                        in = inventoryDbAdapter.getLastRow();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    inventory = new Inventory(in.getName(),in.getInventoryId(),in.getProductsIdWithQuantityList(),in.getBranchId(),in.getHide());
                    caseInventory=true;
                    inventoryIn.setBackgroundResource(R.drawable.bt_dark_pressed);
                    inventoryOut.setBackgroundResource(R.drawable.bt_normal);
                    //  if(provider!=null){
                    final InvoiceImg invoiceImg1 = new InvoiceImg(getApplicationContext());
                    final Inventory finalIn = in;
                    new AsyncTask<Void, Void, Void>(){
                        @Override
                        protected void onPreExecute() {
                            super.onPreExecute();
                        }
                        @Override
                        protected void onPostExecute(Void aVoid) {
                        printInventory();

                        }
                        @Override
                        protected Void doInBackground(Void... voids) {
                            MessageTransmit transmit = new MessageTransmit(SETTINGS.BO_SERVER_URL);
                            JSONObject providerData = new JSONObject();
                            try {
                                ObjectMapper mapper = new ObjectMapper();
                                if(provider==null){
                                    providerData.put("providerId",0);

                                }else {
                                    providerData.put("providerId", provider.getProviderId());

                                }
                                Log.d("productHashMap",productHashMap.toString());
                                InInventory documents;
                                documents = new InInventory("OutInventory", new Timestamp(System.currentTimeMillis()), inventory.getInventoryId(), productHashMap, SESSION._EMPLOYEE.getEmployeeId());


                                String doc = mapper.writeValueAsString(documents);
                                Log.d("doc",doc.toString());

                                JSONObject docJson= new JSONObject(doc);
                                String type = docJson.getString("type");
                                docJson.remove("type");
                                docJson.put("@type",type);
                                docJson.put("provider",providerData);
                                Log.d("Document vale", docJson.toString());
                                BoInvoice invoice;
                                    invoice = new BoInvoice(DocumentType.OUT_INVENTORY,docJson,"");

                                Log.d("Invoice log",invoice.toString());
                                String res=transmit.authPost(ApiURL.Documents,invoice.toString(), SESSION.token);
                                JSONObject jsonObject = new JSONObject(res);
                                String msgData = jsonObject.getString(MessageKey.responseBody);
                                invoiceJsonObject=jsonObject;
                                JSONObject msgDataJson = new JSONObject(msgData);
                                JSONObject jsonObject1=msgDataJson.getJSONObject("documentsData");
                                invoiceNum = msgDataJson.getString("docNum");
                                Log.d("Invoice log res", res+"");
                                Log.d("Invoice Num", invoiceNum);
                            } catch (IOException e) {
                                e.printStackTrace();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            return null;
                        }
                    }.execute();
                }



            }

        });
        gvProduct.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                    addToCart(searchFilter_productList.get(position));
            }
        });
        gvProduct.setOnScrollListener(new AbsListView.OnScrollListener() {
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
                    loadMoreProduct();
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
                caseInventory=true;
                inventoryIn.setBackgroundResource(R.drawable.bt_normal);
                inventoryOut.setBackgroundResource(R.drawable.bt_dark_pressed);
}


        });
        inventoryOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inventoryIn.setBackgroundResource(R.drawable.bt_dark_pressed);
                inventoryOut.setBackgroundResource(R.drawable.bt_normal);
                caseInventory=false;
                /*}
                else {
                    Toast.makeText(InInventoryDoc.this, "Please determine your provider.", Toast.LENGTH_LONG).show();
                }*/


            }
        });
        ETSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                String word = ETSearch.getText().toString();
                if (!word.equals("")) {
                    // Database query can be a time consuming task ..
                    // so its safe to call database query in another thread
                    // Handler, will handle this stuff
                    new AsyncTask<String, Void, Void>() {
                        @Override
                        protected void onPreExecute() {
                            searchFilter_productList = new ArrayList<ProductInventory>();
                            productList = new ArrayList();
                            super.onPreExecute();
                        }

                        @Override
                        protected Void doInBackground(final String... params) {



                            runOnUiThread(new Runnable() {

                                @Override
                                public void run() {
                                    productList.addAll(productDBAdapter.getAllProductsByHint(params[0], productList.size()-1, 20));
                                    for(int i=0;i<productList.size();i++){
                                        searchFilter_productList.add(productInventoryDbAdapter.getProductInventoryByID(productList.get(i).getProductId()));
                                    }
                                    finalAdapter.notifyDataSetChanged();

                                    // Stuff that updates the UI
                                }
                            });
                            return null;
                        }

                        @Override
                        protected void onPostExecute(Void aVoid) {
                            super.onPostExecute(aVoid);
                            finalAdapter = new ProductInventoryCatalogGridViewAdapter(getApplicationContext(), searchFilter_productList);
                            gvProduct.setAdapter(finalAdapter);
                        }
                    }.execute(word);
                } else {
                    searchFilter_productList=filter_productList;
                    finalAdapter = new ProductInventoryCatalogGridViewAdapter(getApplicationContext(), searchFilter_productList);
                    gvProduct.setAdapter(finalAdapter);
                }

            }
        });



        finalProductListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, final int position, long id) {

                removeOrderItemSelection();
                view.findViewById(R.id.rowInventoryDetails_LLMethods).setVisibility(View.VISIBLE);
                selectedIteminCartList = view;
                selectedOrderOnCart = finalListViewProduct.get(position);
                view.setBackgroundColor(getResources().getColor(R.color.list_background_color));
                adapter1.setSelected(position);


                Button btnDelete = (Button) view.findViewById(R.id.rowInventoryDetails_MethodsDelete);
                btnDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        removeFromCart(position);
                    }
                });

                orderCount = (TextView) view.findViewById(R.id.rowInventoryDetails_TVCount);
                Button btnPlusOne = (Button) view.findViewById(R.id.rowInventoryDetails_MethodsPlusOne);
                btnPlusOne.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        increaseItemOnCart(position);


                    }
                });
                Button btnMOne = (Button) view.findViewById(R.id.rowInventoryDetails_MethodsMOne);
                btnMOne.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        decreaseItemOnCart(position);
                        orderCount.setText(finalListViewProduct.get(position).getTempCount() + "");
                    }
                });
                Button btnEdit = (Button) view.findViewById(R.id.rowInventoryDetails_MethodsEdit);
                btnEdit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (selectedIteminCartList != null) {
                            if (finalListViewProduct.contains(selectedOrderOnCart)) {
                                final Dialog cashDialog = new Dialog(InInventoryDoc.this);
                                cashDialog.setTitle(R.string.multi_count_price);
                                cashDialog.setContentView(R.layout.inventory_doc_dialog);
                                cashDialog.show();

                                final Button cashBTOk = (Button) cashDialog.findViewById(R.id.inventoryDocDialog_BtOk);
                                final EditText cashETCash = (EditText) cashDialog.findViewById(R.id.inventoryDocDialog_EtCount);

                                cashETCash.setHint(R.string.count);

                                cashETCash.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                                    @Override
                                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                                        if (actionId == EditorInfo.IME_ACTION_DONE) {
                                            cashBTOk.callOnClick();
                                        }
                                        return false;
                                    }
                                });

                                cashBTOk.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        String _count = cashETCash.getText().toString();
                                        int pid = 1;
                                        double price=0;
                                        if (!(_count.equals(""))){
                                            pid = Integer.parseInt(cashETCash.getText().toString());
                                        int indexOfItem = finalListViewProduct.indexOf(selectedOrderOnCart);
                                            finalListViewProduct.get(indexOfItem).setTempCount(pid);
                                            orderCount.setText(finalListViewProduct.get(position).getTempCount() + "");
                                        }
                                        cashDialog.cancel();
                                    }
                                });
                                Button cashBTCancel = (Button) cashDialog.findViewById(R.id.inventoryDocDialog_BTCancel);
                                cashBTCancel.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        cashDialog.cancel();
                                    }
                                });

                            } else {
                                Toast.makeText(InInventoryDoc.this, getBaseContext().getString(R.string.please_select_item), Toast.LENGTH_SHORT);
                            }
                        } else {
                            Toast.makeText(InInventoryDoc.this, getBaseContext().getString(R.string.please_add_item), Toast.LENGTH_SHORT);
                        }
                    }
                });


            }
        });
    }

    private void increaseItemOnCart(int index) {
         finalListViewProduct.get(index).setTempCount( finalListViewProduct.get(index).getTempCount()+1);
        orderCount.setText(finalListViewProduct.get(index).getTempCount() + "");
    }

    private void decreaseItemOnCart(int index) {
        finalListViewProduct.get(index).setTempCount(finalListViewProduct.get(index).getTempCount()-1);
        orderCount.setText(finalListViewProduct.get(index).getTempCount() + "");

    }
    private void removeFromCart(int index) {
        finalListViewProduct.remove(index);
        adapter1= new InventoryProductDetailsListViewAdapter(getApplicationContext(),R.layout.list_adapter_row_nventory_product_details,finalListViewProduct);
        finalProductListView.setAdapter(adapter1);


    }
    private void addToCart(ProductInventory product) {
        boolean productStatus=false;
        for(int i=0 ; i<finalListViewProduct.size();i++){
            if(finalListViewProduct.get(i).getProductId()==product.getProductId()){
                productStatus=true;
            }
        }
        if(productStatus){
            for(int i=0 ; i<finalListViewProduct.size();i++){
                if(finalListViewProduct.get(i).getProductId()==product.getProductId()){
                    finalListViewProduct.get(i).setTempCount(finalListViewProduct.get(i).getTempCount()+1);
                }
            }
        }else {
            product.setTempCount(1);
        finalListViewProduct.add(product);
        }
        adapter1= new InventoryProductDetailsListViewAdapter(getApplicationContext(),R.layout.list_adapter_row_nventory_product_details,finalListViewProduct);
        finalProductListView.setAdapter(adapter1);
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
    private void removeOrderItemSelection() {
        adapter1.setSelected(-1);
        if (selectedIteminCartList != null) {
            selectedIteminCartList.findViewById(R.id.rowInventoryDetails_LLMethods).setVisibility(View.GONE);
            selectedIteminCartList.setBackgroundColor(getResources().getColor(R.color.white));
            selectedOrderOnCart = null;
        }
    }
    private Bitmap combineImageIntoOne(ArrayList<Bitmap> bitmap) {
        int w = 0, h = 0;
        for (int i = 0; i < bitmap.size(); i++) {
            if (i < bitmap.size() - 1) {
                w = bitmap.get(i).getWidth() > bitmap.get(i + 1).getWidth() ? bitmap.get(i).getWidth() : bitmap.get(i + 1).getWidth();
            }
            h += bitmap.get(i).getHeight();
        }

        Bitmap temp = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(temp);
        int top = 0;
        for (int i = 0; i < bitmap.size(); i++) {
            Log.d("HTML", "Combine: "+i+"/"+bitmap.size()+1);

            top = (i == 0 ? 0 : top+bitmap.get(i).getHeight());
            canvas.drawBitmap(bitmap.get(i), 0f, top, null);
        }
        return temp;
    }
    public  void  printInventory(){
        new AsyncTask<Void, Void, Void>(){
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }
            @Override
            protected void onPostExecute(Void aVoid) {
                try {
                    if(invoiceJsonObject.getString("status").equals("200")) {
                        try {
                            File path = new File(Environment.getExternalStorageDirectory(), context.getPackageName());
                            File file = new File(path, SAMPLE_FILE);
                            RandomAccessFile f = new RandomAccessFile(file, "r");
                            byte[] data = new byte[(int) f.length()];
                            f.readFully(data);
                            DocumentControl.pdfLoadImagesInventoryReport(data, context);
                            finalListViewProduct=new ArrayList<ProductInventory>();
                            adapter1= new InventoryProductDetailsListViewAdapter(getApplicationContext(),R.layout.list_adapter_row_nventory_product_details,finalListViewProduct);
                            finalProductListView.setAdapter(adapter1);
                            inventoryOut.setBackgroundResource(R.drawable.bt_dark_pressed);
                            inventoryIn.setBackgroundResource(R.drawable.bt_dark_pressed);
                            provider = null;
                            providerName.setText("");

                        } catch (Exception ignored) {

                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override
            protected Void doInBackground(Void... voids) {
                PdfUA pdfUA = new PdfUA();
                JSONObject r = null;
                try {
                    r = invoiceJsonObject.getJSONObject(MessageKey.responseBody);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                try {
                    pdfUA.printInventoryReport(context,r.toString(),"outInventory");

                } catch (DocumentException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return null;
            }
        }.execute();
    }
    private void loadMoreProduct() {

        productLoadItemOffset += productCountLoad;
        final String searchWord = ETSearch.getText().toString();
        final ProgressDialog dialog = new ProgressDialog(InInventoryDoc.this);
        dialog.setTitle(getBaseContext().getString(R.string.wait_for_finish));

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected void onPreExecute() {
//                dialog.show();
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                filter_productList = searchFilter_productList;
                if(finalAdapter==null){
                    finalAdapter = new ProductInventoryCatalogGridViewAdapter(getApplicationContext(), filter_productList);
                    gvProduct.setAdapter(finalAdapter);
                }
                dialog.cancel();
                userScrolled=false;
            }

            @Override
            protected Void doInBackground(Void... params) {
                if (!searchWord.equals("")) {
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            // Stuff that updates the UI
                            finalAdapter.notifyDataSetChanged();

                        }
                    });
                }
                 else {
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            searchFilter_productList.addAll(productInventoryDbAdapter.getTopProducts(searchFilter_productList.size()-1, 80));
                            // Stuff that updates the UI
                            finalAdapter.notifyDataSetChanged();

                        }
                    });
                }
                return null;

            }
        }.execute();
    }

}
