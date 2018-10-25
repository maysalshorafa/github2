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
import com.pos.leaders.leaderspossystem.DataBaseAdapter.ProductDBAdapter;
import com.pos.leaders.leaderspossystem.Models.BoInvoice;
import com.pos.leaders.leaderspossystem.Models.CreditInvoiceDocument;
import com.pos.leaders.leaderspossystem.Models.Customer;
import com.pos.leaders.leaderspossystem.Models.Product;
import com.pos.leaders.leaderspossystem.Tools.CreditInvoiceStatus;
import com.pos.leaders.leaderspossystem.Tools.CustomerCatalogGridViewAdapter;
import com.pos.leaders.leaderspossystem.Tools.InvoiceManagementListViewAdapter;
import com.pos.leaders.leaderspossystem.Tools.ProductCatalogGridViewAdapter;
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
import java.util.List;

import static com.pos.leaders.leaderspossystem.Tools.DocumentControl.pdfLoadImages;

public class CreditInvoiceManagementActivity extends AppCompatActivity {

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
    public static  List<String>orderIds=new ArrayList<>();
     Product product;
    double creditAmount=0;
    final String SAMPLE_FILE = "creditinvoice.pdf";

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
                    creditAmount=0;
                    Log.d("Invoice",CreditInvoiceManagementActivity.invoiceList.get(position).toString());
                    List<String>productSkuList = new ArrayList<String>();
                    final List<Product>productList=new ArrayList<Product>();
                    final List<Integer>productCount = new ArrayList<Integer>();
                    ProductDBAdapter productDBAdapter = new ProductDBAdapter(getApplicationContext());
                    productDBAdapter.open();
                    final BoInvoice invoice = CreditInvoiceManagementActivity.invoiceList.get(position);
                    final JSONObject docDocument = invoice.getDocumentsData();
                    JSONArray cartDetailsList = docDocument.getJSONArray("cartDetailsList");
                    final JSONArray newCartDetails = new JSONArray();
                    newCartDetails.put(cartDetailsList.get(position));
                    for (int i=0;i<cartDetailsList.length();i++){
                        JSONObject cartDetailsObject =cartDetailsList.getJSONObject(i);
                        productSkuList.add(cartDetailsObject.getString("sku"));
                        productCount.add(cartDetailsObject.getInt("quantity"));
                        String sku = cartDetailsObject.getString("sku");
                        productList.add(productDBAdapter.getProductByBarCode(sku));
                    }
                    final Dialog productDialog = new Dialog(CreditInvoiceManagementActivity.this);
                    productDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    productDialog.show();
                    productDialog.setContentView(R.layout.credit_invoice_dialog);
                    final ListView gvProduct = (ListView) productDialog.findViewById(R.id.criditInvoice_LVProductInvoice);
                    TextView totalPrice = (TextView)productDialog.findViewById(R.id.creditInvoiceDialog_TVPrice);
                    totalPrice.setText(Util.makePrice(docDocument.getDouble("total")));
                   final TextView credit = (TextView)productDialog.findViewById(R.id.AC_tvReq);
                    Button done =(Button)productDialog.findViewById(R.id.btn_done);
                    Button cancel =(Button)productDialog.findViewById(R.id.btn_cancel);
                    credit.setText(Util.makePrice(0));
                    final ProductCatalogGridViewAdapter productCatalogGridViewAdapter = new ProductCatalogGridViewAdapter(getApplicationContext(),productList);
                    gvProduct.setAdapter(productCatalogGridViewAdapter);
                    gvProduct.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            for (int i = 0; i < gvProduct.getChildCount(); i++) {
                                if(position == i ){
                                    gvProduct.getChildAt(i).setBackgroundColor(Color.RED);
                                }else{
                                    gvProduct.getChildAt(i).setBackgroundColor(Color.TRANSPARENT);
                                }
                                product=productList.get(position);
                                productList.remove(position);
                                final ProductCatalogGridViewAdapter productCatalogGridViewAdapter = new ProductCatalogGridViewAdapter(getApplicationContext(),productList);
                                gvProduct.setAdapter(productCatalogGridViewAdapter);
                                productCatalogGridViewAdapter.notifyDataSetChanged();

                            }
                            creditAmount+=product.getPrice()*productCount.get(position);
                            credit.setText(Util.makePrice(creditAmount));

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
                            new AsyncTask<Void, Void, Void>(){
                                @Override
                                protected void onPreExecute() {
                                    super.onPreExecute();
                                }
                                @Override
                                protected void onPostExecute(Void aVoid) {
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

                                    }
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
                                        Log.d("Document vale", docJson.toString());
                                        BoInvoice invoice = new BoInvoice(DocumentType.CREDIT_INVOICE,docJson,"");
                                        Log.d("Invoice log",invoice.toString());
                                        String res=transmit.authPost(ApiURL.Documents,invoice.toString(), SESSION.token);
                                        JSONObject jsonObject = new JSONObject(res);
                                        String msgData = jsonObject.getString(MessageKey.responseBody);
                                        Log.d("result",msgData.toString());
                                        PdfUA pdfUA = new PdfUA();

                                        try {
                                            pdfUA.printCreditInvoiceReport(context,msgData);
                                        } catch (DocumentException e) {
                                            e.printStackTrace();
                                        }
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
        final Dialog customerDialog = new Dialog(CreditInvoiceManagementActivity.this);
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

        customerList = customerDBAdapter.getAllCustomer();
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
                                c.getPhoneNumber().toLowerCase().contains(word.toLowerCase()) ||
                                c.getStreet().toLowerCase().contains(word.toLowerCase())) {
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

    final ProgressDialog progressDialog = new ProgressDialog(CreditInvoiceManagementActivity.context);
    final ProgressDialog progressDialog2 =new ProgressDialog(CreditInvoiceManagementActivity.context);

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
                        CreditInvoiceManagementActivity.invoiceNumberList.add(msgDataJson.getString("docNum"));
                       invoice = new BoInvoice(DocumentType.INVOICE,msgDataJson.getJSONObject("documentsData"),msgDataJson.getString("docNum"));
                        CreditInvoiceManagementActivity.invoiceList.add(invoice);
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
                    InvoiceManagementListViewAdapter invoiceManagementListViewAdapter = new InvoiceManagementListViewAdapter(CreditInvoiceManagementActivity.context,R.layout.list_adapter_row_invoices_management,CreditInvoiceManagementActivity.invoiceList,CreditInvoiceManagementActivity.invoiceNumberList);
                    CreditInvoiceManagementActivity.invoiceListView.setAdapter(invoiceManagementListViewAdapter);
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
            Toast.makeText(CreditInvoiceManagementActivity.context, "Try Again.", Toast.LENGTH_SHORT).show();
        }
        progressDialog.cancel();
        super.onPostExecute(s);

        //endregion
    }
}
