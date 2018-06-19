package com.pos.leaders.leaderspossystem.syncposservice.Service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.SQLException;
import android.database.sqlite.SQLiteConstraintException;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.AReportDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.AReportDetailsDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.ChecksDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.CityDbAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.ClubAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.CreditCardPaymentDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.Currency.CashPaymentDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.Currency.CurrencyOperationDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.Currency.CurrencyReturnsDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.Currency.CurrencyDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.CustomerAssetDB;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.CustomerDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.CustomerMeasurementAdapter.CustomerMeasurementDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.CustomerMeasurementAdapter.MeasurementDynamicVariableDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.CustomerMeasurementAdapter.MeasurementsDetailsDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.DepartmentDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.OfferDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.OrderDetailsDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.PaymentDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.PermissionsDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.ProductDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.Rule11DBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.Rule3DbAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.Rule7DbAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.Rule8DBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.OrderDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.ScheduleWorkersDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.UsedPointDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.EmployeeDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.EmployeePermissionsDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.ZReportDBAdapter;
import com.pos.leaders.leaderspossystem.Models.AReport;
import com.pos.leaders.leaderspossystem.Models.AReportDetails;
import com.pos.leaders.leaderspossystem.Models.Check;
import com.pos.leaders.leaderspossystem.Models.City;
import com.pos.leaders.leaderspossystem.Models.Club;
import com.pos.leaders.leaderspossystem.Models.CreditCardPayment;
import com.pos.leaders.leaderspossystem.Models.Currency.CashPayment;
import com.pos.leaders.leaderspossystem.Models.Currency.CurrencyOperation;
import com.pos.leaders.leaderspossystem.Models.Currency.CurrencyReturns;
import com.pos.leaders.leaderspossystem.Models.Currency.Currency;

import com.pos.leaders.leaderspossystem.Models.Currency.CurrencyType;
import com.pos.leaders.leaderspossystem.Models.Customer;
import com.pos.leaders.leaderspossystem.Models.CustomerAssistant;
import com.pos.leaders.leaderspossystem.Models.CustomerMeasurement.CustomerMeasurement;
import com.pos.leaders.leaderspossystem.Models.CustomerMeasurement.MeasurementDynamicVariable;
import com.pos.leaders.leaderspossystem.Models.CustomerMeasurement.MeasurementsDetails;
import com.pos.leaders.leaderspossystem.Models.Department;
import com.pos.leaders.leaderspossystem.Models.Offer;
import com.pos.leaders.leaderspossystem.Models.Offers.Rule11;
import com.pos.leaders.leaderspossystem.Models.Offers.Rule3;
import com.pos.leaders.leaderspossystem.Models.Offers.Rule7;
import com.pos.leaders.leaderspossystem.Models.Offers.Rule8;
import com.pos.leaders.leaderspossystem.Models.OrderDetails;
import com.pos.leaders.leaderspossystem.Models.Payment;
import com.pos.leaders.leaderspossystem.Models.Permission.Permissions;
import com.pos.leaders.leaderspossystem.Models.Product;
import com.pos.leaders.leaderspossystem.Models.Order;
import com.pos.leaders.leaderspossystem.Models.ScheduleWorkers;
import com.pos.leaders.leaderspossystem.Models.SumPoint;
import com.pos.leaders.leaderspossystem.Models.UsedPoint;
import com.pos.leaders.leaderspossystem.Models.Employee;
import com.pos.leaders.leaderspossystem.Models.Permission.EmployeesPermissions;
import com.pos.leaders.leaderspossystem.Models.ValueOfPoint;
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
import com.pos.leaders.leaderspossystem.syncposservice.SetupFragments.Token;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class SyncMessage extends Service {

    /**
     * //start
     * <p>
     * Intent intent = new Intent(MainActivity.this, SyncMessage.class);
     * intent.putExtra(SyncMessage.API_DOMAIN_SYNC_MESSAGE, "https://jsonplaceholder.typicode.com");
     * startService(intent);
     * <p>
     * <p>
     * //stop
     * <p>
     * Intent intent = new Intent(MainActivity.this, SyncMessage.class);
     * stopService(intent);
     */

    public static final String API_DOMAIN_SYNC_MESSAGE = "API_DOMAIN_SYNC_MESSAGE";

    private static final String TAG = "SyncMessageService";
    private static String token = SESSION.token;

    private Broker broker;
    private MessageTransmit messageTransmit;

    private long LOOP_TIME = 1 * 30 * 1000;

    private boolean isRunning = false;
    private Context context;

    public static boolean isConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Network[] networks = connectivityManager.getAllNetworks();
            NetworkInfo networkInfo;
            for (Network mNetwork : networks) {
                networkInfo = connectivityManager.getNetworkInfo(mNetwork);
                if (networkInfo.getState().equals(NetworkInfo.State.CONNECTED)) {
                    return true;
                }
            }
        } else {
            if (connectivityManager != null) {
                //noinspection deprecation
                NetworkInfo[] info = connectivityManager.getAllNetworkInfo();
                if (info != null) {
                    for (NetworkInfo anInfo : info) {
                        if (anInfo.getState() == NetworkInfo.State.CONNECTED) {
                            Log.d("Network",
                                    "NETWORKNAME: " + anInfo.getTypeName());
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    @Override
    public void onCreate() {
        Log.i(TAG, "Service onCreate");
        broker = new Broker(this);
        if (SESSION.token.equals("")) {
            token = Token.readToken(getApplicationContext());
            SESSION.token = token;
        }
        token = SESSION.token;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.i(TAG, "Service onStartCommand");
        try {
            messageTransmit = new MessageTransmit(intent.getStringExtra(API_DOMAIN_SYNC_MESSAGE));
        } catch (Exception ex) {
            this.onDestroy();
        }
        //messageTransmit = new MessageTransmit("http://192.168.252.11:8080/webapi/");

        if (!isRunning) {
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
                                if (doSync(bm)) {
                                    broker.Synced(bm.getId());
                                } else {
                                    //break;
                                    //don`t stop on fail
                                }
                            } catch (IOException | JSONException | NullPointerException e) {
                                isRunning = false;
                                e.printStackTrace();
                                stopSelf();
                            }
                        }

                        broker.close();

                        try {
                            getSync();
                        } catch (IOException | JSONException | NullPointerException e) {
                            //isRunning = false;
                            e.printStackTrace();
                            //stopSelf();
                        }

                        try {
                            Thread.sleep(LOOP_TIME);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    //Stop service once it finishes its task
                    stopSelf();
                }
            }).start();
        } else {
            Log.i(TAG, "Thread is running");
        }
        return Service.START_STICKY;
    }

    private void getSync() throws IOException, JSONException {
        String res = "";
        try {
            Log.i("token", token);
            Log.i("sync", ApiURL.Sync);
            res = messageTransmit.authGet(ApiURL.Sync, token);
        } catch (Exception ex) {
            Log.e("getsync exception", ex.getMessage());
        }

        Log.e("getsync result", res);
        if (res.length() != 0) {
            if (!res.equals(MessageResult.Invalid) && res.charAt(0) == '{') {
                Log.w("getSync", res);
                JSONObject jsonObject = new JSONObject(res);

                //todo: execute the command
                try {

                    if (executeMessage(jsonObject)) {
                        String resp = messageTransmit.authPost(ApiURL.Sync, MessagesCreator.acknowledge(jsonObject.getInt(MessageKey.Ak)), token);
                        Log.i(TAG, "getSync: " + resp);
                        if (resp.equals(MessageResult.Invalid)) {
                            //stop
                            return;
                        } else if (resp.equals(MessageResult.OK)) {
                            getSync();
                        }
                    }
                } catch (SQLiteConstraintException e) {
                    e.printStackTrace();
                    Log.e("sync error", e.getMessage());
                    if (e.getMessage().contains("UNIQUE constraint failed")) {
                        String resp = messageTransmit.authPost(ApiURL.Sync, MessagesCreator.acknowledge(jsonObject.getInt(MessageKey.Ak)), token);
                        Log.i(TAG, "getSync: " + resp);
                        if (resp.equals(MessageResult.Invalid)) {
                            //stop
                            return;
                        } else if (resp.equals(MessageResult.OK)) {
                            getSync();
                        }
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            } else if (res.equals(MessageResult.Invalid)) {
                //todo: there is no update is coming
                Log.i(TAG, "there is no update incoming.");
            } else {
                //todo: error 401,404,403
                Log.i(TAG, "Can`t sync messages.");
            }
        }
    }

    private boolean executeMessage(JSONObject jsonObject) throws JSONException, IOException, SQLiteConstraintException, SQLException {
        Log.i(TAG, jsonObject.toString());
        long rID = 0;
        if (jsonObject.has(MessageKey.MessageType)) {
            String msgType = jsonObject.getString(MessageKey.MessageType);
            String msgData = jsonObject.getString(MessageKey.Data);

            if (msgData.startsWith("[")) {
                try {
                    JSONArray jsonArray = jsonObject.getJSONArray(MessageKey.Data);
                    msgData = jsonArray.getJSONObject(0).toString();
                } catch (Exception e) {
                    try {
                        msgData = jsonObject.getJSONObject(MessageKey.Data).toString();
                    } catch (Exception ex) {
                        msgData = msgData.substring(1);
                        msgData = msgData.substring(0, msgData.length() - 1);
                    }
                }
            }
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
                    rID = aReportDBAdapter.insertEntry(aReport);
                    aReportDBAdapter.close();

                    break;
                case MessageType.UPDATE_A_REPORT:
                    break;
                case MessageType.DELETE_A_REPORT:
                    break;
                //endregion A REPORT
                //region A REPORT Details
                case MessageType.ADD_A_REPORT_DETAILS:
                    AReportDetails aReportDetails = null;
                    aReportDetails = objectMapper.readValue(msgData, AReportDetails.class);

                    AReportDetailsDBAdapter aReportDetailsDBAdapter = new AReportDetailsDBAdapter(this);
                    aReportDetailsDBAdapter.open();
                    rID = aReportDetailsDBAdapter.insertEntry(aReportDetails);
                    aReportDetailsDBAdapter.close();

                    break;
                case MessageType.UPDATE_A_REPORT_DETAILS:
                    break;
                case MessageType.DELETE_A_REPORT_DETAILS:
                    break;
                //endregion A REPORT Details

                //region CHECK
                case MessageType.ADD_CHECK:
                    Check check = null;
                    check = objectMapper.readValue(msgData, Check.class);

                    ChecksDBAdapter checksDBAdapter = new ChecksDBAdapter(this);
                    checksDBAdapter.open();
                    rID = checksDBAdapter.insertEntry(check);
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
                    rID = departmentDBAdapter.insertEntry(department);
                    departmentDBAdapter.close();
                    break;
                case MessageType.UPDATE_DEPARTMENT:
                    Department updateDepartment = null;
                    updateDepartment = objectMapper.readValue(msgData, Department.class);
                    DepartmentDBAdapter updateDepartmentDBAdapter = new DepartmentDBAdapter(this);
                    updateDepartmentDBAdapter.open();
                    rID = updateDepartmentDBAdapter.updateEntryBo(updateDepartment);
                    updateDepartmentDBAdapter.close();
                    break;
                case MessageType.DELETE_DEPARTMENT:
                    Department deleteDepartment = null;
                    deleteDepartment = objectMapper.readValue(msgData, Department.class);
                    DepartmentDBAdapter deleteDepartmentDBAdapter = new DepartmentDBAdapter(this);
                    deleteDepartmentDBAdapter.open();
                    rID = deleteDepartmentDBAdapter.deleteEntryBo(deleteDepartment);
                    deleteDepartmentDBAdapter.close();
                    break;
                //endregion DEPARTMENT

                //region OFFER
                case MessageType.ADD_OFFER:
                    Offer offer = null;
                    offer = objectMapper.readValue(msgData, Offer.class);

                    OfferDBAdapter offerDBAdapter = new OfferDBAdapter(this);
                    offerDBAdapter.open();
                    rID = offerDBAdapter.insertEntry(offer);
                    offerDBAdapter.close();
                    break;
                case MessageType.UPDATE_OFFER:
                    break;
                case MessageType.DELETE_OFFER:
                    break;
                //endregion OFFER

                //region Rules3
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
                    rID = paymentDBAdapter.insertEntry(payment);
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

                //region UserPermtiossion
                case MessageType.ADD_EMPLOYEE_PERMISSION:
                    EmployeesPermissions userPermissions = null;
                    userPermissions = objectMapper.readValue(msgData, EmployeesPermissions.class);

                    EmployeePermissionsDBAdapter userPermissionsDBAdapter = new EmployeePermissionsDBAdapter(this);
                    userPermissionsDBAdapter.open();
                    rID = userPermissionsDBAdapter.insertEntry(userPermissions);
                    userPermissionsDBAdapter.close();
                    break;
                case MessageType.UPDATE_EMPLOYEE_PERMISSION:
                    break;
                case MessageType.DELETE_EMPLOYEE_PERMISSION:
                    break;


                //end user permission region

                //region Order
                case MessageType.ADD_ORDER:
                    Order order = null;
                    order = objectMapper.readValue(msgData, Order.class);

                    OrderDBAdapter orderDBAdapter = new OrderDBAdapter(this);
                    orderDBAdapter.open();
                    rID = orderDBAdapter.insertEntry(order);
                    orderDBAdapter.close();
                    break;
                case MessageType.UPDATE_ORDER:
                    break;
                case MessageType.DELETE_ORDER:
                    break;
                //endregion order

                //region CLUB
                case MessageType.ADD_CLUB:
                    Club club = null;
                    club = objectMapper.readValue(msgData, Club.class);

                    ClubAdapter clubAdapter = new ClubAdapter(this);
                    clubAdapter.open();
                    rID = clubAdapter.insertEntry(club);
                    clubAdapter.close();
                    break;
                case MessageType.UPDATE_CLUB:
                    Club updateClub = null;
                    updateClub = objectMapper.readValue(msgData, Club.class);
                    ClubAdapter updateClubAdapter = new ClubAdapter(this);
                    updateClubAdapter.open();
                    rID = updateClubAdapter.updateEntryBo(updateClub);
                    updateClubAdapter.close();
                    break;
                case MessageType.DELETE_CLUB:
                    Club deleteClub = null;
                    deleteClub = objectMapper.readValue(msgData, Club.class);
                    ClubAdapter deleteClubAdapter = new ClubAdapter(this);
                    deleteClubAdapter.open();
                    rID = deleteClubAdapter.deleteEntryBo(deleteClub);
                    deleteClubAdapter.close();
                    break;
                //endregion CLUB
                //region City
                case MessageType.ADD_CITY:
                    City city = null;
                    city = objectMapper.readValue(msgData, City.class);

                    CityDbAdapter cityDbAdapter = new CityDbAdapter(this);
                    cityDbAdapter.open();
                    rID = cityDbAdapter.insertEntry(city);
                    cityDbAdapter.close();
                    break;
                case MessageType.UPDATE_CITY:
                    break;
                case MessageType.DELETE_CITY:
                    break;
                //endregion City


                //region CUSTOMER
                case MessageType.ADD_CUSTOMER:
                    Customer customer = null;
                    customer = objectMapper.readValue(msgData, Customer.class);

                    CustomerDBAdapter customerDBAdapter = new CustomerDBAdapter(this);
                    customerDBAdapter.open();
                    rID = customerDBAdapter.insertEntry(customer);
                    customerDBAdapter.close();

                    break;
                case MessageType.UPDATE_CUSTOMER:
                    Customer updateCustomer = null;
                    updateCustomer = objectMapper.readValue(msgData, Customer.class);
                    CustomerDBAdapter updateCustomerDBAdapter = new CustomerDBAdapter(this);
                    updateCustomerDBAdapter.open();
                    rID = updateCustomerDBAdapter.updateEntryBo(updateCustomer);
                    updateCustomerDBAdapter.close();
                    break;
                case MessageType.DELETE_CUSTOMER:
                    Customer deleteCustomer = null;
                    deleteCustomer = objectMapper.readValue(msgData, Customer.class);
                    CustomerDBAdapter deleteCustomerDBAdapter = new CustomerDBAdapter(this);
                    deleteCustomerDBAdapter.open();
                    rID = deleteCustomerDBAdapter.deleteEntryBo(deleteCustomer);
                    deleteCustomerDBAdapter.close();
                    break;
                //endregion CUSTOMER

                //region ORDERDetails
                case MessageType.ADD_ORDER_DETAILS:
                    OrderDetails o;
                    o = objectMapper.readValue(msgData, OrderDetails.class);

                    OrderDetailsDBAdapter orderDetailsDBAdapter = new OrderDetailsDBAdapter(this);
                    orderDetailsDBAdapter.open();
                    rID = orderDetailsDBAdapter.insertEntry(o);
                    orderDetailsDBAdapter.close();
                    break;
                case MessageType.UPDATE_ORDER_DETAILS:
                    break;
                case MessageType.DELETE_ORDER_DETAILS:
                    break;
                //endregion ORDERDetails

                //region PRODUCT
                case MessageType.ADD_PRODUCT:
                    Product p = null;
                    p = objectMapper.readValue(msgData, Product.class);

                    ProductDBAdapter productDBAdapter = new ProductDBAdapter(this);
                    productDBAdapter.open();
                    try {
                        rID = productDBAdapter.insertEntry(p);
                    } catch (Exception ex) {
                        rID = 0;
                        ex.printStackTrace();
                    }
                    productDBAdapter.close();

                    break;
                case MessageType.UPDATE_PRODUCT:
                    Product updateProduct = null;
                    updateProduct = objectMapper.readValue(msgData, Product.class);

                    ProductDBAdapter updateProductDBAdapter = new ProductDBAdapter(this);
                    updateProductDBAdapter.open();
                    try {
                        rID = updateProductDBAdapter.updateEntryBo(updateProduct);
                    } catch (Exception ex) {
                        rID = 0;
                        ex.printStackTrace();
                    }
                    updateProductDBAdapter.close();
                    break;
                case MessageType.DELETE_PRODUCT:
                    Product deleteProduct = null;
                    deleteProduct = objectMapper.readValue(msgData, Product.class);

                    ProductDBAdapter deleteProductDBAdapter = new ProductDBAdapter(this);
                    deleteProductDBAdapter.open();
                    try {
                        rID = deleteProductDBAdapter.deleteEntryBo(deleteProduct);
                    } catch (Exception ex) {
                        rID = 0;
                        ex.printStackTrace();
                    }
                    deleteProductDBAdapter.close();
                    break;
                //endregion PRODUCT

                //region USER
                case MessageType.ADD_EMPLOYEE:
                    Employee u;
                    u = objectMapper.readValue(msgData, Employee.class);

                    EmployeeDBAdapter userDBAdapter = new EmployeeDBAdapter(this);
                    userDBAdapter.open();
                    rID = userDBAdapter.insertEntry(u);
                    userDBAdapter.close();
                    break;
                case MessageType.UPDATE_EMPLOYEE:
                    Employee updateUser;
                    updateUser = objectMapper.readValue(msgData, Employee.class);

                    EmployeeDBAdapter updateUserDBAdapter = new EmployeeDBAdapter(this);
                    updateUserDBAdapter.open();
                    rID = updateUserDBAdapter.updateEntryBo(updateUser);
                    updateUserDBAdapter.close();
                    break;
                case MessageType.DELETE_EMPLOYEE:
                    Employee deleteUser;
                    deleteUser = objectMapper.readValue(msgData, Employee.class);

                    EmployeeDBAdapter deleteUserDBAdapter = new EmployeeDBAdapter(this);
                    deleteUserDBAdapter.open();
                    rID = deleteUserDBAdapter.deleteEntryBo(deleteUser);
                    deleteUserDBAdapter.close();
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
                    rID = currencyReturnsDBAdapter.insertEntry(c);
                    currencyReturnsDBAdapter.close();

                    break;
                case MessageType.UPDATE_CURRENCY_RETURN:
                    break;
                case MessageType.DELETE_CURRENCY_RETURN:
                    break;
                //endregion Currency Return


                //region CurrencyOpeartion
                case MessageType.ADD_CURRENCY_OPERATION:
                    CurrencyOperation currencyOperation = null;
                    currencyOperation = objectMapper.readValue(msgData, CurrencyOperation.class);

                    CurrencyOperationDBAdapter currencyOperationDBAdapter = new CurrencyOperationDBAdapter(this);
                    currencyOperationDBAdapter.open();
                    rID = currencyOperationDBAdapter.insertEntry(currencyOperation);
                    currencyOperationDBAdapter.close();

                    break;
                case MessageType.UPDATE_CURRENCY_OPERATION:
                    break;
                case MessageType.DELETE_CURRENCY_OPERATION:
                    break;
                //endregion Currency Opeartion


                //region CashPayment
                case MessageType.ADD_CASH_PAYMENT:
                    CashPayment cashPayment = null;
                    cashPayment = objectMapper.readValue(msgData, CashPayment.class);

                    CashPaymentDBAdapter cashPaymentDBAdapter = new CashPaymentDBAdapter(this);
                    cashPaymentDBAdapter.open();
                    rID = cashPaymentDBAdapter.insertEntry(cashPayment);
                    cashPaymentDBAdapter.close();

                    break;
                case MessageType.UPDATE_CASH_PAYMENT:
                    break;
                case MessageType.DELETE_CASH_PAYMENT:
                    break;
                //endregion Cash payment..


                //region Credit Card Payment
                case MessageType.ADD_CREDIT_CARD_PAYMENT:
                    CreditCardPayment creditCardPayment = null;
                    creditCardPayment = objectMapper.readValue(msgData, CreditCardPayment.class);

                    CreditCardPaymentDBAdapter creditCardPaymentDBAdapter = new CreditCardPaymentDBAdapter(this);
                    creditCardPaymentDBAdapter.open();
                    rID = creditCardPaymentDBAdapter.insertEntry(creditCardPayment);
                    creditCardPaymentDBAdapter.close();

                    break;
                case MessageType.UPDATE_CREDIT_CARD_PAYMENT:
                    break;
                case MessageType.DELETE_CREDIT_CARD_PAYMENT:
                    break;
                //endregion Credit Card Payment

                //region Customer Assistant
                case MessageType.ADD_CUSTOMER_ASSISTANT:
                    CustomerAssistant customerAssistant = null;
                    customerAssistant = objectMapper.readValue(msgData, CustomerAssistant.class);

                    CustomerAssetDB customerAssetDB = new CustomerAssetDB(this);
                    customerAssetDB.open();
                    rID = customerAssetDB.insertEntry(customerAssistant);
                    customerAssetDB.close();

                    break;
                case MessageType.UPDATE_CUSTOMER_ASSISTANT:
                    break;
                case MessageType.DELETE_CUSTOMER_ASSISTANT:
                    break;
                //endregion Customer Assistant

                //region Currency
                case MessageType.ADD_CURRENCY:
                    Currency currency = null;
                    currency = objectMapper.readValue(msgData, Currency.class);

                    CurrencyDBAdapter currencyDBAdapter = new CurrencyDBAdapter(this);
                    currencyDBAdapter.open();
                    rID = currencyDBAdapter.insertEntry(currency);
                    currencyDBAdapter.close();
                    break;
                case MessageType.UPDATE_CURRENCY:
                    break;
                case MessageType.DELETE_CURRENCY:
                    break;
                //endregion Currency.

                //region CustomerMeasurement
                case MessageType.ADD_CUSTOMER_MEASUREMENT:
                    CustomerMeasurement customerMeasurement = null;
                    customerMeasurement = objectMapper.readValue(msgData, CustomerMeasurement.class);
                    CustomerMeasurementDBAdapter customerMeasurementDBAdapter = new CustomerMeasurementDBAdapter(this);
                    customerMeasurementDBAdapter.open();
                    rID = customerMeasurementDBAdapter.insertEntry(customerMeasurement);
                    customerMeasurementDBAdapter.close();
                    break;
                //end
                //region MeasurementsDetails
                case MessageType.ADD_MEASUREMENTS_DETAILS:
                    MeasurementsDetails measurementsDetails = null;
                    measurementsDetails = objectMapper.readValue(msgData, MeasurementsDetails.class);
                    MeasurementsDetailsDBAdapter measurementsDetailsDBAdapter = new MeasurementsDetailsDBAdapter(this);
                    measurementsDetailsDBAdapter.open();
                    rID = measurementsDetailsDBAdapter.insertEntry(measurementsDetails);
                    measurementsDetailsDBAdapter.close();
                    break;
                //end
                //region MeasurementDynamicVariable
                case MessageType.ADD_MEASUREMENTS_DYNAMIC_VARIABLE:
                    MeasurementDynamicVariable measurementDynamicVariable = null;
                    measurementDynamicVariable = objectMapper.readValue(msgData, MeasurementDynamicVariable.class);
                    MeasurementDynamicVariableDBAdapter measurementDynamicVariableDBAdapter = new MeasurementDynamicVariableDBAdapter(this);
                    measurementDynamicVariableDBAdapter.open();
                    rID = measurementDynamicVariableDBAdapter.insertEntry(measurementDynamicVariable);
                    measurementDynamicVariableDBAdapter.close();
                    break;
                //end

                //region UsedPoint
                case MessageType.ADD_USED_POINT:
                    UsedPoint up = null;
                    up = objectMapper.readValue(msgData, UsedPoint.class);
                    UsedPointDBAdapter usedPointDBAdapter = new UsedPointDBAdapter(this);

                    usedPointDBAdapter.open();
                    rID = usedPointDBAdapter.insertEntry(up);
                    usedPointDBAdapter.close();
                    break;
                //end

                //region ScheduleWorker
                case MessageType.ADD_SCHEDULE_WORKERS:
                    ScheduleWorkers scheduleWorkers = null;
                    scheduleWorkers = objectMapper.readValue(msgData, ScheduleWorkers.class);
                    ScheduleWorkersDBAdapter scheduleWorkersDBAdapter = new ScheduleWorkersDBAdapter(this);

                    scheduleWorkersDBAdapter.open();
                    rID = scheduleWorkersDBAdapter.insertEntry(scheduleWorkers);
                    scheduleWorkersDBAdapter.close();
                    break;
                //end


            }
        } else {
            //todo: does not have message type
        }

        if (rID > 0) return true;
        else return false;
    }

    private boolean doSync(BrokerMessage bm) throws IOException, JSONException {

        //if(!isConnected(this))
        // return false;
        // TODO: 24/08/2017 ladfjkgnk

        JSONObject jsonObject = new JSONObject(bm.getCommand());
        String res = "";
        String msgType = jsonObject.getString(MessageKey.MessageType);
        String msgData = jsonObject.getString(MessageKey.Data);

        if (msgData.startsWith("[")) {
            try {
                JSONArray jsonArray = jsonObject.getJSONArray(MessageKey.Data);
                msgData = jsonArray.getJSONObject(0).toString();
            } catch (Exception e) {
                try {
                    msgData = jsonObject.getJSONObject(MessageKey.Data).toString();
                } catch (Exception ex) {
                    msgData = msgData.substring(1);
                    msgData = msgData.substring(0, msgData.length() - 1);
                }
            }
        }
        ObjectMapper objectMapper = new ObjectMapper();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
        objectMapper.setDateFormat(dateFormat);


        Log.w("DO_SYNC", bm.getCommand());

        switch (msgType) {

            //region A REPORT
            case MessageType.ADD_A_REPORT:
                res = messageTransmit.authPost(ApiURL.AReport, jsonObject.getString(MessageKey.Data), token);
                break;
            case MessageType.UPDATE_A_REPORT:
                AReport aReport =null;
                aReport=objectMapper.readValue(msgData, AReport.class);
                res = messageTransmit.authPut(ApiURL.AReport, jsonObject.getString(MessageKey.Data), token,aReport.getaReportId());
                break;
            case MessageType.DELETE_A_REPORT:
                res = messageTransmit.authDelete(ApiURL.AReport, jsonObject.getString(MessageKey.Data), token);
                break;
            //endregion A REPORT
            //region A REPORT Details
            case MessageType.ADD_A_REPORT_DETAILS:
                res = messageTransmit.authPost(ApiURL.AReportDetails, jsonObject.getString(MessageKey.Data), token);
                break;
            case MessageType.UPDATE_A_REPORT_DETAILS:
                AReportDetails aReportDetails =null;
                aReportDetails=objectMapper.readValue(msgData, AReportDetails.class);
                res = messageTransmit.authPut(ApiURL.AReportDetails, jsonObject.getString(MessageKey.Data), token,aReportDetails.getaReportDetailsId());
                break;
            case MessageType.DELETE_A_REPORT_DETAILS:
                res = messageTransmit.authDelete(ApiURL.AReportDetails, jsonObject.getString(MessageKey.Data), token);
                break;
            //endregion A REPORT Details

            //region CHECK
            case MessageType.ADD_CHECK:
                res = messageTransmit.authPost(ApiURL.Check, jsonObject.getString(MessageKey.Data), token);
                break;
            case MessageType.UPDATE_CHECK:
                Check check =null;
                check=objectMapper.readValue(msgData, Check.class);
                res = messageTransmit.authPut(ApiURL.Check, jsonObject.getString(MessageKey.Data), token,check.getCheckId());
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
                Department department=null;
                JSONObject newDepartmentJson= new JSONObject(jsonObject.getString(MessageKey.Data));
                newDepartmentJson.remove("createdAt");
                department=objectMapper.readValue(newDepartmentJson.toString(), Department.class);
                res = messageTransmit.authPut(ApiURL.Department, newDepartmentJson.toString(), token,department.getDepartmentId());
                break;
            case MessageType.DELETE_DEPARTMENT:
                JSONObject newDeleteDepartmentJson = new JSONObject(jsonObject.getString(MessageKey.Data));
                res = messageTransmit.authDelete(ApiURL.Department, newDeleteDepartmentJson.getString("departmentId"), token);
                break;
            //endregion DEPARTMENT

            //region OFFER
            case MessageType.ADD_OFFER:
                res = messageTransmit.authPost(ApiURL.Offer, jsonObject.getString(MessageKey.Data), token);
                break;
            case MessageType.UPDATE_OFFER:
                Offer offer =null;
                offer=objectMapper.readValue(msgData, Offer.class);
                res = messageTransmit.authPut(ApiURL.Offer, jsonObject.getString(MessageKey.Data), token,offer.getId());
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
                Payment payment=null;
                payment=objectMapper.readValue(msgData, Payment.class);
                res = messageTransmit.authPut(ApiURL.Payment, jsonObject.getString(MessageKey.Data), token,payment.getPaymentId());
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
                Permissions permissions=null;
                permissions=objectMapper.readValue(msgData, Permissions.class);
                res = messageTransmit.authPut(ApiURL.Permission, jsonObject.getString(MessageKey.Data), token,permissions.getId());
                break;
            case MessageType.DELETE_PERMISSION:
                res = messageTransmit.authDelete(ApiURL.Permission, jsonObject.getString(MessageKey.Data), token);
                break;
            //endregion PERMISSION

            //region UserPermissions
            case MessageType.ADD_EMPLOYEE_PERMISSION:
                res = messageTransmit.authPost(ApiURL.EMPLOYEE_PERMISSION, jsonObject.getString(MessageKey.Data), token);
                break;
            case MessageType.UPDATE_EMPLOYEE_PERMISSION:
                EmployeesPermissions userPermissions=null;
                userPermissions=objectMapper.readValue(msgData, EmployeesPermissions.class);
                res = messageTransmit.authPut(ApiURL.EMPLOYEE_PERMISSION, jsonObject.getString(MessageKey.Data), token,userPermissions.getEmployeePermissionId());
                break;
            case MessageType.DELETE_EMPLOYEE_PERMISSION:
                res = messageTransmit.authDelete(ApiURL.EMPLOYEE_PERMISSION, jsonObject.getString(MessageKey.Data), token);
                break;

            //End region

            //region ORDER
            case MessageType.ADD_ORDER:
                res = messageTransmit.authPost(ApiURL.ORDER, jsonObject.getString(MessageKey.Data), token);
                break;
            case MessageType.UPDATE_ORDER:
                Order order=null;
                order=objectMapper.readValue(msgData, Order.class);
                res = messageTransmit.authPut(ApiURL.ORDER, jsonObject.getString(MessageKey.Data), token,order.getOrderId());
                break;
            case MessageType.DELETE_ORDER:
                JSONObject newDeleteOrderJson = new JSONObject(jsonObject.getString(MessageKey.Data));
                res = messageTransmit.authDelete(ApiURL.ORDER, newDeleteOrderJson.getString("orderId"), token);
                break;
            //endregion Order

            //region Z REPORT
            case MessageType.ADD_Z_REPORT:
                res = messageTransmit.authPost(ApiURL.ZReport, jsonObject.getString(MessageKey.Data), token);
                break;
            case MessageType.UPDATE_Z_REPORT:
                ZReport zReport=null;
                zReport=objectMapper.readValue(msgData, ZReport.class);
                res = messageTransmit.authPut(ApiURL.ZReport, jsonObject.getString(MessageKey.Data), token,zReport.getzReportId());
                break;
            case MessageType.DELETE_Z_REPORT:
                res = messageTransmit.authDelete(ApiURL.ZReport, jsonObject.getString(MessageKey.Data), token);
                break;
            //endregion Z REPORT

            case MessageType.ADD_CLUB:
                res = messageTransmit.authPost(ApiURL.Club, jsonObject.getString(MessageKey.Data), token);
                break;
            case MessageType.UPDATE_CLUB:
                Club club=null;
                club=objectMapper.readValue(msgData, Club.class);
                res = messageTransmit.authPut(ApiURL.Club, jsonObject.getString(MessageKey.Data), token,club.getClubId());
                break;
            case MessageType.DELETE_CLUB:
                JSONObject newDeleteClubJson = new JSONObject(jsonObject.getString(MessageKey.Data));
                res = messageTransmit.authDelete(ApiURL.Club, newDeleteClubJson.getString("clubId"), token);
                break;
            // Sum Point Region
            case MessageType.ADD_SUM_POINT:
                res = messageTransmit.authPost(ApiURL.SumPoint, jsonObject.getString(MessageKey.Data), token);
                break;
            case MessageType.UPDATE_SUM_POINT:
                SumPoint sumPoint=null;
                sumPoint=objectMapper.readValue(msgData, SumPoint.class);
                res = messageTransmit.authPut(ApiURL.SumPoint, jsonObject.getString(MessageKey.Data), token,sumPoint.getSumPointId());
                break;
            case MessageType.DELETE_SUM_POINT:
                res = messageTransmit.authDelete(ApiURL.SumPoint, jsonObject.getString(MessageKey.Data), token);
                break;
            //End Sum Point Region
            // Value Of Point Region
            case MessageType.ADD_VALUE_OF_POINT:
                res = messageTransmit.authPost(ApiURL.ValueOfPoint, jsonObject.getString(MessageKey.Data), token);
                break;
            case MessageType.UPDATE_VALUE_OF_POINT:
                ValueOfPoint valueOfPoint=null;
                valueOfPoint=objectMapper.readValue(msgData, ValueOfPoint.class);
                res = messageTransmit.authPut(ApiURL.ValueOfPoint, jsonObject.getString(MessageKey.Data), token,valueOfPoint.valueOfPointId);
                break;
            case MessageType.DELETE_VALUE_OF_POINT:
                res = messageTransmit.authDelete(ApiURL.ValueOfPoint, jsonObject.getString(MessageKey.Data), token);
                break;
            //End Value Of Point Region
            //
            // UsedPoint Region
            case MessageType.ADD_USED_POINT:
                res = messageTransmit.authPost(ApiURL.UsedPoint, jsonObject.getString(MessageKey.Data), token);
                break;
            case MessageType.UPDATE_USED_POINT:
                UsedPoint usedPoint =null;
                usedPoint=objectMapper.readValue(msgData, UsedPoint.class);
                res = messageTransmit.authPut(ApiURL.UsedPoint, jsonObject.getString(MessageKey.Data), token,usedPoint.getUsedPointId());
                break;
            case MessageType.DELETE_USED_POINT:
                res = messageTransmit.authDelete(ApiURL.UsedPoint, jsonObject.getString(MessageKey.Data), token);
                break;
            //End Value Of Point Region
            //City Region
            case MessageType.ADD_CITY:
                res = messageTransmit.authPost(ApiURL.City, jsonObject.getString(MessageKey.Data), token);
                break;
            case MessageType.UPDATE_CITY:
                City city=null;
                city=objectMapper.readValue(msgData, City.class);
                res = messageTransmit.authPut(ApiURL.City, jsonObject.getString(MessageKey.Data), token,city.getCityId());
                break;
            case MessageType.DELETE_CITY:
                res = messageTransmit.authDelete(ApiURL.City, jsonObject.getString(MessageKey.Data), token);
                break;


            case MessageType.ADD_CUSTOMER:
                res = messageTransmit.authPost(ApiURL.Customer, jsonObject.getString(MessageKey.Data), token);
                break;
            case MessageType.UPDATE_CUSTOMER:
                Customer customer=null;
                customer=objectMapper.readValue(msgData, Customer.class);
                res = messageTransmit.authPut(ApiURL.Customer, jsonObject.getString(MessageKey.Data), token,customer.getCustomerId());
                break;
            case MessageType.DELETE_CUSTOMER:
                JSONObject newDeleteCustomerJson = new JSONObject(jsonObject.getString(MessageKey.Data));
                res = messageTransmit.authDelete(ApiURL.Customer, newDeleteCustomerJson.getString("customerId"), token);
                break;


            case MessageType.ADD_ORDER_DETAILS:
                res = messageTransmit.authPost(ApiURL.ORDER_DETAILS, jsonObject.getString(MessageKey.Data), token);
                break;
            case MessageType.UPDATE_ORDER_DETAILS:
                OrderDetails orderDetails=null;
                orderDetails=objectMapper.readValue(msgData, OrderDetails.class);
                res = messageTransmit.authPut(ApiURL.ORDER_DETAILS, jsonObject.getString(MessageKey.Data), token,orderDetails.getOrderDetailsId());
                break;
            case MessageType.DELETE_ORDER_DETAILS:
                res = messageTransmit.authDelete(ApiURL.ORDER_DETAILS, jsonObject.getString(MessageKey.Data), token);
                break;


            case MessageType.ADD_PRODUCT:
                res = messageTransmit.authPost(ApiURL.Product, jsonObject.getString(MessageKey.Data), token);
                break;
            case MessageType.UPDATE_PRODUCT:
                Product product=null;
                JSONObject newProductJson = new JSONObject(jsonObject.getString(MessageKey.Data));
                newProductJson.remove("createdAt");
                product=objectMapper.readValue(newProductJson.toString(), Product.class);
                res = messageTransmit.authPut(ApiURL.Product, newProductJson.toString(), token,product.getProductId());
                break;
            case MessageType.DELETE_PRODUCT:
                JSONObject newDeleteProductJson = new JSONObject(jsonObject.getString(MessageKey.Data));
                res = messageTransmit.authDelete(ApiURL.Product, newDeleteProductJson.getString("productId"), token);
                break;


            case MessageType.ADD_EMPLOYEE:
                res = messageTransmit.authPost(ApiURL.Employee, jsonObject.getString(MessageKey.Data), token);
                break;
            case MessageType.UPDATE_EMPLOYEE:
                Employee user=null;
                JSONObject newUserJson = new JSONObject(jsonObject.getString(MessageKey.Data));
                newUserJson.remove("createdAt");
                user=objectMapper.readValue(newUserJson.toString(), Employee.class);
                res = messageTransmit.authPut(ApiURL.Employee, newUserJson.toString(), token,user.getEmployeeId());
                break;
            case MessageType.DELETE_EMPLOYEE:
                JSONObject newDeleteUserJson = new JSONObject(jsonObject.getString(MessageKey.Data));
                res = messageTransmit.authDelete(ApiURL.Employee, newDeleteUserJson.getString("userId"), token);
                break;
            //Currencies

            case MessageType.ADD_CURRENCY:
                res = messageTransmit.authPost(ApiURL.Currencies, jsonObject.getString(MessageKey.Data), token);
                break;
            case MessageType.UPDATE_CURRENCY:
                Currency currency=null;
                currency=objectMapper.readValue(msgData, Currency.class);
                res = messageTransmit.authPut(ApiURL.Currencies, jsonObject.getString(MessageKey.Data), token,currency.getCurrencyId());
                break;
            case MessageType.DELETE_CURRENCY:
                res = messageTransmit.authDelete(ApiURL.Currencies, jsonObject.getString(MessageKey.Data), token);
                break;
            //Currencies_Type

            case MessageType.ADD_CURRENCY_TYPE:
                res = messageTransmit.authPost(ApiURL.CurrencyType, jsonObject.getString(MessageKey.Data), token);
                break;
            case MessageType.UPDATE_CURRENCY_TYPE:
                CurrencyType currencyType=null;
                currencyType=objectMapper.readValue(msgData, CurrencyType.class);
                res = messageTransmit.authPut(ApiURL.CurrencyType, jsonObject.getString(MessageKey.Data), token,currencyType.getCurrencyTypeId());
                break;
            case MessageType.DELETE_CURRENCY_TYPE:
                res = messageTransmit.authDelete(ApiURL.CurrencyType, jsonObject.getString(MessageKey.Data), token);
                break;

            //CurrencyReturn

            case MessageType.ADD_CURRENCY_RETURN:
                res = messageTransmit.authPost(ApiURL.CurrencyReturn, jsonObject.getString(MessageKey.Data), token);
                break;
            case MessageType.UPDATE_CURRENCY_RETURN:
                CurrencyReturns currencyReturns=null;
                currencyReturns=objectMapper.readValue(msgData, CurrencyReturns.class);
                res = messageTransmit.authPut(ApiURL.CurrencyReturn, jsonObject.getString(MessageKey.Data), token,currencyReturns.getCurrencyReturnsId());
                break;
            case MessageType.DELETE_CURRENCY_RETURN:
                res = messageTransmit.authDelete(ApiURL.CurrencyReturn, jsonObject.getString(MessageKey.Data), token);
                break;

            //CurrencyOPeration
            case MessageType.ADD_CURRENCY_OPERATION:
                res = messageTransmit.authPost(ApiURL.CurrencyOperation, jsonObject.getString(MessageKey.Data), token);
                break;
            case MessageType.UPDATE_CURRENCY_OPERATION:
                CurrencyOperation currencyOperation=null;
                currencyOperation=objectMapper.readValue(msgData, CurrencyOperation.class);
                res = messageTransmit.authPut(ApiURL.CurrencyOperation, jsonObject.getString(MessageKey.Data), token,currencyOperation.getCurrencyOperationId());
                break;
            case MessageType.DELETE_CURRENCY_OPERATION:
                res = messageTransmit.authDelete(ApiURL.CurrencyOperation, jsonObject.getString(MessageKey.Data), token);

                break;
            //region CashPayment
            case MessageType.ADD_CASH_PAYMENT:
                res = messageTransmit.authPost(ApiURL.CashPayment, jsonObject.getString(MessageKey.Data), token);
                break;
            case MessageType.UPDATE_CASH_PAYMENT:
                CashPayment cashPayment=null;
                cashPayment=objectMapper.readValue(msgData, CashPayment.class);
                res = messageTransmit.authPut(ApiURL.CashPayment, jsonObject.getString(MessageKey.Data), token,cashPayment.getCashPaymentId());
                break;
            case MessageType.DELETE_CASH_PAYMENT:
                res = messageTransmit.authDelete(ApiURL.CashPayment, jsonObject.getString(MessageKey.Data), token);
                break;
            //endregion CashPayment


            //region Credit Card Payment
            case MessageType.ADD_CREDIT_CARD_PAYMENT:
                res = messageTransmit.authPost(ApiURL.CreditCardPayment, jsonObject.getString(MessageKey.Data), token);
                break;
            case MessageType.UPDATE_CREDIT_CARD_PAYMENT:
                CreditCardPayment creditCardPayment=null;
                creditCardPayment=objectMapper.readValue(msgData, CreditCardPayment.class);
                res = messageTransmit.authPut(ApiURL.CreditCardPayment, jsonObject.getString(MessageKey.Data), token,creditCardPayment.getCreditCardPaymentId());
                break;
            case MessageType.DELETE_CREDIT_CARD_PAYMENT:
                res = messageTransmit.authDelete(ApiURL.CreditCardPayment, jsonObject.getString(MessageKey.Data), token);
                break;
            //endregion Credit Card Payment

            //CUSTOMER_ASSISTANT
            case MessageType.ADD_CUSTOMER_ASSISTANT:
                res = messageTransmit.authPost(ApiURL.CustomerAssistant, jsonObject.getString(MessageKey.Data), token);
                break;
            case MessageType.UPDATE_CUSTOMER_ASSISTANT:
                CustomerAssistant customerAssistant=null;
                JSONObject newCustomerAssistantJson = new JSONObject(jsonObject.getString(MessageKey.Data));
                newCustomerAssistantJson.remove("createdAt");
                customerAssistant=objectMapper.readValue(newCustomerAssistantJson.toString(), CustomerAssistant.class);
                res = messageTransmit.authPut(ApiURL.CustomerAssistant, newCustomerAssistantJson.toString(), token,customerAssistant.getCustAssistantId());
                break;
            case MessageType.DELETE_CUSTOMER_ASSISTANT:
                JSONObject newDeleteCustomerAssistantJson = new JSONObject(jsonObject.getString(MessageKey.Data));
                res = messageTransmit.authDelete(ApiURL.CustomerAssistant, newDeleteCustomerAssistantJson.getString("custAssistantId"), token);
                break;

            //CustomerMeasurement
            case MessageType.ADD_CUSTOMER_MEASUREMENT:
                res = messageTransmit.authPost(ApiURL.CustomerMeasurement, jsonObject.getString(MessageKey.Data), token);
                break;
            case MessageType.UPDATE_CUSTOMER_MEASUREMENT:
                CustomerMeasurement customerMeasurement=null;
                JSONObject newCustomerMeasurementJson = new JSONObject(jsonObject.getString(MessageKey.Data));
                newCustomerMeasurementJson.remove("visitDate");
                customerMeasurement=objectMapper.readValue(newCustomerMeasurementJson.toString(), CustomerMeasurement.class);
                res = messageTransmit.authPut(ApiURL.CustomerMeasurement, newCustomerMeasurementJson.toString(), token,customerMeasurement.getCustomerMeasurementId());
                break;
            case MessageType.DELETE_CUSTOMER_MEASUREMENT:
                res = messageTransmit.authDelete(ApiURL.CustomerMeasurement, jsonObject.getString(MessageKey.Data), token);
                break;
            //End
            //MeasurementsDetails
            case MessageType.ADD_MEASUREMENTS_DETAILS:
                res = messageTransmit.authPost(ApiURL.MeasurementsDetails, jsonObject.getString(MessageKey.Data), token);
                break;
            case MessageType.UPDATE_MEASUREMENTS_DETAILS:
                MeasurementsDetails measurementsDetails=null;
                measurementsDetails=objectMapper.readValue(msgData, MeasurementsDetails.class);
                res = messageTransmit.authPut(ApiURL.MeasurementsDetails, jsonObject.getString(MessageKey.Data), token,measurementsDetails.getMeasurementsDetailsId());
                break;
            case MessageType.DELETE_MEASUREMENTS_DETAILS:
                res = messageTransmit.authDelete(ApiURL.MeasurementsDetails, jsonObject.getString(MessageKey.Data), token);
                break;
            //End
            //MeasurementDynamicVariable
            case MessageType.ADD_MEASUREMENTS_DYNAMIC_VARIABLE:
                res = messageTransmit.authPost(ApiURL.MeasurementDynamicVariable, jsonObject.getString(MessageKey.Data), token);
                break;
            case MessageType.UPDATE_MEASUREMENTS_DYNAMIC_VARIABLE:
                MeasurementDynamicVariable measurementDynamicVariable=null;
                measurementDynamicVariable=objectMapper.readValue(msgData, MeasurementDynamicVariable.class);
                res = messageTransmit.authPut(ApiURL.MeasurementDynamicVariable, jsonObject.getString(MessageKey.Data), token,measurementDynamicVariable.getMeasurementDynamicVariableId());
                break;
            case MessageType.DELETE_MEASUREMENTS_DYNAMIC_VARIABLE:
                res = messageTransmit.authDelete(ApiURL.MeasurementDynamicVariable, jsonObject.getString(MessageKey.Data), token);
                break;
            //End
            //ScheduleWorker
            case MessageType.ADD_SCHEDULE_WORKERS:
                res = messageTransmit.authPost(ApiURL.ScheduleWorker, jsonObject.getString(MessageKey.Data), token);
                break;
            case MessageType.UPDATE_SCHEDULE_WORKERS:
                ScheduleWorkers scheduleWorkers=null;
                JSONObject newScheduleWorkersJson = new JSONObject(jsonObject.getString(MessageKey.Data));
                newScheduleWorkersJson.remove("createdAt");
                scheduleWorkers=objectMapper.readValue(newScheduleWorkersJson.toString(), ScheduleWorkers.class);
                res = messageTransmit.authPut(ApiURL.ScheduleWorker,newScheduleWorkersJson.toString(), token,scheduleWorkers.getScheduleWorkersId());
                break;
            case MessageType.DELETE_SCHEDULE_WORKERS:
                res = messageTransmit.authDelete(ApiURL.ScheduleWorker, jsonObject.getString(MessageKey.Data), token);
                break;
            //End


        }
        Log.e("response message", res);

        try {
            if (res.toLowerCase().equals("false"))
                return false;
            else if (!res.toLowerCase().equals("true")) {
                JSONObject object = new JSONObject(res);
                int status = Integer.parseInt(object.get("status").toString());
                if (status == 200 || status == 201) {
                    Log.d("statusstrue",status+"");
                    return true;
                } else {
                    Log.d("statussfalse",status+"");

                    return false;
                }
            }
        } catch (JSONException e) {
            Log.e("Do sync", e.getMessage());
            return false;
        } catch (Exception ex) {
            Log.e("Do sync", ex.getMessage());
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
