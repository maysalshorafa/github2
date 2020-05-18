package com.pos.leaders.leaderspossystem.Printer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.Log;

import com.pos.leaders.leaderspossystem.DataBaseAdapter.ChecksDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.CreditCardPaymentDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.Currency.CashPaymentDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.Currency.CurrencyOperationDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.Currency.CurrencyReturnsDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.Currency.CurrencyTypeDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.EmployeeDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.OrderDetailsDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.ProductDBAdapter;
import com.pos.leaders.leaderspossystem.Models.BoInvoice;
import com.pos.leaders.leaderspossystem.Models.Check;
import com.pos.leaders.leaderspossystem.Models.CreditCardPayment;
import com.pos.leaders.leaderspossystem.Models.Currency.CashPayment;
import com.pos.leaders.leaderspossystem.Models.Currency.CurrencyOperation;
import com.pos.leaders.leaderspossystem.Models.Currency.CurrencyReturns;
import com.pos.leaders.leaderspossystem.Models.Currency.CurrencyType;
import com.pos.leaders.leaderspossystem.Models.CustomerType;
import com.pos.leaders.leaderspossystem.Models.Employee;
import com.pos.leaders.leaderspossystem.Models.Order;
import com.pos.leaders.leaderspossystem.Models.OrderDetails;
import com.pos.leaders.leaderspossystem.Models.Product;
import com.pos.leaders.leaderspossystem.R;
import com.pos.leaders.leaderspossystem.Tools.CONSTANT;
import com.pos.leaders.leaderspossystem.Tools.DateConverter;
import com.pos.leaders.leaderspossystem.Tools.SESSION;
import com.pos.leaders.leaderspossystem.Tools.SETTINGS;
import com.pos.leaders.leaderspossystem.Tools.Util;
import com.pos.leaders.leaderspossystem.Tools.symbolWithCodeHashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/**
 * Created by KARAM on 25/02/2017.
 */

public class InvoiceImg {
    private Context context;
    private int width = CONSTANT.PRINTER_PAGE_WIDTH;
    private List<Block> blocks;
    private static final String LOG_TAG = "Printer Invoice";
    private static final String line = "-----------------------------------";
    private static final String newLineL = "\n" + "\u200E";
    private static final String newLineR = "\n" + "\u200F";
    private static final Block em = new Block("", 30.0f, Color.BLACK);

    public InvoiceImg(Context context, List<Block> blocks) {
        this.context = context;
        this.blocks = blocks;
    }

    public InvoiceImg(Context context) {
        this.context = context;
    }

    public Bitmap make(List<Block> blocks) {

        List<Bitmap> bitmaps = new ArrayList<Bitmap>();
        int height = 0;

        for (Block block : blocks) {
            bitmaps.add(buildBlock(block));
        }

        Bitmap t = null;
        for (Bitmap bitmap : bitmaps) {
            if (bitmap.getWidth() < width) {

            } else {
                if (t != null && t.getWidth() < width)
                    height += t.getHeight();
                height += bitmap.getHeight();
            }
            t = bitmap;
        }

        Bitmap image = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(image);

        //Background
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG
                | Paint.LINEAR_TEXT_FLAG);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.WHITE);


        canvas.drawPaint(paint);

        canvas.save();
        canvas.translate(0, 0);
        Bitmap temp = null;
        for (Bitmap bitmap : bitmaps) {
            //Log.i(LOG_TAG, bitmap.getWidth() + "");
            if (bitmap.getWidth() < width) {
                canvas.drawBitmap(bitmap, 0, 0, null);
                canvas.translate(bitmap.getWidth(), 0);
            } else {
                if (temp != null && temp.getWidth() < width) {
                    int rest = ((1 - (temp.getWidth() / width)) * width);
                    canvas.translate(-rest, temp.getHeight());
                }
                canvas.drawBitmap(bitmap, 0, 0, null);
                canvas.translate(0, bitmap.getHeight());
            }
            temp = bitmap;
        }
        canvas.restore();
        return image;
    }

    public Bitmap buildBlock(Block block) {
        //Typeface plain = Typeface.createFromAsset(context.getAssets(),"miriam_libre_bold.ttf");
        Typeface plain = Typeface.createFromAsset(context.getAssets(), "miriam_libre_regular.ttf");
        Typeface pWidth = Typeface.create(plain, block.textWidth);

        TextPaint tPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG
                | Paint.LINEAR_TEXT_FLAG);

        tPaint.setStyle(Paint.Style.FILL);
        tPaint.setColor(block.textColor);
        tPaint.setTypeface(pWidth);
        tPaint.setTextSize(block.textSize);
        tPaint.setTextAlign(block.align);
        //tPaint.setFakeBoldText(true);


        float baseline = -tPaint.ascent(); //ascent() is negative
        //int width = (int) (paint.measureText(text) + 0.5f); // round
        int height = (int) (baseline + tPaint.descent());


        Layout.Alignment a;
        if (block.align == Paint.Align.LEFT) {
            a = Layout.Alignment.ALIGN_OPPOSITE;

        } else if (block.align == Paint.Align.RIGHT) {
            a = Layout.Alignment.ALIGN_NORMAL;


        } else {
            a = Layout.Alignment.ALIGN_CENTER;
        }

        Log.i("Build block", block.text);
        StaticLayout sNewLine = new StaticLayout(block.text, tPaint,
                block.width, a, 1.0f, 1.0f, true);

        Bitmap image = Bitmap.createBitmap(block.width, sNewLine.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(image);

        //Background
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG
                | Paint.LINEAR_TEXT_FLAG);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.WHITE);

        canvas.drawPaint(paint);


        //canvas.drawText(block.text,0, baseline, tPaint);
        canvas.save();
        canvas.translate(0, 0);
        sNewLine.draw(canvas);
        canvas.restore();
        return image;
    }

    private List<Block> Head(Order sale) {
        String customerName = "";
        List<Block> blocks = new ArrayList<Block>();

        Block Title = new Block("\u200E" + SETTINGS.companyName, 38.0f, Color.BLACK, CONSTANT.PRINTER_PAGE_WIDTH);
        Title.Left().Bold();
        Block subTitle;
        if (SETTINGS.company.name().equals("BO_EXEMPT_DEALER")){
            subTitle = new Block("\u200E " + context.getString(R.string.privet_company_status) +
                    ": " + SETTINGS.companyID, 30.0f, Color.BLACK, CONSTANT.PRINTER_PAGE_WIDTH);
        }
        else {
            subTitle = new Block("\u200E " + context.getString(R.string.private_company) +
                    ": " + SETTINGS.companyID, 30.0f, Color.BLACK, CONSTANT.PRINTER_PAGE_WIDTH);
        }
        if(sale.getCustomer()!=null) {
            if (sale.getCustomer_name() == null) {
                customerName = context.getString(R.string.general_customer);
            } else if (sale.getCustomer_name().equals("")) {
                customerName = context.getString(R.string.general_customer);
            } else {
                customerName = sale.getCustomer_name();
            }
        }else {
            customerName = context.getString(R.string.general_customer);
        }
        Block third_part = new Block("\u200E " + context.getString(R.string.customer_name) +
                ": " + customerName + "\n" + line, 30.0f, Color.BLACK, CONSTANT.PRINTER_PAGE_WIDTH);

        third_part.Left();
        subTitle.Left();
        blocks.add(Title);
        blocks.add(subTitle);
        blocks.add(third_part);
        return blocks;
    }
    private List<Block> HeadCopyInvoice(String customerName) {
        List<Block> blocks = new ArrayList<Block>();

        Block Title = new Block("\u200E" + SETTINGS.companyName, 38.0f, Color.BLACK, CONSTANT.PRINTER_PAGE_WIDTH);
        Title.Left().Bold();
        Block subTitle;
        if (SETTINGS.company.name().equals("BO_EXEMPT_DEALER")){
             subTitle = new Block("\u200E " + context.getString(R.string.privet_company_status) +
                    ": " + SETTINGS.companyID, 30.0f, Color.BLACK, CONSTANT.PRINTER_PAGE_WIDTH);
        }
        else {
         subTitle = new Block("\u200E " + context.getString(R.string.private_company) +
                ": " + SETTINGS.companyID, 30.0f, Color.BLACK, CONSTANT.PRINTER_PAGE_WIDTH);}

        Block third_part = new Block("\u200E " + context.getString(R.string.customer_name) +
                ": " + customerName + "\n" + line, 30.0f, Color.BLACK, CONSTANT.PRINTER_PAGE_WIDTH);

        third_part.Left();
        subTitle.Left();
        blocks.add(Title);
        blocks.add(subTitle);
        blocks.add(third_part);
        return blocks;
    }

    public Bitmap normalInvoice(long id, List<OrderDetails> orders, Order sale, boolean isCopy, Employee user, List<Check> checks,String mainMer) {
        CashPaymentDBAdapter cashPaymentDBAdapter = new CashPaymentDBAdapter(context);
        cashPaymentDBAdapter.open();
        ChecksDBAdapter checksDBAdapter =new ChecksDBAdapter(context);
        checksDBAdapter.open();
        CreditCardPaymentDBAdapter creditCardPaymentDBAdapter =new CreditCardPaymentDBAdapter(context);
        creditCardPaymentDBAdapter.open();
        orders=SESSION._TEMP_ORDER_DETAILES;
        sale=SESSION._TEMP_ORDERS;



        Log.d("testSeesin",orders.toString());
        Log.d("testssss",sale.toString());
        int count =0;
        List<Block> blocks = new ArrayList<Block>();
        blocks.addAll(Head(sale));
        Block lineR = new Block("\u200E" + line + "\u200E", 30.0f, Color.BLACK, Paint.Align.CENTER, CONSTANT.PRINTER_PAGE_WIDTH);
        Block clear = new Block("\u200E" + "" + "\u200E", 1.0f, Color.BLACK, Paint.Align.CENTER, CONSTANT.PRINTER_PAGE_WIDTH);
        String status=context.getString(R.string.source_invoice);
        Block inum;
        if(isCopy)
            status=context.getString(R.string.copy_invoice);
        Block bStatus = new Block("\u200F" + status, 35.0f, Color.BLACK, Paint.Align.CENTER, CONSTANT.PRINTER_PAGE_WIDTH);
        if (SETTINGS.company.name().equals("BO_EXEMPT_DEALER")){
         inum = new Block("\u200E"+ context.getString(R.string.invoice_recipte_company_status)+": " +String.format(" %06d ", id) + "\n"
                + line, 28.0f, Color.BLACK, Paint.Align.LEFT, CONSTANT.PRINTER_PAGE_WIDTH);}
        else {
            inum = new Block("\u200E"+ context.getString(R.string.invoice_receipt)+": " +String.format(" %06d ", id) + "\n"
                    + line, 28.0f, Color.BLACK, Paint.Align.LEFT, CONSTANT.PRINTER_PAGE_WIDTH);
        }
        blocks.add(bStatus);
        blocks.add(inum);
        Block name = new Block("\u200E" + context.getString(R.string.product) + newLineL, 25f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.35));
        Block counter = new Block("\u200E" + context.getString(R.string.qty) + "\n", 25f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.14));
        Block unitPrice = new Block("\u200E" + context.getString(R.string.price) + "\n", 25f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.14));
        Block price = new Block("\u200E" + context.getString(R.string.total) + "\n", 25f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.2));
        Block discount = new Block("\u200E" + "%" + "\n", 25f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.14));

        double SaleOriginalityPrice = 0, saleTotalPrice = 0;
        double totalSaved = 0.0;
        double price_before_tax=0;
        for (OrderDetails o : orders) {
            price_before_tax+=o.getPaidAmountAfterTax();

            count+=o.getQuantity();
            if (o.getProduct().getDisplayName().equals("General"))
                o.getProduct().setProductCode(context.getString(R.string.general));
            int cut = 11;
            if (o.getProduct().getDisplayName().length() < cut)
                cut = o.getProduct().getDisplayName().length();
            name.text += (o.getProduct().getDisplayName().substring(0, cut) + newLineL);
            counter.text += o.getQuantity() + "\n";
            unitPrice.text += Util.makePrice(o.getUnitPrice()) + "\n";
            price.text +=  Util.makePrice(o.getItemTotalPrice()) + "\n";
            discount.text += String.format(new Locale("en"), "%.2f", o.getDiscount()) + "\n";
            SaleOriginalityPrice += (o.getItemTotalPrice() );
            saleTotalPrice += o.getUnitPrice();

        }
        totalSaved = (SaleOriginalityPrice - saleTotalPrice);
        Log.d("priceBN",price_before_tax+"");
        Log.d("SaleOriginalityPrice",SaleOriginalityPrice+"");
        Log.d("saleTotalPrice",saleTotalPrice+"");
        Log.d("testTotalSaved",sale.getTotalSaved()+"");
        blocks.add(discount.Left());
        blocks.add(price.Left());
        blocks.add(unitPrice.Left());
        blocks.add(counter.Left());
        //name.text = String.format(new Locale("he"), name.text);
        name.Left();
        blocks.add(name);
        Block productCountText = new Block("\u200E" + context.getString(R.string.product_quantity), 25f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.75));
        Block productCount = new Block("\u200E" + String.valueOf(count), 25f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.25));
        Block toPidText = new Block("\u200E" + context.getString(R.string.total_price),40f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.75));
        Block discountText = new Block("\u200E" + context.getString(R.string.discount), 25f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.75));

        Block toPidTextBeforeDiscount = new Block("\u200E" + context.getString(R.string.price_before_discount),25f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.75));
        Block toPid = new Block(String.format(new Locale("en"), "%.2f", sale.getTotalPrice()), 35f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.25));
        Block discountAmount = new Block("\u200E" + String.format(new Locale("en"), "%.2f",sale.cartDiscount)+ "%", 25f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.25));

        Block toPidBeforeDiscount= new Block(String.format(new Locale("en"), "%.2f", sale.getTotalPrice()*100/(100-sale.cartDiscount)), 35f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.25));
        productCount.Left();
        productCountText.Left();
        toPid.Left();
        toPidText.Left();
        if (sale.cartDiscount !=0){
        discountText.Left();
        discountAmount.Left();
        toPidBeforeDiscount.Left();
        toPidTextBeforeDiscount.Left();
        }


        toPid.Bold();
        toPidText.Bold();
        blocks.add(lineR);
        blocks.add(productCount);
        blocks.add(productCountText);
        blocks.add(clear.Left());
        if (sale.cartDiscount !=0){
        blocks.add(discountAmount);
        blocks.add(discountText);
        blocks.add(clear.Left());
        blocks.add(toPidBeforeDiscount);
        blocks.add(toPidTextBeforeDiscount);
        blocks.add(clear.Left());}

        blocks.add(toPid);
        blocks.add(toPidText);
        blocks.add(clear.Left());

        Block addsTax = new Block("\u200E" + context.getString(R.string.tax) + ": "+Util.makePrice(SETTINGS.tax)+"%" , 25f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.75));
        Block priceBeforeTaxText = new Block("\u200E" + context.getString(R.string.price_before_tax) , 25f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.75));

        double noTax =price_before_tax - (price_before_tax * (sale.cartDiscount/100));
        double totalPriceAfterDiscount= SaleOriginalityPrice- (SaleOriginalityPrice * (sale.cartDiscount/100));
        Log.d("noTax",noTax+"");
        Log.d("totaN",totalPriceAfterDiscount+"");
        Block addsTaxValue = new Block(Util.makePrice(totalPriceAfterDiscount-price_before_tax), 25f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.25));
        Block priceBeforeTax = new Block(Util.makePrice(price_before_tax), 25f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.25));

        // Block numTax = new Block("\u200E" + String.format(new Locale("en"), "\u200E%.2f\n\u200E%.2f\n\u200E%.2f", noTax * (SETTINGS.tax / 100), 0.0f), 30.0f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.25));
        //blocks.add(numTax.Left());
        blocks.add(addsTaxValue.Left());
        blocks.add(addsTax.Left());
        blocks.add(clear.Left());

        blocks.add(priceBeforeTax.Left());
        blocks.add(priceBeforeTaxText.Left());
        blocks.add(clear.Left());
        //pid and price
        Block paidBy = new Block("\u200E" + context.getString(R.string.paid_by), 28f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.75));
        Block paidByText = new Block("\u200E" + "", 28f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.25));


        Block b_payment = new Block("\u200e" + context.getString(R.string.payment)  , 28.0f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.33));
        Block b_total = new Block("\u200E" + context.getString(R.string.total) + "\n" + Util.makePrice(sale.getTotalPrice()), 28.0f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.21));
        Block b_given = new Block("\u200E" + context.getString(R.string.given) + "\n" + Util.makePrice(sale.getTotalPaidAmount()), 28.0f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.21));
        double calcReturned = 0;
        if (sale.getTotalPrice() < 0 && sale.getTotalPaidAmount() >= 0) {
            calcReturned = (sale.getTotalPrice() + sale.getTotalPaidAmount());
        } else {
            calcReturned = (sale.getTotalPaidAmount() - sale.getTotalPrice());
            if (calcReturned < 0) {
                calcReturned = 0;
            }
        }
        Block b_returned = new Block("\u200E" + context.getString(R.string.rest), 28.0f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.25));
        blocks.add(lineR.Left());
        blocks.add(paidByText.Bold().Left());
        blocks.add(paidBy.Bold().Left());
        blocks.add(clear.Left());
        blocks.add(b_returned.Left());
        blocks.add(b_given.Left());
        blocks.add(b_total.Left());
        blocks.add(b_payment.Left());
        blocks.add(clear.Left());
        double cash_plus = 0;
        double check_plus = 0;
        double creditCard_plus = 0;
        List<CashPayment>cashPaymentList=cashPaymentDBAdapter.getPaymentBySaleID(sale.getOrderId());
        for(int i=0;i<cashPaymentList.size();i++){
            cash_plus+=cashPaymentList.get(i).getAmount()*cashPaymentList.get(i).getCurrencyRate();
        }
        List<Check>checkList=checksDBAdapter.getPaymentBySaleID(sale.getOrderId());
        for(int i=0;i<checkList.size();i++){
            check_plus+=checkList.get(i).getAmount();
        }
        List<CreditCardPayment>creditCardPayments=creditCardPaymentDBAdapter.getPaymentByOrderID(sale.getOrderId());
        for(int i=0;i<creditCardPayments.size();i++){
            creditCard_plus+=creditCardPayments.get(i).getAmount();
        }
        if(cashPaymentList.size()>0){
            Block b_payment_Cash = new Block("\u200e" + context.getString(R.string.cash)  , 28.0f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.33));
            Block b_total_Cash = new Block("\u200E" +  Util.makePrice(sale.getTotalPrice()), 28.0f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.21));
            Block b_given_Cash = new Block("\u200E" + Util.makePrice(cash_plus), 28.0f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.21));
            Block b_returned_Cash = new Block("\u200E" + Util.makePrice(sale.getTotalPrice()-cash_plus), 28.0f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.25));
            blocks.add(clear.Left());
            blocks.add(b_returned_Cash.Left());
            blocks.add(b_given_Cash.Left());
            blocks.add(b_total_Cash.Left());
            blocks.add(b_payment_Cash.Left());
            blocks.add(clear.Left());
            blocks.add(lineR.Left());

        }
        if(checkList.size()>0){
            Block b_payment_Check = new Block("\u200e" + context.getString(R.string.checks)  , 28.0f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.33));
            Block b_total_Check = new Block("\u200E" +  Util.makePrice(sale.getTotalPrice()), 28.0f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.21));
            Block b_given_Check = new Block("\u200E" + check_plus, 28.0f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.21));
            Block b_returned_Check = new Block("\u200E" + Util.makePrice(sale.getTotalPrice()-check_plus), 28.0f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.25));
            blocks.add(clear.Left());
            blocks.add(b_returned_Check.Left());
            blocks.add(b_given_Check.Left());
            blocks.add(b_total_Check.Left());
            blocks.add(b_payment_Check.Left());
            blocks.add(clear.Left());
            blocks.add(lineR.Left());

        }
        if(creditCardPayments.size()>0) {
            Block b_payment_Credit = new Block("\u200e" + context.getString(R.string.credit_card), 28.0f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.33));
            Block b_total_Credit = new Block("\u200E" + Util.makePrice(sale.getTotalPrice()), 28.0f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.21));
            Block b_given_Credit = new Block("\u200E" + creditCard_plus, 28.0f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.21));
            Block b_returned_Credit = new Block("\u200E" + Util.makePrice(sale.getTotalPrice() - creditCard_plus), 28.0f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.25));
            blocks.add(clear.Left());
            blocks.add(b_returned_Credit.Left());
            blocks.add(b_given_Credit.Left());
            blocks.add(b_total_Credit.Left());
            blocks.add(b_payment_Credit.Left());
            blocks.add(clear.Left());
            blocks.add(lineR.Left());
        }


        String strPaymentWay = "";



            //Getting default currencies name and values
            List<CurrencyType> currencyTypesList = null;
            CurrencyTypeDBAdapter currencyTypeDBAdapter = new CurrencyTypeDBAdapter(context);
            currencyTypeDBAdapter.open();
            currencyTypesList = currencyTypeDBAdapter.getAllCurrencyType();
            currencyTypeDBAdapter.close();
            double firstTypePaid =0,firstTypeReturn =0 ,secondTypePaid=0 , secondTypeReturn=0,thirdTypePaid=0,thirdTypeReturn=0,fourthTypePaid=0, fourthTypeReturn=0;
            Block currencyDetails = new Block("\u200E" + context.getString(R.string.currency), 28f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.75));
            Block currencyText = new Block("\u200E" + "", 28f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.25));

            Block currencyType = new Block("\u200E" + context.getString(R.string.type) + newLineL, 28.0f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.21));
            Block currencyAmount = new Block("\u200E" + context.getString(R.string.given) + newLineL, 28.0f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.21));
            Block currencyReturned = new Block("\u200E" + context.getString(R.string.returned) + newLineL, 28.0f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.33));

            CurrencyOperationDBAdapter currencyOperationDBAdapter = new CurrencyOperationDBAdapter(context);
            currencyOperationDBAdapter.open();
            List<CurrencyOperation> currencyOperationList = currencyOperationDBAdapter.getCurrencyOperationByOrderID(sale.getOrderId());
            CurrencyReturnsDBAdapter currencyReturnDBAdapter = new CurrencyReturnsDBAdapter(context);
            currencyReturnDBAdapter.open();
            List<CurrencyReturns> currencyReturnsList=currencyReturnDBAdapter.getCurencyReturnBySaleID(sale.getOrderId());
            for (int i = 0; i < currencyOperationList.size(); i++) {
                if(currencyOperationList.get(i).getCurrencyType().equals(currencyTypesList.get(0).getType())) {
                    firstTypePaid += currencyOperationList.get(i).getAmount();
                }
                if(SETTINGS.enableCurrencies) {if(currencyOperationList.get(i).getCurrencyType().equals(currencyTypesList.get(1).getType())){
                    secondTypePaid+=currencyOperationList.get(i).getAmount();
                }  else if(currencyOperationList.get(i).getCurrencyType().equals(currencyTypesList.get(2).getType())){
                    thirdTypePaid+=currencyOperationList.get(i).getAmount();
                }else  if(currencyOperationList.get(i).getCurrencyType().equals(currencyTypesList.get(3).getType())){
                    fourthTypePaid+=currencyOperationList.get(i).getAmount();
                }}

            }
            for (int i = 0; i < currencyReturnsList.size(); i++) {
                if(currencyReturnsList.get(i).getCurrency_type()==0){
                    firstTypeReturn+=currencyReturnsList.get(i).getAmount();

                } if(SETTINGS.enableCurrencies) {
                if(currencyReturnsList.get(i).getCurrency_type()==1){
                    secondTypeReturn+=currencyReturnsList.get(i).getAmount();
                }  else if(currencyReturnsList.get(i).getCurrency_type()==2){
                    thirdTypeReturn+=currencyReturnsList.get(i).getAmount();
                }else  if(currencyReturnsList.get(i).getCurrency_type()==3){
                    fourthTypeReturn+=currencyReturnsList.get(i).getAmount();
                }}

            }
            if(firstTypePaid>0||firstTypeReturn>0){
                currencyType.text += String.valueOf(symbolWithCodeHashMap.valueOf(currencyTypesList.get(0).getType()).getValue()) + newLineL;
                currencyAmount.text += firstTypePaid + newLineL;
                currencyReturned.text+=firstTypeReturn+newLineL;
            }
        if(SETTINGS.enableCurrencies) {
            if(secondTypePaid>0||secondTypeReturn>0){
                currencyType.text += String.valueOf(symbolWithCodeHashMap.valueOf(currencyTypesList.get(1).getType()).getValue()) + newLineL;
                currencyAmount.text += secondTypePaid + newLineL;
                currencyReturned.text+=secondTypeReturn+newLineL;
            }
            if(thirdTypePaid>0||thirdTypeReturn>0){
                currencyType.text += String.valueOf(symbolWithCodeHashMap.valueOf(currencyTypesList.get(2).getType()).getValue()) + newLineL;
                currencyAmount.text += thirdTypePaid + newLineL;
                currencyReturned.text+=thirdTypeReturn+newLineL;
            }
            if(fourthTypePaid>0||fourthTypeReturn>0){
                currencyType.text += String.valueOf(symbolWithCodeHashMap.valueOf(currencyTypesList.get(3).getType()).getValue()) + newLineL;
                currencyAmount.text += fourthTypePaid + newLineL;
                currencyReturned.text+=fourthTypeReturn+newLineL;
            }}
            blocks.add(currencyText.Bold().Left());
            blocks.add(currencyDetails.Bold().Left());
            blocks.add(clear.Left());
            blocks.add(currencyReturned.Left());
            blocks.add(currencyText.Left());
            blocks.add(currencyAmount.Left());
            blocks.add(currencyType.Left());
            blocks.add(lineR.Left());

        if (checkList .size()>0) {
            Block b_checks_number = new Block("\u200e" + context.getString(R.string.checks), 30.0f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.33));
            Block b_checks_date = new Block(context.getString(R.string.date), 30.0f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.33));
            Block b_checks_amount = new Block(context.getString(R.string.amount), 30.0f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.33));
            for (Check check : checkList) {
                b_checks_number.text += newLineL + check.getCheckNum();
                b_checks_date.text += "\n" + DateConverter.toDate(new Date(check.getCreatedAt().getTime()));
                b_checks_amount.text += "\n" + Util.makePrice(check.getAmount());
            }
            blocks.add(b_checks_amount.Left());
            blocks.add(b_checks_date.Left());
            blocks.add(b_checks_number.Left());

            blocks.add(lineR.Left());
        }
        if(mainMer!=""){
            String str = "";
            for (String s : mainMer.split("\n")) {
                if(!s.replaceAll(" ","").equals("")) {
                    if(s.contains("Powered")){
                        continue;
                    }
                    if(s.contains("מספר כרטיס")){
                        if(s.split("\\s+")[1].length()>4){
                            String head = "מספר כרטיס";
                            String ss = "";
                            for(int i=0;i<s.split("\\s+")[1].length()-4;i++) {
                                ss += "*";
                            }
                            ss += s.split("\\s+")[1].substring(s.split("\\s+")[1].length() - 4, s.split("\\s+")[1].length());

                            str += "\u200E" + ss + "\t" + head + "\n";
                            continue;
                        }
                    }
                    str += "\u200E" + s + "\n";
                }
                Log.i("cc row", s);
            }
            Block CC = new Block("\u200E" + str, 25.0f, Color.BLACK).Left();

            blocks.add(CC);
        }
        Block cashier = new Block("\u200e" + context.getString(R.string.cashier)+ " " , 25f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.5));
        Block cashierName = new Block("\u200E" +  user.getFullName(), 25f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.5));

        cashier.Left();
        cashierName.Left();
        blocks.add(cashierName);
        blocks.add(cashier);
        if(SESSION._ORDERS.getCustomer()!=null){
            if(SESSION._ORDERS.getCustomer().getCustomerType().equals(CustomerType.CREDIT)){
                Block fourth_part = new Block("\u200E " + context.getString(R.string.customer_ledger) +
                        ": " + SESSION._ORDERS.CustomerLedger + "\n" + line, 30.0f, Color.BLACK, CONSTANT.PRINTER_PAGE_WIDTH);

                fourth_part.Left();
                blocks.add(fourth_part);
            }
        }
        Block date = new Block("\u200e" + context.getString(R.string.date) + " :" + DateConverter.DateToString(sale.getCreatedAt()), 28.0f, Color.BLACK, CONSTANT.PRINTER_PAGE_WIDTH);
        blocks.add(date.Left());
        if (isCopy) {
            Block bCopyDate = new Block("\u200E" + context.getString(R.string.copy_date) + ": " + DateConverter.currentDateTime(), 28.0f, Color.BLACK, CONSTANT.PRINTER_PAGE_WIDTH);
            blocks.add(bCopyDate.Left());
        }
        //    if ((int) totalSaved != 0) {
        String s = SETTINGS.currencySymbol;
        Log.d("totalSaved",sale.getTotalSaved()+"jojo");

        Block totSaved = new Block("\u200e" + context.getString(R.string.total_saved) + " :" + String.format(new Locale("en"), "%.2f %s",sale.getTotalSaved(), s), 32.0f, Color.BLACK, CONSTANT.PRINTER_PAGE_WIDTH);
        blocks.add(totSaved.Bold().Left());
        //   }

        Block thanks = new Block(SETTINGS.returnNote, 28.0f, Color.BLACK, CONSTANT.PRINTER_PAGE_WIDTH);
        blocks.add(thanks.Left());

        return make(blocks);
    }
    public Bitmap copyNormalInvoice(long id, List<OrderDetails> orders, Order sale, boolean isCopy, Employee user, List<Check> checks,String mainMer) {
        CashPaymentDBAdapter cashPaymentDBAdapter = new CashPaymentDBAdapter(context);
        cashPaymentDBAdapter.open();
        ChecksDBAdapter checksDBAdapter =new ChecksDBAdapter(context);
        checksDBAdapter.open();
        CreditCardPaymentDBAdapter creditCardPaymentDBAdapter =new CreditCardPaymentDBAdapter(context);
        creditCardPaymentDBAdapter.open();
        orders=SESSION._TEMP_ORDER_DETAILES_COPY;
        sale=SESSION._TEMP_ORDERS_COPY;
        Log.d("testSeesin",orders.toString());
        Log.d("testssss",sale.toString());
        int count =0;
        Block inum;
        List<Block> blocks = new ArrayList<Block>();
        blocks.addAll(Head(sale));
        Block lineR = new Block("\u200E" + line + "\u200E", 30.0f, Color.BLACK, Paint.Align.CENTER, CONSTANT.PRINTER_PAGE_WIDTH);
        Block clear = new Block("\u200E" + "" + "\u200E", 1.0f, Color.BLACK, Paint.Align.CENTER, CONSTANT.PRINTER_PAGE_WIDTH);
        String status=context.getString(R.string.source_invoice);
        if(isCopy)
            status=context.getString(R.string.copy_invoice);
        Block bStatus = new Block("\u200F" + status, 35.0f, Color.BLACK, Paint.Align.CENTER, CONSTANT.PRINTER_PAGE_WIDTH);
        if (SETTINGS.company.name().equals("BO_EXEMPT_DEALER")){
        inum = new Block("\u200E"+ context.getString(R.string.invoice_recipte_company_status)+": " +String.format(" %06d ", id) + "\n"
                + line, 28.0f, Color.BLACK, Paint.Align.LEFT, CONSTANT.PRINTER_PAGE_WIDTH);}
        else {
            inum = new Block("\u200E"+ context.getString(R.string.invoice_receipt)+": " +String.format(" %06d ", id) + "\n"
                    + line, 28.0f, Color.BLACK, Paint.Align.LEFT, CONSTANT.PRINTER_PAGE_WIDTH);
        }
        blocks.add(bStatus);
        blocks.add(inum);
        Block name = new Block("\u200E" + context.getString(R.string.product) + newLineL, 25f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.35));
        Block counter = new Block("\u200E" + context.getString(R.string.qty) + "\n", 25f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.14));
        Block unitPrice = new Block("\u200E" + context.getString(R.string.price) + "\n", 25f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.14));
        Block price = new Block("\u200E" + context.getString(R.string.total) + "\n", 25f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.2));
        Block discount = new Block("\u200E" + "%" + "\n", 25f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.14));

        double SaleOriginalityPrice = 0, saleTotalPrice = 0;
        double totalSaved = 0.0;
        double price_before_tax=0;
        for (OrderDetails o : orders) {
            price_before_tax+=o.getPaidAmountAfterTax();

            count+=o.getQuantity();
            if (o.getProduct().getDisplayName().equals("General"))
                o.getProduct().setProductCode(context.getString(R.string.general));
            int cut = 11;
            if (o.getProduct().getDisplayName().length() < cut)
                cut = o.getProduct().getDisplayName().length();
            name.text += (o.getProduct().getDisplayName().substring(0, cut) + newLineL);
            counter.text += o.getQuantity() + "\n";
            unitPrice.text += Util.makePrice(o.getUnitPrice()) + "\n";
            price.text +=  Util.makePrice(o.getItemTotalPrice()) + "\n";
            discount.text += String.format(new Locale("en"), "%.2f", o.getDiscount()) + "\n";
            SaleOriginalityPrice += (o.getItemTotalPrice() );
            saleTotalPrice += o.getUnitPrice();

        }
        totalSaved = (SaleOriginalityPrice - saleTotalPrice);
        Log.d("testTotalSaved",totalSaved+"");
        blocks.add(discount.Left());
        blocks.add(price.Left());
        blocks.add(unitPrice.Left());
        blocks.add(counter.Left());
        //name.text = String.format(new Locale("he"), name.text);
        name.Left();
        blocks.add(name);
        Block productCountText = new Block("\u200E" + context.getString(R.string.product_quantity), 25f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.75));
        Block productCount = new Block("\u200E" + String.valueOf(count), 25f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.25));
        Block toPidText = new Block("\u200E" + context.getString(R.string.total_price),40f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.75));
        Block discountText = new Block("\u200E" + context.getString(R.string.discount), 25f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.75));

        Block toPidTextBeforeDiscount = new Block("\u200E" + context.getString(R.string.price_before_discount),25f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.75));
        Block toPid = new Block(String.format(new Locale("en"), "%.2f", sale.getTotalPrice()), 35f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.25));
        Block discountAmount = new Block("\u200E" + String.format(new Locale("en"), "%.2f",sale.cartDiscount)+ "%", 25f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.25));

        Block toPidBeforeDiscount= new Block(String.format(new Locale("en"), "%.2f", sale.getTotalPrice()*100/(100-sale.cartDiscount)), 35f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.25));
        productCount.Left();
        productCountText.Left();
        toPid.Left();
        toPidText.Left();
        if (sale.cartDiscount !=0){
            discountText.Left();
            discountAmount.Left();
            toPidBeforeDiscount.Left();
            toPidTextBeforeDiscount.Left();
        }


        toPid.Bold();
        toPidText.Bold();
        blocks.add(lineR);
        blocks.add(productCount);
        blocks.add(productCountText);
        blocks.add(clear.Left());
        if (sale.cartDiscount !=0){
            blocks.add(discountAmount);
            blocks.add(discountText);
            blocks.add(clear.Left());
            blocks.add(toPidBeforeDiscount);
            blocks.add(toPidTextBeforeDiscount);
            blocks.add(clear.Left());}

        blocks.add(toPid);
        blocks.add(toPidText);
        blocks.add(clear.Left());

        Block addsTax = new Block("\u200E" + context.getString(R.string.tax) + ": "+Util.makePrice(SETTINGS.tax)+"%" , 25f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.75));
        Block priceBeforeTaxText = new Block("\u200E" + context.getString(R.string.price_before_tax) , 25f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.75));

        double noTax =price_before_tax - (price_before_tax * (sale.cartDiscount/100));
        double totalPriceAfterDiscount= SaleOriginalityPrice- (SaleOriginalityPrice * (sale.cartDiscount/100));

        Block addsTaxValue = new Block(Util.makePrice(totalPriceAfterDiscount-noTax), 25f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.25));
        Block priceBeforeTax = new Block(Util.makePrice(noTax), 25f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.25));

        // Block numTax = new Block("\u200E" + String.format(new Locale("en"), "\u200E%.2f\n\u200E%.2f\n\u200E%.2f", noTax * (SETTINGS.tax / 100), 0.0f), 30.0f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.25));
        //blocks.add(numTax.Left());
        blocks.add(addsTaxValue.Left());
        blocks.add(addsTax.Left());
        blocks.add(clear.Left());

        blocks.add(priceBeforeTax.Left());
        blocks.add(priceBeforeTaxText.Left());
        blocks.add(clear.Left());
        //pid and price
        Block paidBy = new Block("\u200E" + context.getString(R.string.paid_by), 28f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.75));
        Block paidByText = new Block("\u200E" + "", 28f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.25));


        Block b_payment = new Block("\u200e" + context.getString(R.string.payment)  , 28.0f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.33));
        Block b_total = new Block("\u200E" + context.getString(R.string.total) + "\n" + Util.makePrice(sale.getTotalPrice()), 28.0f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.21));
        Block b_given = new Block("\u200E" + context.getString(R.string.given) + "\n" + Util.makePrice(sale.getTotalPaidAmount()), 28.0f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.21));
        double calcReturned = 0;
        if (sale.getTotalPrice() < 0 && sale.getTotalPaidAmount() >= 0) {
            calcReturned = (sale.getTotalPrice() + sale.getTotalPaidAmount());
        } else {
            calcReturned = (sale.getTotalPaidAmount() - sale.getTotalPrice());
            if (calcReturned < 0) {
                calcReturned = 0;
            }
        }
        Block b_returned = new Block("\u200E" + context.getString(R.string.rest), 28.0f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.25));
        blocks.add(lineR.Left());
        blocks.add(paidByText.Bold().Left());
        blocks.add(paidBy.Bold().Left());
        blocks.add(clear.Left());
        blocks.add(b_returned.Left());
        blocks.add(b_given.Left());
        blocks.add(b_total.Left());
        blocks.add(b_payment.Left());
        blocks.add(clear.Left());
        double cash_plus = 0;
        double check_plus = 0;
        double creditCard_plus = 0;
        List<CashPayment>cashPaymentList=cashPaymentDBAdapter.getPaymentBySaleID(sale.getOrderId());
        for(int i=0;i<cashPaymentList.size();i++){
            cash_plus+=cashPaymentList.get(i).getAmount()*cashPaymentList.get(i).getCurrencyRate();
        }
        List<Check>checkList=checksDBAdapter.getPaymentBySaleID(sale.getOrderId());
        for(int i=0;i<checkList.size();i++){
            check_plus+=checkList.get(i).getAmount();
        }
        List<CreditCardPayment>creditCardPayments=creditCardPaymentDBAdapter.getPaymentByOrderID(sale.getOrderId());
        for(int i=0;i<creditCardPayments.size();i++){
            creditCard_plus+=creditCardPayments.get(i).getAmount();
        }
        if(cashPaymentList.size()>0){
            Block b_payment_Cash = new Block("\u200e" + context.getString(R.string.cash)  , 28.0f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.33));
            Block b_total_Cash = new Block("\u200E" +  Util.makePrice(sale.getTotalPrice()), 28.0f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.21));
            Block b_given_Cash = new Block("\u200E" + cash_plus, 28.0f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.21));
            Block b_returned_Cash = new Block("\u200E" + Util.makePrice(sale.getTotalPrice()-cash_plus), 28.0f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.25));
            blocks.add(clear.Left());
            blocks.add(b_returned_Cash.Left());
            blocks.add(b_given_Cash.Left());
            blocks.add(b_total_Cash.Left());
            blocks.add(b_payment_Cash.Left());
            blocks.add(clear.Left());
            blocks.add(lineR.Left());

        }
        if(checkList.size()>0){
            Block b_payment_Check = new Block("\u200e" + context.getString(R.string.checks)  , 28.0f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.33));
            Block b_total_Check = new Block("\u200E" +  Util.makePrice(sale.getTotalPrice()), 28.0f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.21));
            Block b_given_Check = new Block("\u200E" + check_plus, 28.0f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.21));
            Block b_returned_Check = new Block("\u200E" + Util.makePrice(sale.getTotalPrice()-check_plus), 28.0f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.25));
            blocks.add(clear.Left());
            blocks.add(b_returned_Check.Left());
            blocks.add(b_given_Check.Left());
            blocks.add(b_total_Check.Left());
            blocks.add(b_payment_Check.Left());
            blocks.add(clear.Left());
            blocks.add(lineR.Left());

        }
        if(creditCardPayments.size()>0) {
            Block b_payment_Credit = new Block("\u200e" + context.getString(R.string.credit_card), 28.0f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.33));
            Block b_total_Credit = new Block("\u200E" + Util.makePrice(sale.getTotalPrice()), 28.0f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.21));
            Block b_given_Credit = new Block("\u200E" + creditCard_plus, 28.0f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.21));
            Block b_returned_Credit = new Block("\u200E" + Util.makePrice(sale.getTotalPrice() - creditCard_plus), 28.0f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.25));
            blocks.add(clear.Left());
            blocks.add(b_returned_Credit.Left());
            blocks.add(b_given_Credit.Left());
            blocks.add(b_total_Credit.Left());
            blocks.add(b_payment_Credit.Left());
            blocks.add(clear.Left());
            blocks.add(lineR.Left());
        }


        String strPaymentWay = "";

            List<CurrencyType> currencyTypesList = null;
            CurrencyTypeDBAdapter currencyTypeDBAdapter = new CurrencyTypeDBAdapter(context);
            currencyTypeDBAdapter.open();
            currencyTypesList = currencyTypeDBAdapter.getAllCurrencyType();
            currencyTypeDBAdapter.close();
            double firstTypePaid =0,firstTypeReturn =0 ,secondTypePaid=0 , secondTypeReturn=0,thirdTypePaid=0,thirdTypeReturn=0,fourthTypePaid=0, fourthTypeReturn=0;
            Block currencyDetails = new Block("\u200E" + context.getString(R.string.currency), 28f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.75));
            Block currencyText = new Block("\u200E" + "", 28f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.25));

            Block currencyType = new Block("\u200E" + context.getString(R.string.type) + newLineL, 28.0f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.21));
            Block currencyAmount = new Block("\u200E" + context.getString(R.string.given) + newLineL, 28.0f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.21));
            Block currencyReturned = new Block("\u200E" + context.getString(R.string.returned) + newLineL, 28.0f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.33));

            CurrencyOperationDBAdapter currencyOperationDBAdapter = new CurrencyOperationDBAdapter(context);
            currencyOperationDBAdapter.open();
            List<CurrencyOperation> currencyOperationList = currencyOperationDBAdapter.getCurrencyOperationByOrderID(sale.getOrderId());
            CurrencyReturnsDBAdapter currencyReturnDBAdapter = new CurrencyReturnsDBAdapter(context);
            currencyReturnDBAdapter.open();
            List<CurrencyReturns> currencyReturnsList=currencyReturnDBAdapter.getCurencyReturnBySaleID(sale.getOrderId());
            for (int i = 0; i < currencyOperationList.size(); i++) {
                if(currencyOperationList.get(i).getCurrencyType().equals(currencyTypesList.get(0).getType())){
                    firstTypePaid+=currencyOperationList.get(i).getAmount();
                }if (SETTINGS.enableCurrencies){
                if(currencyOperationList.get(i).getCurrencyType().equals(currencyTypesList.get(1).getType())){
                    secondTypePaid+=currencyOperationList.get(i).getAmount();
                }  else if(currencyOperationList.get(i).getCurrencyType().equals(currencyTypesList.get(2).getType())){
                    thirdTypePaid+=currencyOperationList.get(i).getAmount();
                }else  if(currencyOperationList.get(i).getCurrencyType().equals(currencyTypesList.get(3).getType())){
                    fourthTypePaid+=currencyOperationList.get(i).getAmount();
                }}

            }
            for (int i = 0; i < currencyReturnsList.size(); i++) {
                if(currencyReturnsList.get(i).getCurrency_type()==0){
                    firstTypeReturn+=currencyReturnsList.get(i).getAmount();
                }if(SETTINGS.enableCurrencies){
                if(currencyReturnsList.get(i).getCurrency_type()==1){
                    secondTypeReturn+=currencyReturnsList.get(i).getAmount();
                }  else if(currencyReturnsList.get(i).getCurrency_type()==2){
                    thirdTypeReturn+=currencyReturnsList.get(i).getAmount();
                }else  if(currencyReturnsList.get(i).getCurrency_type()==3){
                    fourthTypeReturn+=currencyReturnsList.get(i).getAmount();
                }}

            }
            if(firstTypePaid>0||firstTypeReturn>0){
                currencyType.text += String.valueOf(symbolWithCodeHashMap.valueOf(currencyTypesList.get(0).getType()).getValue()) + newLineL;
                currencyAmount.text += firstTypePaid + newLineL;
                currencyReturned.text+=firstTypeReturn+newLineL;
            }
            if (SETTINGS.enableCurrencies){
            if(secondTypePaid>0||secondTypeReturn>0){
                currencyType.text += String.valueOf(symbolWithCodeHashMap.valueOf(currencyTypesList.get(1).getType()).getValue())+ newLineL;
                currencyAmount.text += secondTypePaid + newLineL;
                currencyReturned.text+=secondTypeReturn+newLineL;
            }
            if(thirdTypePaid>0||thirdTypeReturn>0){
                currencyType.text += String.valueOf(symbolWithCodeHashMap.valueOf(currencyTypesList.get(2).getType()).getValue()) + newLineL;
                currencyAmount.text += thirdTypePaid + newLineL;
                currencyReturned.text+=thirdTypeReturn+newLineL;
            }
            if(fourthTypePaid>0||fourthTypeReturn>0){
                currencyType.text += String.valueOf(symbolWithCodeHashMap.valueOf(currencyTypesList.get(3).getType()).getValue()) + newLineL;
                currencyAmount.text += fourthTypePaid + newLineL;
                currencyReturned.text+=fourthTypeReturn+newLineL;
            }}
            blocks.add(currencyText.Bold().Left());
            blocks.add(currencyDetails.Bold().Left());
            blocks.add(clear.Left());
            blocks.add(currencyReturned.Left());
            blocks.add(currencyText.Left());
            blocks.add(currencyAmount.Left());
            blocks.add(currencyType.Left());
            blocks.add(lineR.Left());

        if (checkList .size()>0) {
            Block b_checks_number = new Block("\u200e" + context.getString(R.string.checks), 30.0f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.33));
            Block b_checks_date = new Block(context.getString(R.string.date), 30.0f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.33));
            Block b_checks_amount = new Block(context.getString(R.string.amount), 30.0f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.33));
            for (Check check : checkList) {
                b_checks_number.text += newLineL + check.getCheckNum();
                b_checks_date.text += "\n" + DateConverter.toDate(new Date(check.getCreatedAt().getTime()));
                b_checks_amount.text += "\n" + Util.makePrice(check.getAmount());
            }
            blocks.add(b_checks_amount.Left());
            blocks.add(b_checks_date.Left());
            blocks.add(b_checks_number.Left());

            blocks.add(lineR.Left());
        }
        if(mainMer!=""){
            String str = "";
            for (String s : mainMer.split("\n")) {
                if(!s.replaceAll(" ","").equals("")) {
                    if(s.contains("Powered")){
                        continue;
                    }
                    if(s.contains("מספר כרטיס")){
                        if(s.split("\\s+")[1].length()>4){
                            String head = "מספר כרטיס";
                            String ss = "";
                            for(int i=0;i<s.split("\\s+")[1].length()-4;i++) {
                                ss += "*";
                            }
                            ss += s.split("\\s+")[1].substring(s.split("\\s+")[1].length() - 4, s.split("\\s+")[1].length());

                            str += "\u200E" + ss + "\t" + head + "\n";
                            continue;
                        }
                    }
                    str += "\u200E" + s + "\n";
                }
                Log.i("cc row", s);
            }
            Block CC = new Block("\u200E" + str, 25.0f, Color.BLACK).Left();

            blocks.add(CC);
        }
        Block cashier = new Block("\u200e" + context.getString(R.string.cashier)+ " " , 25f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.5));
        Block cashierName = new Block("\u200E" +  user.getFullName(), 25f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.5));

        cashier.Left();
        cashierName.Left();
        blocks.add(cashierName);
        blocks.add(cashier);
        if(SESSION._TEMP_ORDERS_COPY.getCustomer()!=null){
            if(SESSION._TEMP_ORDERS_COPY.getCustomer().getCustomerType().equals(CustomerType.CREDIT)){
                Block fourth_part = new Block("\u200E " + context.getString(R.string.customer_ledger) +
                        ": " + SESSION._ORDERS.CustomerLedger + "\n" + line, 30.0f, Color.BLACK, CONSTANT.PRINTER_PAGE_WIDTH);

                fourth_part.Left();
                blocks.add(fourth_part);
            }
        }
        Block date = new Block("\u200e" + context.getString(R.string.date) + " :" + DateConverter.DateToString(sale.getCreatedAt()), 28.0f, Color.BLACK, CONSTANT.PRINTER_PAGE_WIDTH);
        blocks.add(date.Left());
        if (isCopy) {
            Block bCopyDate = new Block("\u200E" + context.getString(R.string.copy_date) + ": " + DateConverter.currentDateTime(), 28.0f, Color.BLACK, CONSTANT.PRINTER_PAGE_WIDTH);
            blocks.add(bCopyDate.Left());
        }
        //    if ((int) totalSaved != 0) {
        String s = SETTINGS.currencySymbol;
        Block totSaved = new Block("\u200e" + context.getString(R.string.total_saved) + " :" + String.format(new Locale("en"), "%.2f %s",totalSaved, s), 32.0f, Color.BLACK, CONSTANT.PRINTER_PAGE_WIDTH);
        blocks.add(totSaved.Bold().Left());
        //   }

        Block thanks = new Block(SETTINGS.returnNote, 28.0f, Color.BLACK, CONSTANT.PRINTER_PAGE_WIDTH);
        blocks.add(thanks.Left());

        return make(blocks);
    }
    public Bitmap copyInvoice(long id, List<OrderDetails> orders, Order sale, boolean isCopy, Employee user, List<Check> checks) {
        int count =0;
        List<Block> blocks = new ArrayList<Block>();
        blocks.addAll(Head(sale));
        Block lineR = new Block("\u200E" + line + "\u200E", 30.0f, Color.BLACK, Paint.Align.CENTER, CONSTANT.PRINTER_PAGE_WIDTH);
        Block clear = new Block("\u200E" + "" + "\u200E", 1.0f, Color.BLACK, Paint.Align.CENTER, CONSTANT.PRINTER_PAGE_WIDTH);
        String status=context.getString(R.string.source_invoice);
        Block inum;
        if(isCopy)
            status=context.getString(R.string.copy_invoice);
        Block bStatus = new Block("\u200F" + status, 35.0f, Color.BLACK, Paint.Align.CENTER, CONSTANT.PRINTER_PAGE_WIDTH);
        if (SETTINGS.company.name().equals("BO_EXEMPT_DEALER")){
         inum = new Block("\u200E"+ context.getString(R.string.invoice_recipte_company_status)+": " +String.format(" %06d ", id) + "\n"
                + line, 28.0f, Color.BLACK, Paint.Align.LEFT, CONSTANT.PRINTER_PAGE_WIDTH);}
        else {
            inum = new Block("\u200E"+ context.getString(R.string.invoice_receipt)+": " +String.format(" %06d ", id) + "\n"
                    + line, 28.0f, Color.BLACK, Paint.Align.LEFT, CONSTANT.PRINTER_PAGE_WIDTH)  ;
        }
        blocks.add(bStatus);
        blocks.add(inum);
        Block name = new Block("\u200E" + context.getString(R.string.product) + newLineL, 25f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.35));
        Block counter = new Block("\u200E" + context.getString(R.string.qty) + "\n", 25f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.14));
        Block unitPrice = new Block("\u200E" + context.getString(R.string.price) + "\n", 25f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.14));
        Block price = new Block("\u200E" + context.getString(R.string.total) + "\n", 25f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.2));
        Block discount = new Block("\u200E" + "%" + "\n", 25f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.14));

        double SaleOriginalityPrice = 0, saleTotalPrice = 0;
        double totalSaved = 0.0;
        for (OrderDetails o : orders) {
            count+=o.getQuantity();
            if (o.getProduct().getDisplayName().equals("General"))
                o.getProduct().setProductCode(context.getString(R.string.general));
            int cut = 11;
            if (o.getProduct().getDisplayName().length() < cut)
                cut = o.getProduct().getDisplayName().length();
            name.text += (o.getProduct().getDisplayName().substring(0, cut) + newLineL);
            counter.text += o.getQuantity() + "\n";
            unitPrice.text += Util.makePrice(o.getUnitPrice()) + "\n";
            price.text +=  Util.makePrice(o.getItemTotalPrice()) + "\n";
            discount.text += String.format(new Locale("en"), "%.2f", o.getDiscount()) + "\n";
            SaleOriginalityPrice += (o.getItemTotalPrice() );
            saleTotalPrice += o.getUnitPrice();
        }
        totalSaved = (SaleOriginalityPrice - saleTotalPrice);
        Log.d("testTotalSaved",sale.getTotalSaved()+"");
        blocks.add(discount.Left());
        blocks.add(price.Left());
        blocks.add(unitPrice.Left());
        blocks.add(counter.Left());
        //name.text = String.format(new Locale("he"), name.text);
        name.Left();
        blocks.add(name);
        Block productCountText = new Block("\u200E" + context.getString(R.string.product_quantity), 25f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.75));
        Block productCount = new Block("\u200E" + String.valueOf(count), 25f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.25));
        Block toPidText = new Block("\u200E" + context.getString(R.string.total_price),40f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.75));
        Block discountText = new Block("\u200E" + context.getString(R.string.discount), 25f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.75));

        Block toPidTextBeforeDiscount = new Block("\u200E" + context.getString(R.string.price_before_discount),40f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.75));
        Block toPid = new Block(String.format(new Locale("en"), "%.2f", sale.getTotalPrice()), 35f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.25));
        Block discountAmount = new Block("\u200E" + String.format(new Locale("en"), "%.2f",sale.cartDiscount)+ "%", 25f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.25));

        Block toPidBeforeDiscount= new Block(String.format(new Locale("en"), "%.2f", sale.getTotalPrice()*100/(100-sale.cartDiscount)), 35f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.25));
        productCount.Left();
        productCountText.Left();
        toPid.Left();
        toPidText.Left();

        discountText.Left();
        discountAmount.Left();
        toPidBeforeDiscount.Left();
        toPidTextBeforeDiscount.Left();
        toPid.Bold();
        toPidText.Bold();
        blocks.add(lineR);
        blocks.add(productCount);
        blocks.add(productCountText);
        blocks.add(clear.Left());
        if(sale.cartDiscount>0) {
            blocks.add(discountAmount);
            blocks.add(discountText);
            blocks.add(clear.Left());
            blocks.add(toPidBeforeDiscount);
            blocks.add(toPidTextBeforeDiscount);
            blocks.add(clear.Left());
        }
        blocks.add(toPid);
        blocks.add(toPidText);
        blocks.add(clear.Left());

        Block addsTax = new Block("\u200E" + context.getString(R.string.tax) + ": "+Util.makePrice(SETTINGS.tax)+"%" , 30.0f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.75));
        double noTax = sale.getTotalPrice() / (1 + (SETTINGS.tax / 100));
        Block addsTaxValue = new Block(Util.makePrice(noTax), 30.0f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.25));
        // Block numTax = new Block("\u200E" + String.format(new Locale("en"), "\u200E%.2f\n\u200E%.2f\n\u200E%.2f", noTax * (SETTINGS.tax / 100), 0.0f), 30.0f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.25));
        //blocks.add(numTax.Left());
        blocks.add(addsTaxValue.Left());
        blocks.add(addsTax.Left());
        blocks.add(clear.Left());
        //pid and price
        Block paidBy = new Block("\u200E" + context.getString(R.string.paid_by), 28f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.75));
        Block paidByText = new Block("\u200E" + "", 28f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.25));

        String strPaymentWay = "";
        /*sale.getPayment().getPaymentWay().toString();
        switch (sale.getPayment().getPaymentWay().toString()) {
            case CONSTANT.CASH:
                strPaymentWay = context.getString(R.string.cash);
                break;
            case CONSTANT.CHECKS:
                strPaymentWay = context.getString(R.string.checks);
                break;
            case CONSTANT.CREDIT_CARD:
                strPaymentWay = context.getString(R.string.credit_card);
                break;
        }*/

        Block b_payment = new Block("\u200e" + context.getString(R.string.payment) + newLineL + strPaymentWay, 28.0f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.33));
        Block b_total = new Block("\u200E" + context.getString(R.string.total) + "\n" + Util.makePrice(sale.getTotalPrice()), 28.0f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.21));
        Block b_given = new Block("\u200E" + context.getString(R.string.given) + "\n" + Util.makePrice(sale.getTotalPaidAmount()), 28.0f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.21));
        double calcReturned = 0;
        if (sale.getTotalPrice() < 0 && sale.getTotalPaidAmount() >= 0) {
            calcReturned = (sale.getTotalPrice() + sale.getTotalPaidAmount());
        } else {
            calcReturned = (sale.getTotalPaidAmount() - sale.getTotalPrice());
            if (calcReturned < 0) {
                calcReturned = 0;
            }
        }
        Block b_returned = new Block("\u200E" + context.getString(R.string.returned) + "\n" + Util.makePrice(calcReturned), 28.0f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.25));
        blocks.add(lineR.Left());
        blocks.add(paidByText.Bold().Left());
        blocks.add(paidBy.Bold().Left());
        blocks.add(clear.Left());
        blocks.add(b_returned.Left());
        blocks.add(b_given.Left());
        blocks.add(b_total.Left());
        blocks.add(b_payment.Left());

        blocks.add(lineR.Left());
            List<CurrencyType> currencyTypesList = null;
            CurrencyTypeDBAdapter currencyTypeDBAdapter = new CurrencyTypeDBAdapter(context);
            currencyTypeDBAdapter.open();
            currencyTypesList = currencyTypeDBAdapter.getAllCurrencyType();
            currencyTypeDBAdapter.close();
            double firstTypePaid =0,firstTypeReturn =0 ,secondTypePaid=0 , secondTypeReturn=0,thirdTypePaid=0,thirdTypeReturn=0,fourthTypePaid=0, fourthTypeReturn=0;
            Block currencyDetails = new Block("\u200E" + context.getString(R.string.currency), 28f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.75));
            Block currencyText = new Block("\u200E" + "", 28f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.25));

            Block currencyType = new Block("\u200E" + context.getString(R.string.type) + newLineL, 28.0f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.21));
            Block currencyAmount = new Block("\u200E" + context.getString(R.string.given) + newLineL, 28.0f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.21));
            Block currencyReturned = new Block("\u200E" + context.getString(R.string.returned) + newLineL, 28.0f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.33));

            CurrencyOperationDBAdapter currencyOperationDBAdapter = new CurrencyOperationDBAdapter(context);
            currencyOperationDBAdapter.open();
            List<CurrencyOperation> currencyOperationList = currencyOperationDBAdapter.getCurrencyOperationByOrderID(sale.getOrderId());
            CurrencyReturnsDBAdapter currencyReturnDBAdapter = new CurrencyReturnsDBAdapter(context);
            currencyReturnDBAdapter.open();
            List<CurrencyReturns> currencyReturnsList=currencyReturnDBAdapter.getCurencyReturnBySaleID(sale.getOrderId());
            for (int i = 0; i < currencyOperationList.size(); i++) {
                if(currencyOperationList.get(i).getCurrencyType().equals(currencyTypesList.get(0).getType())){
                    firstTypePaid+=currencyOperationList.get(i).getAmount();
                }if (SETTINGS.enableCurrencies){
                if(currencyOperationList.get(i).getCurrencyType().equals(currencyTypesList.get(1).getType())){
                    secondTypePaid+=currencyOperationList.get(i).getAmount();
                }  else if(currencyOperationList.get(i).getCurrencyType().equals(currencyTypesList.get(2).getType())){
                    thirdTypePaid+=currencyOperationList.get(i).getAmount();
                }else  if(currencyOperationList.get(i).getCurrencyType().equals(currencyTypesList.get(3).getType())){
                    fourthTypePaid+=currencyOperationList.get(i).getAmount();
                }}

            }
            for (int i = 0; i < currencyReturnsList.size(); i++) {
                if(currencyReturnsList.get(i).getCurrency_type()==0){
                    firstTypeReturn+=currencyReturnsList.get(i).getAmount();
                }if (SETTINGS.enableCurrencies) {
                    if (currencyReturnsList.get(i).getCurrency_type() == 1) {
                        secondTypeReturn += currencyReturnsList.get(i).getAmount();
                    } else if (currencyReturnsList.get(i).getCurrency_type() == 2) {
                        thirdTypeReturn += currencyReturnsList.get(i).getAmount();
                    } else if (currencyReturnsList.get(i).getCurrency_type() == 3) {
                        fourthTypeReturn += currencyReturnsList.get(i).getAmount();
                    }
                }
            }
            if(firstTypePaid>0||firstTypeReturn>0){
                currencyType.text += String.valueOf(symbolWithCodeHashMap.valueOf(currencyTypesList.get(0).getType()).getValue()) + newLineL;
                currencyAmount.text += firstTypePaid + newLineL;
                currencyReturned.text+=firstTypeReturn+newLineL;
            }
            if (SETTINGS.enableCurrencies){
            if(secondTypePaid>0||secondTypeReturn>0){
                currencyType.text += String.valueOf(symbolWithCodeHashMap.valueOf(currencyTypesList.get(1).getType()).getValue()) + newLineL;
                currencyAmount.text += secondTypePaid + newLineL;
                currencyReturned.text+=secondTypeReturn+newLineL;
            }
            if(thirdTypePaid>0||thirdTypeReturn>0){
                currencyType.text += String.valueOf(symbolWithCodeHashMap.valueOf(currencyTypesList.get(2).getType()).getValue()) + newLineL;
                currencyAmount.text += thirdTypePaid + newLineL;
                currencyReturned.text+=thirdTypeReturn+newLineL;
            }
            if(fourthTypePaid>0||fourthTypeReturn>0){
                currencyType.text +=String.valueOf(symbolWithCodeHashMap.valueOf(currencyTypesList.get(3).getType()).getValue()) + newLineL;
                currencyAmount.text += fourthTypePaid + newLineL;
                currencyReturned.text+=fourthTypeReturn+newLineL;
            }}
            blocks.add(currencyText.Bold().Left());
            blocks.add(currencyDetails.Bold().Left());
            blocks.add(clear.Left());
            blocks.add(currencyReturned.Left());
            blocks.add(currencyText.Left());
            blocks.add(currencyAmount.Left());
            blocks.add(currencyType.Left());
            blocks.add(lineR.Left());

        if (checks != null) {
            Block b_checks_number = new Block("\u200e" + context.getString(R.string.checks), 30.0f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.33));
            Block b_checks_date = new Block(context.getString(R.string.date), 30.0f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.33));
            Block b_checks_amount = new Block(context.getString(R.string.amount), 30.0f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.33));
            for (Check check : checks) {
                b_checks_number.text += newLineL + check.getCheckNum();
                b_checks_date.text += "\n" + DateConverter.toDate(new Date(check.getCreatedAt().getTime()));
                b_checks_amount.text += "\n" + Util.makePrice(check.getAmount());
            }
            blocks.add(b_checks_amount.Left());
            blocks.add(b_checks_date.Left());
            blocks.add(b_checks_number.Left());

            blocks.add(lineR.Left());
        }
        Block cashier = new Block("\u200e" + context.getString(R.string.cashier)+ " " , 25f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.5));
        Block cashierName = new Block("\u200E" +  user.getFullName(), 25f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.5));

        cashier.Left();
        cashierName.Left();
        blocks.add(cashierName);
        blocks.add(cashier);
     /**   if(SESSION._ORDERS.getCustomer()!=null){
            if(SESSION._ORDERS.getCustomer().getCustomerType().equals(CustomerType.CREDIT)){
                Block fourth_part = new Block("\u200E " + context.getString(R.string.customer_ledger) +
                        ": " + SESSION._ORDERS.CustomerLedger + "\n" + line, 30.0f, Color.BLACK, CONSTANT.PRINTER_PAGE_WIDTH);

                fourth_part.Left();
                blocks.add(fourth_part);
            }
        }**/
        Block date = new Block("\u200e" + context.getString(R.string.date) + " :" + DateConverter.DateToString(sale.getCreatedAt()), 28.0f, Color.BLACK, CONSTANT.PRINTER_PAGE_WIDTH);
        blocks.add(date.Left());
            Block bCopyDate = new Block("\u200E" + context.getString(R.string.copy_date) + ": " + DateConverter.currentDateTime(), 28.0f, Color.BLACK, CONSTANT.PRINTER_PAGE_WIDTH);
            blocks.add(bCopyDate.Left());

        //    if ((int) totalSaved != 0) {
        String s = SETTINGS.currencySymbol;
        Block totSaved = new Block("\u200e" + context.getString(R.string.total_saved) + " :" + String.format(new Locale("en"), "%.2f %s", sale.getTotalSaved(), s), 32.0f, Color.BLACK, CONSTANT.PRINTER_PAGE_WIDTH);
        blocks.add(totSaved.Bold().Left());
        //   }

        Block thanks = new Block(SETTINGS.returnNote, 28.0f, Color.BLACK, CONSTANT.PRINTER_PAGE_WIDTH);
        blocks.add(thanks.Left());

        return make(blocks);
    }

    public Bitmap creditCardInvoice(Order sale, Boolean isCopy, String mainMer) {
        Block lineR = new Block("\u200E" + line, 30.0f, Color.BLACK, CONSTANT.PRINTER_PAGE_WIDTH);
        int count =0;
        List<Block> blocks = new ArrayList<Block>();
        blocks.addAll(Head(sale));
        Block clear = new Block("\u200E" + "" + "\u200E", 1.0f, Color.BLACK, Paint.Align.CENTER, CONSTANT.PRINTER_PAGE_WIDTH);
        String status = context.getString(R.string.source_invoice);
        if (isCopy)
            status = context.getString(R.string.copy_invoice);
        Block inum = new Block("\u200E" + status + "\n" + "\u200E" + context.getString(R.string.invoice_with_tax) + String.format(" %06d ", sale.getOrderId()) + "\n"
                + line, 35.0f, Color.BLACK, Paint.Align.LEFT, CONSTANT.PRINTER_PAGE_WIDTH);
        blocks.add(inum);


        Block name = new Block("\u200E" + context.getString(R.string.product) + newLineL, 25f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.5));
        Block counter = new Block("\u200E" + context.getString(R.string.qty) + "\n", 25f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.14));
        Block unitPrice = new Block("\u200E" + context.getString(R.string.price) + "\n", 25f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.2));
        Block price = new Block("\u200E" + context.getString(R.string.total) + "\n", 25f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.14));

        double SaleOriginalityPrice = 0, saleTotalPrice = 0;
        double totalSaved = 0.0;
        for (OrderDetails o : sale.getOrders()) {
            count+=o.getQuantity();
            if (o.getProduct().getDisplayName().equals("General"))
                o.getProduct().setProductCode(context.getString(R.string.general));
            int cut = 11;
            if (o.getProduct().getDisplayName().length() < cut)
                cut = o.getProduct().getDisplayName().length();
            name.text += (o.getProduct().getDisplayName().substring(0, cut) + newLineL);
            counter.text += o.getQuantity() + "\n";
            unitPrice.text +=String.format(new Locale("en"), "%.2f", o.getUnitPrice()) + "\n";
            price.text += String.format(new Locale("en"), "%.2f", o.getItemTotalPrice()) + "\n";
            SaleOriginalityPrice += (o.getUnitPrice() * o.getQuantity());
            saleTotalPrice += o.getItemTotalPrice();
        }
        totalSaved = (SaleOriginalityPrice - saleTotalPrice);
        blocks.add(price.Left());
        blocks.add(unitPrice.Left());
        blocks.add(counter.Left());
        name.Left();
        blocks.add(name);
        Block productCountText = new Block("\u200E" + context.getString(R.string.product_quantity), 25f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.75));
        Block productCount = new Block("\u200E" + String.valueOf(count), 25f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.25));
        Block toPidText = new Block("\u200E" + context.getString(R.string.total_price),40f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.75));
        Block toPid = new Block(String.format(new Locale("en"), "%.2f", sale.getTotalPrice()), 35f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.25));
        productCount.Left();
        productCountText.Left();
        toPid.Left();
        toPidText.Left();
        toPid.Bold();
        toPidText.Bold();
        blocks.add(lineR);
        blocks.add(productCount);
        blocks.add(productCountText);
        blocks.add(clear.Left());
        blocks.add(toPid);
        blocks.add(toPidText);
        blocks.add(clear.Left());
        Block addsTax = new Block("\u200E" + context.getString(R.string.tax) + ": "+Util.makePrice(SETTINGS.tax) , 30.0f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.75));
        double noTax = sale.getTotalPrice() / (1 + (SETTINGS.tax / 100));
        Block addsTaxValue = new Block(Util.makePrice(noTax), 30.0f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.25));
        blocks.add(addsTaxValue.Left());
        blocks.add(addsTax.Left());
        blocks.add(clear.Left());
        Block paidBy = new Block("\u200E" + context.getString(R.string.paid_by), 28f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.75));
        Block paidByText = new Block("\u200E" + "", 28f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.25));

        blocks.add(lineR.Left());
        blocks.add(paidByText.Bold().Left());
        blocks.add(paidBy.Bold().Left());
        blocks.add(clear.Left());
        //pid and price
        Block b_payment = new Block("\u200e" + context.getString(R.string.payment) + newLineL + "", 28.0f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.33));
        Block b_total = new Block(context.getString(R.string.total) + "\n" + Util.makePrice(sale.getTotalPrice()), 28.0f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.21));
        Block b_given = new Block(context.getString(R.string.given) + "\n" + Util.makePrice(sale.getTotalPaidAmount()), 28.0f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.21));
        double calcReturned = (sale.getTotalPaidAmount() - sale.getTotalPrice());
        Block b_returned = new Block(context.getString(R.string.returned) + "\n" + Util.makePrice(calcReturned), 28.0f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.25));


        blocks.add(b_returned.Left());
        blocks.add(b_given.Left());
        blocks.add(b_total.Left());
        blocks.add(b_payment.Left());

        blocks.add(lineR.Left());

        Block cashier = new Block("\u200e" + context.getString(R.string.cashier)+ " " , 25f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.5));
        Block cashierName = new Block("\u200E" +  sale.getUser().getFullName(), 25f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.5));

        cashier.Left();
        cashierName.Left();
        blocks.add(cashierName);
        blocks.add(cashier);
        Block date = new Block("\u200e" + context.getString(R.string.date) + " :" + DateConverter.DateToString(sale.getCreatedAt()), 28.0f, Color.BLACK, CONSTANT.PRINTER_PAGE_WIDTH);
        blocks.add(date.Left());
        if(isCopy){
            Block bCopyDate = new Block("\u200E" + context.getString(R.string.copy_date) + ": " + DateConverter.currentDateTime(), 28.0f, Color.BLACK, CONSTANT.PRINTER_PAGE_WIDTH);
            blocks.add(bCopyDate.Left());
        }
        if ((int) totalSaved != 0) {
            Block totSaved = new Block("\u200e" + context.getString(R.string.total_saved) + " :" + String.format(new Locale("en"), "%.2f", sale.getTotalSaved()) + " " + SETTINGS.currencySymbol, 32.0f, Color.BLACK, CONSTANT.PRINTER_PAGE_WIDTH);
            blocks.add(totSaved.Bold().Left());
        }

        String str = "";
        for (String s : mainMer.split("\n")) {
            if(!s.replaceAll(" ","").equals("")) {
                if(s.contains("Powered")){
                    continue;
                }
                if(s.contains("מספר כרטיס")){
                    if(s.split("\\s+")[1].length()>4){
                        String head = "מספר כרטיס";
                        String ss = "";
                        for(int i=0;i<s.split("\\s+")[1].length()-4;i++) {
                            ss += "*";
                        }
                        ss += s.split("\\s+")[1].substring(s.split("\\s+")[1].length() - 4, s.split("\\s+")[1].length());

                        str += "\u200E" + ss + "\t" + head + "\n";
                        continue;
                    }
                }
                str += "\u200E" + s + "\n";
            }
            Log.i("cc row", s);
        }
        Block CC = new Block("\u200E" + str, 25.0f, Color.BLACK).Left();

        blocks.add(CC);

        Block thanks = new Block("\u200e" + SETTINGS.returnNote, 28.0f, Color.BLACK, CONSTANT.PRINTER_PAGE_WIDTH);
        blocks.add(thanks.Left());

        return make(blocks);
    }
    public Bitmap cancelingInvoice(Order sale,List<OrderDetails>orderDetailsList, Boolean isCopy, List<Check> checks) {
       /* OrderDetailsDBAdapter orderDetailsDBAdapter = new OrderDetailsDBAdapter(context);
        orderDetailsDBAdapter.open();
        List<OrderDetails>orderDetailsList=orderDetailsDBAdapter.getOrderBySaleID(sale.ge);*/
   Log.d("orderDetailsListCan",orderDetailsList.toString());

        CashPaymentDBAdapter cashPaymentDBAdapter = new CashPaymentDBAdapter(context);
        cashPaymentDBAdapter.open();
        ChecksDBAdapter checksDBAdapter =new ChecksDBAdapter(context);
        checksDBAdapter.open();
        CreditCardPaymentDBAdapter creditCardPaymentDBAdapter =new CreditCardPaymentDBAdapter(context);
        creditCardPaymentDBAdapter.open();

        Block clear = new Block("\u200E" + "" + "\u200E", 1.0f, Color.BLACK, Paint.Align.CENTER, CONSTANT.PRINTER_PAGE_WIDTH);
        Block lineR = new Block("\u200E" + line, 30.0f, Color.BLACK, CONSTANT.PRINTER_PAGE_WIDTH);
        int count =0;
        List<Block> blocks = new ArrayList<Block>();
        blocks.addAll(Head(sale));
        String status = context.getString(R.string.source_invoice);
        if (isCopy)
            status = context.getString(R.string.copy_invoice);
        Block inum = new Block("\u200E" + status + "\n" + "\u200E" + context.getString(R.string.credit_invoice) + String.format(" %06d ", sale.getOrderId()) + "\n"
                + line, 35.0f, Color.BLACK, Paint.Align.LEFT, CONSTANT.PRINTER_PAGE_WIDTH);
        blocks.add(inum);


        Block name = new Block("\u200E" + context.getString(R.string.product) + newLineL, 25f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.35));
        Block counter = new Block("\u200E" + context.getString(R.string.qty) + "\n", 25f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.14));
        Block unitPrice = new Block("\u200E" + context.getString(R.string.price) + "\n", 25f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.14));
        Block price = new Block("\u200E" + context.getString(R.string.total) + "\n", 25f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.2));
        Block discount = new Block("\u200E" + "%" + "\n", 25f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.14));
        double SaleOriginalityPrice = 0, saleTotalPrice = 0;
        double totalSaved = 0.0;
        double price_before_tax=0;
        for (OrderDetails o : orderDetailsList) {

            price_before_tax+=o.getPaidAmountAfterTax();
            ProductDBAdapter productDBAdapter =new ProductDBAdapter(context);
            productDBAdapter.open();
            Product p= productDBAdapter.getProductByID(o.getProductId());
            o.setProduct(p);
            count += o.getQuantity();
            if (o.getProduct().getDisplayName().equalsIgnoreCase("General"))
                o.getProduct().setProductCode(context.getString(R.string.general));
            int cut = 11;
            if (o.getProduct().getDisplayName().length() < cut)
                cut = o.getProduct().getDisplayName().length();
            name.text += (o.getProduct().getDisplayName().substring(0, cut) + newLineL);
            counter.text += o.getQuantity() + "\n";
            unitPrice.text += String.format(new Locale("en"), "%.2f", o.getUnitPrice()) + "\n";
            price.text += String.format(new Locale("en"), "%.2f", o.getItemTotalPrice()) + "\n";
            discount.text += String.format(new Locale("en"), "%.2f", o.getDiscount()) + "\n";
            SaleOriginalityPrice += (o.getItemTotalPrice() );
            saleTotalPrice += o.getUnitPrice();

        }
        Log.d("priceBeforCancle",price_before_tax+"");
 Log.d("SaleOrieCancle",SaleOriginalityPrice+"");
        Log.d("saleTotalPriceCancle",saleTotalPrice+"");
        totalSaved = (SaleOriginalityPrice - saleTotalPrice);
        Log.d("cancleInvoiceTotalSaved",totalSaved+"");
        Log.d("discauvc",sale.cartDiscount+"");


        blocks.add(discount.Left());
        blocks.add(price.Left());
        blocks.add(unitPrice.Left());
        blocks.add(counter.Left());
        name.Left();
        blocks.add(name);
        Block productCountText = new Block("\u200E" + context.getString(R.string.product_quantity), 25f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.75));
        Block productCount = new Block("\u200E" + String.valueOf(count), 25f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.25));
        Block toPidText = new Block("\u200E" + context.getString(R.string.total_price),40f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.75));
        Block discountText = new Block("\u200E" + context.getString(R.string.discount), 25f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.75));
        Block toPidTextBeforeDiscount = new Block("\u200E" + context.getString(R.string.price_before_discount),25f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.75));
        Block toPid = new Block(String.format(new Locale("en"), "%.2f", sale.getTotalPrice()), 35f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.25));
        Block discountAmount = new Block("\u200E" + String.format(new Locale("en"), "%.2f",sale.cartDiscount)+ "%", 25f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.25));

        Block toPidBeforeDiscount= new Block(String.format(new Locale("en"), "%.2f", sale.getTotalPrice()*100/(100-sale.cartDiscount)), 35f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.25));
        productCount.Left();
        productCountText.Left();


        toPid.Left();
        toPidText.Left();
        discountText.Left();
        discountAmount.Left();
        toPidTextBeforeDiscount.Left();
        toPidBeforeDiscount.Left();
        toPid.Bold();
        toPidText.Bold();
        blocks.add(lineR);
        blocks.add(productCount);
        blocks.add(productCountText);
        blocks.add(clear.Left());
        if(sale.cartDiscount>0) {
            blocks.add(discountAmount);
            blocks.add(discountText);
            blocks.add(clear.Left());
            blocks.add(toPidBeforeDiscount);
            blocks.add(toPidTextBeforeDiscount);
            blocks.add(clear.Left());
        }
        blocks.add(toPid);
        blocks.add(toPidText);
        blocks.add(clear.Left());


        Block addsTax = new Block("\u200E" + context.getString(R.string.tax) + ": "+Util.makePrice(SETTINGS.tax)+"%" , 25f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.75));
        Block priceBeforeTaxText = new Block("\u200E" + context.getString(R.string.price_before_tax) , 25f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.75));

        double noTax =price_before_tax - (price_before_tax * (sale.cartDiscount/100));
        double totalPriceAfterDiscount= SaleOriginalityPrice- (SaleOriginalityPrice * (sale.cartDiscount/100));
        Log.d("noTaxCan",noTax+"");
        Log.d("totalPriceAfterDiscouCanc",totalPriceAfterDiscount+"");
        Log.d("priceBefor",totalPriceAfterDiscount-noTax+"");
        Block addsTaxValue = new Block(Util.makePrice(totalPriceAfterDiscount-noTax), 25f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.25));
        Block priceBeforeTax = new Block(Util.makePrice(noTax), 25f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.25));

        // Block numTax = new Block("\u200E" + String.format(new Locale("en"), "\u200E%.2f\n\u200E%.2f\n\u200E%.2f", noTax * (SETTINGS.tax / 100), 0.0f), 30.0f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.25));
        //blocks.add(numTax.Left());
        blocks.add(addsTaxValue.Left());
        blocks.add(addsTax.Left());
        blocks.add(clear.Left());

        blocks.add(priceBeforeTax.Left());
        blocks.add(priceBeforeTaxText.Left());
        blocks.add(clear.Left());







     /*   Block paidBy = new Block("\u200E" + context.getString(R.string.paid_by), 28f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.75));
        Block paidByText = new Block("\u200E" + "", 28f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.25));
        *//*blocks.add(lineR.Left());
        blocks.add(paidByText.Bold().Left());
        blocks.add(paidBy.Bold().Left());
        blocks.add(clear.Left());*//*
        //pid and price
        Block b_payment = new Block("\u200e" + context.getString(R.string.payment) + newLineL + "", 32.0f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.332));
        Block b_total = new Block(context.getString(R.string.total) + "\n" + Util.makePrice(sale.getTotalPrice()), 32.0f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.21));
        Block b_given = new Block(context.getString(R.string.given) + "\n" + Util.makePrice(sale.getTotalPaidAmount()), 32.0f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.21));
        double calcReturned = 0;
        if (sale.getTotalPrice() < 0 && sale.getTotalPaidAmount() >= 0) {
            calcReturned = (sale.getTotalPrice() + sale.getTotalPaidAmount());
        } else {
            calcReturned = (sale.getTotalPaidAmount() - sale.getTotalPrice());
            if (calcReturned < 0) {
                calcReturned = 0;
            }
        }
        Block b_returned = new Block("\u200E" + context.getString(R.string.returned) + "\n" + Util.makePrice(calcReturned), 28.0f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.25));
        blocks.add(lineR.Left());
        blocks.add(paidByText.Bold().Left());
        blocks.add(paidBy.Bold().Left());
        blocks.add(clear.Left());
        blocks.add(b_returned.Left());
        blocks.add(b_given.Left());
        blocks.add(b_total.Left());
        blocks.add(b_payment.Left());
        blocks.add(lineR.Left());*/
        Block paidBy = new Block("\u200E" + context.getString(R.string.paid_by), 28f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.75));
        Block paidByText = new Block("\u200E" + "", 28f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.25));


        Block b_payment = new Block("\u200e" + context.getString(R.string.payment)  , 28.0f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.33));
        Block b_total = new Block("\u200E" + context.getString(R.string.total) + "\n" + Util.makePrice(sale.getTotalPrice()), 28.0f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.21));
        Block b_given = new Block("\u200E" + context.getString(R.string.given) + "\n" + Util.makePrice(sale.getTotalPaidAmount()), 28.0f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.21));
        double calcReturned = 0;
        if (sale.getTotalPrice() < 0 && sale.getTotalPaidAmount() >= 0) {
            calcReturned = (sale.getTotalPrice() + sale.getTotalPaidAmount());
        } else {
            calcReturned = (sale.getTotalPaidAmount() - sale.getTotalPrice());
            if (calcReturned < 0) {
                calcReturned = 0;
            }
        }
        Block b_returned = new Block("\u200E" + context.getString(R.string.rest), 28.0f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.25));
        blocks.add(lineR.Left());
        blocks.add(paidByText.Bold().Left());
        blocks.add(paidBy.Bold().Left());
        blocks.add(clear.Left());
        blocks.add(b_returned.Left());
        blocks.add(b_given.Left());
        blocks.add(b_total.Left());
        blocks.add(b_payment.Left());
        blocks.add(clear.Left());

        double cash_plus = 0;
        double check_plus = 0;
        double creditCard_plus = 0;
        List<CashPayment>cashPaymentList=cashPaymentDBAdapter.getPaymentBySaleID(sale.getOrderId());
        for(int i=0;i<cashPaymentList.size();i++){
            cash_plus+=cashPaymentList.get(i).getAmount()*cashPaymentList.get(i).getCurrencyRate();
        }
        List<Check>checkList=checksDBAdapter.getPaymentBySaleID(sale.getOrderId());
        for(int i=0;i<checkList.size();i++){
            check_plus+=checkList.get(i).getAmount();
        }
        List<CreditCardPayment>creditCardPayments=creditCardPaymentDBAdapter.getPaymentByOrderID(sale.getOrderId());
        for(int i=0;i<creditCardPayments.size();i++){
            creditCard_plus+=creditCardPayments.get(i).getAmount();
        }
        if(cashPaymentList.size()>0){
            Block b_payment_Cash = new Block("\u200e" + context.getString(R.string.cash)  , 28.0f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.33));
            Block b_total_Cash = new Block("\u200E" +  Util.makePrice(sale.getTotalPrice()*-1), 28.0f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.21));
            Block b_given_Cash = new Block("\u200E" + Util.makePrice(cash_plus*-1), 28.0f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.21));
            Block b_returned_Cash = new Block("\u200E" + Util.makePrice((sale.getTotalPrice()-cash_plus)*-1), 28.0f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.25));
            blocks.add(clear.Left());
            blocks.add(b_returned_Cash.Left());
            blocks.add(b_given_Cash.Left());
            blocks.add(b_total_Cash.Left());
            blocks.add(b_payment_Cash.Left());
            blocks.add(clear.Left());
            blocks.add(lineR.Left());

        }
        if(checkList.size()>0){
            Block b_payment_Check = new Block("\u200e" + context.getString(R.string.checks)  , 28.0f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.33));
            Block b_total_Check = new Block("\u200E" +  Util.makePrice(sale.getTotalPrice()*-1), 28.0f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.21));
            Block b_given_Check = new Block("\u200E" + check_plus*-1, 28.0f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.21));
            Block b_returned_Check = new Block("\u200E" + Util.makePrice((sale.getTotalPrice()-check_plus)*-1), 28.0f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.25));
            blocks.add(clear.Left());
            blocks.add(b_returned_Check.Left());
            blocks.add(b_given_Check.Left());
            blocks.add(b_total_Check.Left());
            blocks.add(b_payment_Check.Left());
            blocks.add(clear.Left());
            blocks.add(lineR.Left());

        }
        if(creditCardPayments.size()>0) {
            Block b_payment_Credit = new Block("\u200e" + context.getString(R.string.credit_card), 28.0f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.33));
            Block b_total_Credit = new Block("\u200E" + Util.makePrice(sale.getTotalPrice()*-1), 28.0f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.21));
            Block b_given_Credit = new Block("\u200E" + creditCard_plus*-1, 28.0f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.21));
            Block b_returned_Credit = new Block("\u200E" + Util.makePrice((sale.getTotalPrice() - creditCard_plus)*-1), 28.0f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.25));
            blocks.add(clear.Left());
            blocks.add(b_returned_Credit.Left());
            blocks.add(b_given_Credit.Left());
            blocks.add(b_total_Credit.Left());
            blocks.add(b_payment_Credit.Left());
            blocks.add(clear.Left());
            blocks.add(lineR.Left());
        }




            List<CurrencyType> currencyTypesList = null;
            CurrencyTypeDBAdapter currencyTypeDBAdapter = new CurrencyTypeDBAdapter(context);
            currencyTypeDBAdapter.open();
            currencyTypesList = currencyTypeDBAdapter.getAllCurrencyType();
            currencyTypeDBAdapter.close();

            double firstTypePaid =0,firstTypeReturn =0 ,secondtypePaid=0 , secondTypeReturn=0,thirdTypePaid=0,thirdTypeReturn=0,fourthTypePaid=0, fourthTypeReturn=0;
            Block currencyDetails = new Block("\u200E" + context.getString(R.string.currency), 28f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.75));
            Block currencyText = new Block("\u200E" + "", 28f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.25));

            Block currencyType = new Block("\u200E" + context.getString(R.string.type) + newLineL, 28.0f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.21));
            Block currencyAmount = new Block("\u200E" + context.getString(R.string.given) + newLineL, 28.0f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.21));
            Block currencyReturned = new Block("\u200E" + context.getString(R.string.returned) + newLineL, 28.0f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.33));

            CurrencyOperationDBAdapter currencyOperationDBAdapter = new CurrencyOperationDBAdapter(context);
            currencyOperationDBAdapter.open();
        Log.d("slajojo",sale.getCancellingOrderId()+"jojojjjjj");
            List<CurrencyOperation> currencyOperationList = currencyOperationDBAdapter.getCurrencyOperationByOrderID(sale.getCancellingOrderId());
            CurrencyReturnsDBAdapter currencyReturnDBAdapter = new CurrencyReturnsDBAdapter(context);
            currencyReturnDBAdapter.open();
            List<CurrencyReturns> currencyReturnsList=currencyReturnDBAdapter.getCurencyReturnBySaleID(sale.getCancellingOrderId());
            for (int i = 0; i < currencyOperationList.size(); i++) {
                if(currencyOperationList.get(i).getCurrencyType().equals(currencyTypesList.get(0).getType())){
                    firstTypePaid+=currencyOperationList.get(i).getAmount();
                }if (SETTINGS.enableCurrencies) {
                    if (currencyOperationList.get(i).getCurrencyType().equals(currencyTypesList.get(1).getType())) {
                        secondtypePaid += currencyOperationList.get(i).getAmount();
                    } else if (currencyOperationList.get(i).getCurrencyType().equals(currencyTypesList.get(2).getType())) {
                        thirdTypePaid += currencyOperationList.get(i).getAmount();
                    } else if (currencyOperationList.get(i).getCurrencyType().equals(currencyTypesList.get(3).getType())) {
                        fourthTypePaid += currencyOperationList.get(i).getAmount();
                    }
                }
            }
            for (int i = 0; i < currencyReturnsList.size(); i++) {
                if(currencyReturnsList.get(i).getCurrency_type()==0){
                    firstTypeReturn+=currencyReturnsList.get(i).getAmount();
                }if (SETTINGS.enableCurrencies){
                if(currencyReturnsList.get(i).getCurrency_type()==1){
                    secondTypeReturn+=currencyReturnsList.get(i).getAmount();
                }  else if(currencyReturnsList.get(i).getCurrency_type()==2){
                    thirdTypeReturn+=currencyReturnsList.get(i).getAmount();
                }else  if(currencyReturnsList.get(i).getCurrency_type()==3){
                    fourthTypeReturn+=currencyReturnsList.get(i).getAmount();
                }}

            }
            if(firstTypePaid>0||firstTypeReturn>0){
                currencyType.text += String.valueOf(symbolWithCodeHashMap.valueOf(currencyTypesList.get(0).getType()).getValue()) + newLineL;
                currencyAmount.text += (firstTypePaid*-1)+ newLineL;
                currencyReturned.text+=(firstTypeReturn*-1)+newLineL;
            }
            if (SETTINGS.enableCurrencies){
            if(secondtypePaid>0||secondTypeReturn>0){
                currencyType.text += String.valueOf(symbolWithCodeHashMap.valueOf(currencyTypesList.get(1).getType()).getValue()) + newLineL;
                currencyAmount.text += (secondtypePaid*-1) + newLineL;
                currencyReturned.text+=(secondTypeReturn*-1)+newLineL;
            }
            if(thirdTypePaid>0||thirdTypeReturn>0){
                currencyType.text += String.valueOf(symbolWithCodeHashMap.valueOf(currencyTypesList.get(2).getType()).getValue())+ newLineL;
                currencyAmount.text += (thirdTypePaid*-1) + newLineL;
                currencyReturned.text+=(thirdTypeReturn*-1)+newLineL;
            }
            if(fourthTypePaid>0||fourthTypeReturn>0){
                currencyType.text += String.valueOf(symbolWithCodeHashMap.valueOf(currencyTypesList.get(3).getType()).getValue()) + newLineL;
                currencyAmount.text += (fourthTypePaid*-1) + newLineL;
                currencyReturned.text+=(fourthTypeReturn*-1)+newLineL;
            }}
            if(firstTypeReturn==0&&secondTypeReturn==0&&thirdTypeReturn==0&&fourthTypeReturn==0){

            }
            blocks.add(currencyText.Bold().Left());
            blocks.add(currencyDetails.Bold().Left());
            blocks.add(clear.Left());
            blocks.add(currencyReturned.Left());
            blocks.add(currencyText.Left());
            blocks.add(currencyAmount.Left());
            blocks.add(currencyType.Left());
            blocks.add(lineR.Left());

        if (checks != null) {
            Block b_checks_number = new Block("\u200e" + context.getString(R.string.checks), 30.0f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.33));
            Block b_checks_date = new Block(context.getString(R.string.date), 30.0f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.33));
            Block b_checks_amount = new Block(context.getString(R.string.amount), 30.0f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.33));
            for (Check check : checks) {
                b_checks_number.text += newLineL + check.getCheckNum();
                b_checks_date.text += "\n" + DateConverter.toDate(new Date(check.getCreatedAt().getTime()));
                b_checks_amount.text += "\n" + Util.makePrice((check.getAmount()*-1));
            }
            blocks.add(b_checks_amount.Left());
            blocks.add(b_checks_date.Left());
            blocks.add(b_checks_number.Left());

            blocks.add(lineR.Left());
        }
        Block cashier = new Block("\u200e" + context.getString(R.string.cashier)+ " " , 25f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.5));
        Block cashierName = new Block("\u200E" +  SESSION._EMPLOYEE.getFullName(), 25f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.5));

        cashier.Left();
        cashierName.Left();
        blocks.add(cashierName);
        blocks.add(cashier);
        Block date = new Block("\u200e" + context.getString(R.string.date) + " :" + DateConverter.DateToString(sale.getCreatedAt()), 28.0f, Color.BLACK, CONSTANT.PRINTER_PAGE_WIDTH);
        blocks.add(date.Left());
        if (isCopy) {
            Block bCopyDate = new Block("\u200E" + context.getString(R.string.copy_date) + ": " + DateConverter.currentDateTime(), 28.0f, Color.BLACK, CONSTANT.PRINTER_PAGE_WIDTH);
            blocks.add(bCopyDate.Left());
        }
        Block totSaved = new Block("\u200e" + context.getString(R.string.total_saved) + " :" +(sale.getTotalSaved()*-1), 32.0f, Color.BLACK, CONSTANT.PRINTER_PAGE_WIDTH);
        blocks.add(totSaved.Bold().Left());
        Block thanks = new Block(SETTINGS.returnNote, 28.0f, Color.BLACK, CONSTANT.PRINTER_PAGE_WIDTH);
        blocks.add(thanks.Left());

        return make(blocks);
    }
    public Bitmap replacmentNote(Order sale, boolean isCopy) {
        int count=0;
        Block clear = new Block("\u200E" + "" + "\u200E", 1.0f, Color.BLACK, Paint.Align.CENTER, CONSTANT.PRINTER_PAGE_WIDTH);
        List<Block> blocks = new ArrayList<Block>();
        blocks.addAll(Head(sale));
        String status = context.getString(R.string.source_invoice);
        if (isCopy)
            status = context.getString(R.string.copy_invoice);
        Block inum = new Block("\u200E" + status + "\n" + "\u200E" + context.getString(R.string.replacement_invoice) + String.format(" %06d ", sale.getOrderId()) + "\n"
                + line, 35.0f, Color.BLACK, Paint.Align.LEFT, CONSTANT.PRINTER_PAGE_WIDTH);
        blocks.add(inum);

        Block name = new Block("\u200e" + context.getString(R.string.product) + "\n", 25f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.5));
        Block counter =new Block("\u200e" + context.getString(R.string.qty) +   "\n", 25f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.5));
        //Block unitPrice = new Block("\u200E" + context.getString(R.string.price) + "\n", 25f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.14));
    //    Block price = new Block("\u200E" + context.getString(R.string.total) + "\n", 25f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.2));
      //  Block discount = new Block("\u200E" + "%" + "\n", 25f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.14));
        OrderDetailsDBAdapter orderDetailsDBAdapter = new OrderDetailsDBAdapter(context);
        orderDetailsDBAdapter.open();
        List<OrderDetails>orderDetailsList=orderDetailsDBAdapter.getOrderBySaleID(sale.getOrderId());
        for (OrderDetails o :orderDetailsList) {
            ProductDBAdapter productDBAdapter =new ProductDBAdapter(context);
            productDBAdapter.open();
            Product p= productDBAdapter.getProductByID(o.getProductId());
            o.setProduct(p);
            count=o.getQuantity();
            if (o.getProduct().getDisplayName().equals("General"))
                o.getProduct().setProductCode(context.getString(R.string.general));
            int cut = 11;
            if (o.getProduct().getDisplayName().length() < cut)
                cut = o.getProduct().getDisplayName().length();
            name.text += (o.getProduct().getDisplayName().substring(0, cut) + newLineL);
            counter.text += o.getQuantity() + "\n";
          //  unitPrice.text += String.format(new Locale("en"), "%.2f", o.getUnitPrice()) + "\n";
          //  price.text += String.format(new Locale("en"), "%.2f", o.getItemTotalPrice()) + "\n";
        }
      //  blocks.add(discount.Left());
      //  blocks.add(price.Left());
      //  blocks.add(unitPrice.Left());
        blocks.add(counter.Left());
        name.Left();
        blocks.add(name);
        Block lineR = new Block("\u200E" + line, 30.0f, Color.BLACK, CONSTANT.PRINTER_PAGE_WIDTH);
        Block productCountText = new Block("\u200E" + context.getString(R.string.product_quantity), 25f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.5));
        Block productCount = new Block("\u200E" + String.valueOf(count), 25f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.5));
      //  Block toPidText = new Block("\u200E" + context.getString(R.string.total_price),40f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.75));
      //  Block toPid = new Block(String.format(new Locale("en"), "%.2f", sale.getTotalPrice()), 35f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.25));
        productCount.Left();
        productCountText.Left();
     //   toPid.Left();
     //   toPidText.Left();
     //   toPid.Bold();
     //   toPidText.Bold();
        blocks.add(lineR);
        blocks.add(productCount);
        blocks.add(productCountText);
        blocks.add(clear.Left());
       // blocks.add(toPid);
      //  blocks.add(toPidText);
    //    blocks.add(clear.Left());
       // Block addsTax = new Block("\u200E" + context.getString(R.string.tax) + ": "+Util.makePrice(SETTINGS.tax)+ "%" , 30.0f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.75));
      //  double noTax = sale.getTotalPrice() / (1 + (SETTINGS.tax / 100));
    //    Block addsTaxValue = new Block(Util.makePrice(noTax), 30.0f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.25));
    //    blocks.add(addsTaxValue.Left());
    //    blocks.add(addsTax.Left());
     //   blocks.add(clear.Left());
        blocks.add(lineR.Left());
        Block cashierName;
        Block cashier = new Block("\u200e" + context.getString(R.string.cashier)+ " " , 25f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.5));
        if(sale.getUser()==null){
             cashierName = new Block("\u200E" +  "master master", 25f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.5));

        }else {
             cashierName = new Block("\u200E" + sale.getUser().getFullName(), 25f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.5));
        }
        cashier.Left();
        cashierName.Left();
        blocks.add(cashierName);
        blocks.add(cashier);

      /*  if(sale.getCustomer().getCustomerType().equals(CustomerType.CREDIT)){
            Block fourth_part = new Block("\u200E " + context.getString(R.string.customer_ledger) +
                    ": " + SESSION._ORDERS.CustomerLedger + "\n" + line, 30.0f, Color.BLACK, CONSTANT.PRINTER_PAGE_WIDTH);

            fourth_part.Left();
            blocks.add(fourth_part);
        }*/
        Block date = new Block("\u200e" + context.getString(R.string.date) + " :" + DateConverter.DateToString(sale.getCreatedAt()), 28.0f, Color.BLACK, CONSTANT.PRINTER_PAGE_WIDTH);
        blocks.add(date.Left());

        Block bCopyDate = new Block("\u200E" + context.getString(R.string.copy_date) + ": " + DateConverter.currentDateTime(), 28.0f, Color.BLACK, CONSTANT.PRINTER_PAGE_WIDTH);
        blocks.add(bCopyDate.Left());


        return make(blocks);
    }

    public Bitmap Invoice( List<OrderDetails> orders, Order sale, boolean isCopy, Employee user , String invoiceNum,double customerGeneralLedger) {
        int count =0;
        List<Block> blocks = new ArrayList<Block>();
        Block inum;
        blocks.addAll(Head(sale));
        Block lineR = new Block("\u200E" + line + "\u200E", 30.0f, Color.BLACK, Paint.Align.CENTER, CONSTANT.PRINTER_PAGE_WIDTH);
        Block clear = new Block("\u200E" + "" + "\u200E", 1.0f, Color.BLACK, Paint.Align.CENTER, CONSTANT.PRINTER_PAGE_WIDTH);
        String status=context.getString(R.string.source_invoice);
        if(isCopy)
            status=context.getString(R.string.copy_invoice);
        Block bStatus = new Block("\u200F" + status, 35.0f, Color.BLACK, Paint.Align.CENTER, CONSTANT.PRINTER_PAGE_WIDTH);
        if (SETTINGS.company.name().equals("BO_EXEMPT_DEALER")){
            inum = new Block("\u200E"+ context.getString(R.string.invoice_company_status)+": " +invoiceNum
                    , 28.0f, Color.BLACK, Paint.Align.LEFT, CONSTANT.PRINTER_PAGE_WIDTH);
        }
        else {
         inum = new Block("\u200E"+ context.getString(R.string.invoice)+": " +invoiceNum
                , 28.0f, Color.BLACK, Paint.Align.LEFT, CONSTANT.PRINTER_PAGE_WIDTH);}
        blocks.add(bStatus);
        blocks.add(inum);
        blocks.add(lineR);
        Block name = new Block("\u200E" + context.getString(R.string.product) + newLineL, 25f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.35));
        Block counter = new Block("\u200E" + context.getString(R.string.qty) + "\n", 25f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.14));
        Block unitPrice = new Block("\u200E" + context.getString(R.string.price) + "\n", 25f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.14));
        Block price = new Block("\u200E" + context.getString(R.string.total) + "\n", 25f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.2));
        Block discount = new Block("\u200E" + "%" + "\n", 25f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.14));

        double SaleOriginalityPrice = 0, saleTotalPrice = 0;
        double totalSaved = 0.0;
        for (OrderDetails o : orders) {
            count+=o.getQuantity();
            if (o.getProduct().getDisplayName().equals("General"))
                o.getProduct().setProductCode(context.getString(R.string.general));
            int cut = 11;
            if (o.getProduct().getDisplayName().length() < cut)
                cut = o.getProduct().getDisplayName().length();
            name.text += (o.getProduct().getDisplayName().substring(0, cut) + newLineL);
            counter.text += o.getQuantity() + "\n";
            unitPrice.text +=String.format(new Locale("en"), "%.2f", o.getUnitPrice()) + "\n";
            price.text += String.format(new Locale("en"), "%.2f", o.getItemTotalPrice()) + "\n";
            discount.text += String.format(new Locale("en"), "%.2f", o.getDiscount()) + "\n";

            SaleOriginalityPrice += (o.getUnitPrice() * o.getQuantity());
            saleTotalPrice += o.getItemTotalPrice();
        }
        totalSaved = (SaleOriginalityPrice - saleTotalPrice);
        blocks.add(discount.Left());
        blocks.add(price.Left());
        blocks.add(unitPrice.Left());
        blocks.add(counter.Left());
        //name.text = String.format(new Locale("he"), name.text);
        name.Left();
        blocks.add(name);
        Block productCountText = new Block("\u200E" + context.getString(R.string.product_quantity), 25f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.75));
        Block productCount = new Block("\u200E" + String.valueOf(count), 25f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.25));
        Block toPidText = new Block("\u200E" + context.getString(R.string.total_price),25f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.75));
        Block discountText = new Block("\u200E" + context.getString(R.string.discount), 25f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.75));


        Block toPidTextBeforeDiscount = new Block("\u200E" + context.getString(R.string.price_before_discount),40f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.75));
        Block toPid = new Block(String.format(new Locale("en"), "%.2f", sale.getTotalPrice()), 35f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.25));
        Block discountAmount = new Block("\u200E" + String.valueOf(sale.cartDiscount)+ "%", 25f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.25));

        Block toPidBeforeDiscount= new Block(String.format(new Locale("en"), "%.2f", sale.getTotalPrice()+(sale.cartDiscount/100)*sale.getTotalPrice()), 35f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.25));
        productCount.Bold();
        productCountText.Bold();
        productCount.Left();
        productCountText.Left();
        toPid.Left();
        toPidText.Left();
        discountText.Left();
        discountAmount.Left();
        toPidTextBeforeDiscount.Left();
        toPidBeforeDiscount.Left();
        toPid.Bold();
        toPidText.Bold();
        blocks.add(lineR);
        blocks.add(productCount);
        blocks.add(productCountText);
        blocks.add(clear.Left());
        blocks.add(toPid);
        blocks.add(toPidText);
        blocks.add(clear.Left());
        if(sale.cartDiscount>0) {
            blocks.add(discountAmount);
            blocks.add(discountText);
            blocks.add(clear.Left());
            blocks.add(toPidBeforeDiscount);
            blocks.add(toPidTextBeforeDiscount);
            blocks.add(clear.Left());
        }
        Block addsTax = new Block("\u200E" + context.getString(R.string.tax) + ": "+Util.makePrice(SETTINGS.tax)+"%"  , 30.0f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.75));
        double noTax = sale.getTotalPrice() / (1 + (SETTINGS.tax / 100));
        Block addsTaxValue = new Block(Util.makePrice(noTax), 30.0f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.25));
        // Block numTax = new Block("\u200E" + String.format(new Locale("en"), "\u200E%.2f\n\u200E%.2f\n\u200E%.2f", noTax * (SETTINGS.tax / 100), 0.0f), 30.0f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.25));
        //blocks.add(numTax.Left());
        blocks.add(addsTaxValue.Left().Bold());
        blocks.add(addsTax.Left().Bold());
        blocks.add(clear.Left());
        //pid and price
        blocks.add(clear.Left());
        blocks.add(lineR.Left());
        Block cashier = new Block("\u200e" + context.getString(R.string.cashier)+ " " , 25f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.5));
        Block cashierName = new Block("\u200E" +  user.getFullName(), 25f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.5));
        cashier.Left();
        cashierName.Left();
        blocks.add(cashierName);
        blocks.add(cashier);
        Block customerGeneralLedgerView = new Block("\u200E" + context.getString(R.string.customer_ledger)+ ":"+Util.makePrice(customerGeneralLedger)+""+ "\n"+ line, 28.0f, Color.BLACK, Paint.Align.LEFT, CONSTANT.PRINTER_PAGE_WIDTH);
        blocks.add(customerGeneralLedgerView);
        Block date = new Block("\u200e" + context.getString(R.string.date) + " :" + DateConverter.DateToString(sale.getCreatedAt()), 28.0f, Color.BLACK, CONSTANT.PRINTER_PAGE_WIDTH);
        blocks.add(date.Left());
        if (isCopy) {
            Block bCopyDate = new Block("\u200E" + context.getString(R.string.copy_date) + ": " + DateConverter.currentDateTime(), 28.0f, Color.BLACK, CONSTANT.PRINTER_PAGE_WIDTH);
            blocks.add(bCopyDate.Left());
        }
        if ((int) totalSaved != 0) {
            String s = SETTINGS.currencySymbol;
            Block totSaved = new Block("\u200e" + context.getString(R.string.total_saved) + " :" +totalSaved, 32.0f, Color.BLACK, CONSTANT.PRINTER_PAGE_WIDTH);
            blocks.add(totSaved.Bold().Left());
        }

        Block thanks = new Block(SETTINGS.returnNote, 28.0f, Color.BLACK, CONSTANT.PRINTER_PAGE_WIDTH);
        blocks.add(thanks.Left());

        return make(blocks);
    }
    public Bitmap OrderDocument( List<OrderDetails> orders, Order sale, boolean isCopy, Employee user , String invoiceNum) {
        int count =0;
        List<Block> blocks = new ArrayList<Block>();
        blocks.addAll(Head(sale));
        Block lineR = new Block("\u200E" + line + "\u200E", 30.0f, Color.BLACK, Paint.Align.CENTER, CONSTANT.PRINTER_PAGE_WIDTH);
        Block clear = new Block("\u200E" + "" + "\u200E", 1.0f, Color.BLACK, Paint.Align.CENTER, CONSTANT.PRINTER_PAGE_WIDTH);
        String status=context.getString(R.string.source_invoice);
        if(isCopy)
            status=context.getString(R.string.copy_invoice);
        Block bStatus = new Block("\u200F" + status, 35.0f, Color.BLACK, Paint.Align.CENTER, CONSTANT.PRINTER_PAGE_WIDTH);
        Block inum = new Block("\u200E"+ context.getString(R.string.order_document)+": " +invoiceNum
                , 28.0f, Color.BLACK, Paint.Align.LEFT, CONSTANT.PRINTER_PAGE_WIDTH);
        blocks.add(bStatus);
        blocks.add(inum);
        blocks.add(lineR);
        Block name = new Block("\u200E" + context.getString(R.string.product) + newLineL, 25f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.35));
        Block counter = new Block("\u200E" + context.getString(R.string.qty) + "\n", 25f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.14));
        Block unitPrice = new Block("\u200E" + context.getString(R.string.price) + "\n", 25f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.14));
        Block price = new Block("\u200E" + context.getString(R.string.total) + "\n", 25f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.2));
        Block discount = new Block("\u200E" + "%" + "\n", 25f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.14));

        double SaleOriginalityPrice = 0, saleTotalPrice = 0;
        double totalSaved = 0.0;
        for (OrderDetails o : orders) {
            count+=o.getQuantity();
            if (o.getProduct().getDisplayName().equals("General"))
                o.getProduct().setProductCode(context.getString(R.string.general));
            int cut = 11;
            if (o.getProduct().getDisplayName().length() < cut)
                cut = o.getProduct().getDisplayName().length();
            name.text += (o.getProduct().getDisplayName().substring(0, cut) + newLineL);
            counter.text += o.getQuantity() + "\n";
            unitPrice.text +=String.format(new Locale("en"), "%.2f", o.getUnitPrice()) + "\n";
            price.text += String.format(new Locale("en"), "%.2f", o.getItemTotalPrice()) + "\n";
            discount.text += String.format(new Locale("en"), "%.2f", o.getDiscount()) + "\n";

            SaleOriginalityPrice += (o.getUnitPrice() * o.getQuantity());
            saleTotalPrice += o.getItemTotalPrice();
        }
        totalSaved = (SaleOriginalityPrice - saleTotalPrice);
        blocks.add(discount.Left());
        blocks.add(price.Left());
        blocks.add(unitPrice.Left());
        blocks.add(counter.Left());
        //name.text = String.format(new Locale("he"), name.text);
        name.Left();
        blocks.add(name);
        Block productCountText = new Block("\u200E" + context.getString(R.string.product_quantity), 25f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.75));
        Block productCount = new Block("\u200E" + String.valueOf(count), 25f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.25));
        Block toPidText = new Block("\u200E" + context.getString(R.string.total_price),25f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.75));
        Block discountText = new Block("\u200E" + context.getString(R.string.discount), 25f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.75));


        Block toPidTextBeforeDiscount = new Block("\u200E" + context.getString(R.string.price_before_discount),40f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.75));
        Block toPid = new Block(String.format(new Locale("en"), "%.2f", sale.getTotalPrice()), 35f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.25));
        Block discountAmount = new Block("\u200E" + String.valueOf(sale.cartDiscount)+ "%", 25f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.25));

        Block toPidBeforeDiscount= new Block(String.format(new Locale("en"), "%.2f", sale.getTotalPrice()+(sale.cartDiscount/100)*sale.getTotalPrice()), 35f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.25));
        productCount.Bold();
        productCountText.Bold();
        productCount.Left();
        productCountText.Left();
        toPid.Left();
        toPidText.Left();
        discountText.Left();
        discountAmount.Left();
        toPidTextBeforeDiscount.Left();
        toPidBeforeDiscount.Left();
        toPid.Bold();
        toPidText.Bold();
        blocks.add(lineR);
        blocks.add(productCount);
        blocks.add(productCountText);
        blocks.add(clear.Left());
        blocks.add(toPid);
        blocks.add(toPidText);
        blocks.add(clear.Left());
        if(sale.cartDiscount>0) {
            blocks.add(discountAmount);
            blocks.add(discountText);
            blocks.add(clear.Left());
            blocks.add(toPidBeforeDiscount);
            blocks.add(toPidTextBeforeDiscount);
            blocks.add(clear.Left());
        }
        Block addsTax = new Block("\u200E" + context.getString(R.string.tax) + ": "+Util.makePrice(SETTINGS.tax)+"%"  , 30.0f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.75));
        double noTax = sale.getTotalPrice() / (1 + (SETTINGS.tax / 100));
        Block addsTaxValue = new Block(Util.makePrice(noTax), 30.0f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.25));
        // Block numTax = new Block("\u200E" + String.format(new Locale("en"), "\u200E%.2f\n\u200E%.2f\n\u200E%.2f", noTax * (SETTINGS.tax / 100), 0.0f), 30.0f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.25));
        //blocks.add(numTax.Left());
        blocks.add(addsTaxValue.Left().Bold());
        blocks.add(addsTax.Left().Bold());
        blocks.add(clear.Left());
        //pid and price
        blocks.add(clear.Left());
        blocks.add(lineR.Left());
        Block cashier = new Block("\u200e" + context.getString(R.string.cashier)+ " " , 25f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.5));
        Block cashierName = new Block("\u200E" +  user.getFullName(), 25f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.5));
        cashier.Left();
        cashierName.Left();
        blocks.add(cashierName);
        blocks.add(cashier);
        Block date = new Block("\u200e" + context.getString(R.string.date) + " :" + DateConverter.DateToString(sale.getCreatedAt()), 28.0f, Color.BLACK, CONSTANT.PRINTER_PAGE_WIDTH);
        blocks.add(date.Left());
        if (isCopy) {
            Block bCopyDate = new Block("\u200E" + context.getString(R.string.copy_date) + ": " + DateConverter.currentDateTime(), 28.0f, Color.BLACK, CONSTANT.PRINTER_PAGE_WIDTH);
            blocks.add(bCopyDate.Left());
        }
        if ((int) totalSaved != 0) {
            String s = SETTINGS.currencySymbol;
            Block totSaved = new Block("\u200e" + context.getString(R.string.total_saved) + " :" + String.format(new Locale("en"), "%.2f %s", sale.getTotalSaved(), s), 32.0f, Color.BLACK, CONSTANT.PRINTER_PAGE_WIDTH);
            blocks.add(totSaved.Bold().Left());
        }

        Block thanks = new Block(SETTINGS.returnNote, 28.0f, Color.BLACK, CONSTANT.PRINTER_PAGE_WIDTH);
        blocks.add(thanks.Left());

        return make(blocks);
    }

    public Bitmap copyInvoice(BoInvoice boInvoice) throws JSONException {
        JSONObject documentsData = boInvoice.getDocumentsData();
        JSONObject customerJson= documentsData.getJSONObject("customer");
        String invoiceNum= boInvoice.getDocNum();
        JSONArray cartDetailsList = documentsData.getJSONArray("cartDetailsList");
        int count =0;
        Block inum;
        List<Block> blocks = new ArrayList<Block>();
        blocks.addAll(HeadCopyInvoice(customerJson.getString("firstName")));
        Block lineR = new Block("\u200E" + line + "\u200E", 30.0f, Color.BLACK, Paint.Align.CENTER, CONSTANT.PRINTER_PAGE_WIDTH);
        Block clear = new Block("\u200E" + "" + "\u200E", 1.0f, Color.BLACK, Paint.Align.CENTER, CONSTANT.PRINTER_PAGE_WIDTH);
        String status=context.getString(R.string.copy_invoice);
        Block bStatus = new Block("\u200F" + status, 35.0f, Color.BLACK, Paint.Align.CENTER, CONSTANT.PRINTER_PAGE_WIDTH);
        if (SETTINGS.company.name().equals("BO_EXEMPT_DEALER")){
         inum = new Block("\u200E"+ context.getString(R.string.invoice_company_status)+": " +invoiceNum
                , 28.0f, Color.BLACK, Paint.Align.LEFT, CONSTANT.PRINTER_PAGE_WIDTH);}
        else {
            inum = new Block("\u200E"+ context.getString(R.string.invoice)+": " +invoiceNum
                    , 28.0f, Color.BLACK, Paint.Align.LEFT, CONSTANT.PRINTER_PAGE_WIDTH);
        }
        blocks.add(bStatus);
        blocks.add(inum);
        blocks.add(lineR);
        Block name = new Block("\u200E" + context.getString(R.string.product) + newLineL, 25f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.35));
        Block counter = new Block("\u200E" + context.getString(R.string.qty) + "\n", 25f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.14));
        Block unitPrice = new Block("\u200E" + context.getString(R.string.price) + "\n", 25f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.14));
        Block price = new Block("\u200E" + context.getString(R.string.total) + "\n", 25f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.2));
        Block discount = new Block("\u200E" + "%" + "\n", 25f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.14));

        double SaleOriginalityPrice = 0, saleTotalPrice = 0;
        double totalSaved = 0.0;
        for (int a=0;a<cartDetailsList.length();a++) {
            JSONObject o = cartDetailsList.getJSONObject(a);
            count+=o.getInt("quantity");
            int cut = 11;
            if (o.getString("name").length() < cut)
                cut = o.getString("name").length();
            name.text += (o.getString("name").substring(0, cut) + newLineL);
            counter.text += o.getInt("quantity") + "\n";
            unitPrice.text +=String.format(new Locale("en"), "%.2f", o.getDouble("unitPrice")) + "\n";
            price.text += String.format(new Locale("en"), "%.2f",(o.getDouble("unitPrice")*o.getInt("quantity"))-(o.getDouble("unitPrice")*o.getInt("quantity")*o.getDouble("discount")/100)) + "\n";
            discount.text += String.format(new Locale("en"), "%.2f", o.getDouble("discount")) + "\n";

            SaleOriginalityPrice +=o.getDouble("unitPrice")*o.getInt("quantity") ;
            saleTotalPrice += o.getDouble("unitPrice")*o.getInt("quantity")-o.getDouble("unitPrice")*o.getInt("quantity")*o.getDouble("discount")/100;
        }
        totalSaved = (SaleOriginalityPrice - saleTotalPrice);
        blocks.add(discount.Left());
        blocks.add(price.Left());
        blocks.add(unitPrice.Left());
        blocks.add(counter.Left());
        //name.text = String.format(new Locale("he"), name.text);
        name.Left();
        blocks.add(name);
        Block productCountText = new Block("\u200E" + context.getString(R.string.product_quantity), 25f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.75));
        Block productCount = new Block("\u200E" + String.valueOf(count), 25f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.25));
        Block toPidText = new Block("\u200E" + context.getString(R.string.total_price),25f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.75));
        Block discountText = new Block("\u200E" + context.getString(R.string.discount), 25f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.75));


        Block toPidTextBeforeDiscount = new Block("\u200E" + context.getString(R.string.price_before_discount),40f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.75));
        Block toPid = new Block(String.format(new Locale("en"), "%.2f", saleTotalPrice), 35f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.25));
        Block discountAmount = new Block("\u200E" + String.valueOf(documentsData.getDouble("cartDiscount"))+ "%", 25f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.25));

        Block toPidBeforeDiscount= new Block(String.format(new Locale("en"), "%.2f", saleTotalPrice+(documentsData.getDouble("cartDiscount")/100)*saleTotalPrice), 35f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.25));
        productCount.Bold();
        productCountText.Bold();
        productCount.Left();
        productCountText.Left();
        toPid.Left();
        toPidText.Left();
        discountText.Left();
        discountAmount.Left();
        toPidTextBeforeDiscount.Left();
        toPidBeforeDiscount.Left();
        toPid.Bold();
        toPidText.Bold();
        blocks.add(lineR);
        blocks.add(productCount);
        blocks.add(productCountText);
        blocks.add(clear.Left());
        blocks.add(toPid);
        blocks.add(toPidText);
        blocks.add(clear.Left());
        if(documentsData.getDouble("cartDiscount")>0) {
            blocks.add(discountAmount);
            blocks.add(discountText);
            blocks.add(clear.Left());
            blocks.add(toPidBeforeDiscount);
            blocks.add(toPidTextBeforeDiscount);
            blocks.add(clear.Left());
        }
        Block addsTax = new Block("\u200E" + context.getString(R.string.tax) + ": "+Util.makePrice(SETTINGS.tax)+"%"  , 30.0f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.75));
        double noTax = saleTotalPrice / (1 + (SETTINGS.tax / 100));
        Block addsTaxValue = new Block(Util.makePrice(noTax), 30.0f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.25));
        // Block numTax = new Block("\u200E" + String.format(new Locale("en"), "\u200E%.2f\n\u200E%.2f\n\u200E%.2f", noTax * (SETTINGS.tax / 100), 0.0f), 30.0f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.25));
        //blocks.add(numTax.Left());
        blocks.add(addsTaxValue.Left().Bold());
        blocks.add(addsTax.Left().Bold());
        blocks.add(clear.Left());
        //pid and price
        blocks.add(clear.Left());
        blocks.add(lineR.Left());
        Block cashier = new Block("\u200e" + context.getString(R.string.cashier)+ " " , 25f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.5));
        EmployeeDBAdapter employeeDBAdapter = new EmployeeDBAdapter(context);
        employeeDBAdapter.open();
        Employee employee = employeeDBAdapter.getEmployeeByID( documentsData.getJSONObject("user").getLong("employeeId"));
        Block cashierName = new Block("\u200E" +employee.getFirstName() , 25f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.5));
        cashier.Left();
        cashierName.Left();
        blocks.add(cashierName);
        blocks.add(cashier);
        Block customerGeneralLedgerView = new Block("\u200E" + context.getString(R.string.customer_ledger)+ ":"+Util.makePrice(documentsData.getDouble("customerGeneralLedger"))+""+ "\n"+ line, 28.0f, Color.BLACK, Paint.Align.LEFT, CONSTANT.PRINTER_PAGE_WIDTH);
        blocks.add(customerGeneralLedgerView);
        Block date = new Block("\u200e" + context.getString(R.string.date) + " :" + DateConverter.DateToString(documentsData.getLong("date")), 28.0f, Color.BLACK, CONSTANT.PRINTER_PAGE_WIDTH);
        blocks.add(date.Left());

            Block bCopyDate = new Block("\u200E" + context.getString(R.string.copy_date) + ": " + DateConverter.currentDateTime(), 28.0f, Color.BLACK, CONSTANT.PRINTER_PAGE_WIDTH);
            blocks.add(bCopyDate.Left());

        if ((int) totalSaved != 0) {
            String s = SETTINGS.currencySymbol;
            Block totSaved = new Block("\u200e" + context.getString(R.string.total_saved) + " :" + String.format(new Locale("en"), "%.2f %s",totalSaved, s), 32.0f, Color.BLACK, CONSTANT.PRINTER_PAGE_WIDTH);
            blocks.add(totSaved.Bold().Left());
        }

        Block thanks = new Block(SETTINGS.returnNote, 28.0f, Color.BLACK, CONSTANT.PRINTER_PAGE_WIDTH);
        blocks.add(thanks.Left());

        return make(blocks);
    }


    public Bitmap replaceInvoice(BoInvoice boInvoice) throws JSONException {
        JSONObject documentsData = boInvoice.getDocumentsData();
        JSONObject customerJson= documentsData.getJSONObject("customer");
        String invoiceNum= boInvoice.getDocNum();
        JSONArray cartDetailsList = documentsData.getJSONArray("cartDetailsList");
        int count =0;
        Block inum;
        List<Block> blocks = new ArrayList<Block>();
        blocks.addAll(HeadCopyInvoice(customerJson.getString("firstName")));
        Block lineR = new Block("\u200E" + line + "\u200E", 30.0f, Color.BLACK, Paint.Align.CENTER, CONSTANT.PRINTER_PAGE_WIDTH);
        Block clear = new Block("\u200E" + "" + "\u200E", 1.0f, Color.BLACK, Paint.Align.CENTER, CONSTANT.PRINTER_PAGE_WIDTH);
        String status=context.getString(R.string.copy_invoice);
        Block bStatus = new Block("\u200F" + status, 35.0f, Color.BLACK, Paint.Align.CENTER, CONSTANT.PRINTER_PAGE_WIDTH);
        if (SETTINGS.company.name().equals("BO_EXEMPT_DEALER")){
        inum = new Block("\u200E"+ context.getString(R.string.invoice_company_status)+": " +invoiceNum
                , 28.0f, Color.BLACK, Paint.Align.LEFT, CONSTANT.PRINTER_PAGE_WIDTH);}
        else {
            inum = new Block("\u200E"+ context.getString(R.string.invoice)+": " +invoiceNum
                    , 28.0f, Color.BLACK, Paint.Align.LEFT, CONSTANT.PRINTER_PAGE_WIDTH);
        }
        blocks.add(bStatus);
        blocks.add(inum);
        blocks.add(lineR);
        Block name = new Block("\u200e" + context.getString(R.string.product) + "\n", 25f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.5));
        Block counter =new Block("\u200e" + context.getString(R.string.qty) +   "\n", 25f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.5));
        for (int a=0;a<cartDetailsList.length();a++) {
            JSONObject o = cartDetailsList.getJSONObject(a);
            count += o.getInt("quantity");
            int cut = 11;
            if (o.getString("name").length() < cut)
                cut = o.getString("name").length();
            name.text += (o.getString("name").substring(0, cut) + newLineL);
            counter.text += o.getInt("quantity") + "\n";
        }
        counter.Left();
        name.Left();
        blocks.add(counter);
        blocks.add(name);
            //name.text = String.format(new Locale("he"), name.text);


            // Block numTax = new Block("\u200E" + String.format(new Locale("en"), "\u200E%.2f\n\u200E%.2f\n\u200E%.2f", noTax * (SETTINGS.tax / 100), 0.0f), 30.0f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.25));
            //blocks.add(numTax.Left());
            //pid and price
            blocks.add(clear.Left());
            blocks.add(lineR.Left());
            Block cashier = new Block("\u200e" + context.getString(R.string.cashier) + " ", 25f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.5));
            EmployeeDBAdapter employeeDBAdapter = new EmployeeDBAdapter(context);
            employeeDBAdapter.open();
            Employee employee = employeeDBAdapter.getEmployeeByID(documentsData.getJSONObject("user").getLong("employeeId"));
            Block cashierName = new Block("\u200E" + employee.getFirstName(), 25f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.5));
            cashier.Left();
            cashierName.Left();
            blocks.add(cashierName);
            blocks.add(cashier);
            Block customerGeneralLedgerView = new Block("\u200E" + context.getString(R.string.customer_ledger) + ":" + Util.makePrice(documentsData.getDouble("customerGeneralLedger")) + "" + "\n" + line, 28.0f, Color.BLACK, Paint.Align.LEFT, CONSTANT.PRINTER_PAGE_WIDTH);
            blocks.add(customerGeneralLedgerView);
            Block date = new Block("\u200e" + context.getString(R.string.date) + " :" + DateConverter.DateToString(documentsData.getLong("date")), 28.0f, Color.BLACK, CONSTANT.PRINTER_PAGE_WIDTH);
            blocks.add(date.Left());

            Block bCopyDate = new Block("\u200E" + context.getString(R.string.copy_date) + ": " + DateConverter.currentDateTime(), 28.0f, Color.BLACK, CONSTANT.PRINTER_PAGE_WIDTH);
            blocks.add(bCopyDate.Left());


            Block thanks = new Block(SETTINGS.returnNote, 28.0f, Color.BLACK, CONSTANT.PRINTER_PAGE_WIDTH);
            blocks.add(thanks.Left());

        return make(blocks);
    }


    public Bitmap pinPadInvoice(Order order, Boolean isCopy, HashMap<String, String> mainMer) {
        Block rest = new Block("\u200E" , 30.0f, Color.BLACK, CONSTANT.PRINTER_PAGE_WIDTH);
        Block clear = new Block("\u200E" + "" + "\u200E", 1.0f, Color.BLACK, Paint.Align.CENTER, CONSTANT.PRINTER_PAGE_WIDTH);
        int count=0;
        final List<Block> blocks = new ArrayList<Block>();
        blocks.addAll(Head(order));
        String status = context.getString(R.string.source_invoice);
        if (isCopy)
            status = context.getString(R.string.copy_invoice);
        Block inum = new Block("\u200E" + status + "\n" + "\u200E" + context.getString(R.string.invoice_with_tax) + String.format(" %06d ", order.getOrderId()) + "\n"
                + line, 35.0f, Color.BLACK, Paint.Align.LEFT, CONSTANT.PRINTER_PAGE_WIDTH);
        blocks.add(inum);


        Block name = new Block("\u200E" + context.getString(R.string.product) + newLineL, 25f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.5));
        Block counter = new Block("\u200E" + context.getString(R.string.qty) + "\n", 25f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.14));
        Block unitPrice = new Block("\u200E" + context.getString(R.string.price) + "\n", 25f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.2));
        Block price = new Block("\u200E" + context.getString(R.string.total) + "\n", 25f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.14));



        double SaleOriginalityPrice = 0, saleTotalPrice = 0;
        double totalSaved = 0.0;
        for (OrderDetails o : order.getOrders()) {
            count=o.getQuantity();
            if (o.getProduct().getDisplayName().equals("General"))
                o.getProduct().setProductCode(context.getString(R.string.general));
            int cut = 11;
            if (o.getProduct().getDisplayName().length() < cut)
                cut = o.getProduct().getDisplayName().length();
            name.text += (o.getProduct().getDisplayName().substring(0, cut) + newLineL);
            counter.text += o.getQuantity() + "\n";
            unitPrice.text += String.format(new Locale("en"), "%.2f", o.getUnitPrice()) + "\n";
            price.text += String.format(new Locale("en"), "%.2f", o.getItemTotalPrice()) + "\n";
            SaleOriginalityPrice += (o.getUnitPrice() * o.getQuantity());
            saleTotalPrice += o.getItemTotalPrice();
        }

        blocks.add(price.Left());
        blocks.add(unitPrice.Left());
        blocks.add(counter.Left());
        name.Left();
        blocks.add(name);
        Block lineR = new Block("\u200E" + line, 30.0f, Color.BLACK, CONSTANT.PRINTER_PAGE_WIDTH);
        Block productCountText = new Block("\u200E" + context.getString(R.string.product_quantity), 25f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.75));
        Block productCount = new Block("\u200E" + String.valueOf(count), 25f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.25));
        Block toPidText = new Block("\u200E" + context.getString(R.string.total_price),40f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.75));
        Block toPid = new Block(String.format(new Locale("en"), "%.2f", order.getTotalPrice()), 35f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.25));
        productCount.Left();
        productCountText.Left();
        toPid.Left();
        toPidText.Left();
        toPid.Bold();
        toPidText.Bold();
        blocks.add(lineR);
        blocks.add(productCount);
        blocks.add(productCountText);
        blocks.add(clear.Left());
        blocks.add(toPid);
        blocks.add(toPidText);
        blocks.add(clear.Left());

        Block addsTax = new Block("\u200E" + context.getString(R.string.tax) + ": "+Util.makePrice(SETTINGS.tax) , 30.0f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.75));
        double noTax = order.getTotalPrice() / (1 + (SETTINGS.tax / 100));
        Block addsTaxValue = new Block(Util.makePrice(noTax), 30.0f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.25));
        blocks.add(addsTaxValue.Left());
        blocks.add(addsTax.Left());
        blocks.add(clear.Left());
        blocks.add(lineR.Left());


        //pid and price

        Block b_payment = new Block("\u200e" + context.getString(R.string.payment) + newLineL + "", 32.0f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.33));
        Block b_total = new Block(context.getString(R.string.total) + "\n" + Util.makePrice(order.getTotalPrice()), 32.0f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.21));
        Block b_given = new Block(context.getString(R.string.given) + "\n" + Util.makePrice(order.getTotalPaidAmount()), 32.0f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.21));
        double calcReturned = (order.getTotalPaidAmount() - order.getTotalPrice());
        Block b_returned = new Block(context.getString(R.string.returned) + "\n" + Util.makePrice(calcReturned), 32.0f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.25));


        blocks.add(b_returned.Left());
        blocks.add(b_given.Left());
        blocks.add(b_total.Left());
        blocks.add(b_payment.Left());

        blocks.add(lineR.Left());
        Block cashier = new Block("\u200e" + context.getString(R.string.cashier)+ " " , 25f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.5));
        Block cashierName = new Block("\u200E" +  order.getUser().getFullName(), 25f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.5));

        cashier.Left();
        cashierName.Left();
        blocks.add(cashierName);
        blocks.add(cashier);

        Block date = new Block("\u200e" + context.getString(R.string.date) + " :" + DateConverter.DateToString(order.getCreatedAt()), 28.0f, Color.BLACK, CONSTANT.PRINTER_PAGE_WIDTH);
        blocks.add(date.Left());
        if(isCopy){
            Block bCopyDate = new Block("\u200E" + context.getString(R.string.copy_date) + ": " + DateConverter.currentDateTime(), 28.0f, Color.BLACK, CONSTANT.PRINTER_PAGE_WIDTH);
            blocks.add(bCopyDate.Left());
        }
        if ((int) totalSaved != 0) {
            Block totSaved = new Block("\u200e" + context.getString(R.string.total_saved) + " :" + String.format(new Locale("en"), "%.2f", totalSaved) + " " + SETTINGS.currencySymbol, 32.0f, Color.BLACK, CONSTANT.PRINTER_PAGE_WIDTH);
            blocks.add(totSaved.Bold().Left());
        }

        Block thanks = new Block("\u200e" + SETTINGS.returnNote, 28.0f, Color.BLACK, CONSTANT.PRINTER_PAGE_WIDTH);
        blocks.add(thanks.Left());


        //// TODO: 05/06/2018 printing this part not working fine
        Log.e("printer", mainMer.toString());
        Set set = mainMer.entrySet();
        Iterator iterator = set.iterator();

        String ccrn = "\u200e", ccrv="";
        while (iterator.hasNext()) {
            Map.Entry mentry = (Map.Entry)iterator.next();
            ccrn += "" + mentry.getKey().toString() + newLineL;

            ccrv += " " + mentry.getValue().toString().replaceAll("\r\n","").toString() + "\n";
        }

        blocks.add(new Block(ccrv, 21.0f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.5)).Left());
        blocks.add(new Block(ccrn, 21.0f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.5)).Left());


        String ss = "\u200e",sv="";
        if (mainMer.get("מספר כרטיס").length() > 5) {
            ss += "" + "מספר טלפון של לקוח" + newLineL;
            ss += "" + "חתימת לקוח" + newLineL;
            sv += "____________________" + "\n";
            sv += "____________________" + "\n";

            blocks.add(rest.Left());
            blocks.add(new Block(sv, 28.0f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.5)).Left());
            blocks.add(new Block(ss, 28.0f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.5)).Left());
        }
        blocks.add(lineR.Left());

        return make(blocks);
    }

}
