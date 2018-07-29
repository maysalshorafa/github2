package com.pos.leaders.leaderspossystem;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.pos.leaders.leaderspossystem.Models.OrderDetails;
import com.pos.leaders.leaderspossystem.Tools.DateConverter;
import com.pos.leaders.leaderspossystem.Tools.SESSION;
import com.pos.leaders.leaderspossystem.Tools.SETTINGS;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Invoice extends Activity {

    private TextView tvTitle,tvID,tvCopy, tvInvoiceNumber;
    private TextView tvProductsNames,tvProductsIDs,tvQty, tvProductsPrices;
    private TextView tvTotalPrice, tvTax;
    private TextView tvCashier, tvDate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice);

        tvTitle = (TextView) findViewById(R.id.invoice_tvTitle);
        tvID = (TextView) findViewById(R.id.invoice_tvID);
        tvCopy = (TextView) findViewById(R.id.invoice_tvCopy);
        tvInvoiceNumber = (TextView) findViewById(R.id.invoice_tvInvoiceNumber);

        tvProductsNames = (TextView) findViewById(R.id.invoice_tvProductName);
        tvProductsIDs = (TextView) findViewById(R.id.invoice_tvProductID);
        tvQty = (TextView) findViewById(R.id.invoice_tvQty);
        tvProductsPrices = (TextView) findViewById(R.id.invoice_tvPrice);

        tvTotalPrice = (TextView) findViewById(R.id.invoice_tvTotalPrice);
        tvTax = (TextView) findViewById(R.id.invoice_tvTax);

        tvCashier = (TextView) findViewById(R.id.invoice_tvCashier);
        tvDate = (TextView) findViewById(R.id.invoice_tvDate);

        //init invoice
        tvTitle.setText(SETTINGS.companyName);
        tvID.setText(getString(R.string.private_company)+ " " +SETTINGS.companyID);

        tvCopy.setText(getString(R.string.source_invoice));
        if(true)
            tvCopy.setText(getString(R.string.copy_invoice));
        tvInvoiceNumber.setText("");

        List<OrderDetails> orders = new ArrayList<OrderDetails>();
        for (OrderDetails o:orders){
            tvProductsNames.append(o.getProduct().getDisplayName()+"\n\r");
            tvProductsIDs.append(o.getProduct().getProductId() + "\n\r");
            tvQty.append(o.getQuantity() + "\n\r");
            tvProductsPrices.append(o.getItemTotalPrice() + "\n\r");
        }

        tvCashier.setText(SESSION._EMPLOYEE.getFullName());
        tvDate.setText(DateConverter.dateToString(new Date().getTime()));
        //// TODO: 08/04/2017 Fill all the field and lock this example:
        // http://stackoverflow.com/questions/29730402/how-to-convert-android-view-to-pdf
    }
}
