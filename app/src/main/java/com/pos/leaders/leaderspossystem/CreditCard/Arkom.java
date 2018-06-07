package com.pos.leaders.leaderspossystem.CreditCard;

import android.os.StrictMode;
import android.util.Log;

import com.pos.leaders.leaderspossystem.Tools.SETTINGS;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.Locale;

/**
 * Created by KARAM on 08/12/2016.
 * Upgrading by KARAM on 11/04/2016.
 */

public class Arkom {
    static private final String NAMESPACE = "https://secure.arkom.co.il/";

    //For release mode
    static private final String URL = "https://cc.arkom.co.il/MTS_WebService.asmx";

    //For test mode
    //static private final String URL = "https://secure.arkom.co.il/wsdev/MTS_WebService.asmx";

    static private final String SOAP_ACTION_MTS_PING = "https://secure.arkom.co.il/MTS_Ping";
    static private final String METHOD_NAME_MTS_PING = "MTS_Ping";

    static private final String SOAP_ACTION_MTS_GetTransactionID = "https://secure.arkom.co.il/MTS_GetTransactionID";
    static private final String METHOD_NAME_MTS_GetTransactionID = "MTS_GetTransactionID";

    static private final String SOAP_ACTION_MTS_CC_Transaction = "https://secure.arkom.co.il/MTS_CC_Transaction";
    static private final String METHOD_NAME_MTS_CC_Transaction = "MTS_CC_Transaction";

    static private final String SOAP_ACTION_MTS_PutTransactionAcknowledge = "https://secure.arkom.co.il/MTS_PutTransactionAcknowledge";
    static private final String METHOD_NAME_MTS_PutTransactionAcknowledge = "MTS_PutTransactionAcknowledge";

    static private final String SOAP_ACTION_MTS_GetErrorDescription = "https://secure.arkom.co.il/MTS_GetErrorDescription";
    static private final String METHOD_NAME_MTS_GetErrorDescription = "MTS_GetErrorDescription";


    public Arkom()  {

    }

    static public String Ping(String terminalNum, String password) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);

        // Create the soap request object
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME_MTS_PING);

        // Set the property info for the to currency
        PropertyInfo termProp = new PropertyInfo();
        termProp.setName("TerminalNum");
        termProp.setValue(terminalNum);
        termProp.setType(String.class);
        request.addProperty(termProp);

        PropertyInfo passProp = new PropertyInfo();
        passProp.setName("Password");
        passProp.setValue(password);
        passProp.setType(String.class);
        request.addProperty(passProp);


        // Create the envelop.Envelop will be used to send the request
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.setOutputSoapObject(request);
        // Says that the soap webservice is a .Net service
        envelope.dotNet = true;

        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
        String s = "";
        try {
            androidHttpTransport.call(SOAP_ACTION_MTS_PING, envelope);

            SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
            // TextView v=(TextView) findViewById(R.id.currency);
            // v.setText("1" +fromUnit +"=" +response.toString()+toUnit);

            // Output to the log
            SoapObject soapObject = (SoapObject) envelope.bodyIn;
            s = soapObject.getProperty("MTS_PingResult").toString();

            Log.d("response ", soapObject.toString());
            return s;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return s.toString();
    }

    static public String GetTransactionID(String terminalNum, String password) {
        String TransactionID = "";
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);

        // Create the soap request object
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME_MTS_GetTransactionID);

        // Set the property info for the to currency
        PropertyInfo termProp = new PropertyInfo();
        termProp.setName("TerminalNum");
        termProp.setValue(terminalNum);
        termProp.setType(String.class);
        request.addProperty(termProp);

        PropertyInfo passProp = new PropertyInfo();
        passProp.setName("Password");
        passProp.setValue(password);
        passProp.setType(String.class);
        request.addProperty(passProp);

        PropertyInfo tranProp = new PropertyInfo();
        tranProp.setName("TransactionID");
        tranProp.setValue(TransactionID);
        tranProp.setType(String.class);
        request.addProperty(tranProp);


        // Create the envelop.Envelop will be used to send the request
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.setOutputSoapObject(request);
        // Says that the soap webservice is a .Net service
        envelope.dotNet = true;

        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
        String s = "";
        try {
            androidHttpTransport.call(SOAP_ACTION_MTS_GetTransactionID, envelope);

            SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
            // TextView v=(TextView) findViewById(R.id.currency);
            // v.setText("1" +fromUnit +"=" +response.toString()+toUnit);

            // Output to the log

            SoapObject soapObject = (SoapObject) envelope.bodyIn;
            //SoapFault soapFault=(SoapFault)envelope.bodyIn;
            Log.d("response re", soapObject.toString());

            TransactionID = soapObject.getProperty("TransactionID").toString();

            //TransactionID

        } catch (Exception e) {
            e.printStackTrace();
        }
        return TransactionID;
    }

    static public String GetTransactionResults(String TransactionID){
        return "";
    }

    static public String PutTransactionAcknowledge(String terminalNum, String password, String transactionID) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);

        // Create the soap request object
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME_MTS_PutTransactionAcknowledge);

        // Set the property info for the to currency
        PropertyInfo termProp = new PropertyInfo();
        termProp.setName("TerminalNum");
        termProp.setValue(terminalNum);
        termProp.setType(String.class);
        request.addProperty(termProp);

        PropertyInfo passProp = new PropertyInfo();
        passProp.setName("Password");
        passProp.setValue(password);
        passProp.setType(String.class);
        request.addProperty(passProp);

        PropertyInfo transProp = new PropertyInfo();
        transProp.setName("TransactionID");
        transProp.setValue(transactionID);
        transProp.setType(String.class);
        request.addProperty(transProp);


        // Create the envelop.Envelop will be used to send the request
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.setOutputSoapObject(request);
        // Says that the soap webservice is a .Net service
        envelope.dotNet = true;

        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
        String s = "";
        try {
            androidHttpTransport.call(SOAP_ACTION_MTS_PutTransactionAcknowledge, envelope);

            SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
            // TextView v=(TextView) findViewById(R.id.currency);
            // v.setText("1" +fromUnit +"=" +response.toString()+toUnit);

            // Output to the log
            SoapObject soapObject = (SoapObject) envelope.bodyIn;
            s = soapObject.getProperty("MTS_PutTransactionAcknowledgeResult").toString();
            Log.d("response ", soapObject.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return s;
    }

    static public String GetErrorDescription(String terminalNum,String password,String transactionID,String errorCode){
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);

        // Create the soap request object
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME_MTS_GetErrorDescription);

        // Set the property info for the to currency
        request.addProperty("TerminalNUM", terminalNum);
        request.addProperty("Password", password);
        request.addProperty("TransactionID", transactionID);
        request.addProperty("ErrorCode", errorCode);


        request.addProperty("ErrorDesc","");


        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.setOutputSoapObject(request);
        // Says that the soap webservice is a .Net service
        envelope.dotNet = true;
        envelope.encodingStyle = SoapSerializationEnvelope.XSD;

        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
        try {
            androidHttpTransport.call(SOAP_ACTION_MTS_GetErrorDescription, envelope);

            SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
            // TextView v=(TextView) findViewById(R.id.currency);
            // v.setText("1" +fromUnit +"=" +response.toString()+toUnit);

            // Output to the log

            SoapObject soapObject = (SoapObject) envelope.bodyIn;
            //SoapFault soapFault=(SoapFault)envelope.bodyIn;



            Log.d("ErrorDesc ", soapObject.getProperty("ErrorDesc").toString());
            return  soapObject.getProperty("ErrorDesc").toString();
            //TransactionID

        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    static public SoapObject PassCard(String cardNumber,int numberOfPay,double sumPrice,String ApprovalCode,int CreditType){
        String TransactionID=GetTransactionID(SETTINGS.ccNumber,SETTINGS.ccPassword);
        String transactionAcknowledge = "";

       /* String CreditCardNumber="4557440410014606",CardExpiry = "1216",Last4Digits="4606",CVV2="444",ID="890109629",ISO_Currency="ILS",ApprovalCode="",TransRef="",Z_Prm="",Q_Prm="",R_Prm="";
                            double TransSum=200.990d,FirstPayment=200.99d,TransPoints =0,FixedPayment=0;
                            int TransCurrency=1,CreditType=2,NumOfFixedPayments=0,J_Prm=0;
                            Arkom.CCTransaction(SETTINGS.ccNumber,SETTINGS.ccPassword,transID,CreditCardNumber,CardExpiry,TransSum,
                                    TransPoints,Last4Digits,CVV2,ID,TransCurrency,ISO_Currency,CreditType,ApprovalCode,FirstPayment,
                                    FixedPayment,NumOfFixedPayments,TransRef,J_Prm,Z_Prm,Q_Prm,R_Prm);*/

        double totalPrice = Double.parseDouble(String.format(Locale.ENGLISH, "%.2f", sumPrice));
        double fixedPayment = Double.parseDouble(String.format(Locale.ENGLISH, "%.2f", totalPrice / numberOfPay));
        double firstPayment = Double.parseDouble(String.format(Locale.ENGLISH, "%.2f", totalPrice - (fixedPayment * (numberOfPay - 1))));

        try{
                SoapObject soap=CCTransaction(SETTINGS.ccNumber,SETTINGS.ccPassword,TransactionID,cardNumber,"",totalPrice,
                0,"","","",1,"ILS",CreditType,ApprovalCode,firstPayment,fixedPayment,numberOfPay,"",0,"","","");

            transactionAcknowledge=PutTransactionAcknowledge(SETTINGS.ccNumber,SETTINGS.ccPassword,TransactionID);
            soap.addProperty("TransactionID", TransactionID);
        return  soap;}
        catch (Exception ex){
          //  Log.e("error",ex.getMessage());
        }
        return null;
    }

    static public SoapObject CCTransaction(String terminalNum, String password, String transactionID, String cardNum, String cardExpiry,
                                       double transSum, double transPoints, String last4Digits, String CVV2, String ID, int transCurrency,
                                       String ISO_Currenc, int creditType, String approvalCode, double firstPayment, double fixedPayment,
                                       int numOfFixedPayments, String transRef, int J_Prm, String Z_Prm, String Q_Prm, String A_Prm) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);

        // Create the soap request object
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME_MTS_CC_Transaction);

        // Set the property info for the to currency
        request.addProperty("TerminalNUM", terminalNum);
        request.addProperty("Password", password);
        request.addProperty("TransactionID", transactionID);
        request.addProperty("CardNum", cardNum);
        request.addProperty("CardExpiry", cardExpiry);
        request.addProperty("TransSum", String.format("%.2f", transSum));
        /*
        PropertyInfo TransSumProp = new PropertyInfo();
        TransSumProp.setName("TransSum");
        TransSumProp.setValue(transSum);
        TransSumProp.setType(Integer.class);
        request.addProperty(TransSumProp);
        */
        request.addProperty("TransPoints", String.format("%.2f", transPoints));
        request.addProperty("Last4Digits", last4Digits);
        request.addProperty("CVV2", CVV2);
        request.addProperty("ID", ID);
        request.addProperty("TransCurrency", transCurrency);
        request.addProperty("ISO_Currency", ISO_Currenc);
        request.addProperty("CreditType", creditType);
        request.addProperty("ApprovalCode", approvalCode);
        request.addProperty("FirstPayment", String.format("%.2f", firstPayment));
        request.addProperty("FixedPayment", String.format("%.2f", fixedPayment));
        request.addProperty("NumOfFixedPayments", numOfFixedPayments);
        request.addProperty("TransRef", transRef);
        request.addProperty("J_Prm", J_Prm);
        request.addProperty("Z_Prm", Z_Prm);
        request.addProperty("Q_Prm", Q_Prm);
        request.addProperty("A_Prm", A_Prm);


        request.addProperty("Answer", "");
        request.addProperty("MerchantNote", "");
        request.addProperty("ClientNote", "");


// Create the envelop.Envelop will be used to send the request
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.setOutputSoapObject(request);
        // Says that the soap webservice is a .Net service
        envelope.dotNet = true;
       // envelope.encodingStyle = SoapSerializationEnvelope.XSD;
        String result="";
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
        try {
            androidHttpTransport.call(SOAP_ACTION_MTS_CC_Transaction, envelope);

            SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
            // TextView v=(TextView) findViewById(R.id.currency);
            // v.setText("1" +fromUnit +"=" +response.toString()+toUnit);

            // Output to the log

            SoapObject soapObject = (SoapObject) envelope.bodyIn;
            //SoapFault soapFault=(SoapFault)envelope.bodyIn;


            //TransactionID = soapObject.getProperty("TransactionID").toString();
            Log.d("CC_Transaction ", soapObject.toString());

            return soapObject;
            //TransactionID

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String remotereq(String to_curr, String from_curr, String value) {

        // Strict mode is defined because executing network operations in the
        // main
        // thread will give exception
        // Strict mode is available only above version 9
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);

        // Create the soap request object
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME_MTS_PING);

        // Convert from
        String fromUnit = from_curr;
        // Convert to
        String toUnit = to_curr;
        if (value == null) {
            value = "1";
        }

        // Set the property info for the to currency
        PropertyInfo FromCurrency = new PropertyInfo();
        FromCurrency.setName("FromCurrency");
        FromCurrency.setValue(fromUnit);
        FromCurrency.setType(String.class);
        request.addProperty(FromCurrency);

        PropertyInfo ToCurrency = new PropertyInfo();
        ToCurrency.setName("ToCurrency");
        ToCurrency.setValue(toUnit);
        ToCurrency.setType(String.class);
        request.addProperty(ToCurrency);

        // Create the envelop.Envelop will be used to send the request
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.setOutputSoapObject(request);
        // Says that the soap webservice is a .Net service
        envelope.dotNet = true;

        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
        String s = "";
        Double x = 1.0;
        try {
            androidHttpTransport.call(SOAP_ACTION_MTS_PING, envelope);

            SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
            // TextView v=(TextView) findViewById(R.id.currency);
            // v.setText("1" +fromUnit +"=" +response.toString()+toUnit);

            // Output to the log
            s = response.toString();
            x = Double.parseDouble(s) * Double.parseDouble(value);
            Log.d("Converter", response.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return x.toString();

    }




                                            /*
                                              String CreditCardNumber="4557440410014606",CardExpiry = "1217",Last4Digits="4606",CVV2="444",ID="890109629",ISO_Currency="ILS",ApprovalCode="",TransRef="",Z_Prm="",Q_Prm="",R_Prm="";
                                double TransSum=200.990d,FirstPayment=200.99d,TransPoints =0,FixedPayment=0;
                                int TransCurrency=1,CreditType=2,NumOfFixedPayments=0,J_Prm=0;
                                            SoapObject soap=Arkom.CCTransaction(SETTINGS.ccNumber,SETTINGS.ccPassword,TransactionID,CreditCardNumber,CardExpiry,TransSum,
                                        TransPoints,Last4Digits,CVV2,ID,TransCurrency,ISO_Currency,CreditType,ApprovalCode,FirstPayment,
                                        FixedPayment,NumOfFixedPayments,TransRef,J_Prm,Z_Prm,Q_Prm,R_Prm);
*/

    public static SoapObject ByPhone(String creditCardNumber, String cardExpiry, double transSum, String cvv2, String idNumber, String approvalCode, int numOfFixedPayments,int CreditType) {
        String TransactionID = GetTransactionID(SETTINGS.ccNumber, SETTINGS.ccPassword);
        String last4Digits = creditCardNumber.substring(creditCardNumber.length() - 4, creditCardNumber.length());
        double fixedPayment=transSum/numOfFixedPayments;
        try {
            SoapObject soapObject = Arkom.CCTransaction(SETTINGS.ccNumber, SETTINGS.ccPassword, TransactionID,
                    creditCardNumber, cardExpiry, transSum, 0, last4Digits, cvv2,
                    idNumber, 1, "ILS", CreditType, approvalCode, fixedPayment,
                    fixedPayment, numOfFixedPayments, "", 0, "", "", "");
            PutTransactionAcknowledge(SETTINGS.ccNumber, SETTINGS.ccPassword, TransactionID);
            return soapObject;
        }
        catch (Exception ex){
            return null;
        }
    }
}
