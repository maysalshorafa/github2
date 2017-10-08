package com.pos.leaders.leaderspossystem.syncposservice.Service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.IBinder;
import android.util.Log;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.AReportDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.ChecksDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.Currency.CashPaymentDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.Currency.CurrencyOperationDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.Currency.CurrencyReturnsDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.Currency.CurrencysDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.CustomerDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.DepartmentDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.OfferDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.OrderDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.PaymentDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.PermissionsDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.ProductDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.Rule11DBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.Rule3DbAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.Rule7DbAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.Rule8DBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.SaleDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.UserDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.ZReportDBAdapter;
import com.pos.leaders.leaderspossystem.Models.AReport;
import com.pos.leaders.leaderspossystem.Models.Check;
import com.pos.leaders.leaderspossystem.Models.Currency.CashPayment;
import com.pos.leaders.leaderspossystem.Models.Currency.CurrencyOperation;
import com.pos.leaders.leaderspossystem.Models.Currency.CurrencyReturns;
import com.pos.leaders.leaderspossystem.Models.Currency.Currencys;
import com.pos.leaders.leaderspossystem.Models.Customer_M;
import com.pos.leaders.leaderspossystem.Models.Department;
import com.pos.leaders.leaderspossystem.Models.Offer;
import com.pos.leaders.leaderspossystem.Models.Offers.Rule11;
import com.pos.leaders.leaderspossystem.Models.Offers.Rule3;
import com.pos.leaders.leaderspossystem.Models.Offers.Rule7;
import com.pos.leaders.leaderspossystem.Models.Offers.Rule8;
import com.pos.leaders.leaderspossystem.Models.Order;
import com.pos.leaders.leaderspossystem.Models.Payment;
import com.pos.leaders.leaderspossystem.Models.Permissions;
import com.pos.leaders.leaderspossystem.Models.Product;
import com.pos.leaders.leaderspossystem.Models.Sale;
import com.pos.leaders.leaderspossystem.Models.User;
import com.pos.leaders.leaderspossystem.Models.ZReport;
import com.pos.leaders.leaderspossystem.syncposservice.DBHelper.Broker;
import com.pos.leaders.leaderspossystem.syncposservice.Enums.ApiURL;
import com.pos.leaders.leaderspossystem.syncposservice.Enums.MessageKey;
import com.pos.leaders.leaderspossystem.syncposservice.Enums.MessageResult;
import com.pos.leaders.leaderspossystem.syncposservice.Enums.MessageType;
import com.pos.leaders.leaderspossystem.syncposservice.MessageTransmit;
import com.pos.leaders.leaderspossystem.syncposservice.MessagesCreator;
import com.pos.leaders.leaderspossystem.syncposservice.Model.BrokerMessage;
import com.pos.leaders.leaderspossystem.Tools.SESSION;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class SyncMessage extends Service {

    /**
     *
     *
     *

     //start

     Intent intent = new Intent(MainActivity.this, SyncMessage.class);
     intent.putExtra(SyncMessage.API_DOMAIN_SYNC_MESSAGE, "https://jsonplaceholder.typicode.com");
     startService(intent);


     //stop

     Intent intent = new Intent(MainActivity.this, SyncMessage.class);
     stopService(intent);


     */

    public static final String API_DOMAIN_SYNC_MESSAGE = "API_DOMAIN_SYNC_MESSAGE";

    private static final String TAG = "SyncMessageService";
    private static String token = SESSION.token;

    private Broker broker;
    private MessageTransmit messageTransmit;

    private long LOOP_TIME = 1 * 10 * 1000;

    private boolean isRunning = false;

    public static boolean isConnected(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED)
                        return true;
                }
            }
        }
        return false;
    }

    @Override
    public void onCreate() {
        Log.i(TAG, "Service onCreate");
        broker = new Broker(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.i(TAG, "Service onStartCommand");
        //messageTransmit = new MessageTransmit(intent.getStringExtra(API_DOMAIN_SYNC_MESSAGE));
        messageTransmit = new MessageTransmit("http://192.168.252.11:8080/webapi/");

        if(!isRunning) {
            Log.i(TAG, "Create New Thread");

            isRunning = true;
            //Creating new thread for my service
            //Always write your long running tasks in a separate thread, to avoid ANR
            new Thread(new Runnable() {
                @Override
                public void run() {
                    //Your logic that service will perform will be placed here
                    //In this example we are just looping and waits for 1000 milliseconds in each loop.
                    while (isRunning) {
                        Log.i(TAG, "Service running");
                        broker.open();
                        List<BrokerMessage> bms = broker.getAllNotSyncedCommand();
                        for (BrokerMessage bm : bms) {
                            try {
                                if(doSync(bm)) {
                                    broker.Synced(bm.getId());
                                }else {
                                    break;
                                }
                            } catch (IOException | JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        broker.close();

                        try {
                            getSync();
                        } catch (IOException | JSONException e) {
                            e.printStackTrace();
                            stopSelf();
                        }

                        try {
                            Thread.sleep(LOOP_TIME);//2 min thread sleep
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    //Stop service once it finishes its task
                    stopSelf();
                }
            }).start();
        }
        else{
            Log.i(TAG, "Thread is running");
        }
        return Service.START_STICKY;
    }

    private void getSync() throws IOException, JSONException {
        String res = messageTransmit.authGet(ApiURL.Sync, token);
        if(!res.equals(MessageResult.Invalid)){
            JSONObject jsonObject = new JSONObject(res);

            //todo: execute the command
            if(executeMessage(jsonObject)) {
                String resp = messageTransmit.authPost(ApiURL.Sync, MessagesCreator.acknowledge(jsonObject.getInt(MessageKey.Ak)), token);
                Log.i(TAG, "getSync: " + resp);
                if (resp.equals(MessageResult.Invalid)) {
                    //stop
                    return;
                } else if (resp.equals(MessageResult.OK)) {
                    getSync();
                }
            }
        } else if(res.equals(MessageResult.Invalid)) {
            //todo: there is no update is coming
        }
        else {
            //todo: error 401,404,403
        }
    }

    private boolean executeMessage(JSONObject jsonObject) throws JSONException, IOException {
        Log.i(TAG, jsonObject.toString());
        long rID = 0;
        if(jsonObject.has(MessageKey.MessageType)) {
            String msgType = jsonObject.getString(MessageKey.MessageType);
            String msgData = jsonObject.getString(MessageKey.Data);

            ObjectMapper objectMapper = new ObjectMapper();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
            objectMapper.setDateFormat(dateFormat);


            switch (msgType) {

                //region A REPORT
                case MessageType.ADD_A_REPORT:
                    AReport aReport = null;
                        aReport = objectMapper.readValue(msgData, AReport.class);

                    AReportDBAdapter aReportDBAdapter = new AReportDBAdapter(this);
                    aReportDBAdapter.open();
                    rID=aReportDBAdapter.insertEntry(aReport);
                    aReportDBAdapter.close();

                    break;
                case MessageType.UPDATE_A_REPORT:
                    break;
                case MessageType.DELETE_A_REPORT:
                    break;
                //endregion A REPORT

                //region CHECK
                case MessageType.ADD_CHECK:
                    Check check = null;
                    check = objectMapper.readValue(msgData, Check.class);

                    ChecksDBAdapter checksDBAdapter = new ChecksDBAdapter(this);
                    checksDBAdapter.open();
                    rID=checksDBAdapter.insertEntry(check);
                    checksDBAdapter.close();
                    break;
                case MessageType.UPDATE_CHECK:
                    break;
                case MessageType.DELETE_CHECK:
                    break;
                //endregion CHECK

                //region DEPARTMENT
                case MessageType.ADD_DEPARTMENT:
                    Department department = null;
                    department = objectMapper.readValue(msgData, Department.class);

                    DepartmentDBAdapter departmentDBAdapter = new DepartmentDBAdapter(this);
                    departmentDBAdapter.open();
                    rID=departmentDBAdapter.insertEntry(department);
                    departmentDBAdapter.close();
                    break;
                case MessageType.UPDATE_DEPARTMENT:
                    break;
                case MessageType.DELETE_DEPARTMENT:
                    break;
                //endregion DEPARTMENT

                //region OFFER
                case MessageType.ADD_OFFER:
                    Offer offer = null;
                    offer = objectMapper.readValue(msgData, Offer.class);

                    OfferDBAdapter offerDBAdapter = new OfferDBAdapter(this);
                    offerDBAdapter.open();
                    rID=offerDBAdapter.insertEntry(offer);
                    offerDBAdapter.close();
                    break;
                case MessageType.UPDATE_OFFER:
                    break;
                case MessageType.DELETE_OFFER:
                    break;
                //endregion OFFER

                //region Rules

                //region Rule3
                case MessageType.ADD_RULE_3:
                    Rule3 rule3 = null;
                    rule3 = objectMapper.readValue(msgData, Rule3.class);

                    Rule3DbAdapter rule3DbAdapter = new Rule3DbAdapter(this);
                    rule3DbAdapter.open();
                    rID = rule3DbAdapter.insertEntry(rule3);
                    rule3DbAdapter.close();
                    break;
                case MessageType.UPDATE_RULE_3:
                    break;
                case MessageType.DELETE_RULE_3:
                    break;
                //endregion Rule3

                //region Rule7
                case MessageType.ADD_RULE_7:
                    Rule7 rule7 = null;
                    rule7 = objectMapper.readValue(msgData, Rule7.class);

                    Rule7DbAdapter rule7DbAdapter = new Rule7DbAdapter(this);
                    rule7DbAdapter.open();
                    rID = rule7DbAdapter.insertEntry(rule7);
                    rule7DbAdapter.close();
                    break;
                case MessageType.UPDATE_RULE_7:
                    break;
                case MessageType.DELETE_RULE_7:
                    break;
                //endregion Rule7

                //region Rule8
                case MessageType.ADD_RULE_8:
                    Rule8 rule8 = null;
                    rule8 = objectMapper.readValue(msgData, Rule8.class);

                    Rule8DBAdapter rule8DbAdapter = new Rule8DBAdapter(this);
                    rule8DbAdapter.open();
                    rID = rule8DbAdapter.insertEntry(rule8);
                    rule8DbAdapter.close();
                    break;
                case MessageType.UPDATE_RULE_8:
                    break;
                case MessageType.DELETE_RULE_8:
                    break;
                //endregion Rule8

                //region Rule11
                case MessageType.ADD_RULE_11:
                    Rule11 rule11 = null;
                    rule11 = objectMapper.readValue(msgData, Rule11.class);

                    Rule11DBAdapter rule11DbAdapter = new Rule11DBAdapter(this);
                    rule11DbAdapter.open();
                    rID = rule11DbAdapter.insertEntry(rule11);
                    rule11DbAdapter.close();
                    break;
                case MessageType.UPDATE_RULE_11:
                    break;
                case MessageType.DELETE_RULE_11:
                    break;
                //endregion Rule11

                //endregion Rules

                //region PAYMENT
                case MessageType.ADD_PAYMENT:
                    Payment payment = null;
                    payment = objectMapper.readValue(msgData, Payment.class);

                    PaymentDBAdapter paymentDBAdapter = new PaymentDBAdapter(this);
                    paymentDBAdapter.open();
                    rID=paymentDBAdapter.insertEntry(payment);
                    paymentDBAdapter.close();
                    break;
                case MessageType.UPDATE_PAYMENT:
                    break;
                case MessageType.DELETE_PAYMENT:
                    break;
                //endregion PAYMENT

                //region PERMISSION
                case MessageType.ADD_PERMISSION:
                    Permissions permissions = null;
                    permissions = objectMapper.readValue(msgData, Permissions.class);

                    PermissionsDBAdapter permissionsDBAdapter = new PermissionsDBAdapter(this);
                    permissionsDBAdapter.open();
                    //// TODO: 27/08/2017 insert to database
                    //rID=permissionsDBAdapter.insertEntry(permissions);
                    permissionsDBAdapter.close();
                    break;
                case MessageType.UPDATE_PERMISSION:
                    break;
                case MessageType.DELETE_PERMISSION:
                    break;
                //endregion PERMISSION

                //region SALE
                case MessageType.ADD_SALE:
                    Sale sale = null;
                    sale = objectMapper.readValue(msgData, Sale.class);

                    SaleDBAdapter saleDBAdapter = new SaleDBAdapter(this);
                    saleDBAdapter.open();
                    rID = saleDBAdapter.insertEntry(sale);
                    saleDBAdapter.close();
                    break;
                case MessageType.UPDATE_SALE:
                    break;
                case MessageType.DELETE_SALE:
                    break;
                //endregion SALE

                //region CLUB
                case MessageType.ADD_CLUB:
                    break;
                case MessageType.UPDATE_CLUB:
                    break;
                case MessageType.DELETE_CLUB:
                    break;
                //endregion CLUB

                //region CUSTOMER
                case MessageType.ADD_CUSTOMER:
                    Customer_M customer = null;
                    customer = objectMapper.readValue(msgData, Customer_M.class);

                    CustomerDBAdapter customerDBAdapter = new CustomerDBAdapter(this);
                    customerDBAdapter.open();
                    rID = customerDBAdapter.insertEntry(customer);
                    customerDBAdapter.close();

                    break;
                case MessageType.UPDATE_CUSTOMER:
                    break;
                case MessageType.DELETE_CUSTOMER:
                    break;
                //endregion CUSTOMER

                //region ORDER
                case MessageType.ADD_ORDER:
                    Order o;
                        o = objectMapper.readValue(msgData, Order.class);

                    OrderDBAdapter orderDBAdapter = new OrderDBAdapter(this);
                    orderDBAdapter.open();
                    rID = orderDBAdapter.insertEntry(o);
                    orderDBAdapter.close();
                    break;
                case MessageType.UPDATE_ORDER:
                    break;
                case MessageType.DELETE_ORDER:
                    break;
                //endregion ORDER

                //region PRODUCT
                case MessageType.ADD_PRODUCT:
                    Product p = null;
                        p = objectMapper.readValue(msgData, Product.class);

                    ProductDBAdapter productDBAdapter = new ProductDBAdapter(this);
                    productDBAdapter.open();
                    rID = productDBAdapter.insertEntry(p);
                    productDBAdapter.close();

                    break;
                case MessageType.UPDATE_PRODUCT:
                    break;
                case MessageType.DELETE_PRODUCT:
                    break;
                //endregion PRODUCT

                //region USER
                case MessageType.ADD_USER:
                    User u;
                    u = objectMapper.readValue(msgData, User.class);

                    UserDBAdapter userDBAdapter = new UserDBAdapter(this);
                    userDBAdapter.open();
                    rID = userDBAdapter.insertEntry(u);
                    userDBAdapter.close();
                    break;
                case MessageType.UPDATE_USER:
                    break;
                case MessageType.DELETE_USER:
                    break;
                //endregion USER

                //region Z REPORT
                case MessageType.ADD_Z_REPORT:
                    ZReport zReport = null;
                    zReport = objectMapper.readValue(msgData, ZReport.class);

                    ZReportDBAdapter zReportDBAdapter = new ZReportDBAdapter(this);
                    zReportDBAdapter.open();
                    rID = zReportDBAdapter.insertEntry(zReport);
                    zReportDBAdapter.close();

                    break;
                case MessageType.UPDATE_Z_REPORT:
                    break;
                case MessageType.DELETE_Z_REPORT:
                    break;
                //endregion Z REPORT


                //region CurrencyReturns
                case MessageType.ADD_CURRENCY_RETURN:
                    CurrencyReturns c = null;
                    c = objectMapper.readValue(msgData, CurrencyReturns.class);

                    CurrencyReturnsDBAdapter currencyReturnsDBAdapter = new CurrencyReturnsDBAdapter(this);
                    currencyReturnsDBAdapter.open();
                    currencyReturnsDBAdapter.insertEntry(c);
                    currencyReturnsDBAdapter.close();

                    break;
                case MessageType.UPDATE_CURRENCY_RETURN:
                    break;
                case MessageType.DELETE_CURRENCY_RETURN:
                    break;
                //endregion Currency Return


                //region CurrencyOpeartion
                case MessageType.ADD_CURRENCY_OPEARATION:
                    CurrencyOperation currencyOperation = null;
                    currencyOperation = objectMapper.readValue(msgData, CurrencyOperation.class);

                    CurrencyOperationDBAdapter currencyOperationDBAdapter = new CurrencyOperationDBAdapter(this);
                    currencyOperationDBAdapter.open();
                    currencyOperationDBAdapter.insertEntry(currencyOperation);
                    currencyOperationDBAdapter.close();

                    break;
                case MessageType.UPDATE_CURRENCY_OPEARATION:
                    break;
                case MessageType.DELETE_CURRENCY_OPEARATION:
                    break;
                //endregion Currency Opeartion


                //region CashPayment
                case MessageType.ADD_CASH_PAYMENT:
                    CashPayment cashPayment = null;
                    cashPayment = objectMapper.readValue(msgData, CashPayment.class);

                    CashPaymentDBAdapter cashPaymentDBAdapter = new CashPaymentDBAdapter(this);
                    cashPaymentDBAdapter.open();
                    cashPaymentDBAdapter.insertEntry(cashPayment);
                    cashPaymentDBAdapter.close();

                    break;
                case MessageType.UPDATE_CASH_PAYMENT:
                    break;
                case MessageType.DELETE_CASH_PAYMENT:
                    break;
                //endregion Cash payment..


                //region Currencys
                case MessageType.ADD_CURRENCYS:
                    Currencys currencys = null;
                    currencys = objectMapper.readValue(msgData, Currencys.class);

                    CurrencysDBAdapter currencysDBAdapter = new CurrencysDBAdapter(this);
                    currencysDBAdapter.open();
                    currencysDBAdapter.insertEntry(currencys);
                    currencysDBAdapter.close();

                    break;
                case MessageType.UPDATE_CURRENCYS:
                    break;
                case MessageType.DELETE_CURRENCYS:
                    break;
                //endregion Currency Opeartion.

            }
        }else{
            //todo: does not have message type
        }

        if(rID>0) return true;
        else return false;
    }

    private boolean doSync(BrokerMessage bm) throws IOException, JSONException {

        //if(!isConnected(this))
           // return false;
        // TODO: 24/08/2017 ladfjkgnk

        JSONObject jsonObject = new JSONObject(bm.getCommand());
        String res = "";
        String msgType = jsonObject.getString(MessageKey.MessageType);

        switch (msgType){

            //region A REPORT
            case MessageType.ADD_A_REPORT:
                res = messageTransmit.authPost(ApiURL.AReport, jsonObject.getString(MessageKey.Data), token);
                break;
            case MessageType.UPDATE_A_REPORT:
                res = messageTransmit.authPut(ApiURL.AReport, jsonObject.getString(MessageKey.Data), token);
                break;
            case MessageType.DELETE_A_REPORT:
                res = messageTransmit.authDelete(ApiURL.AReport, jsonObject.getString(MessageKey.Data), token);
                break;
            //endregion A REPORT

            //region CHECK
            case MessageType.ADD_CHECK:
                res = messageTransmit.authPost(ApiURL.Check, jsonObject.getString(MessageKey.Data), token);
                break;
            case MessageType.UPDATE_CHECK:
                res = messageTransmit.authPut(ApiURL.Check, jsonObject.getString(MessageKey.Data), token);
                break;
            case MessageType.DELETE_CHECK:
                res = messageTransmit.authDelete(ApiURL.Check, jsonObject.getString(MessageKey.Data), token);
                break;
            //endregion CHECK

            //region DEPARTMENT
            case MessageType.ADD_DEPARTMENT:
                res = messageTransmit.authPost(ApiURL.Department, jsonObject.getString(MessageKey.Data), token);
                break;
            case MessageType.UPDATE_DEPARTMENT:
                res = messageTransmit.authPut(ApiURL.Department, jsonObject.getString(MessageKey.Data), token);
                break;
            case MessageType.DELETE_DEPARTMENT:
                res = messageTransmit.authDelete(ApiURL.Department, jsonObject.getString(MessageKey.Data), token);
                break;
            //endregion DEPARTMENT

            //region OFFER
            case MessageType.ADD_OFFER:
                res = messageTransmit.authPost(ApiURL.Offer, jsonObject.getString(MessageKey.Data), token);
                break;
            case MessageType.UPDATE_OFFER:
                res = messageTransmit.authPut(ApiURL.Offer, jsonObject.getString(MessageKey.Data), token);
                break;
            case MessageType.DELETE_OFFER:
                res = messageTransmit.authDelete(ApiURL.Offer, jsonObject.getString(MessageKey.Data), token);
                break;
            //endregion OFFER

            //region PAYMENT
            case MessageType.ADD_PAYMENT:
                res = messageTransmit.authPost(ApiURL.Payment, jsonObject.getString(MessageKey.Data), token);
                break;
            case MessageType.UPDATE_PAYMENT:
                res = messageTransmit.authPut(ApiURL.Payment, jsonObject.getString(MessageKey.Data), token);
                break;
            case MessageType.DELETE_PAYMENT:
                res = messageTransmit.authDelete(ApiURL.Payment, jsonObject.getString(MessageKey.Data), token);
                break;
            //endregion PAYMENT

            //region PERMISSION
            case MessageType.ADD_PERMISSION:
                res = messageTransmit.authPost(ApiURL.Permission, jsonObject.getString(MessageKey.Data), token);
                break;
            case MessageType.UPDATE_PERMISSION:
                res = messageTransmit.authPut(ApiURL.Permission, jsonObject.getString(MessageKey.Data), token);
                break;
            case MessageType.DELETE_PERMISSION:
                res = messageTransmit.authDelete(ApiURL.Permission, jsonObject.getString(MessageKey.Data), token);
                break;
            //endregion PERMISSION

            //region SALE
            case MessageType.ADD_SALE:
                res = messageTransmit.authPost(ApiURL.Sale, jsonObject.getString(MessageKey.Data), token);
                break;
            case MessageType.UPDATE_SALE:
                res = messageTransmit.authPut(ApiURL.Sale, jsonObject.getString(MessageKey.Data), token);
                break;
            case MessageType.DELETE_SALE:
                res = messageTransmit.authDelete(ApiURL.Sale, jsonObject.getString(MessageKey.Data), token);
                break;
            //endregion SALE

            //region Z REPORT
            case MessageType.ADD_Z_REPORT:
                res = messageTransmit.authPost(ApiURL.ZReport, jsonObject.getString(MessageKey.Data), token);
                break;
            case MessageType.UPDATE_Z_REPORT:
                res = messageTransmit.authPut(ApiURL.ZReport, jsonObject.getString(MessageKey.Data), token);
                break;
            case MessageType.DELETE_Z_REPORT:
                res = messageTransmit.authDelete(ApiURL.ZReport, jsonObject.getString(MessageKey.Data), token);
                break;
            //endregion Z REPORT

            case MessageType.ADD_CLUB:
                res = messageTransmit.authPost(ApiURL.Club, jsonObject.getString(MessageKey.Data), token);
                break;
            case MessageType.UPDATE_CLUB:
                res = messageTransmit.authPut(ApiURL.Club, jsonObject.getString(MessageKey.Data), token);
                break;
            case MessageType.DELETE_CLUB:
                res = messageTransmit.authDelete(ApiURL.Club, jsonObject.getString(MessageKey.Data), token);
                break;


            case MessageType.ADD_CUSTOMER:
                res = messageTransmit.authPost(ApiURL.Customer, jsonObject.getString(MessageKey.Data), token);
                break;
            case MessageType.UPDATE_CUSTOMER:
                res = messageTransmit.authPut(ApiURL.Customer, jsonObject.getString(MessageKey.Data), token);
                break;
            case MessageType.DELETE_CUSTOMER:
                res = messageTransmit.authDelete(ApiURL.Customer, jsonObject.getString(MessageKey.Data), token);
                break;


            case MessageType.ADD_ORDER:
                res = messageTransmit.authPost(ApiURL.Order, jsonObject.getString(MessageKey.Data), token);
                break;
            case MessageType.UPDATE_ORDER:
                res = messageTransmit.authPut(ApiURL.Order, jsonObject.getString(MessageKey.Data), token);
                break;
            case MessageType.DELETE_ORDER:
                res = messageTransmit.authDelete(ApiURL.Order, jsonObject.getString(MessageKey.Data), token);
                break;


            case MessageType.ADD_PRODUCT:
                res = messageTransmit.authPost(ApiURL.Product, jsonObject.getString(MessageKey.Data), token);
                break;
            case MessageType.UPDATE_PRODUCT:
                res = messageTransmit.authPut(ApiURL.Product, jsonObject.getString(MessageKey.Data), token);
                break;
            case MessageType.DELETE_PRODUCT:
                res = messageTransmit.authDelete(ApiURL.Product, jsonObject.getString(MessageKey.Data), token);
                break;


            case MessageType.ADD_USER:
                res = messageTransmit.authPost(ApiURL.Users, jsonObject.getString(MessageKey.Data), token);
                break;
            case MessageType.UPDATE_USER:
                res = messageTransmit.authPut(ApiURL.Users, jsonObject.getString(MessageKey.Data), token);
                break;
            case MessageType.DELETE_USER:
                res = messageTransmit.authDelete(ApiURL.Users, jsonObject.getString(MessageKey.Data), token);
                break;
            //CurrencyReturns

            case MessageType.ADD_CURRENCY_RETURN:
                res = messageTransmit.authPost(ApiURL.CurrencyReturn, jsonObject.getString(MessageKey.Data), token);
                break;
            case MessageType.UPDATE_CURRENCY_RETURN:
                res = messageTransmit.authPut(ApiURL.CurrencyReturn, jsonObject.getString(MessageKey.Data), token);
                break;
            case MessageType.DELETE_CURRENCY_RETURN:
                res = messageTransmit.authDelete(ApiURL.CurrencyReturn, jsonObject.getString(MessageKey.Data), token);
                break;
            //CurrencyOPeration
            case MessageType.ADD_CURRENCY_OPEARATION:
                res = messageTransmit.authPost(ApiURL.CurrencyOpearation, jsonObject.getString(MessageKey.Data), token);
                break;
            case MessageType.UPDATE_CURRENCY_OPEARATION:
                res = messageTransmit.authPut(ApiURL.CurrencyOpearation, jsonObject.getString(MessageKey.Data), token);
                break;
            case MessageType.DELETE_CURRENCY_OPEARATION:
                res = messageTransmit.authDelete(ApiURL.CurrencyOpearation, jsonObject.getString(MessageKey.Data), token);
                break;
            //CashPayment
            case MessageType.ADD_CASH_PAYMENT:
                res = messageTransmit.authPost(ApiURL.CashPayment, jsonObject.getString(MessageKey.Data), token);
                break;
            case MessageType.UPDATE_CASH_PAYMENT:
                res = messageTransmit.authPut(ApiURL.CashPayment, jsonObject.getString(MessageKey.Data), token);
                break;
            case MessageType.DELETE_CASH_PAYMENT:
                res = messageTransmit.authDelete(ApiURL.CashPayment, jsonObject.getString(MessageKey.Data), token);
                break;
            //CustmerAssest
            case MessageType.ADD_CUSTMER_ASSEST:
                res = messageTransmit.authPost(ApiURL.CustmerAssest, jsonObject.getString(MessageKey.Data), token);
                break;
            case MessageType.UPDATE_CUSTMER_ASSEST:
                res = messageTransmit.authPut(ApiURL.CustmerAssest, jsonObject.getString(MessageKey.Data), token);
                break;
            case MessageType.DELETE_CUSTMER_ASSEST:
                res = messageTransmit.authDelete(ApiURL.CustmerAssest, jsonObject.getString(MessageKey.Data), token);
                break;




        }

        try {
            if(res.toLowerCase().equals("false"))
                return false;
            else if(!res.toLowerCase().equals("true")){
                JSONObject object = new JSONObject(res);
            }
        } catch (JSONException e) {
            return false;
        }

        return true;
    }

    @Override
    public IBinder onBind(Intent arg0) {
        Log.i(TAG, "Service onBind");
        return null;
    }

    @Override
    public void onDestroy() {
        isRunning = false;
        Log.i(TAG, "Service onDestroy");
    }
}