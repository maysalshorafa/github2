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

import com.pos.leaders.leaderspossystem.Models.Check;
import com.pos.leaders.leaderspossystem.Models.Employee;
import com.pos.leaders.leaderspossystem.Models.Order;
import com.pos.leaders.leaderspossystem.Models.OrderDetails;
import com.pos.leaders.leaderspossystem.R;
import com.pos.leaders.leaderspossystem.Tools.CONSTANT;
import com.pos.leaders.leaderspossystem.Tools.DateConverter;
import com.pos.leaders.leaderspossystem.Tools.SETTINGS;
import com.pos.leaders.leaderspossystem.Tools.Util;

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

        Block subTitle = new Block("\u200E " + context.getString(R.string.private_company) +
                ": " + SETTINGS.companyID, 30.0f, Color.BLACK, CONSTANT.PRINTER_PAGE_WIDTH);

        if (sale.getCustomer_name() == null) {
            customerName = context.getString(R.string.general_customer);
        } else if (sale.getCustomer_name().equals("")) {
            customerName = context.getString(R.string.general_customer);
        } else {
            customerName = sale.getCustomer_name();
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

    public Bitmap normalInvoice(long id, List<OrderDetails> orders, Order sale, boolean isCopy, Employee user, List<Check> checks) {
        int count =0;
        List<Block> blocks = new ArrayList<Block>();
        blocks.addAll(Head(sale));
        Block lineR = new Block("\u200E" + line + "\u200E", 30.0f, Color.BLACK, Paint.Align.CENTER, CONSTANT.PRINTER_PAGE_WIDTH);
        Block clear = new Block("\u200E" + "" + "\u200E", 1.0f, Color.BLACK, Paint.Align.CENTER, CONSTANT.PRINTER_PAGE_WIDTH);
        String status=context.getString(R.string.source_invoice);
        if(isCopy)
            status=context.getString(R.string.copy_invoice);
        Block bStatus = new Block("\u200F" + status, 35.0f, Color.BLACK, Paint.Align.CENTER, CONSTANT.PRINTER_PAGE_WIDTH);
        Block inum = new Block("\u200E"+ context.getString(R.string.invoice_with_tax)+": " +String.format(" %06d ", id) + "\n"
                + line, 28.0f, Color.BLACK, Paint.Align.LEFT, CONSTANT.PRINTER_PAGE_WIDTH);
        blocks.add(bStatus);
        blocks.add(inum);
        Block name = new Block("\u200E" + context.getString(R.string.product) + newLineL, 25f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.5));
        Block counter = new Block("\u200E" + context.getString(R.string.qty) + "\n", 25f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.14));
        Block unitPrice = new Block("\u200E" + context.getString(R.string.price) + "\n", 25f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.2));
        Block price = new Block("\u200E" + context.getString(R.string.total) + "\n", 25f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.14));

        double SaleOriginalityPrice = 0, saleTotalPrice = 0;
        double totalSaved = 0.0;
        for (OrderDetails o : orders) {
            count+=o.getQuantity();
            if (o.getProduct().getDisplayName().equals("General"))
                o.getProduct().setName(context.getString(R.string.general));
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
        //name.text = String.format(new Locale("he"), name.text);
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
        // Block numTax = new Block("\u200E" + String.format(new Locale("en"), "\u200E%.2f\n\u200E%.2f\n\u200E%.2f", noTax * (SETTINGS.tax / 100), 0.0f), 30.0f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.25));
        //blocks.add(numTax.Left());
        blocks.add(addsTaxValue.Left());
        blocks.add(addsTax.Left());
        blocks.add(clear.Left());
        //pid and price
        Block paidBy = new Block("\u200E" + context.getString(R.string.paid_by), 28f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.75));
        Block paidByText = new Block("\u200E" + "", 28f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.25));
        String strPaymentWay = sale.getPayment().getPaymentWay().toString();
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
        }
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
        Block date = new Block("\u200e" + context.getString(R.string.date) + " :" + DateConverter.DateToString(sale.getCreatedAt()), 28.0f, Color.BLACK, CONSTANT.PRINTER_PAGE_WIDTH);
        blocks.add(date.Left());
        if (isCopy) {
            Block bCopyDate = new Block("\u200E" + context.getString(R.string.copy_date) + ": " + DateConverter.currentDateTime(), 28.0f, Color.BLACK, CONSTANT.PRINTER_PAGE_WIDTH);
            blocks.add(bCopyDate.Left());
        }
        if ((int) totalSaved != 0) {
            String s = context.getString(R.string.ins);
            Block totSaved = new Block("\u200e" + context.getString(R.string.total_saved) + " :" + String.format(new Locale("en"), "%.2f %s", totalSaved, s), 32.0f, Color.BLACK, CONSTANT.PRINTER_PAGE_WIDTH);
            blocks.add(totSaved.Bold().Left());
        }

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
                o.getProduct().setName(context.getString(R.string.general));
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
        Block b_payment = new Block("\u200e" + context.getString(R.string.payment) + newLineL + sale.getPayment().getPaymentWay().toString(), 28.0f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.33));
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
            Block totSaved = new Block("\u200e" + context.getString(R.string.total_saved) + " :" + String.format(new Locale("en"), "%.2f", totalSaved) + " " + context.getString(R.string.ins), 32.0f, Color.BLACK, CONSTANT.PRINTER_PAGE_WIDTH);
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
    public Bitmap cancelingInvoice(Order sale, Boolean isCopy, List<Check> checks) {
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


        Block name = new Block("\u200E" + context.getString(R.string.product) + newLineL, 25f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.5));
        Block counter = new Block("\u200E" + context.getString(R.string.qty) + "\n", 25f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.14));
        Block unitPrice = new Block("\u200E" + context.getString(R.string.price) + "\n", 25f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.2));
        Block price = new Block("\u200E" + context.getString(R.string.total) + "\n", 25f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.14));

        for (OrderDetails o : sale.getOrders()) {
            count += o.getQuantity();
            if (o.getProduct().getDisplayName().equals("General"))
                o.getProduct().setName(context.getString(R.string.general));
            int cut = 11;
            if (o.getProduct().getDisplayName().length() < cut)
                cut = o.getProduct().getDisplayName().length();
            name.text += (o.getProduct().getDisplayName().substring(0, cut) + newLineL);
            counter.text += o.getQuantity() + "\n";
            unitPrice.text += String.format(new Locale("en"), "%.2f", o.getUnitPrice()) + "\n";
            price.text += String.format(new Locale("en"), "%.2f", o.getItemTotalPrice()) + "\n";
        }

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
        Block b_payment = new Block("\u200e" + context.getString(R.string.payment) + newLineL + sale.getPayment().getPaymentWay().toString(), 32.0f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.332));
        Block b_total = new Block(context.getString(R.string.total) + "\n" + Util.makePrice(sale.getTotalPrice()), 32.0f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.21));
        Block b_given = new Block(context.getString(R.string.given) + "\n" + Util.makePrice(sale.getTotalPaidAmount()), 32.0f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.21));
        double calcReturned = (sale.getTotalPaidAmount() - sale.getTotalPrice());
        Block b_returned = new Block(context.getString(R.string.returned) + "\n" + Util.makePrice(calcReturned), 32.0f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.25));
        blocks.add(b_returned.Left());
        blocks.add(b_given.Left());
        blocks.add(b_total.Left());
        blocks.add(b_payment.Left());

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
        Block cashierName = new Block("\u200E" +  sale.getUser().getFullName(), 25f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.5));

        cashier.Left();
        cashierName.Left();
        blocks.add(cashierName);
        blocks.add(cashier);
        Block date = new Block("\u200e" + context.getString(R.string.date) + " :" + DateConverter.DateToString(sale.getCreatedAt()), 28.0f, Color.BLACK, CONSTANT.PRINTER_PAGE_WIDTH);
        blocks.add(date.Left());

        Block bCopyDate = new Block("\u200E" + context.getString(R.string.copy_date) + ": " + DateConverter.currentDateTime(), 28.0f, Color.BLACK, CONSTANT.PRINTER_PAGE_WIDTH);
        blocks.add(bCopyDate.Left());


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

        Block name = new Block("\u200E" + context.getString(R.string.product) + newLineL, 25f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.5));
        Block counter = new Block("\u200E" + context.getString(R.string.qty) + "\n", 25f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.14));
        Block unitPrice = new Block("\u200E" + context.getString(R.string.price) + "\n", 25f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.2));
        Block price = new Block("\u200E" + context.getString(R.string.total) + "\n", 25f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.14));

        for (OrderDetails o : sale.getOrders()) {
            count=o.getQuantity();
            if (o.getProduct().getDisplayName().equals("General"))
                o.getProduct().setName(context.getString(R.string.general));
            int cut = 11;
            if (o.getProduct().getDisplayName().length() < cut)
                cut = o.getProduct().getDisplayName().length();
            name.text += (o.getProduct().getDisplayName().substring(0, cut) + newLineL);
            counter.text += o.getQuantity() + "\n";
            unitPrice.text += String.format(new Locale("en"), "%.2f", o.getUnitPrice()) + "\n";
            price.text += String.format(new Locale("en"), "%.2f", o.getItemTotalPrice()) + "\n";
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
        blocks.add(lineR.Left());

        Block cashier = new Block("\u200e" + context.getString(R.string.cashier)+ " " , 25f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.5));
        Block cashierName = new Block("\u200E" +  sale.getUser().getFullName(), 25f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.5));

        cashier.Left();
        cashierName.Left();
        blocks.add(cashierName);
        blocks.add(cashier);
        Block date = new Block("\u200e" + context.getString(R.string.date) + " :" + DateConverter.DateToString(sale.getCreatedAt()), 28.0f, Color.BLACK, CONSTANT.PRINTER_PAGE_WIDTH);
        blocks.add(date.Left());

        Block bCopyDate = new Block("\u200E" + context.getString(R.string.copy_date) + ": " + DateConverter.currentDateTime(), 28.0f, Color.BLACK, CONSTANT.PRINTER_PAGE_WIDTH);
        blocks.add(bCopyDate.Left());


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
                o.getProduct().setName(context.getString(R.string.general));
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

        Block b_payment = new Block("\u200e" + context.getString(R.string.payment) + newLineL + order.getPayment().getPaymentWay().toString(), 32.0f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.33));
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
            Block totSaved = new Block("\u200e" + context.getString(R.string.total_saved) + " :" + String.format(new Locale("en"), "%.2f", totalSaved) + " " + context.getString(R.string.ins), 32.0f, Color.BLACK, CONSTANT.PRINTER_PAGE_WIDTH);
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
    public Bitmap Invoice( List<OrderDetails> orders, Order sale, boolean isCopy, Employee user , String invoiceNum,double customerGeneralLedger) {
        int count =0;
        List<Block> blocks = new ArrayList<Block>();
        blocks.addAll(Head(sale));
        Block lineR = new Block("\u200E" + line + "\u200E", 30.0f, Color.BLACK, Paint.Align.CENTER, CONSTANT.PRINTER_PAGE_WIDTH);
        Block clear = new Block("\u200E" + "" + "\u200E", 1.0f, Color.BLACK, Paint.Align.CENTER, CONSTANT.PRINTER_PAGE_WIDTH);
        String status=context.getString(R.string.source_invoice);
        if(isCopy)
            status=context.getString(R.string.copy_invoice);
        Block bStatus = new Block("\u200F" + status, 35.0f, Color.BLACK, Paint.Align.CENTER, CONSTANT.PRINTER_PAGE_WIDTH);
        Block inum = new Block("\u200E"+ context.getString(R.string.invoice_with_tax)+": " +invoiceNum
                , 28.0f, Color.BLACK, Paint.Align.LEFT, CONSTANT.PRINTER_PAGE_WIDTH);
        blocks.add(bStatus);
        Block customerGeneralLedgerView = new Block("\u200E" + "CustomerLedger"+ ":"+Util.makePrice(customerGeneralLedger)+""+ "\n"+ line, 28.0f, Color.BLACK, Paint.Align.LEFT, CONSTANT.PRINTER_PAGE_WIDTH);
        blocks.add(inum);
        blocks.add(customerGeneralLedgerView);
        Block name = new Block("\u200E" + context.getString(R.string.product) + newLineL, 25f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.5));
        Block counter = new Block("\u200E" + context.getString(R.string.qty) + "\n", 25f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.14));
        Block unitPrice = new Block("\u200E" + context.getString(R.string.price) + "\n", 25f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.2));
        Block price = new Block("\u200E" + context.getString(R.string.total) + "\n", 25f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.14));

        double SaleOriginalityPrice = 0, saleTotalPrice = 0;
        double totalSaved = 0.0;
        for (OrderDetails o : orders) {
            count+=o.getQuantity();
            if (o.getProduct().getDisplayName().equals("General"))
                o.getProduct().setName(context.getString(R.string.general));
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
        //name.text = String.format(new Locale("he"), name.text);
        name.Left();
        blocks.add(name);
        Block productCountText = new Block("\u200E" + context.getString(R.string.product_quantity), 25f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.75));
        Block productCount = new Block("\u200E" + String.valueOf(count), 25f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.25));
        Block toPidText = new Block("\u200E" + context.getString(R.string.total_price),25f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.75));
        Block toPid = new Block(String.format(new Locale("en"), "%.2f", sale.getTotalPrice()), 35f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.25));
        productCount.Bold();
        productCountText.Bold();
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
            String s = context.getString(R.string.ins);
            Block totSaved = new Block("\u200e" + context.getString(R.string.total_saved) + " :" + String.format(new Locale("en"), "%.2f %s", totalSaved, s), 32.0f, Color.BLACK, CONSTANT.PRINTER_PAGE_WIDTH);
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
        Block inum = new Block("\u200E"+ context.getString(R.string.invoice_with_tax)+": " +invoiceNum
                , 28.0f, Color.BLACK, Paint.Align.LEFT, CONSTANT.PRINTER_PAGE_WIDTH);
        blocks.add(bStatus);
        blocks.add(inum);
        Block name = new Block("\u200E" + context.getString(R.string.product) + newLineL, 25f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.5));
        Block counter = new Block("\u200E" + context.getString(R.string.qty) + "\n", 25f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.14));
        Block unitPrice = new Block("\u200E" + context.getString(R.string.price) + "\n", 25f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.2));
        Block price = new Block("\u200E" + context.getString(R.string.total) + "\n", 25f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.14));

        double SaleOriginalityPrice = 0, saleTotalPrice = 0;
        double totalSaved = 0.0;
        for (OrderDetails o : orders) {
            count+=o.getQuantity();
            if (o.getProduct().getDisplayName().equals("General"))
                o.getProduct().setName(context.getString(R.string.general));
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
        //name.text = String.format(new Locale("he"), name.text);
        name.Left();
        blocks.add(name);
        Block productCountText = new Block("\u200E" + context.getString(R.string.product_quantity), 25f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.75));
        Block productCount = new Block("\u200E" + String.valueOf(count), 25f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.25));
        Block toPidText = new Block("\u200E" + context.getString(R.string.total_price),25f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.75));
        Block toPid = new Block(String.format(new Locale("en"), "%.2f", sale.getTotalPrice()), 35f, Color.BLACK, (int) (CONSTANT.PRINTER_PAGE_WIDTH * 0.25));
        productCount.Bold();
        productCountText.Bold();
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
            String s = context.getString(R.string.ins);
            Block totSaved = new Block("\u200e" + context.getString(R.string.total_saved) + " :" + String.format(new Locale("en"), "%.2f %s", totalSaved, s), 32.0f, Color.BLACK, CONSTANT.PRINTER_PAGE_WIDTH);
            blocks.add(totSaved.Bold().Left());
        }

        Block thanks = new Block(SETTINGS.returnNote, 28.0f, Color.BLACK, CONSTANT.PRINTER_PAGE_WIDTH);
        blocks.add(thanks.Left());

        return make(blocks);
    }

}