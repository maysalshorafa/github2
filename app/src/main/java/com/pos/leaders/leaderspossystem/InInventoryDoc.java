package com.pos.leaders.leaderspossystem;

import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
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
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itextpdf.text.DocumentException;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.InventoryDbAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.ProductDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.ProviderDbAdapter;
import com.pos.leaders.leaderspossystem.Models.BoInvoice;
import com.pos.leaders.leaderspossystem.Models.InInventory;
import com.pos.leaders.leaderspossystem.Models.Inventory;
import com.pos.leaders.leaderspossystem.Models.Product;
import com.pos.leaders.leaderspossystem.Models.Provider;
import com.pos.leaders.leaderspossystem.Printer.InvoiceImg;
import com.pos.leaders.leaderspossystem.Tools.InventoryProductDetailsListViewAdapter;
import com.pos.leaders.leaderspossystem.Tools.ProductCatalogGridViewAdapter;
import com.pos.leaders.leaderspossystem.Tools.ProviderCatalogGridViewAdapter;
import com.pos.leaders.leaderspossystem.Tools.SESSION;
import com.pos.leaders.leaderspossystem.Tools.SETTINGS;
import com.pos.leaders.leaderspossystem.Tools.TitleBar;
import com.pos.leaders.leaderspossystem.syncposservice.Enums.ApiURL;
import com.pos.leaders.leaderspossystem.syncposservice.Enums.MessageKey;
import com.pos.leaders.leaderspossystem.syncposservice.MessageTransmit;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
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
    List<Product>filter_productList=new ArrayList<>();
    List<Product>finalListViewProduct;
    InventoryProductDetailsListViewAdapter adapter ;
    View selectedIteminCartList;
    TextView  orderCount, orderTotalPrice;
    Product selectedOrderOnCart = null;
    InventoryProductDetailsListViewAdapter adapter1;
    Inventory inventory;
    JSONObject invoiceJsonObject;
    String invoiceNum;
    boolean caseInventory=false;
    HashMap<String,Integer>productHashMap=new HashMap<String, Integer>();
    Context context;

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
        btnSave=(Button)findViewById(R.id.saveInventory);
        gvProduct =(GridView)findViewById(R.id.inOutInventoryGvProducts);
        finalProductListView=(ListView)findViewById(R.id.inOutInventoryFinalProductListView);
        filter_productList=new ArrayList<>();
        finalListViewProduct=new ArrayList<>();
        final ProductDBAdapter productDBAdapter = new ProductDBAdapter(this);
        productDBAdapter.open();
        filter_productList = productDBAdapter.getAllProducts();
        ProductCatalogGridViewAdapter adapter = new ProductCatalogGridViewAdapter(this, filter_productList);
        gvProduct.setAdapter(adapter);
        context=this;
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(inventory!=null&&provider!=null){
                final InvoiceImg invoiceImg1 = new InvoiceImg(getApplicationContext());
                new AsyncTask<Void, Void, Void>(){
                    @Override
                    protected void onPreExecute() {
                        super.onPreExecute();
                    }
                    @Override
                    protected void onPostExecute(Void aVoid) {
                        try {
                            if(invoiceJsonObject.getString("status").equals("200")) {
                                PdfUA pdfUA = new PdfUA();
                                JSONObject r = invoiceJsonObject.getJSONObject(MessageKey.responseBody);

                                try {
                                    if(caseInventory){
                                        pdfUA.printInventoryReport(context,r.toString(),"outInventory");
                                    }else {
                                        pdfUA.printInventoryReport(context, r.toString(), "inInventory");
                                    }
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
                            providerData.put("providerId", provider.getProviderId());
                            Log.d("productHashMap",productHashMap.toString());
                            InInventory documents;
                            if(!caseInventory) {
                                documents = new InInventory("InInventory", new Timestamp(System.currentTimeMillis()), inventory.getInventoryId(), productHashMap, SESSION._EMPLOYEE.getEmployeeId());

                            }
                            else {
                                documents = new InInventory("OutInventory", new Timestamp(System.currentTimeMillis()), inventory.getInventoryId(), productHashMap, SESSION._EMPLOYEE.getEmployeeId());

                            }
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
                else {
                    Toast.makeText(InInventoryDoc.this, "Please determine your provider and if process in or out .", Toast.LENGTH_LONG).show();
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
                for(int i= 0 ;i<finalListViewProduct.size();i++){
                    productHashMap.put(String.valueOf(finalListViewProduct.get(i).getProductId()),finalListViewProduct.get(i).getStockQuantity());
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
                caseInventory=false;
                inventoryIn.setBackgroundResource(R.drawable.bt_normal);
                inventoryOut.setBackgroundResource(R.drawable.bt_dark_pressed);

            }
        });
        inventoryOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(int i= 0 ;i<finalListViewProduct.size();i++){
                    productHashMap.put(String.valueOf(finalListViewProduct.get(i).getProductId()),finalListViewProduct.get(i).getStockQuantity());
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
                        orderCount.setText(finalListViewProduct.get(position).getStockQuantity() + "");
                        orderTotalPrice.setText(finalListViewProduct.get(position).getCostPrice() * finalListViewProduct.get(position).getStockQuantity() + getString(R.string.ins));
                    }
                });
                Button btnEdit = (Button) view.findViewById(R.id.rowInventoryDetails_MethodsEdit);
                btnEdit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (selectedIteminCartList != null) {
                            if (finalListViewProduct.contains(selectedOrderOnCart)) {
                                final Dialog cashDialog = new Dialog(InInventoryDoc.this);
                                cashDialog.setTitle(R.string.multi_count);
                                cashDialog.setContentView(R.layout.cash_payment_dialog);
                                cashDialog.show();

                                final Button cashBTOk = (Button) cashDialog.findViewById(R.id.cashPaymentDialog_BTOk);
                                final EditText cashETCash = (EditText) cashDialog.findViewById(R.id.cashPaymentDialog_TECash);
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
                                        if (!(_count.equals("")))
                                            pid = Integer.parseInt(cashETCash.getText().toString());
                                        int indexOfItem = finalListViewProduct.indexOf(selectedOrderOnCart);
                                            finalListViewProduct.get(indexOfItem).setStockQuantity(pid);
                                            orderCount.setText(finalListViewProduct.get(position).getStockQuantity() + "");
                                            orderTotalPrice.setText(selectedOrderOnCart.getCostPrice() * selectedOrderOnCart.getStockQuantity() + getString(R.string.ins));

                                        cashDialog.cancel();
                                    }
                                });
                                Button cashBTCancel = (Button) cashDialog.findViewById(R.id.cashPaymentDialog_BTCancel);
                                cashBTCancel.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        cashDialog.cancel();
                                    }
                                });

                                Switch s = (Switch) cashDialog.findViewById(R.id.cashPaymentDialog_SwitchProportion);
                                s.setVisibility(View.GONE);
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
         finalListViewProduct.get(index).setStockQuantity(finalListViewProduct.get(index).getStockQuantity()+1);
        adapter1= new InventoryProductDetailsListViewAdapter(getApplicationContext(),R.layout.list_adapter_row_nventory_product_details,finalListViewProduct);
        finalProductListView.setAdapter(adapter1);    }

    private void decreaseItemOnCart(int index) {
        Product p = finalListViewProduct.get(index);
        finalListViewProduct.get(index).setStockQuantity(finalListViewProduct.get(index).getStockQuantity()-1);
        adapter1= new InventoryProductDetailsListViewAdapter(getApplicationContext(),R.layout.list_adapter_row_nventory_product_details,finalListViewProduct);
        finalProductListView.setAdapter(adapter1);
    }
    private void removeFromCart(int index) {
        finalListViewProduct.remove(index);
        adapter1= new InventoryProductDetailsListViewAdapter(getApplicationContext(),R.layout.list_adapter_row_nventory_product_details,finalListViewProduct);
        finalProductListView.setAdapter(adapter1);


    }
    private void addToCart(Product product) {
        boolean productStatus=false;
        for(int i=0 ; i<finalListViewProduct.size();i++){
            if(finalListViewProduct.get(i).getProductId()==product.getProductId()){
                productStatus=true;
            }
        }
        if(productStatus){
            for(int i=0 ; i<finalListViewProduct.size();i++){
                if(finalListViewProduct.get(i).getProductId()==product.getProductId()){
                    finalListViewProduct.get(i).setStockQuantity(finalListViewProduct.get(i).getStockQuantity()+1);
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
}
