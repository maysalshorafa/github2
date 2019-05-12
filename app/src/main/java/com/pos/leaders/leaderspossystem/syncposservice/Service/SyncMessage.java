package com.pos.leaders.leaderspossystem.syncposservice.Service;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.SQLException;
import android.database.sqlite.SQLiteConstraintException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.IBinder;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.CategoryDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.ChecksDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.CityDbAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.ClubAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.CreditCardPaymentDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.Currency.CashPaymentDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.Currency.CurrencyDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.Currency.CurrencyOperationDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.Currency.CurrencyTypeDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.CustomerAssetDB;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.CustomerDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.CustomerMeasurementAdapter.CustomerMeasurementDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.CustomerMeasurementAdapter.MeasurementDynamicVariableDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.CustomerMeasurementAdapter.MeasurementsDetailsDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.EmployeeDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.EmployeePermissionsDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.GroupDbAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.GroupsResourceDbAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.InventoryDbAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.OfferCategoryDbAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.OfferDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.OpiningReportDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.PaymentDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.PermissionsDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.ProductDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.ScheduleWorkersDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.SettingsDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.UsedPointDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.ZReportDBAdapter;
import com.pos.leaders.leaderspossystem.LogInActivity;
import com.pos.leaders.leaderspossystem.Models.Category;
import com.pos.leaders.leaderspossystem.Models.Check;
import com.pos.leaders.leaderspossystem.Models.City;
import com.pos.leaders.leaderspossystem.Models.Club;
import com.pos.leaders.leaderspossystem.Models.CreditCardPayment;
import com.pos.leaders.leaderspossystem.Models.Currency.CashPayment;
import com.pos.leaders.leaderspossystem.Models.Currency.Currency;
import com.pos.leaders.leaderspossystem.Models.Currency.CurrencyOperation;
import com.pos.leaders.leaderspossystem.Models.Currency.CurrencyReturns;
import com.pos.leaders.leaderspossystem.Models.Currency.CurrencyType;
import com.pos.leaders.leaderspossystem.Models.Customer;
import com.pos.leaders.leaderspossystem.Models.CustomerAssistant;
import com.pos.leaders.leaderspossystem.Models.CustomerMeasurement.CustomerMeasurement;
import com.pos.leaders.leaderspossystem.Models.CustomerMeasurement.MeasurementDynamicVariable;
import com.pos.leaders.leaderspossystem.Models.CustomerMeasurement.MeasurementsDetails;
import com.pos.leaders.leaderspossystem.Models.Employee;
import com.pos.leaders.leaderspossystem.Models.Inventory;
import com.pos.leaders.leaderspossystem.Models.Offer;
import com.pos.leaders.leaderspossystem.Models.OfferCategory;
import com.pos.leaders.leaderspossystem.Models.OpiningReport;
import com.pos.leaders.leaderspossystem.Models.OpiningReportDetails;
import com.pos.leaders.leaderspossystem.Models.Order;
import com.pos.leaders.leaderspossystem.Models.OrderDetails;
import com.pos.leaders.leaderspossystem.Models.Payment;
import com.pos.leaders.leaderspossystem.Models.Permission.EmployeesPermissions;
import com.pos.leaders.leaderspossystem.Models.Permission.Permissions;
import com.pos.leaders.leaderspossystem.Models.Product;
import com.pos.leaders.leaderspossystem.Models.ScheduleWorkers;
import com.pos.leaders.leaderspossystem.Models.SumPoint;
import com.pos.leaders.leaderspossystem.Models.UsedPoint;
import com.pos.leaders.leaderspossystem.Models.ValueOfPoint;
import com.pos.leaders.leaderspossystem.Models.XReport;
import com.pos.leaders.leaderspossystem.Models.ZReport;
import com.pos.leaders.leaderspossystem.Offers.ResourceType;
import com.pos.leaders.leaderspossystem.Offers.Rules;
import com.pos.leaders.leaderspossystem.R;
import com.pos.leaders.leaderspossystem.Tools.CONSTANT;
import com.pos.leaders.leaderspossystem.Tools.DateConverter;
import com.pos.leaders.leaderspossystem.Tools.SESSION;
import com.pos.leaders.leaderspossystem.syncposservice.DBHelper.Broker;
import com.pos.leaders.leaderspossystem.syncposservice.Enums.ApiURL;
import com.pos.leaders.leaderspossystem.syncposservice.Enums.MessageKey;
import com.pos.leaders.leaderspossystem.syncposservice.Enums.MessageResult;
import com.pos.leaders.leaderspossystem.syncposservice.Enums.MessageType;
import com.pos.leaders.leaderspossystem.syncposservice.MessageTransmit;
import com.pos.leaders.leaderspossystem.syncposservice.MessagesCreator;
import com.pos.leaders.leaderspossystem.syncposservice.Model.BrokerMessage;
import com.pos.leaders.leaderspossystem.syncposservice.SetupFragments.Token;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static android.content.Intent.ACTION_MAIN;

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
    private int NOTIFICATION_ID = 783;

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

        Intent notificationIntent = new Intent(getApplicationContext(), LogInActivity.class);

        notificationIntent.setAction(ACTION_MAIN);  // A string containing the action name
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent contentPendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

        Bitmap icon = BitmapFactory.decodeResource(getResources(), R.drawable.white_color_logo);

        Notification notification = new NotificationCompat.Builder(this)
                .setContentTitle(getResources().getString(R.string.app_name))
                .setTicker(getResources().getString(R.string.app_name))
                .setContentText(getResources().getString(R.string.server_info))
                .setSmallIcon(R.drawable.white_color_logo)
                .setLargeIcon(Bitmap.createScaledBitmap(icon, 128, 128, false))
                .setContentIntent(contentPendingIntent)
                .setOngoing(true)
//                .setDeleteIntent(contentPendingIntent)  // if needed
                .build();
        notification.flags = notification.flags | Notification.FLAG_NO_CLEAR;     // NO_CLEAR makes the notification stay when the user performs a "delete all" command
        startForeground(NOTIFICATION_ID, notification);

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
                        String currencyRes="";
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
                        //  Log.i("date", DateConverter.toDate(currency.getLastUpdate().getTime()));
                        //Log.i("date", DateConverter.toDate(timestamp.getTime()));
                        //if(!(DateConverter.toDate(currency.getLastUpdate().getTime()) == DateConverter.toDate(timestamp.getTime()))){

                        try {
                            updateCurrency();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        //}

                        try {
                            //getSync();
                            multiLineSync();
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

    private void multiLineSync() throws JSONException, IOException {
        List<Integer> AksFail = new ArrayList<>();
        String res = "";
        try {
            res = messageTransmit.authGet(ApiURL.Sync, token);
            Log.e("syncResponse",res);
        } catch (IOException e) {
            Log.e("getsync exception", e.getMessage(), e);
        }

        if (res.length() != 0) {
            if (!res.equals(MessageResult.Invalid)) {
                Log.w("getSync", res);
                JSONArray jsonArray = new JSONArray(res);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    try {
                        if (executeMessage(jsonObject)) {

                        } else {
                            AksFail.add(jsonObject.getInt(MessageKey.Ak));
                        }
                    } catch (SQLiteConstraintException e) {
                        Log.e("sync error", e.getMessage());
                        AksFail.add(jsonObject.getInt(MessageKey.Ak));

                    } catch (Exception ex) {
                        ex.printStackTrace();
                        AksFail.add(jsonObject.getInt(MessageKey.Ak));
                    }
                }
                String resp = messageTransmit.authPost(ApiURL.Sync + "/" + jsonArray.getJSONObject(0).getLong(MessageKey.TrackingId), MessagesCreator.ackTrackID(AksFail), token);
                Log.d("AkBody",AksFail.toString());
                Log.i(TAG, "getSync: " + resp);
                multiLineSync();
            } else if (res.equals(MessageResult.Invalid)) {
                Log.i(TAG, "there is no update incoming.");
            } else {
                //todo: error 401,404,403
                Log.i(TAG, "Can`t sync messages.");
            }
        }
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
            //ObjectMapper objectMapper = new ObjectMapper();
            ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
            objectMapper.setDateFormat(dateFormat);


            switch (msgType) {

                //region A REPORT
                case MessageType.ADD_OPINING_REPORT:

                    OpiningReport aReport = null;
                    aReport = objectMapper.readValue(msgData, OpiningReport.class);

                    OpiningReportDBAdapter aReportDBAdapter = new OpiningReportDBAdapter(this);
                    aReportDBAdapter.open();
                    rID = aReportDBAdapter.insertEntry(aReport);
                    aReportDBAdapter.close();
                    rID = 1;
                    break;
                case MessageType.UPDATE_OPINING_REPORT:
                    break;
                case MessageType.DELETE_OPINING_REPORT:
                    break;
                //endregion A REPORT
                //region A REPORT Details
                case MessageType.ADD_OPINING_REPORT_DETAILS:
                    /*
                    OpiningReportDetails aReportDetails = null;
                    aReportDetails = objectMapper.readValue(msgData, OpiningReportDetails.class);
                    AReportDetailsDBAdapter aReportDetailsDBAdapter = new AReportDetailsDBAdapter(this);
                    aReportDetailsDBAdapter.open();
                    rID = aReportDetailsDBAdapter.insertEntry(aReportDetails);
                    aReportDetailsDBAdapter.close();
*/
                    rID = 1;
                    break;
                case MessageType.UPDATE_OPINING_REPORT_DETAILS:
                    break;
                case MessageType.DELETE_OPINING_REPORT_DETAILS:
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

                //region Category
                case MessageType.ADD_CATEGORY:
                    Category category = null;
                    category = objectMapper.readValue(msgData, Category.class);

                    CategoryDBAdapter categoryDBAdapter = new CategoryDBAdapter(this);
                    categoryDBAdapter.open();
                    rID = categoryDBAdapter.insertEntry(category);
                    categoryDBAdapter.close();
                    break;
                case MessageType.UPDATE_CATEGORY:
                    Category updateCategory = null;
                    updateCategory = objectMapper.readValue(msgData, Category.class);
                    CategoryDBAdapter updateCategoryDBAdapter = new CategoryDBAdapter(this);
                    updateCategoryDBAdapter.open();
                    rID = updateCategoryDBAdapter.updateEntryBo(updateCategory);
                    updateCategoryDBAdapter.close();
                    break;
                case MessageType.DELETE_CATEGORY:
                    Category deleteCategory = null;
                    deleteCategory = objectMapper.readValue(msgData, Category.class);
                    CategoryDBAdapter deleteCategoryDBAdapter = new CategoryDBAdapter(this);
                    deleteCategoryDBAdapter.open();
                    rID = deleteCategoryDBAdapter.deleteEntryBo(deleteCategory);
                    deleteCategoryDBAdapter.close();
                    break;
                //endregion Category

                /**region CHECK
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
                 **/
                //region DEPARTMENT


                /**region CHECK
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
                 **/
                //region DEPARTMENT
                //region OFFER
                case MessageType.ADD_OFFER:
                    Offer offer = null;
                    offer = objectMapper.readValue(msgData, Offer.class);
                    OfferDBAdapter offerDBAdapter = new OfferDBAdapter(this);
                    offerDBAdapter.open();
                    rID = offerDBAdapter.insertEntry(offer);
                    offerDBAdapter.close();
                    if (offer.getResourceType() == ResourceType.MULTIPRODUCT) {
                        JSONArray skus = offer.getDataAsJsonObject().getJSONObject(Rules.RULES.getValue()).getJSONArray(Rules.product_sku.getValue());
                        if (skus.length() > 0) {
                            //creating new group
                            GroupDbAdapter groupDbAdapter = new GroupDbAdapter(this);
                            groupDbAdapter.open();
                            long groupId = groupDbAdapter.insertEntry(offer.getResourceId(), offer.getName());
                            groupDbAdapter.close();

                            //insert product to group
                            GroupsResourceDbAdapter groupsProductsDbAdapter = new GroupsResourceDbAdapter(this);
                            groupsProductsDbAdapter.open();
                            for(int i=0;i<skus.length();i++) {
                                groupsProductsDbAdapter.insertEntry(skus.getString(i), groupId);
                            }
                            groupsProductsDbAdapter.close();

                        }
                    }
                    if (offer.getResourceType() == ResourceType.CATEGORY) {
                        JSONArray categoryList = offer.getDataAsJsonObject().getJSONObject(Rules.RULES.getValue()).getJSONArray(Rules.categoryList.getValue());
                        if (categoryList.length() > 0) {
                            //creating new group
                            GroupDbAdapter groupDbAdapter = new GroupDbAdapter(this);
                            groupDbAdapter.open();
                            long groupId = groupDbAdapter.insertEntry(offer.getResourceId(), offer.getName());
                            groupDbAdapter.close();
                            //insert product to group
                            GroupsResourceDbAdapter groupsCategoryDbAdapter = new GroupsResourceDbAdapter(this);
                            groupsCategoryDbAdapter.open();
                            for(int i=0;i<categoryList.length();i++) {
                                groupsCategoryDbAdapter.insertEntry(categoryList.getString(i), groupId);
                            }
                            groupsCategoryDbAdapter.close();

                        }
                    }

                    break;
                case MessageType.UPDATE_OFFER:
                    Offer updateOffer = null;
                    updateOffer = objectMapper.readValue(msgData, Offer.class);
                    OfferDBAdapter updateOfferDBAdapter = new OfferDBAdapter(this);
                    updateOfferDBAdapter.open();
                    rID = updateOfferDBAdapter.updateEntryBo(updateOffer);
                    updateOfferDBAdapter.close();
                    break;
                case MessageType.DELETE_OFFER:
                    Offer deleteOffer = null;
                    deleteOffer = objectMapper.readValue(msgData, Offer.class);

                    OfferDBAdapter deleteOfferDBAdapter = new OfferDBAdapter(this);
                    deleteOfferDBAdapter.open();
                    try {
                        rID = deleteOfferDBAdapter.deleteEntryBo(deleteOffer);
                    } catch (Exception ex) {
                        rID = 0;
                        ex.printStackTrace();
                    }
                    deleteOfferDBAdapter.close();
                    break;
                //endregion OFFER

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
                    /**
                     Order order = null;
                     order = objectMapper.readValue(msgData, Order.class);
                     OrderDBAdapter orderDBAdapter = new OrderDBAdapter(this);
                     orderDBAdapter.open();
                     rID = orderDBAdapter.insertEntry(order);
                     orderDBAdapter.close();**/
                    rID=1;
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
                    /**   OrderDetails o;
                     o = objectMapper.readValue(msgData, OrderDetails.class);
                     OrderDetailsDBAdapter orderDetailsDBAdapter = new OrderDetailsDBAdapter(this);
                     orderDetailsDBAdapter.open();
                     rID = orderDetailsDBAdapter.insertEntry(o);
                     orderDetailsDBAdapter.close();**/
                    rID=1;
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
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
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
                    rID = 1;

                    break;
                case MessageType.UPDATE_Z_REPORT:
                    break;
                case MessageType.DELETE_Z_REPORT:
                    break;
                //endregion Z REPORT


                //region CurrencyReturns
                case MessageType.ADD_CURRENCY_RETURN:
                    /**    CurrencyReturns c = null;
                     c = objectMapper.readValue(msgData, CurrencyReturns.class);
                     CurrencyReturnsDBAdapter currencyReturnsDBAdapter = new CurrencyReturnsDBAdapter(this);
                     currencyReturnsDBAdapter.open();
                     rID = currencyReturnsDBAdapter.insertEntry(c);
                     currencyReturnsDBAdapter.close();**/
                    rID=1;

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
                case MessageType.UPDATE_COMPANY_CREDENTIALS:
                    SettingsDBAdapter settingsDBAdapter = new SettingsDBAdapter(this);
                    settingsDBAdapter.open();
                    JSONObject respnse = new JSONObject(msgData);
                    int i = settingsDBAdapter.updateEntry(respnse.getString(MessageKey.companyID), respnse.getString(MessageKey.companyName), SESSION.POS_ID_NUMBER + "",
                            (float) respnse.getDouble(MessageKey.tax), respnse.getString(MessageKey.returnNote), respnse.getInt(MessageKey.endOfReturnNote),
                            respnse.getString(MessageKey.CCUN), respnse.getString(MessageKey.CCPW),respnse.getInt(MessageKey.branchId));
                    settingsDBAdapter.close();
                    break;
                //end

                case MessageType.ADD_OFFER_CATEGORY:
                    OfferCategory offerCategory = null;
                    offerCategory = objectMapper.readValue(msgData, OfferCategory.class);
                    OfferCategoryDbAdapter offerCategoryDbAdapter = new OfferCategoryDbAdapter(this);
                    offerCategoryDbAdapter.open();
                    rID = offerCategoryDbAdapter.insertEntry(offerCategory);
                    offerCategoryDbAdapter.close();

                    break;
                case MessageType.UPDATE_OFFER_CATEGORY:
                    OfferCategory updateOfferCategory = null;
                    updateOfferCategory = objectMapper.readValue(msgData, OfferCategory.class);
                    OfferCategoryDbAdapter updateOfferCategoryDBAdapter = new OfferCategoryDbAdapter(this);
                    updateOfferCategoryDBAdapter.open();
                    rID = updateOfferCategoryDBAdapter.updateEntryBo(updateOfferCategory);
                    updateOfferCategoryDBAdapter.close();
                    break;
                case MessageType.DELETE_OFFER_CATEGORY:
                    OfferCategory deleteOfferategory = null;
                    deleteOfferategory = objectMapper.readValue(msgData, OfferCategory.class);

                    OfferCategoryDbAdapter deleteOfferCategoryDBAdapter = new OfferCategoryDbAdapter(this);
                    deleteOfferCategoryDBAdapter.open();
                    try {
                        rID = deleteOfferCategoryDBAdapter.deleteEntryBo(deleteOfferategory);
                    } catch (Exception ex) {
                        rID = 0;
                        ex.printStackTrace();
                    }
                    deleteOfferCategoryDBAdapter.close();
                    break;
                //region Inventory
                case MessageType.ADD_INVENTORY:
                    Inventory inventory = null;
                    inventory = objectMapper.readValue(msgData, Inventory.class);

                    InventoryDbAdapter inventoryDbAdapter = new InventoryDbAdapter(this);
                    inventoryDbAdapter.open();
                    rID = inventoryDbAdapter.insertEntry(inventory);
                    inventoryDbAdapter.close();
                    break;
                case MessageType.UPDATE_INVENTORY:
                    Inventory updateInventory = null;
                    updateInventory = objectMapper.readValue(msgData, Inventory.class);
                    InventoryDbAdapter updateInventoryDBAdapter = new InventoryDbAdapter(this);
                    updateInventoryDBAdapter.open();
                 //   rID = updateInventoryDBAdapter.updateEntryBo(updateInventory);
                    updateInventoryDBAdapter.close();
                    break;
                case MessageType.DELETE_INVENTORY:
                    Inventory deleteInventory = null;
                    deleteInventory = objectMapper.readValue(msgData, Inventory.class);
                    InventoryDbAdapter deleteInventoryDBAdapter = new InventoryDbAdapter(this);
                    deleteInventoryDBAdapter.open();
                    rID = deleteInventoryDBAdapter.deleteEntryBo(deleteInventory);
                    deleteInventoryDBAdapter.close();
                    break;
                //endregion Category
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
            case MessageType.ADD_OPINING_REPORT:
                res = messageTransmit.authPost(ApiURL.OpiningReport, jsonObject.getString(MessageKey.Data), token);
                break;
            case MessageType.UPDATE_OPINING_REPORT:
                OpiningReport aReport =null;
                aReport=objectMapper.readValue(msgData, OpiningReport.class);
                res = messageTransmit.authPut(ApiURL.OpiningReport, jsonObject.getString(MessageKey.Data), token,aReport.getOpiningReportId());
                break;
            case MessageType.DELETE_OPINING_REPORT:
                res = messageTransmit.authDelete(ApiURL.OpiningReport, jsonObject.getString(MessageKey.Data), token);
                break;
            //endregion A REPORT
            //region A REPORT Details
            case MessageType.ADD_OPINING_REPORT_DETAILS:
                res = messageTransmit.authPost(ApiURL.OpiningReportDetails, jsonObject.getString(MessageKey.Data), token);
                break;
            case MessageType.UPDATE_OPINING_REPORT_DETAILS:
                OpiningReportDetails aReportDetails =null;
                aReportDetails=objectMapper.readValue(msgData, OpiningReportDetails.class);
                res = messageTransmit.authPut(ApiURL.OpiningReportDetails, jsonObject.getString(MessageKey.Data), token,aReportDetails.getOpiningReportDetailsId());
                break;
            case MessageType.DELETE_OPINING_REPORT_DETAILS:
                res = messageTransmit.authDelete(ApiURL.OpiningReportDetails, jsonObject.getString(MessageKey.Data), token);
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

            //region Category
            case MessageType.ADD_CATEGORY:
                res = messageTransmit.authPost(ApiURL.Category, jsonObject.getString(MessageKey.Data), token);
                break;
            case MessageType.UPDATE_CATEGORY:
                Category category=null;
                JSONObject newCategoryJson= new JSONObject(jsonObject.getString(MessageKey.Data));
                newCategoryJson.remove("createdAt");
                category=objectMapper.readValue(newCategoryJson.toString(), Category.class);
                res = messageTransmit.authPut(ApiURL.Category, newCategoryJson.toString(), token,category.getCategoryId());
                break;
            case MessageType.DELETE_CATEGORY:
                JSONObject newDeleteCategoryJson = new JSONObject(jsonObject.getString(MessageKey.Data));
                res = messageTransmit.authDelete(ApiURL.Category, newDeleteCategoryJson.getString("categoryId"), token);
                break;
            //endregion Category

            //region OFFER
            case MessageType.ADD_OFFER:
                JSONObject newDJson = new JSONObject(jsonObject.getString(MessageKey.Data));
                JSONObject jsonObject1 = new JSONObject(newDJson.getString("offerData"));
                newDJson.remove("offerData");
                newDJson.put("offerData",jsonObject1);
                res = messageTransmit.authPost(ApiURL.Offer,newDJson.toString(), token);
                break;
            case MessageType.UPDATE_OFFER:
                Offer offer =null;
                JSONObject newOfferJson = new JSONObject(jsonObject.getString(MessageKey.Data));
                JSONObject jsonObject2 = new JSONObject(newOfferJson.getString("offerData"));
                newOfferJson.remove("offerData");
                newOfferJson.put("offerData",jsonObject2);
                Log.d("newOffer",newOfferJson.toString());
                //   offer=objectMapper.readValue(msgData, Offer.class);
                res = messageTransmit.authPut(ApiURL.Offer, newOfferJson.toString(), token,newOfferJson.getLong("offerId"));
                break;
            case MessageType.DELETE_OFFER:
                JSONObject newDeleteOfferJson = new JSONObject(jsonObject.getString(MessageKey.Data));
                res = messageTransmit.authDelete(ApiURL.Offer, newDeleteOfferJson.getString("offerId"), token);
                break;
            //endregion OFFER

            //region PAYMENT
            case MessageType.ADD_PAYMENT:
                JSONObject newJsonObject = new JSONObject(jsonObject.getString(MessageKey.Data));
                String paymentWay = newJsonObject.getString("paymentWay");
                long orderId = newJsonObject.getLong("orderId");
                List<CashPayment> cashPaymentList = new ArrayList<CashPayment>();
                List<Payment> paymentList = new ArrayList<Payment>();
                List<CreditCardPayment> creditCardPaymentList = new ArrayList<CreditCardPayment>();
                List<Check> checkList = new ArrayList<Check>();
                if(paymentWay.equalsIgnoreCase(CONSTANT.CASH)){
                    //get cash payment detail by order id
                    CashPaymentDBAdapter cashPaymentDBAdapter = new CashPaymentDBAdapter(getApplicationContext());
                    cashPaymentDBAdapter.open();
                    cashPaymentList = cashPaymentDBAdapter.getPaymentBySaleID(orderId);
                    JSONArray jsonArray = new JSONArray(cashPaymentList.toString());
                    newJsonObject.put("paymentDetails",jsonArray);
                }
                if(paymentWay.equalsIgnoreCase(CONSTANT.CREDIT_CARD)){
                    //get credit payment detail by order id
                    CreditCardPaymentDBAdapter creditCardPaymentDBAdapter = new CreditCardPaymentDBAdapter(getApplicationContext());
                    creditCardPaymentDBAdapter.open();
                    creditCardPaymentList = creditCardPaymentDBAdapter.getPaymentByOrderID(orderId);
                    JSONArray jsonArray = new JSONArray(creditCardPaymentList.toString());
                    newJsonObject.put("paymentDetails",jsonArray);
                }
                if(paymentWay.equalsIgnoreCase(CONSTANT.CHECKS)){
                    //get check payment detail by order id
                    ChecksDBAdapter checksDBAdapter = new ChecksDBAdapter(getApplicationContext());
                    checksDBAdapter.open();
                    checkList = checksDBAdapter.getPaymentBySaleID(orderId);
                    JSONArray jsonArray = new JSONArray(checkList.toString());
                    newJsonObject.put("paymentDetails",jsonArray);
                }
                Log.d("ppppppppp",newJsonObject.toString());
                res = messageTransmit.authPost(ApiURL.Payment, newJsonObject.toString(), token);

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
                JSONObject newOrderJson = new JSONObject(jsonObject.getString(MessageKey.Data));
                newOrderJson.remove("createdAt");
                order=objectMapper.readValue(newOrderJson.toString(), Order.class);
                res = messageTransmit.authPut(ApiURL.ORDER, newOrderJson.toString(), token,order.getOrderId());
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
            //region X REPORT
            case MessageType.ADD_X_REPORT:
                res = messageTransmit.authPost(ApiURL.XReport, jsonObject.getString(MessageKey.Data), token);
                break;
            case MessageType.UPDATE_X_REPORT:
                XReport xReport=null;
                xReport=objectMapper.readValue(msgData, XReport.class);
                res = messageTransmit.authPut(ApiURL.XReport, jsonObject.getString(MessageKey.Data), token,xReport.getxReportId());
                break;
            case MessageType.DELETE_X_REPORT:
                res = messageTransmit.authDelete(ApiURL.XReport, jsonObject.getString(MessageKey.Data), token);
                break;
            //endregion X REPORT
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
                Log.d("cccc22",customer.toString());
                Log.d("cccc",jsonObject.getString(MessageKey.Data));
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
                res = messageTransmit.authDelete(ApiURL.Employee, newDeleteUserJson.getString("employeeId"), token);
                break;
            //Currencies

            case MessageType.ADD_CURRENCY:
                res = messageTransmit.authPost(ApiURL.Currencies, jsonObject.getString(MessageKey.Data), token);
                break;
            case MessageType.UPDATE_CURRENCY:
                Currency currency=null;
                currency=objectMapper.readValue(msgData, Currency.class);
                res = messageTransmit.authPut(ApiURL.Currencies, jsonObject.getString(MessageKey.Data), token,currency.getId());
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
            //endregion Credit Card Payment**/

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
            case MessageType.ADD_CLOSING_REPORT:
                res = messageTransmit.authPost(ApiURL.ClosingReport, jsonObject.getString(MessageKey.Data), token);
                break;
            case MessageType.ADD_CLOSING_REPORT_DETAILS:
                Log.d("ClosingReportDetails",jsonObject.getString(MessageKey.Data));
                res = messageTransmit.authPost(ApiURL.ClosingReportDetails, jsonObject.getString(MessageKey.Data), token);
                break;

            case MessageType.ADD_OFFER_CATEGORY:
                res = messageTransmit.authPost(ApiURL.OfferCategory, jsonObject.getString(MessageKey.Data), token);
                break;
            case MessageType.UPDATE_OFFER_CATEGORY:
                OfferCategory offerCategory=null;
                JSONObject newOfferCategoryJson = new JSONObject(jsonObject.getString(MessageKey.Data));
                newOfferCategoryJson.remove("createdAt");
                offerCategory=objectMapper.readValue(newOfferCategoryJson.toString(), OfferCategory.class);
                res = messageTransmit.authPut(ApiURL.OfferCategory, newOfferCategoryJson.toString(), token,offerCategory.getOfferCategoryId());
                break;
            case MessageType.DELETE_OFFER_CATEGORY:
                JSONObject newDeleteOfferCategoryJson = new JSONObject(jsonObject.getString(MessageKey.Data));
                res = messageTransmit.authDelete(ApiURL.OfferCategory, newDeleteOfferCategoryJson.getString("offerCategoryId"), token);
                break;

            //endregion ClosingReport

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
    public void updateCurrency() throws JSONException, IOException {
        CurrencyTypeDBAdapter currencyTypeDBAdapter = new CurrencyTypeDBAdapter(this);
        currencyTypeDBAdapter.open();
        List<CurrencyType> currencyTypesList = currencyTypeDBAdapter.getAllCurrencyType();
        currencyTypeDBAdapter.close();
        CurrencyDBAdapter currencyDBAdapter =new CurrencyDBAdapter(this);
        currencyDBAdapter.open();
        Currency lastCurrency =currencyDBAdapter.getLastCurrency();
        Timestamp timestamp =new Timestamp(System.currentTimeMillis());
        if (DateConverter.toDate(lastCurrency.getLastUpdate().getTime()).equals(DateConverter.toDate(timestamp.getTime()))) {
            //do nothing
        }else {
            String currencyRes = messageTransmit.getCurrency(ApiURL.Currencies,SESSION.token);
            Log.i("Currency", currencyRes);
            ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
            objectMapper.setDateFormat(dateFormat);
            JSONObject jsonObject = null;
            Log.i("Currency", currencyRes);

            jsonObject = new JSONObject(currencyRes);
            try {
                String msgData = jsonObject.getString(MessageKey.responseBody);

                if (msgData.startsWith("[")) {
                    try {
                        JSONArray jsonArray = new JSONArray(msgData);

                        for (int i = 0; i < jsonArray.length() - 1; i++) {
                            msgData = jsonArray.getJSONObject(i).toString();
                            Currency currency = null;
                            currency = objectMapper.readValue(msgData, Currency.class);
                            currencyDBAdapter.insertEntry(currency);
                        }
                    } catch (Exception e) {
                    }

                }

            } catch (JSONException e) {

            }
            currencyDBAdapter.deleteOldRate(currencyTypesList);


        }
    }
}