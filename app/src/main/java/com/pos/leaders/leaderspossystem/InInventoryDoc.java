package com.pos.leaders.leaderspossystem;

import android.app.Dialog;
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
import com.pos.leaders.leaderspossystem.Models.BoInventory;
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
import com.pos.leaders.leaderspossystem.syncposservice.Enums.ApiURL;
import com.pos.leaders.leaderspossystem.syncposservice.Enums.MessageKey;
import com.pos.leaders.leaderspossystem.syncposservice.Enums.MessageType;
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

import static com.pos.leaders.leaderspossystem.syncposservice.Util.BrokerHelper.sendToBroker;

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
    List<ProductInventory>finalListViewProduct;
    InventoryProductDetailsListViewAdapter adapter ;
    View selectedIteminCartList;
    TextView  orderCount, orderTotalPrice , orderPrice;
    ProductInventory selectedOrderOnCart = null;
    InventoryProductDetailsListViewAdapter adapter1;
    Inventory inventory;
    JSONObject invoiceJsonObject=new JSONObject();
    String invoiceNum;
    boolean caseInventory=false;
    HashMap<String,Integer>productHashMap=new HashMap<String, Integer>();
    Context context;
    final String SAMPLE_FILE = "inventoryReport.pdf";
    ProductInventoryDbAdapter productInventoryDbAdapter;
    ProductDBAdapter productDBAdapter;
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
        filter_productList=new ArrayList<>();
        finalListViewProduct=new ArrayList<>();
        productHashMap=new HashMap<>();
        productInventoryDbAdapter=new ProductInventoryDbAdapter(InInventoryDoc.this);
        productInventoryDbAdapter.open();
        productDBAdapter=new ProductDBAdapter(InInventoryDoc.this);
        productDBAdapter.open();
        filter_productList = productInventoryDbAdapter.getAllProducts();
        ProductInventoryCatalogGridViewAdapter adapter = new ProductInventoryCatalogGridViewAdapter(this, filter_productList);
        gvProduct.setAdapter(adapter);
        context=this;
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if(invoiceJsonObject.getString("status").equals("200")) {
                         try {
                                    try {
                                        Thread.sleep(3000);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                            }
                            File path = new File(Environment.getExternalStorageDirectory(), context.getPackageName());
                            File file = new File(path, SAMPLE_FILE);
                            RandomAccessFile f = new RandomAccessFile(file, "r");
                            byte[] data = new byte[(int) f.length()];
                            f.readFully(data);
                            DocumentControl.pdfLoadImagesOpiningReport(data, context);
                             try {
                                 Thread.sleep(3000);
                             } catch (InterruptedException e) {
                                 e.printStackTrace();
                             }
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

        });
        gvProduct.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                    addToCart(filter_productList.get(position));
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
                InventoryDbAdapter inventoryDbAdapter =new InventoryDbAdapter(InInventoryDoc.this);
                inventoryDbAdapter.open();
                Inventory in=null;
                try {
                    in = inventoryDbAdapter.getLastRow();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                for(int i= 0 ;i<finalListViewProduct.size();i++){
                    productHashMap.put(String.valueOf(finalListViewProduct.get(i).getProductId()),finalListViewProduct.get(i).getQty());
                }
                 inventory = new Inventory(in.getName(),in.getInventoryId(),in.getProductsIdWithQuantityList(),in.getBranchId(),in.getHide());
                caseInventory=false;
                inventoryIn.setBackgroundResource(R.drawable.bt_normal);
                inventoryOut.setBackgroundResource(R.drawable.bt_dark_pressed);
              //  if(provider!=null){
                    final Inventory finalIn = in;
                    new AsyncTask<Void, Void, Void>(){
                        @Override
                        protected void onPreExecute() {
                            super.onPreExecute();
                        }
                        @Override
                        protected void onPostExecute(Void aVoid) {
                            try {
                                if(invoiceJsonObject.getString("status").equals("200")) {
                                    for(int i= 0 ;i<finalListViewProduct.size();i++){
                                        ProductInventory productInventory = productInventoryDbAdapter.getProductInventoryByID(finalListViewProduct.get(i).getProductId());
                                        productInventoryDbAdapter.updateEntry(finalListViewProduct.get(i).getProductId(),productInventory.getQty());
                                        Product p=productDBAdapter.getProductByID(finalListViewProduct.get(i).getProductId());
                                        p.setLastCostPriceInventory(finalListViewProduct.get(i).getPrice());
                                        productDBAdapter.updateProductPrice(p);
                                    }
                                    BoInventory inventory = new BoInventory(finalIn.getName(), finalIn.getInventoryId(),productHashMap, finalIn.getBranchId(), finalIn.getHide());
                                    sendToBroker(MessageType.UPDATE_INVENTORY, inventory, InInventoryDoc.this);
                                    PdfUA pdfUA = new PdfUA();
                                    JSONObject r = invoiceJsonObject.getJSONObject(MessageKey.responseBody);

                                    try {
                                            pdfUA.printInventoryReport(context, r.toString(), "inInventory");
                                    } catch (DocumentException e) {
                                        e.printStackTrace();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                                else {

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

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
                                if(caseInventory) {
                                    invoice = new BoInvoice(DocumentType.OUT_INVENTORY,docJson,"");
                                }else {
                                    invoice = new BoInvoice(DocumentType.IN_INVENTORY,docJson,"");

                                }
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

                                try {
                                    Thread.sleep(100);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }

                            } catch (IOException e) {
                                e.printStackTrace();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            return null;
                        }
                    }.execute();}
              /*  else {
                    Toast.makeText(InInventoryDoc.this, "Please determine your provider .", Toast.LENGTH_LONG).show();
                }*/


        });
        inventoryOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(int i= 0 ;i<finalListViewProduct.size();i++){
                    productHashMap.put(String.valueOf(finalListViewProduct.get(i).getProductId()),finalListViewProduct.get(i).getQty());
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
                            try {
                                if(invoiceJsonObject.getString("status").equals("200")) {
                                    for(int i= 0 ;i<finalListViewProduct.size();i++){
                                        ProductInventory productInventory = productInventoryDbAdapter.getProductInventoryByID(finalListViewProduct.get(i).getProductId());
                                        productInventoryDbAdapter.updateEntry(finalListViewProduct.get(i).getProductId(),finalListViewProduct.get(i).getQty());
                                        BoInventory inventory = new BoInventory(finalIn.getName(), finalIn.getInventoryId(),productHashMap, finalIn.getBranchId(), finalIn.getHide());
                                        sendToBroker(MessageType.UPDATE_INVENTORY, inventory, InInventoryDoc.this);
                                    }
                                    PdfUA pdfUA = new PdfUA();
                                    JSONObject r = invoiceJsonObject.getJSONObject(MessageKey.responseBody);

                                    try {
                                            pdfUA.printInventoryReport(context,r.toString(),"outInventory");

                                    } catch (DocumentException e) {
                                        e.printStackTrace();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                                else {

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

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
                                if(caseInventory) {
                                    invoice = new BoInvoice(DocumentType.OUT_INVENTORY,docJson,"");
                                }else {
                                    invoice = new BoInvoice(DocumentType.IN_INVENTORY,docJson,"");

                                }
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

                                try {
                                    Thread.sleep(100);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }

                            } catch (IOException e) {
                                e.printStackTrace();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            return null;
                        }
                    }.execute();
                /*}
                else {
                    Toast.makeText(InInventoryDoc.this, "Please determine your provider.", Toast.LENGTH_LONG).show();
                }*/


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
                orderTotalPrice = (TextView) view.findViewById(R.id.rowInventoryDetails_TVTotalPrice);
                orderPrice = (TextView) view.findViewById(R.id.rowInventoryDetails_TVPrice);
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
                        orderCount.setText(finalListViewProduct.get(position).getQty() + "");
                        orderTotalPrice.setText(finalListViewProduct.get(position).getPrice() * finalListViewProduct.get(position).getQty() + getString(R.string.ins));
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
                                final EditText inventoryEtPrice = (EditText) cashDialog.findViewById(R.id.inventoryDocDialog_EtPrice);

                                cashETCash.setHint(R.string.count);
                                inventoryEtPrice.setHint(R.string.price);

                                cashETCash.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                                    @Override
                                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                                        if (actionId == EditorInfo.IME_ACTION_DONE) {
                                            cashBTOk.callOnClick();
                                        }
                                        return false;
                                    }
                                });
                                inventoryEtPrice.setOnEditorActionListener(new TextView.OnEditorActionListener() {
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
                                        String priceStr = inventoryEtPrice.getText().toString();
                                        int pid = 1;
                                        double price=0;
                                        if (!(_count.equals(""))){
                                            pid = Integer.parseInt(cashETCash.getText().toString());
                                        int indexOfItem = finalListViewProduct.indexOf(selectedOrderOnCart);
                                            finalListViewProduct.get(indexOfItem).setQty(pid);
                                            orderCount.setText(finalListViewProduct.get(position).getQty() + "");
                                            orderTotalPrice.setText(selectedOrderOnCart.getPrice() * selectedOrderOnCart.getQty() + getString(R.string.ins));
                                        }
                                        if (!(priceStr.equals(""))){
                                            price = Double.parseDouble(inventoryEtPrice.getText().toString());
                                            int indexOfItem = finalListViewProduct.indexOf(selectedOrderOnCart);
                                            finalListViewProduct.get(indexOfItem).setPrice(price);
                                            orderTotalPrice.setText(selectedOrderOnCart.getPrice() * selectedOrderOnCart.getQty() + getString(R.string.ins));
                                            orderPrice.setText(selectedOrderOnCart.getPrice()+getString(R.string.ins));
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
         finalListViewProduct.get(index).setQty( finalListViewProduct.get(index).getQty()+1);
        adapter1= new InventoryProductDetailsListViewAdapter(getApplicationContext(),R.layout.list_adapter_row_nventory_product_details,finalListViewProduct);
        finalProductListView.setAdapter(adapter1);    }

    private void decreaseItemOnCart(int index) {
        finalListViewProduct.get(index).setQty(finalListViewProduct.get(index).getQty()-1);
        adapter1= new InventoryProductDetailsListViewAdapter(getApplicationContext(),R.layout.list_adapter_row_nventory_product_details,finalListViewProduct);
        finalProductListView.setAdapter(adapter1);
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
                    finalListViewProduct.get(i).setQty(finalListViewProduct.get(i).getQty()+1);
                }
            }
        }else {
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

}
