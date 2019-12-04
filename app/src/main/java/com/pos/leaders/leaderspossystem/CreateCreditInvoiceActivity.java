package com.pos.leaders.leaderspossystem;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itextpdf.text.DocumentException;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.CustomerDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.InventoryDbAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.PosInvoiceDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.ProductDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.ProductInventoryDbAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.ZReportCountDbAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.ZReportDBAdapter;
import com.pos.leaders.leaderspossystem.Models.BoInvoice;
import com.pos.leaders.leaderspossystem.Models.CreditInvoiceDocument;
import com.pos.leaders.leaderspossystem.Models.Customer;
import com.pos.leaders.leaderspossystem.Models.OrderDetails;
import com.pos.leaders.leaderspossystem.Models.Product;
import com.pos.leaders.leaderspossystem.Models.ProductInventory;
import com.pos.leaders.leaderspossystem.Models.ZReport;
import com.pos.leaders.leaderspossystem.Models.ZReportCount;
import com.pos.leaders.leaderspossystem.Tools.CONSTANT;
import com.pos.leaders.leaderspossystem.Tools.CreditInvoiceProductCatalogGridViewAdapter;
import com.pos.leaders.leaderspossystem.Tools.CreditInvoiceStatus;
import com.pos.leaders.leaderspossystem.Tools.CustomerCatalogGridViewAdapter;
import com.pos.leaders.leaderspossystem.Tools.InvoiceManagementListViewAdapter;
import com.pos.leaders.leaderspossystem.Tools.SESSION;
import com.pos.leaders.leaderspossystem.Tools.SETTINGS;
import com.pos.leaders.leaderspossystem.Tools.TitleBar;
import com.pos.leaders.leaderspossystem.Tools.Util;
import com.pos.leaders.leaderspossystem.syncposservice.Enums.ApiURL;
import com.pos.leaders.leaderspossystem.syncposservice.Enums.MessageKey;
import com.pos.leaders.leaderspossystem.syncposservice.MessageTransmit;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.pos.leaders.leaderspossystem.Tools.DocumentControl.pdfLoadImages;

public class CreateCreditInvoiceActivity extends AppCompatActivity {

    public static ListView invoiceListView;
    EditText etSearch;
    ImageView chooseCustomer;
    TextView customerName;
    boolean userScrolled =false;
    public static Customer customer;
    List<Customer> customerList = new ArrayList<>();
    List<Customer> AllCustmerList = new ArrayList<>();
    public static Context context = null;
    public static List<BoInvoice>invoiceList=new ArrayList<>();
    public static ArrayList<String>invoiceNumberList=new ArrayList<>();
    View previousView = null;
    boolean haveCart=false;
    public static  List<String>orderIds=new ArrayList<>();
     Product product;
    double creditAmount=0;
    final String SAMPLE_FILE = "creditinvoice.pdf";
    JSONArray newCartDetails = new JSONArray();
    JSONObject jsonObject = new JSONObject();
    ProductInventoryDbAdapter productInventoryDbAdapter;
    InventoryDbAdapter inventoryDbAdapter ;
    double SalesWitheTax=0,SalesWithoutTax=0,salesaftertax=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);


        // Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_invoice_management);
        TitleBar.setTitleBar(this);
        Log.d("token", SESSION.token+"");
        context=this;
        Bundle bundle = getIntent().getExtras();

        invoiceListView = (ListView)findViewById(R.id.invoiceManagement_LVInvoice);
        etSearch = (EditText)findViewById(R.id.etSearch);
        chooseCustomer = (ImageView)findViewById(R.id.chooseCustomer);
        customerName = (TextView)findViewById(R.id.customerName);
        productInventoryDbAdapter=new ProductInventoryDbAdapter(CreateCreditInvoiceActivity.this);
        productInventoryDbAdapter.open();
        inventoryDbAdapter=new InventoryDbAdapter(CreateCreditInvoiceActivity.this);
        inventoryDbAdapter.open();
        chooseCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                invoiceList=new ArrayList<>();
                invoiceNumberList=new ArrayList<>();
                callCustomerDialog();


            }
        });
        invoiceListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                try {
                     newCartDetails = new JSONArray();
                    creditAmount=0;
                    Log.d("Invoice", CreateCreditInvoiceActivity.invoiceList.get(position).toString());
                    List<String>productSkuList = new ArrayList<String>();
                    final List<Product>productList=new ArrayList<Product>();
                    final List<Integer>productCount = new ArrayList<Integer>();
                    ProductDBAdapter productDBAdapter = new ProductDBAdapter(getApplicationContext());
                    productDBAdapter.open();
                    final BoInvoice invoice = CreateCreditInvoiceActivity.invoiceList.get(position);
                    final JSONObject docDocument = invoice.getDocumentsData();
                    String reference=invoice.getDocNum();
                    final JSONArray cartDetailsList = docDocument.getJSONArray("cartDetailsList");
                    Log.d("ttttt",cartDetailsList.toString());
                    for (int i=0;i<cartDetailsList.length();i++){
                        JSONObject cartDetailsObject =cartDetailsList.getJSONObject(i);
                        productSkuList.add(cartDetailsObject.getString("sku"));
                        productCount.add(cartDetailsObject.getInt("quantity"));
                        String sku = cartDetailsObject.getString("sku");
                        String pId = cartDetailsObject.getString("productId");
                        if(Long.parseLong(pId)==-1){
                            Product product =new Product(Long.parseLong(String.valueOf(-1)),"General","General",cartDetailsObject.getDouble("amount"),"0","0",Long.parseLong(String.valueOf(1)),Long.parseLong(String.valueOf(1)));

                                if(docDocument.getDouble("cartDiscount")>0){
                                    product.setPrice((cartDetailsObject.getDouble("amount"))*(100-(docDocument.getDouble("cartDiscount")))/100);

                                }else {
                                    product.setPrice(cartDetailsObject.getDouble("amount"));

                                }

                            productList.add(product);
                        }else {
                            Product product = productDBAdapter.getProductByID(Long.parseLong(pId));
                            if(cartDetailsObject.getDouble("discount")>0){
                                if(docDocument.getDouble("cartDiscount")>0){
                                    product.setPrice((cartDetailsObject.getDouble("unitPrice"))*(100-(cartDetailsObject.getDouble("discount")+docDocument.getDouble("cartDiscount")))/100);

                                }else {
                                    product.setPrice(cartDetailsObject.getDouble("unitPrice")*(100-cartDetailsObject.getDouble("discount"))/100);

                                }
                            }else {
                                if(docDocument.getDouble("cartDiscount")>0){
                                    product.setPrice((cartDetailsObject.getDouble("unitPrice"))*(100-(cartDetailsObject.getDouble("discount")+docDocument.getDouble("cartDiscount")))/100);

                                }else {
                                    product.setPrice(cartDetailsObject.getDouble("unitPrice"));

                                }
                            }

                            productList.add(product);
                        }
                    }
                    final Dialog productDialog = new Dialog(CreateCreditInvoiceActivity.this);
                    productDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    productDialog.show();
                    productDialog.setContentView(R.layout.credit_invoice_dialog);
                    final ListView gvProduct = (ListView) productDialog.findViewById(R.id.criditInvoice_LVProductInvoice);
                    TextView totalPrice = (TextView)productDialog.findViewById(R.id.creditInvoiceDialog_TVPrice);
                    totalPrice.setText(Util.makePrice(docDocument.getDouble("total")));
                    TextView tvCartDiscount = (TextView)productDialog.findViewById(R.id.tvCartDiscountValue);
                    tvCartDiscount.setText(Util.makePrice(docDocument.getDouble("cartDiscount")));
                   final TextView credit = (TextView)productDialog.findViewById(R.id.AC_tvReq);
                    final TextView refInvoice = (TextView)productDialog.findViewById(R.id.ReferenceInvoice_tvValue);
                    refInvoice.setText(reference);
                    Button done =(Button)productDialog.findViewById(R.id.btn_done);
                    Button cancel =(Button)productDialog.findViewById(R.id.btn_cancel);
                    credit.setText(Util.makePrice(0));
                    final CreditInvoiceProductCatalogGridViewAdapter productCatalogGridViewAdapter = new CreditInvoiceProductCatalogGridViewAdapter(getApplicationContext(),productList,productCount);
                    gvProduct.setAdapter(productCatalogGridViewAdapter);

                    gvProduct.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position1, long id) {
                            product=productList.get(position1);
                            int count = productCount.get(position1);
                            haveCart=false;
                            creditAmount+=product.getPrice();
                            credit.setText(Util.makePrice(creditAmount));
                            JSONObject newCartJson=new JSONObject();
                            try {
                            for(int a= 0;a<cartDetailsList.length();a++){

                                    if(cartDetailsList.getJSONObject(a).getLong("productId")==product.getProductId()){
                                        newCartJson=cartDetailsList.getJSONObject(a);

                                    }
                                }
                                for(int p=0;p<newCartDetails.length();p++){

                                    if(newCartDetails.getJSONObject(p).getLong("productId")==newCartJson.getLong("productId")){
                                        int c=newCartDetails.getJSONObject(p).getInt("quantity");
                                        newCartDetails.getJSONObject(p).remove("quantity");
                                        newCartDetails.getJSONObject(p).put("quantity",c+1);
                                        newCartDetails.getJSONObject(p).remove("unitPrice");
                                        newCartDetails.getJSONObject(p).put("unitPrice", product.getPrice());
                                        haveCart=true;
                                        if(product.isWithTax()){
                                            if(docDocument.getDouble("cartDiscount")>0){
                                                SalesWithoutTax+=((product.getPrice()*(c+1))-((product.getPrice()*(c+1))*docDocument.getDouble("cartDiscount")/100));
                                            }
                                            else {
                                                SalesWithoutTax+=product.getPrice()*(c+1);
                                            }


                                        }else {
                                            if(docDocument.getDouble("cartDiscount")>0) {
                                                salesaftertax += ((product.getPrice() * (c + 1))- ((product.getPrice() * (c + 1)) * (docDocument.getDouble("cartDiscount") / 100)));
                                                Log.d("salesaftertax", salesaftertax + "k6666666666");
                                                SalesWitheTax += ((product.getPrice() * (c + 1) / (1 + (SETTINGS.tax / 100))) - ((product.getPrice() * (c + 1) / (1 + (SETTINGS.tax / 100))) * (docDocument.getDouble("cartDiscount") / 100)));
                                            }
                                        else {
                                                salesaftertax += product.getPrice() * (c + 1);
                                                Log.d("salesaftertax", salesaftertax + "k6666666666");
                                                SalesWitheTax += product.getPrice() * (c + 1) / (1 + (SETTINGS.tax / 100));
                                            }
                                        }
                                        Log.d("teeee888888s",SalesWithoutTax+"  "+salesaftertax+"   "+SalesWitheTax);

                                    }
                                }
                                if(!haveCart){
                                    newCartJson.remove("unitPrice");
                                    newCartJson.put("unitPrice", product.getPrice());
                                    newCartJson.remove("quantity");
                                    newCartJson.put("quantity", 1);
                                  newCartDetails.put(newCartJson);
                                    Log.d("newCartDeteails111",newCartJson.toString());
                                    if(product.isWithTax()){
                                        if(docDocument.getDouble("cartDiscount")>0) {
                                            SalesWithoutTax += ((product.getPrice() * 1)-((product.getPrice() * 1)*(docDocument.getDouble("cartDiscount") / 100)));
                                        }
                                        else {
                                            SalesWithoutTax += product.getPrice() * 1;
                                        }
                                    }else {
                                        if(docDocument.getDouble("cartDiscount")>0){
                                        salesaftertax+=((product.getPrice()*1)-((product.getPrice()*1)*(docDocument.getDouble("cartDiscount") / 100)));
                                        Log.d("salesaftertax",salesaftertax+"k6666666666");
                                        SalesWitheTax+=(((product.getPrice()*1)/ (1 + (SETTINGS.tax / 100)))-((product.getPrice()*1/ (1 + (SETTINGS.tax / 100)))*(docDocument.getDouble("cartDiscount") / 100)));}
                                        else {
                                            salesaftertax+=product.getPrice()*1;
                                            Log.d("salesaftertax",salesaftertax+"k6666666666");
                                            SalesWitheTax+=(product.getPrice()*1)/ (1 + (SETTINGS.tax / 100));
                                        }
                                    }
                                    Log.d("teeees",SalesWithoutTax+"  "+salesaftertax+"   "+SalesWitheTax);

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                          Log.d("newCartDeteails",newCartDetails.toString());
                           /* try {
                                JSONObject tempJson = cartDetailsList.getJSONObject(position1);
                                Log.d("newObjectFinal222",tempJson.toString());
                                long productId = tempJson.getLong("productId");
                                Log.d("newObjectFinal",newCartDetails.toString());
                                for(int i=0;i<newCartDetails.length();i++) {
                                    JSONObject currentCartObject = newCartDetails.getJSONObject(i);
                                    if(productId==currentCartObject.getLong("productId")){
                                        int c = currentCartObject.getInt("quantity");
                                        currentCartObject.remove("quantity");
                                        currentCartObject.put("quantity",c+1);
                                        currentCartObject.remove("unitPrice");
                                        currentCartObject.put("unitPrice", product.getPrice());
                                        newCartDetails.remove(i);
                                        newCartDetails.put(currentCartObject);
                                        Log.d("newObject1",currentCartObject.toString());
                                        haveCart=true;
                                    }
                                }
                                if(!haveCart){
                                    JSONObject a= tempJson;
                                    a.remove("unitPrice");
                                    a.put("unitPrice", product.getPrice());
                                    a.remove("quantity");
                                    a.put("quantity", 1);
                                    newCartDetails.put(a);
                                    Log.d("newObject",a.toString());

                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }*/
                            for (int i = 0; i < gvProduct.getChildCount(); i++) {
                                if(position1 == i ){
                                    gvProduct.getChildAt(i).setBackgroundColor(Color.RED);
                                }else{
                                    gvProduct.getChildAt(i).setBackgroundColor(Color.TRANSPARENT);
                                }
                            }

                            if(count<=1){
                                productList.remove(position1);
                                final CreditInvoiceProductCatalogGridViewAdapter productCatalogGridViewAdapter = new CreditInvoiceProductCatalogGridViewAdapter(getApplicationContext(),productList,productCount);
                                gvProduct.setAdapter(productCatalogGridViewAdapter);
                                productCatalogGridViewAdapter.notifyDataSetChanged();
                            }else {
                                productCount.set(position1,count-1);
                                final CreditInvoiceProductCatalogGridViewAdapter productCatalogGridViewAdapter = new CreditInvoiceProductCatalogGridViewAdapter(getApplicationContext(),productList,productCount);
                                gvProduct.setAdapter(productCatalogGridViewAdapter);
                                productCatalogGridViewAdapter.notifyDataSetChanged();
                            }




                        }
                    });
                    cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            productDialog.dismiss();
                        }
                    });
                    done.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            if(newCartDetails.length()==0){
                            Toast.makeText(CreateCreditInvoiceActivity.this,"Choose Product want to return",Toast.LENGTH_LONG).show();
                            }else {
                                Log.d("empty",newCartDetails.toString()+"");

                                new AsyncTask<Void, Void, Void>(){
                                @Override
                                protected void onPreExecute() {
                                    super.onPreExecute();
                                }
                                @Override
                                protected void onPostExecute(Void aVoid) {
                                     //   if(jsonObject.get("status").equals("200")){
                                    try {
                                        if(jsonObject.get("status").equals("200")){
                                            HashMap<String,Integer>productHashMap=new HashMap<String, Integer>();
                                            for(OrderDetails o :SESSION._ORDER_DETAILES){
                                                if(o.getProduct().getProductId()!=-1) {
                                                    ProductInventory productInventory = productInventoryDbAdapter.getProductInventoryByID(o.getProduct().getProductId());
                                                    productInventoryDbAdapter.updateEntry(o.getProduct().getProductId(), productInventory.getQty() - o.getQuantity());
                                                    productHashMap.put(String.valueOf(o.getProduct().getProductId()),productInventory.getQty()-o.getQuantity());
                                                }
                                            }

                                                try
                                            {
                                                File path = new File( Environment.getExternalStorageDirectory(), context.getPackageName());
                                                File file = new File(path,SAMPLE_FILE);
                                                RandomAccessFile f = new RandomAccessFile(file, "r");
                                                byte[] data = new byte[(int)f.length()];
                                                f.readFully(data);
                                                pdfLoadImages(data,context);
                                            }
                                            catch(Exception ignored)
                                            {

                                            }}
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    /*    }else {
                                            new android.support.v7.app.AlertDialog.Builder(context)
                                                    .setTitle(context.getString(R.string.invoice))
                                                    .setMessage(context.getString(R.string.cant_make_invoice_check_internet_connection))
                                                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int which) {

                                                        }
                                                    })
                                                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int which) {

                                                        }
                                                    })
                                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                                    .show();
                                        }*/

                                }
                                @Override
                                protected Void doInBackground(Void... voids) {
                                    MessageTransmit transmit = new MessageTransmit(SETTINGS.BO_SERVER_URL);
                                    JSONObject customerData = new JSONObject();
                                    JSONObject userData = new JSONObject();
                                    try {
                                        ObjectMapper mapper = new ObjectMapper();
                                        customerData=docDocument.getJSONObject("customer");
                                        userData = docDocument.getJSONObject("user");
                                        CreditInvoiceDocument creditInvoiceDocument =new CreditInvoiceDocument("CreditInvoice",new Timestamp(System.currentTimeMillis()),creditAmount, CreditInvoiceStatus.OPEN,"ILS", docDocument.getDouble("cartDiscount"),invoice.getDocNum());
                                        String doc = mapper.writeValueAsString(creditInvoiceDocument);
                                        JSONObject docJson= new JSONObject(doc);
                                        String type = docJson.getString("type");
                                        docJson.remove("type");
                                        docJson.put("@type",type);
                                        docJson.put("customer",customerData);
                                        docJson.put("user",userData);
                                        docJson.put("cartDetailsList",newCartDetails);
                                        Log.d("NewCartDetailes",newCartDetails.toString());
                                        Log.d("Document vale", docJson.toString());
                                        BoInvoice invoice = new BoInvoice(DocumentType.CREDIT_INVOICE,docJson,"");
                                        Log.d("Invoice log",invoice.toString());
                                        String res=transmit.authPost(ApiURL.Documents,invoice.toString(), SESSION.token);
                                         jsonObject = new JSONObject(res);
                                        Log.d("rrrrr",res);

                                        if(jsonObject.get("status").equals("200")){
                                            newCartDetails=new JSONArray();
                                            ZReportDBAdapter zReportDBAdapter = new ZReportDBAdapter(context);
                                            zReportDBAdapter.open();
                                            ZReportCountDbAdapter zReportCountDbAdapter=new ZReportCountDbAdapter(context);
                                            zReportCountDbAdapter.open();
                                            ZReport zReport =null;
                                            ZReportCount zReportCount=null;
                                            JSONObject r = jsonObject.getJSONObject(MessageKey.responseBody);
                                            try {
                                                zReport = zReportDBAdapter.getLastRow();
                                                zReportCount=zReportCountDbAdapter.getLastRow();
                                                PosInvoiceDBAdapter posInvoiceDBAdapter = new PosInvoiceDBAdapter(context);
                                                posInvoiceDBAdapter.open();
                                                posInvoiceDBAdapter.insertEntry(creditAmount*-1,zReport.getzReportId()-1,DocumentType.CREDIT_INVOICE.getValue(),"",r.getString("docNum"), CONSTANT.CASH);
                                                zReport.setCreditInvoiceAmount(zReport.getCreditInvoiceAmount()+creditAmount*-1);
                                                zReport.setTotalSales(zReport.getTotalSales()+creditAmount*-1);
                                                zReport.setSalesBeforeTax(Double.parseDouble(Util.makePrice(zReport.getSalesBeforeTax() - (SalesWithoutTax))));
                                                zReport.setSalesWithTax(Double.parseDouble(Util.makePrice(zReport.getSalesWithTax() - (SalesWitheTax))));
                                                Log.d("setSalesWithTaxReport",zReport.getSalesWithTax()+"");
                                                zReport.setTotalTax(Double.parseDouble(Util.makePrice(zReport.getTotalTax()-Math.abs(salesaftertax - SalesWitheTax))));

                                                zReportCount.setCreditInvoiceCount(zReportCount.getCreditInvoiceCount()+1);
                                                    zReportDBAdapter.updateEntry(zReport);
                                                zReportCountDbAdapter.updateEntry(zReportCount);

                                            } catch (Exception e) {
                                                PosInvoiceDBAdapter posInvoiceDBAdapter = new PosInvoiceDBAdapter(context);
                                                posInvoiceDBAdapter.open();
                                                posInvoiceDBAdapter.insertEntry(creditAmount*-1,-1,DocumentType.CREDIT_INVOICE.getValue(),"",r.getString("docNum"), CONSTANT.CASH);
                                                zReport.setCreditInvoiceAmount(zReport.getCreditInvoiceAmount()+creditAmount*-1);
                                                zReport.setTotalSales(zReport.getTotalSales()+creditAmount*-1);
                                                zReport.setSalesBeforeTax(Double.parseDouble(Util.makePrice(zReport.getSalesBeforeTax() - (SalesWithoutTax))));
                                                zReport.setSalesWithTax(Double.parseDouble(Util.makePrice(zReport.getSalesWithTax() - (SalesWitheTax))));
                                                zReport.setTotalTax(Double.parseDouble(Util.makePrice(zReport.getTotalTax()-Math.abs(salesaftertax - SalesWitheTax))));
                                                zReportCount.setCreditInvoiceCount(zReportCount.getCreditInvoiceCount()+1);
                                                zReportCountDbAdapter.updateEntry(zReportCount);
                                                zReportDBAdapter.updateEntry(zReport);

                                                e.printStackTrace();
                                            }
                                        PdfUA pdfUA = new PdfUA();

                                        try {
                                            Log.d("yyyyy",r.toString());
                                            pdfUA.printCreditInvoiceReport(context,r.toString(),"source");
                                        } catch (DocumentException e) {
                                            e.printStackTrace();
                                        }
                                            try {
                                                Thread.sleep(100);
                                            } catch (InterruptedException e) {
                                                e.printStackTrace();
                                            }
                                        }

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

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }});




    }
    //choose customer invoice
    private void callCustomerDialog() {
        orderIds=new ArrayList<>();
        CustomerDBAdapter customerDBAdapter =new CustomerDBAdapter(this);
        customerDBAdapter.open();
        final Dialog customerDialog = new Dialog(CreateCreditInvoiceActivity.this);
        customerDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        customerDialog.show();
        customerDialog.setContentView(R.layout.pop_up);
        final EditText customer_id = (EditText) customerDialog.findViewById(R.id.customer_name);
        final GridView gvCustomer = (GridView) customerDialog.findViewById(R.id.popUp_gvCustomer);
        gvCustomer.setNumColumns(3);

        Button btn_cancel = (Button) customerDialog.findViewById(R.id.btn_cancel);

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customerDialog.dismiss();
            }
        });

        customer_id.setText("");
        customer_id.setHint("Search..");

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        gvCustomer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
        gvCustomer.setOnScrollListener(new AbsListView.OnScrollListener() {
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

        customerList = customerDBAdapter.getAllNormalCustomer();
        AllCustmerList = customerList;

        CustomerCatalogGridViewAdapter custmerCatalogGridViewAdapter = new CustomerCatalogGridViewAdapter(getApplicationContext(), customerList);

        gvCustomer.setAdapter(custmerCatalogGridViewAdapter);

        gvCustomer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                customer= customerList.get(position);
                customerName.setText(customer.getFullName());
                if(customer!=null) {
                    StartCreditInvoiceConnection startInvoiceConnection = new StartCreditInvoiceConnection();
                    startInvoiceConnection.execute(String.valueOf(customer.getCustomerId()));
                }
                customerDialog.dismiss();
            }
        });

        customer_id.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                gvCustomer.setTextFilterEnabled(true);

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                customerList = new ArrayList<Customer>();
                String word = customer_id.getText().toString();

                if (!word.equals("")) {
                    for (Customer c : AllCustmerList) {

                        if (c.getFirstName().toLowerCase().contains(word.toLowerCase()) ||
                                c.getLastName().toLowerCase().contains(word.toLowerCase())||
                                c.getCustomerIdentity().toLowerCase().contains(word.toLowerCase())||
                                c.getPhoneNumber().toLowerCase().contains(word.toLowerCase())){
                            customerList.add(c);

                        }
                    }
                } else {
                    customerList = AllCustmerList;
                }
                CustomerCatalogGridViewAdapter adapter = new CustomerCatalogGridViewAdapter(getApplicationContext(), customerList);
                gvCustomer.setAdapter(adapter);
                // Log.i("products", productList.toString());


            }
        });


    }

}
class StartCreditInvoiceConnection extends AsyncTask<String,Void,String> {
    private MessageTransmit messageTransmit;
    ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    BoInvoice invoice;
    StartCreditInvoiceConnection() {
        messageTransmit = new MessageTransmit(SETTINGS.BO_SERVER_URL);
    }

    final ProgressDialog progressDialog = new ProgressDialog(CreateCreditInvoiceActivity.context);
    final ProgressDialog progressDialog2 =new ProgressDialog(CreateCreditInvoiceActivity.context);

    @Override
    protected void onPreExecute() {
        progressDialog.setTitle("Please Wait");
        progressDialog2.setTitle("Please Wait");
        progressDialog.show();
    }

    @Override
    protected String doInBackground(String... args) {
        String customerId=args[0];
        try {
            String url = ApiURL.Documents+"/InvoicesForCustomer/"+customerId;
            String invoiceRes = messageTransmit.authGet(url,SESSION.token);
            JSONObject jsonObject = new JSONObject(invoiceRes);
            String msgData = jsonObject.getString(MessageKey.responseBody);
            if (msgData.startsWith("[")) {
                try {
                    JSONArray jsonArray = new JSONArray(msgData);

                    for (int i = 0; i <= jsonArray.length() ; i++) {
                        msgData = jsonArray.getJSONObject(i).toString();
                        JSONObject msgDataJson =new JSONObject(msgData);
                        CreateCreditInvoiceActivity.invoiceNumberList.add(msgDataJson.getString("docNum"));
                       invoice = new BoInvoice(DocumentType.INVOICE,msgDataJson.getJSONObject("documentsData"),msgDataJson.getString("docNum"));
                        CreateCreditInvoiceActivity.invoiceList.add(invoice);
                    }

                } catch (Exception e) {
                    Log.d("exception1",e.toString());
                }

            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return "";
    }

    @Override
    protected void onPostExecute(String s) {
        if (s != null) {
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    progressDialog2.setTitle("Success.");
                    progressDialog2.show();
                    InvoiceManagementListViewAdapter invoiceManagementListViewAdapter = new InvoiceManagementListViewAdapter(CreateCreditInvoiceActivity.context,R.layout.list_adapter_row_invoices_management, CreateCreditInvoiceActivity.invoiceList, CreateCreditInvoiceActivity.invoiceNumberList);
                    CreateCreditInvoiceActivity.invoiceListView.setAdapter(invoiceManagementListViewAdapter);
                }

                @Override
                protected Void doInBackground(Void... params) {
                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    progressDialog2.cancel();
                    super.onPostExecute(aVoid);
                }
            }.execute();
        } else {
            //fail
            Toast.makeText(CreateCreditInvoiceActivity.context, "Try Again.", Toast.LENGTH_SHORT).show();
        }
        progressDialog.cancel();
        super.onPostExecute(s);

        //endregion
    }
}
