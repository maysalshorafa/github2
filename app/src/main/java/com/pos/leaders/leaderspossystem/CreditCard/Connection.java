//package com.pos.leaders.leaderspossystem.CreditCard;
//
//import android.app.ProgressDialog;
//import android.content.Context;
//import android.os.AsyncTask;
//import android.widget.Toast;
//
//import org.ksoap2.SoapEnvelope;
//import org.ksoap2.serialization.SoapObject;
//import org.ksoap2.serialization.SoapSerializationEnvelope;
//import org.ksoap2.transport.HttpTransportSE;
//
//
///**
// * Created by KARAM on 05/12/2016.
// */
//
//
//
//class Connection extends AsyncTask<Void, Void, Void> {
//    Context context;
//    String list[];
//    boolean exc=false;
//    public Connection(Context context) {
//        this.context=context;
//    }
//
//    private final ProgressDialog dialog = new ProgressDialog(this.context);
//
//    private final String SOAP_ACTION = "http://tempuri.org/GetDistrict";
//    private final String METHOD_NAME = "GetDistrict";
//    private final String NAMESPACE = "http://tempuri.org/";
//    private final String URL = "http://192.168.1.5/webapi/test1";
//
//    @Override
//    protected void onPreExecute() {
//        this.dialog.setMessage("Loading data");
//        this.dialog.show();
//    }
//
//    @Override
//    protected Void doInBackground(Void... unused) {
//
//        SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
//        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
//        envelope.dotNet = true;
//        envelope.setOutputSoapObject(Request);
//        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
//
//        try {
//            androidHttpTransport.call(SOAP_ACTION, envelope);
//            SoapObject response = (SoapObject) envelope.getResponse();
//
//            System.out.println("response" + response);
//            int intPropertyCount = response.getPropertyCount();
//            list = new String[intPropertyCount];
//
//            for (int i = 0; i < intPropertyCount; i++) {
//                //list[i] = response.getPropertyAsString(i).toString();
//            }
//        } catch (Exception e) {
//            exc = true;
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//    @Override
//    protected void onPostExecute(Void result) {
//
//        if (this.dialog.isShowing()) {
//            this.dialog.dismiss();
//        }
//        if (exc) {
//            Toast.makeText(context, "Error", Toast.LENGTH_LONG).show();
//        } else {
//            //spinner();
//            exc = false;
//        }
//    }
//}