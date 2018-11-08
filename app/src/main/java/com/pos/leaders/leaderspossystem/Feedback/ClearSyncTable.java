package com.pos.leaders.leaderspossystem.Feedback;

import android.util.Log;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pos.leaders.leaderspossystem.Models.OpiningReport;
import com.pos.leaders.leaderspossystem.Models.OpiningReportDetails;
import com.pos.leaders.leaderspossystem.Models.Check;
import com.pos.leaders.leaderspossystem.syncposservice.Enums.ApiURL;
import com.pos.leaders.leaderspossystem.syncposservice.Enums.MessageKey;
import com.pos.leaders.leaderspossystem.syncposservice.Enums.MessageType;
import com.pos.leaders.leaderspossystem.syncposservice.MessageTransmit;
import com.pos.leaders.leaderspossystem.syncposservice.Model.BrokerMessage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class ClearSyncTable {


    public static boolean doSyncV1(BrokerMessage bm, MessageTransmit messageTransmit,String token) throws JSONException, IOException {
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
                res = messageTransmit.authPost(ApiURL.OpiningReport, msgData, token);
                break;
            case MessageType.UPDATE_OPINING_REPORT:
                OpiningReport aReport = null;
                aReport = objectMapper.readValue(msgData, OpiningReport.class);
                res = messageTransmit.authPut(ApiURL.OpiningReport, msgData, token, aReport.getOpiningReportId());
                break;
            case MessageType.DELETE_OPINING_REPORT:
                res = messageTransmit.authDelete(ApiURL.OpiningReport, msgData, token);
                break;
            //endregion A REPORT
            //region A REPORT Details
            case MessageType.ADD_OPINING_REPORT_DETAILS:
                res = messageTransmit.authPost(ApiURL.OpiningReportDetails, msgData, token);
                break;
            case MessageType.UPDATE_OPINING_REPORT_DETAILS:
                OpiningReportDetails aReportDetails = null;
                aReportDetails = objectMapper.readValue(msgData, OpiningReportDetails.class);
                res = messageTransmit.authPut(ApiURL.OpiningReportDetails, msgData, token, aReportDetails.getOpiningReportDetailsId());
                break;
            case MessageType.DELETE_OPINING_REPORT_DETAILS:
                res = messageTransmit.authDelete(ApiURL.OpiningReportDetails, msgData, token);
                break;
            //endregion A REPORT Details

            //region CHECK
            case MessageType.ADD_CHECK:
                res = messageTransmit.authPost(ApiURL.Check, msgData, token);
                break;
            case MessageType.UPDATE_CHECK:
                Check check = null;
                check = objectMapper.readValue(msgData, Check.class);
                res = messageTransmit.authPut(ApiURL.Check, msgData, token, check.getCheckId());
                break;
            case MessageType.DELETE_CHECK:
                res = messageTransmit.authDelete(ApiURL.Check, msgData, token);
                break;
            //endregion CHECK

            //region DEPARTMENT
            case "AddDepartment":
                res = messageTransmit.authPost("Department", msgData, token);
                break;
            case "UpdateDepartment":
                JSONObject newDepartmentJson = new JSONObject(msgData);
                res = messageTransmit.authPut("Department", msgData, token, newDepartmentJson.getLong("departmentId"));
                break;
            case "DeleteDepartment":
                JSONObject newDeleteDepartmentJson = new JSONObject(msgData);
                res = messageTransmit.authDelete("Department", newDeleteDepartmentJson.getString("departmentId"), token);
                break;
            //endregion DEPARTMENT

            //region OFFER
            case MessageType.ADD_OFFER:
                res = messageTransmit.authPost(ApiURL.Offer, msgData, token);
                break;
            case MessageType.UPDATE_OFFER:
                res = messageTransmit.authPut(ApiURL.Offer, msgData, token, new JSONObject(msgData).getLong("id"));
                break;
            case MessageType.DELETE_OFFER:
                res = messageTransmit.authDelete(ApiURL.Offer, msgData, token);
                break;
            //endregion OFFER

            //region PAYMENT
            case MessageType.ADD_PAYMENT:
                res = messageTransmit.authPost(ApiURL.Payment, msgData, token);
                break;
            case MessageType.UPDATE_PAYMENT:
                res = messageTransmit.authPut(ApiURL.Payment, msgData, token, new JSONObject(msgData).getLong("paymentId"));
                break;
            case MessageType.DELETE_PAYMENT:
                res = messageTransmit.authDelete(ApiURL.Payment, msgData, token);
                break;
            //endregion PAYMENT

            //region PERMISSION
            case MessageType.ADD_PERMISSION:
                res = messageTransmit.authPost(ApiURL.Permission, msgData, token);
                break;
            case MessageType.UPDATE_PERMISSION:
                res = messageTransmit.authPut(ApiURL.Permission, msgData, token, new JSONObject(msgData).getLong("id"));
                break;
            case MessageType.DELETE_PERMISSION:
                res = messageTransmit.authDelete(ApiURL.Permission, msgData, token);
                break;
            //endregion PERMISSION

            //region UserPermissions
            case MessageType.ADD_EMPLOYEE_PERMISSION:
                res = messageTransmit.authPost(ApiURL.EMPLOYEE_PERMISSION, msgData, token);
                break;
            case MessageType.UPDATE_EMPLOYEE_PERMISSION:
                res = messageTransmit.authPut(ApiURL.EMPLOYEE_PERMISSION, msgData, token, new JSONObject(msgData).getLong("employeePermissionId"));
                break;
            case MessageType.DELETE_EMPLOYEE_PERMISSION:
                res = messageTransmit.authDelete(ApiURL.EMPLOYEE_PERMISSION, msgData, token);
                break;

            //End region

            //region ORDER
            case MessageType.ADD_ORDER:
                res = messageTransmit.authPost(ApiURL.ORDER, msgData, token);
                break;
            case MessageType.UPDATE_ORDER:
                res = messageTransmit.authPut(ApiURL.ORDER, msgData, token, new JSONObject(msgData).getLong("orderId"));
                break;
            case MessageType.DELETE_ORDER:
                JSONObject newDeleteOrderJson = new JSONObject(msgData);
                res = messageTransmit.authDelete(ApiURL.ORDER, newDeleteOrderJson.getString("orderId"), token);
                break;
            //endregion Order

            //region Z REPORT
            case MessageType.ADD_Z_REPORT:
                res = messageTransmit.authPost(ApiURL.ZReport, msgData, token);
                break;
            case MessageType.UPDATE_Z_REPORT:
                res = messageTransmit.authPut(ApiURL.ZReport, msgData, token, new JSONObject(msgData).getLong("zReportId"));
                break;
            case MessageType.DELETE_Z_REPORT:
                res = messageTransmit.authDelete(ApiURL.ZReport, msgData, token);
                break;
            //endregion Z REPORT

            case MessageType.ADD_CLUB:
                res = messageTransmit.authPost(ApiURL.Club, msgData, token);
                break;
            case MessageType.UPDATE_CLUB:
                res = messageTransmit.authPut(ApiURL.Club, msgData, token, new JSONObject(msgData).getLong("clubId"));
                break;
            case MessageType.DELETE_CLUB:
                JSONObject newDeleteClubJson = new JSONObject(msgData);
                res = messageTransmit.authDelete(ApiURL.Club, newDeleteClubJson.getString("clubId"), token);
                break;
            // Sum Point Region
            case MessageType.ADD_SUM_POINT:
                res = messageTransmit.authPost(ApiURL.SumPoint, msgData, token);
                break;
            case MessageType.UPDATE_SUM_POINT:
                res = messageTransmit.authPut(ApiURL.SumPoint, msgData, token, new JSONObject(msgData).getLong("sumPointId"));
                break;
            case MessageType.DELETE_SUM_POINT:
                res = messageTransmit.authDelete(ApiURL.SumPoint, msgData, token);
                break;
            //End Sum Point Region
            // Value Of Point Region
            case MessageType.ADD_VALUE_OF_POINT:
                res = messageTransmit.authPost(ApiURL.ValueOfPoint, msgData, token);
                break;
            case MessageType.UPDATE_VALUE_OF_POINT:
                res = messageTransmit.authPut(ApiURL.ValueOfPoint, msgData, token, new JSONObject(msgData).getLong("valueOfPointId"));
                break;
            case MessageType.DELETE_VALUE_OF_POINT:
                res = messageTransmit.authDelete(ApiURL.ValueOfPoint, msgData, token);
                break;
            //End Value Of Point Region
            //
            // UsedPoint Region
            case MessageType.ADD_USED_POINT:
                res = messageTransmit.authPost(ApiURL.UsedPoint, msgData, token);
                break;
            case MessageType.UPDATE_USED_POINT:
                res = messageTransmit.authPut(ApiURL.UsedPoint, msgData, token, new JSONObject(msgData).getLong("usedPointId"));
                break;
            case MessageType.DELETE_USED_POINT:
                res = messageTransmit.authDelete(ApiURL.UsedPoint, msgData, token);
                break;
            //End Value Of Point Region
            //City Region
            case MessageType.ADD_CITY:
                res = messageTransmit.authPost(ApiURL.City, msgData, token);
                break;
            case MessageType.UPDATE_CITY:
                res = messageTransmit.authPut(ApiURL.City, msgData, token, new JSONObject(msgData).getLong("cityId"));
                break;
            case MessageType.DELETE_CITY:
                res = messageTransmit.authDelete(ApiURL.City, msgData, token);
                break;


            case MessageType.ADD_CUSTOMER:
                res = messageTransmit.authPost(ApiURL.Customer, msgData, token);
                break;
            case MessageType.UPDATE_CUSTOMER:
                res = messageTransmit.authPut(ApiURL.Customer, msgData, token, new JSONObject(msgData).getLong("customerId"));
                break;
            case MessageType.DELETE_CUSTOMER:
                JSONObject newDeleteCustomerJson = new JSONObject(msgData);
                res = messageTransmit.authDelete(ApiURL.Customer, newDeleteCustomerJson.getString("customerId"), token);
                break;


            case MessageType.ADD_ORDER_DETAILS:
                res = messageTransmit.authPost(ApiURL.ORDER_DETAILS, msgData, token);
                break;
            case MessageType.UPDATE_ORDER_DETAILS:
                res = messageTransmit.authPut(ApiURL.ORDER_DETAILS, msgData, token, new JSONObject(msgData).getLong("orderDetailsId"));
                break;
            case MessageType.DELETE_ORDER_DETAILS:
                res = messageTransmit.authDelete(ApiURL.ORDER_DETAILS, msgData, token);
                break;


            case MessageType.ADD_PRODUCT:
                res = messageTransmit.authPost(ApiURL.Product, msgData, token);
                break;
            case MessageType.UPDATE_PRODUCT:
                res = messageTransmit.authPut(ApiURL.Product, msgData, token, new JSONObject(msgData).getLong("productId"));
                break;
            case MessageType.DELETE_PRODUCT:
                JSONObject newDeleteProductJson = new JSONObject(msgData);
                res = messageTransmit.authDelete(ApiURL.Product, newDeleteProductJson.getString("productId"), token);
                break;


            case MessageType.ADD_EMPLOYEE:
                res = messageTransmit.authPost(ApiURL.Employee, msgData, token);
                break;
            case MessageType.UPDATE_EMPLOYEE:
                res = messageTransmit.authPut(ApiURL.Employee, msgData, token, new JSONObject(msgData).getLong("employeeId"));
                break;
            case MessageType.DELETE_EMPLOYEE:
                JSONObject newDeleteUserJson = new JSONObject(msgData);
                res = messageTransmit.authDelete(ApiURL.Employee, newDeleteUserJson.getString("employeeId"), token);
                break;
            //Currencies

            case MessageType.ADD_CURRENCY:
                res = messageTransmit.authPost(ApiURL.Currencies, msgData, token);
                break;
            case MessageType.UPDATE_CURRENCY:
                res = messageTransmit.authPut(ApiURL.Currencies, msgData, token, new JSONObject(msgData).getLong("currencyId"));
                break;
            case MessageType.DELETE_CURRENCY:
                res = messageTransmit.authDelete(ApiURL.Currencies, msgData, token);
                break;
            //Currencies_Type

            case MessageType.ADD_CURRENCY_TYPE:
                res = messageTransmit.authPost(ApiURL.CurrencyType, msgData, token);
                break;
            case MessageType.UPDATE_CURRENCY_TYPE:
                res = messageTransmit.authPut(ApiURL.CurrencyType, msgData, token, new JSONObject(msgData).getLong("currencyTypeId"));
                break;
            case MessageType.DELETE_CURRENCY_TYPE:
                res = messageTransmit.authDelete(ApiURL.CurrencyType, msgData, token);
                break;

            //CurrencyReturn

            case MessageType.ADD_CURRENCY_RETURN:
                res = messageTransmit.authPost(ApiURL.CurrencyReturn, msgData, token);
                break;
            case MessageType.UPDATE_CURRENCY_RETURN:
                res = messageTransmit.authPut(ApiURL.CurrencyReturn, msgData, token, -1L);
                break;
            case MessageType.DELETE_CURRENCY_RETURN:
                res = messageTransmit.authDelete(ApiURL.CurrencyReturn, msgData, token);
                break;

            //CurrencyOPeration
            case MessageType.ADD_CURRENCY_OPERATION:
                res = messageTransmit.authPost(ApiURL.CurrencyOperation, msgData, token);
                break;
            case MessageType.UPDATE_CURRENCY_OPERATION:
                res = messageTransmit.authPut(ApiURL.CurrencyOperation, msgData, token, new JSONObject(msgData).getLong("currencyOperationId"));
                break;
            case MessageType.DELETE_CURRENCY_OPERATION:
                res = messageTransmit.authDelete(ApiURL.CurrencyOperation, msgData, token);

                break;
            //region CashPayment
            case MessageType.ADD_CASH_PAYMENT:
                res = messageTransmit.authPost(ApiURL.CashPayment, msgData, token);
                break;
            case MessageType.UPDATE_CASH_PAYMENT:
                res = messageTransmit.authPut(ApiURL.CashPayment, msgData, token, new JSONObject(msgData).getLong("cashPaymentId"));
                break;
            case MessageType.DELETE_CASH_PAYMENT:
                res = messageTransmit.authDelete(ApiURL.CashPayment, msgData, token);
                break;
            //endregion CashPayment


            //region Credit Card Payment
            case MessageType.ADD_CREDIT_CARD_PAYMENT:
                res = messageTransmit.authPost(ApiURL.CreditCardPayment, msgData, token);
                break;
            case MessageType.UPDATE_CREDIT_CARD_PAYMENT:
                res = messageTransmit.authPut(ApiURL.CreditCardPayment, msgData, token, new JSONObject(msgData).getLong("creditCardPaymentId"));
                break;
            case MessageType.DELETE_CREDIT_CARD_PAYMENT:
                res = messageTransmit.authDelete(ApiURL.CreditCardPayment, msgData, token);
                break;
            //endregion Credit Card Payment

            //CUSTOMER_ASSISTANT
            case MessageType.ADD_CUSTOMER_ASSISTANT:
                res = messageTransmit.authPost(ApiURL.CustomerAssistant, msgData, token);
                break;
            case MessageType.UPDATE_CUSTOMER_ASSISTANT:
                JSONObject newCustomerAssistantJson = new JSONObject(msgData);
                newCustomerAssistantJson.remove("createdAt");
                res = messageTransmit.authPut(ApiURL.CustomerAssistant, newCustomerAssistantJson.toString(), token, new JSONObject(msgData).getLong("custAssistantId"));
                break;
            case MessageType.DELETE_CUSTOMER_ASSISTANT:
                JSONObject newDeleteCustomerAssistantJson = new JSONObject(msgData);
                res = messageTransmit.authDelete(ApiURL.CustomerAssistant, newDeleteCustomerAssistantJson.getString("custAssistantId"), token);
                break;

            //CustomerMeasurement
            case MessageType.ADD_CUSTOMER_MEASUREMENT:
                res = messageTransmit.authPost(ApiURL.CustomerMeasurement, msgData, token);
                break;
            case MessageType.UPDATE_CUSTOMER_MEASUREMENT:
                JSONObject newCustomerMeasurementJson = new JSONObject(msgData);
                newCustomerMeasurementJson.remove("visitDate");
                res = messageTransmit.authPut(ApiURL.CustomerMeasurement, newCustomerMeasurementJson.toString(), token, new JSONObject(msgData).getLong("customerMeasurementId"));
                break;
            case MessageType.DELETE_CUSTOMER_MEASUREMENT:
                res = messageTransmit.authDelete(ApiURL.CustomerMeasurement, msgData, token);
                break;
            //End
            //MeasurementsDetails
            case MessageType.ADD_MEASUREMENTS_DETAILS:
                res = messageTransmit.authPost(ApiURL.MeasurementsDetails, msgData, token);
                break;
            case MessageType.UPDATE_MEASUREMENTS_DETAILS:
                res = messageTransmit.authPut(ApiURL.MeasurementsDetails, msgData, token, new JSONObject(msgData).getLong("measurementsDetailsId"));
                break;
            case MessageType.DELETE_MEASUREMENTS_DETAILS:
                res = messageTransmit.authDelete(ApiURL.MeasurementsDetails, msgData, token);
                break;
            //End
            //MeasurementDynamicVariable
            case MessageType.ADD_MEASUREMENTS_DYNAMIC_VARIABLE:
                res = messageTransmit.authPost(ApiURL.MeasurementDynamicVariable, msgData, token);
                break;
            case MessageType.UPDATE_MEASUREMENTS_DYNAMIC_VARIABLE:
                res = messageTransmit.authPut(ApiURL.MeasurementDynamicVariable, msgData, token, new JSONObject(msgData).getLong("measurementDynamicVariableId"));
                break;
            case MessageType.DELETE_MEASUREMENTS_DYNAMIC_VARIABLE:
                res = messageTransmit.authDelete(ApiURL.MeasurementDynamicVariable, msgData, token);
                break;
            //End
            //ScheduleWorker
            case MessageType.ADD_SCHEDULE_WORKERS:
                res = messageTransmit.authPost(ApiURL.ScheduleWorker, msgData, token);
                break;
            case MessageType.UPDATE_SCHEDULE_WORKERS:
                JSONObject newScheduleWorkersJson = new JSONObject(msgData);
                newScheduleWorkersJson.remove("createdAt");
                res = messageTransmit.authPut(ApiURL.ScheduleWorker, newScheduleWorkersJson.toString(), token, new JSONObject(msgData).getLong("scheduleWorkersId"));
                break;
            case MessageType.DELETE_SCHEDULE_WORKERS:
                res = messageTransmit.authDelete(ApiURL.ScheduleWorker, msgData, token);
                break;
            //End

        }

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
}
