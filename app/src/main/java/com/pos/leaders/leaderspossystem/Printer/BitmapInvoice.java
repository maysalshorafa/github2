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
import android.widget.Toast;

import com.pos.leaders.leaderspossystem.Models.Order;
import com.pos.leaders.leaderspossystem.Models.Sale;
import com.pos.leaders.leaderspossystem.Models.User;
import com.pos.leaders.leaderspossystem.Models.ZReport;
import com.pos.leaders.leaderspossystem.R;
import com.pos.leaders.leaderspossystem.Tools.CONSTANT;
import com.pos.leaders.leaderspossystem.Tools.DateConverter;
import com.pos.leaders.leaderspossystem.Tools.SETTINGS;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Karam on 26/12/2016.
 */

public class BitmapInvoice {

    public static Bitmap print(int id, List<Order> orders, Sale sale, boolean isCopy, User user, Context context) {
        //miriam_libre_bold.ttf
        //miriam_libre_regular.ttf
        //carmelitregular.ttf
        int PAGE_WIDTH = 800;
        String status = context.getString(R.string.source_invoice);
        Typeface plain = Typeface.createFromAsset(context.getAssets(), "miriam_libre_regular.ttf");
        Typeface normal = Typeface.create(plain, Typeface.NORMAL);
        Typeface bold = Typeface.create(plain, Typeface.BOLD);

        TextPaint head = new TextPaint(Paint.ANTI_ALIAS_FLAG
                | Paint.LINEAR_TEXT_FLAG);
        head.setStyle(Paint.Style.FILL);
        head.setColor(Color.BLACK);
        head.setTypeface(normal);
        head.setTextSize(40);
        if (isCopy)
            status = context.getString(R.string.copy_invoice);
        StaticLayout sHead = new StaticLayout(context.getString(R.string.private_company) + ":" + SETTINGS.companyID + "\n\r" + status, head,
                PAGE_WIDTH, Layout.Alignment.ALIGN_CENTER, 1.0f, 1.0f, true);


        TextPaint invoiceHead = new TextPaint(Paint.ANTI_ALIAS_FLAG | Paint.LINEAR_TEXT_FLAG);
        invoiceHead.setStyle(Paint.Style.FILL);
        invoiceHead.setColor(Color.BLACK);
        invoiceHead.setTypeface(bold);
        invoiceHead.setTextSize(60);
        StaticLayout sInvoiceHead = new StaticLayout(context.getString(R.string.invoice_with_tax) + " " + String.format("%06d", id) + "\n\r-----------------------------", invoiceHead,
                PAGE_WIDTH, Layout.Alignment.ALIGN_CENTER, 1.0f, 1.0f, true);


        TextPaint invoiceD = new TextPaint(Paint.ANTI_ALIAS_FLAG | Paint.LINEAR_TEXT_FLAG);
        invoiceD.setStyle(Paint.Style.FILL);
        invoiceD.setColor(Color.BLACK);
        invoiceD.setTypeface(bold);

        invoiceD.setTextSize(40);
        // invoiceD.setTextAlign(Paint.Align.CENTER);

        StaticLayout sInvoiceD = new StaticLayout("פריט        \t\t\t\t\t\t\t\t\t מק''ט  \t\t\t\t\t\t כמות \t מחיר", invoiceD,
                PAGE_WIDTH, Layout.Alignment.ALIGN_CENTER, 1.0f, 1.0f, true);

        TextPaint orderTP = new TextPaint(Paint.ANTI_ALIAS_FLAG | Paint.LINEAR_TEXT_FLAG);
        orderTP.setStyle(Paint.Style.FILL);
        orderTP.setColor(Color.BLACK);
        orderTP.setTypeface(normal);
        orderTP.setTextSize(40);
        orderTP.setTextAlign(Paint.Align.LEFT);
        orderTP.setLinearText(true);


        String names = "", prices = "", count = "", barcode = "";
        for (Order o : orders) {
            int cut = 10;
            if (o.getProduct().getName().length() < 10)
                cut = o.getProduct().getName().length() - 1;
            String productName = o.getProduct().getName().substring(0, cut);
            names += "\u200F" + productName + "\n";
            barcode += o.getProduct().getBarCode() + "\n";
            count += o.getCount() + "\n";
            prices += String.format(new Locale("en"), "%.2f", o.getItemTotalPrice()) + "\n";
        }
        StaticLayout slNames = new StaticLayout(names, orderTP,
                (int) (PAGE_WIDTH * 0.35), Layout.Alignment.ALIGN_NORMAL, 1.0f, 1.0f, false);
        StaticLayout slCount = new StaticLayout(count, orderTP,
                (int) (PAGE_WIDTH * 0.1), Layout.Alignment.ALIGN_NORMAL, 1.0f, 1.0f, false);
        StaticLayout slBarcode = new StaticLayout(barcode, orderTP,
                (int) (PAGE_WIDTH * 0.35), Layout.Alignment.ALIGN_NORMAL, 1.0f, 1.0f, false);
        StaticLayout slPrices = new StaticLayout(prices, orderTP,
                (int) (PAGE_WIDTH * 0.2), Layout.Alignment.ALIGN_NORMAL, 1.0f, 1.0f, false);

        //orderTP.setTextSize(60);
        //orderTP.setTypeface(bold);


        StaticLayout sNewLine = new StaticLayout("-----------------------------", invoiceHead,
                PAGE_WIDTH, Layout.Alignment.ALIGN_CENTER, 1.0f, 1.0f, true);


        TextPaint ntp = new TextPaint(invoiceHead);
        ntp.setTextAlign(Paint.Align.LEFT);
        StaticLayout slTotalText = new StaticLayout("סה''כ", ntp,
                (int) (PAGE_WIDTH * 0.35), Layout.Alignment.ALIGN_NORMAL, 1.0f, 1.0f, true);

        StaticLayout slTotalPrice = new StaticLayout(String.format(new Locale("en"), "%.2f", sale.getTotalPrice()), ntp,
                (int) (PAGE_WIDTH * 0.35), Layout.Alignment.ALIGN_NORMAL, 1.0f, 1.0f, true);


        TextPaint tax = new TextPaint(ntp);
        tax.setTextSize(35);

        StaticLayout slTaxText = new StaticLayout("חייב במע''מ" + "\n" + "מע''מ " + SETTINGS.tax + " % \n" + "לא חייב במע''מ", tax,
                (int) (PAGE_WIDTH * 0.35), Layout.Alignment.ALIGN_NORMAL, 1.0f, 1.0f, true);

        double notax = sale.getTotalPrice() / (1 + (SETTINGS.tax / 100));
        StaticLayout slTaxNumber = new StaticLayout(String.format(new Locale("en"), "%.2f\n%.2f\n%.2f", notax, notax * (SETTINGS.tax / 100), 0.0f), tax,
                (int) (PAGE_WIDTH * 0.35), Layout.Alignment.ALIGN_NORMAL, 1.0f, 1.0f, true);

        //// TODO: 29/12/2016 proint tootlal price

        /*
        String str="";
        for (Order o : orders) {
            int cut=10;
            if( o.getProduct().getName().length()<10)
                cut=o.getProduct().getName().length()-1;
            String productName=o.getProduct().getName().substring(0,cut);
            while (productName.length()<12){
                productName+="\u3000";
            }
            String Barcode=o.getProduct().getBarCode();
            while(Barcode.length()<12){
                Barcode+="\u2007";
            }
            //u2003
            //\u3000
            str+="\u200F"+productName+"\t\t\t"+Barcode+"\t\t"+o.getCount()+"\t\t"+String.format(new Locale("en"),"%.2f",o.getItemTotalPrice());
            if(true)
                str+="\n";
        }
        Log.i("str",str);
         //String.format(new Locale("he"),str)
        StaticLayout sOrderTP = new StaticLayout(str, orderTP,
                PAGE_WIDTH, Layout.Alignment.ALIGN_NORMAL,1.0f,1.0f,false);

*/


        // Create bitmap and canvas to draw to
        //RGB_565
        int Page_Height = sHead.getHeight() + sInvoiceHead.getHeight() + sInvoiceD.getHeight() + slNames.getHeight() + sNewLine.getHeight() + slTotalText.getHeight() + slTaxText.getHeight();
        Bitmap b = Bitmap.createBitmap(PAGE_WIDTH, Page_Height, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);


        // Draw background
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG
                | Paint.LINEAR_TEXT_FLAG);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.WHITE);
        c.drawPaint(paint);

        // Draw text
        c.save();
        c.translate(0, 0);
        sHead.draw(c);
        c.translate(0, sHead.getHeight());
        sInvoiceHead.draw(c);
        c.translate(0, sInvoiceHead.getHeight());
        sInvoiceD.draw(c);
        c.translate(0, sInvoiceD.getHeight());


        slPrices.draw(c);
        c.translate(slPrices.getWidth(), 0);
        slCount.draw(c);
        c.translate(slCount.getWidth(), 0);
        slBarcode.draw(c);
        c.translate(slBarcode.getWidth(), 0);
        slNames.draw(c);


        c.translate(-(int) (PAGE_WIDTH * 0.65), slNames.getHeight());
        sNewLine.draw(c);

        /*total price*/

        c.translate(0, sNewLine.getHeight());
        slTotalPrice.draw(c);
        c.translate(slTotalText.getWidth(), 0);
        slTotalText.draw(c);


        c.translate(-slTotalText.getWidth(), slTotalPrice.getHeight());
        slTaxNumber.draw(c);
        c.translate(slTaxNumber.getWidth(), 0);
        slTaxText.draw(c);


        c.restore();

        return b;

    }

    public static Bitmap textAsBitmap(String text, float textSize, int textColor, int width, int height, Context context) {
        Typeface plain = Typeface.createFromAsset(context.getAssets(), "miriam_libre_regular.ttf");
        Typeface normal = Typeface.create(plain, Typeface.NORMAL);
        Typeface bold = Typeface.create(plain, Typeface.BOLD);

        TextPaint head = new TextPaint(Paint.ANTI_ALIAS_FLAG
                | Paint.LINEAR_TEXT_FLAG);
        head.setStyle(Paint.Style.FILL);
        head.setColor(Color.BLACK);
        head.setTypeface(normal);
        head.setTextSize(40);


        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setTextSize(textSize);
        paint.setColor(textColor);
        paint.setTextAlign(Paint.Align.RIGHT);
        float baseline = -paint.ascent(); //ascent() is negative
        //int width = (int) (paint.measureText(text) + 0.5f); // round
        //int height = (int) (baseline + paint.descent() + 0.5f);
        Bitmap image = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(image);
        canvas.drawText(text, 0, baseline, paint);
        return image;
    }

    public static Bitmap drawText(String text, int textWidth, int textSize, Context context) {
        // Get text dimensions
        Typeface plain = Typeface.createFromAsset(context.getAssets(), "miriam_libre_regular.ttf");
        Typeface normal = Typeface.create(plain, Typeface.BOLD);

        TextPaint textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG
                | Paint.LINEAR_TEXT_FLAG);
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setColor(Color.BLACK);
        textPaint.setTypeface(normal);
        textPaint.setTextSize(textSize);
        StaticLayout mTextLayout = new StaticLayout(text, textPaint,
                textWidth, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, true);
        // Create bitmap and canvas to draw to
        //RGB_565
        Bitmap b = Bitmap.createBitmap(textWidth, mTextLayout.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);


        // Draw background
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG
                | Paint.LINEAR_TEXT_FLAG);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.WHITE);
        c.drawPaint(paint);

        // Draw text
        c.save();
        c.translate(0, 0);
        mTextLayout.draw(c);
        c.restore();

        return b;
    }

    public static Bitmap zPrint(Context context, ZReport zReport, double usa_plus, double usa_minus, double eur_plus, double eur_minus, double gbp_plus, double gbp_minus, double sheqle_plus, double sheqle_minus, double cash_plus, double cash_minus, double check_plus, double check_minus, double creditCard_plus, double creditCard_minus, boolean isCopy, double starterAmount) {
        //miriam_libre_bold.ttf
        //miriam_libre_regular.ttf
        //carmelitregular.ttf
        int PAGE_WIDTH = CONSTANT.PRINTER_PAGE_WIDTH;
        String status = context.getString(R.string.source_invoice);

        Typeface plain = Typeface.createFromAsset(context.getAssets(), "carmelitregular.ttf");
        Typeface normal = Typeface.create(plain, Typeface.NORMAL);
        Typeface bold = Typeface.create(plain, Typeface.BOLD);

        TextPaint head = new TextPaint(Paint.ANTI_ALIAS_FLAG
                | Paint.LINEAR_TEXT_FLAG);
        head.setStyle(Paint.Style.FILL);
        head.setColor(Color.BLACK);
        head.setTypeface(normal);
        head.setTextSize(38);
        if (isCopy)
            status = context.getString(R.string.copy_invoice);
        StaticLayout sHead = new StaticLayout(context.getString(R.string.private_company) + ":" + SETTINGS.companyID + "\n\r" + status + "\n\r" + DateConverter.dateToString(zReport.getCreationDate().getTime()) + "\n\r" + "קןפאי : " + zReport.getUser().getFullName(), head,
                PAGE_WIDTH, Layout.Alignment.ALIGN_CENTER, 1.0f, 1.0f, true);


        TextPaint invoiceHead = new TextPaint(Paint.ANTI_ALIAS_FLAG | Paint.LINEAR_TEXT_FLAG);
        invoiceHead.setStyle(Paint.Style.FILL);
        invoiceHead.setColor(Color.BLACK);
        invoiceHead.setTypeface(bold);
        invoiceHead.setTextSize(38);
        StaticLayout sInvoiceHead = new StaticLayout(context.getString(R.string.z_number) + " " + String.format("%06d", zReport.getId()) + "\n\r---------------------------", invoiceHead,
                PAGE_WIDTH, Layout.Alignment.ALIGN_CENTER, 1.0f, 1.0f, true);


        TextPaint invoiceD = new TextPaint(Paint.ANTI_ALIAS_FLAG | Paint.LINEAR_TEXT_FLAG);
        invoiceD.setStyle(Paint.Style.FILL);
        invoiceD.setColor(Color.BLACK);
        invoiceD.setTypeface(bold);

        invoiceD.setTextSize(28);


        StaticLayout sInvoiceD = new StaticLayout("פריט \t\t\t\t\t\t\t\t נכנס  \t\t\t\t\t זיכוי \t\t\t סה''כ", invoiceD,
                PAGE_WIDTH, Layout.Alignment.ALIGN_CENTER, 1.0f, 1.0f, true);
        StaticLayout scInvoiceD = new StaticLayout(" " + "\t\t" + "Input" + "\t\t\t" + "OutPut" + "\t\t\t" + "Different"+"\t\t\t", invoiceD,
                PAGE_WIDTH, Layout.Alignment.ALIGN_CENTER, 1.0f, 1.0f, true);

        TextPaint orderTP = new TextPaint(Paint.ANTI_ALIAS_FLAG | Paint.LINEAR_TEXT_FLAG);
        orderTP.setStyle(Paint.Style.FILL);
        orderTP.setColor(Color.BLACK);
        orderTP.setTypeface(normal);
        orderTP.setTextSize(25);
        orderTP.setTextAlign(Paint.Align.LEFT);
        orderTP.setLinearText(true);

        String names = "", in = "", out = "", total = "";
        names += "מזומן" + "\n" + "אשראי" + "\n" + "המחאות" + "\n" + "קרן" + "\n" + "סה''כ";

        in += dTS(cash_plus) + "\n" + dTS(creditCard_plus) + "\n" + dTS(check_plus) + "\n" + "~" + "\n" + dTS(cash_plus + check_plus + creditCard_plus);
        out += dTS(cash_minus) + "\n" + dTS(creditCard_minus) + "\n" + dTS(check_minus) + "\n" + "~" + "\n" + dTS(cash_minus + check_minus + creditCard_minus);
        total += dTS(cash_plus + cash_minus) + "\n" + dTS(creditCard_plus + creditCard_minus) + "\n" + dTS(check_plus + check_minus) + "\n" + starterAmount + "\n" +
                dTS(cash_plus + cash_minus + creditCard_plus + creditCard_minus + check_plus + check_minus + starterAmount);

        StaticLayout slNames = new StaticLayout(names, orderTP,
                (int) (PAGE_WIDTH * 0.4), Layout.Alignment.ALIGN_NORMAL, 1.0f, 1.0f, false);
        StaticLayout slIn = new StaticLayout(in, orderTP,
                (int) (PAGE_WIDTH * 0.2), Layout.Alignment.ALIGN_NORMAL, 1.0f, 1.0f, false);
        StaticLayout slOut = new StaticLayout(out, orderTP,
                (int) (PAGE_WIDTH * 0.2), Layout.Alignment.ALIGN_NORMAL, 1.0f, 1.0f, false);
        StaticLayout slTotal = new StaticLayout(total, orderTP,
                (int) (PAGE_WIDTH * 0.2), Layout.Alignment.ALIGN_NORMAL, 1.0f, 1.0f, false);

        //orderTP.setTextSize(60);
        //orderTP.setTypeface(bold);


        StaticLayout sNewLine = new StaticLayout("-----------------------------", invoiceHead,
                PAGE_WIDTH, Layout.Alignment.ALIGN_CENTER, 1.0f, 1.0f, true);


        String currN = "USD " + "\n" + "EUR" + "\n" + "GBP" + "\n" + "SHEKEL" + "\n";
        String cIn = "", cOut = "", cTotal = "";
        cIn = dTS(usa_plus) + "\n" + dTS(eur_plus) + "\n" + dTS(gbp_plus) + "\n" + dTS(sheqle_plus);
        cOut = dTS(usa_minus) + "\n" + dTS(eur_minus) + "\n" + dTS(gbp_minus) + "\n" + dTS(sheqle_minus);
        cTotal = dTS(usa_plus - usa_minus) + "\n" + dTS(eur_plus - eur_minus) + "\n" + dTS(gbp_plus - gbp_minus) + "\n" + dTS(sheqle_plus - sheqle_minus);

        StaticLayout cSlNames = new StaticLayout(currN, orderTP, (int) (PAGE_WIDTH * 0.4), Layout.Alignment.ALIGN_NORMAL, 1.0f, 1.0f, false);
        StaticLayout cSlIn = new StaticLayout(cIn, orderTP,
                (int) (PAGE_WIDTH * 0.2), Layout.Alignment.ALIGN_NORMAL, 1.0f, 1.0f, false);
        StaticLayout cSlOut = new StaticLayout(cOut, orderTP,
                (int) (PAGE_WIDTH * 0.2), Layout.Alignment.ALIGN_NORMAL, 1.0f, 1.0f, false);
        StaticLayout cSlTotal = new StaticLayout(cTotal, orderTP,
                (int) (PAGE_WIDTH * 0.2), Layout.Alignment.ALIGN_NORMAL, 1.0f, 1.0f, false);


        // invoiceD.setTextAlign(Paint.Align.CENTER);
        // Create bitmap and canvas to draw to
        //RGB_565
        int Page_Height = 0;
        if (SETTINGS.enableCurrencies)
            Page_Height = sHead.getHeight() + sInvoiceHead.getHeight() + sInvoiceD.getHeight() + slNames.getHeight() + sNewLine.getHeight() + cSlNames.getHeight() + sNewLine.getHeight()+scInvoiceD.getHeight();
        else
            Page_Height = sHead.getHeight() + sInvoiceHead.getHeight() + sInvoiceD.getHeight() + slNames.getHeight() + sNewLine.getHeight();
        Bitmap b = Bitmap.createBitmap(PAGE_WIDTH, Page_Height, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);


        // Draw background
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG
                | Paint.LINEAR_TEXT_FLAG);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.WHITE);
        c.drawPaint(paint);

        // Draw text
        c.save();
        c.translate(0, 0);
        sHead.draw(c);
        c.translate(0, sHead.getHeight());
        sInvoiceHead.draw(c);
        c.translate(0, sInvoiceHead.getHeight());
        sInvoiceD.draw(c);

        c.translate(0, sInvoiceD.getHeight());
        slTotal.draw(c);
        c.translate(slTotal.getWidth(), 0);
        slOut.draw(c);
        c.translate(slOut.getWidth(), 0);
        slIn.draw(c);
        c.translate(slIn.getWidth(), 0);
        slNames.draw(c);

        c.translate(-(int) (PAGE_WIDTH * 0.60), slNames.getHeight());
        sNewLine.draw(c);

        if (SETTINGS.enableCurrencies) {
            c.translate(0, sNewLine.getHeight());
            cSlTotal.draw(c);
            scInvoiceD.draw(c);

            c.translate(cSlTotal.getWidth(), 0);
            c.translate(0,scInvoiceD.getHeight());

            cSlOut.draw(c);
            c.translate(cSlOut.getWidth(), 0);
            cSlIn.draw(c);
            c.translate(cSlIn.getWidth(), 0);
            cSlNames.draw(c);

            c.translate(-(int) (PAGE_WIDTH * 0.60), cSlNames.getHeight());
            sNewLine.draw(c);
        }


        c.restore();
        return b;
    }

    private static String dTS(double d) {
        return String.format(new Locale("en"), "%.2f", d);
    }

    public static Bitmap xPrint(Context context, User user, long date, double usd_plus, double usd_minus, double eur_plus, double eur_minus, double gbp_plus, double gbp_minus, double sheqle_plus, double sheqle_minus, double cash_plus, double cash_minus, double check_plus, double check_minus, double creditCard_plus, double creditCard_minus, double starterAmount) {
        //miriam_libre_bold.ttf
        //miriam_libre_regular.ttf
        //carmelitregular.ttf
        int PAGE_WIDTH = 800;

        Typeface plain = Typeface.createFromAsset(context.getAssets(), "carmelitregular.ttf");
        Typeface normal = Typeface.create(plain, Typeface.NORMAL);
        Typeface bold = Typeface.create(plain, Typeface.BOLD);

        TextPaint head = new TextPaint(Paint.ANTI_ALIAS_FLAG
                | Paint.LINEAR_TEXT_FLAG);
        head.setStyle(Paint.Style.FILL);
        head.setColor(Color.BLACK);
        head.setTypeface(normal);
        head.setTextSize(38);
        StaticLayout sHead = new StaticLayout(context.getString(R.string.private_company) + ":" + SETTINGS.companyID + "\n\r" + DateConverter.dateToString(date) + "\n\r" + "קןפאי : " + user.getFullName(), head,
                PAGE_WIDTH, Layout.Alignment.ALIGN_CENTER, 1.0f, 1.0f, true);


        TextPaint invoiceHead = new TextPaint(Paint.ANTI_ALIAS_FLAG | Paint.LINEAR_TEXT_FLAG);
        invoiceHead.setStyle(Paint.Style.FILL);
        invoiceHead.setColor(Color.BLACK);
        invoiceHead.setTypeface(bold);
        invoiceHead.setTextSize(38);
        StaticLayout sInvoiceHead = new StaticLayout("---------------------------", invoiceHead,
                PAGE_WIDTH, Layout.Alignment.ALIGN_CENTER, 1.0f, 1.0f, true);


        TextPaint invoiceD = new TextPaint(Paint.ANTI_ALIAS_FLAG | Paint.LINEAR_TEXT_FLAG);
        invoiceD.setStyle(Paint.Style.FILL);
        invoiceD.setColor(Color.BLACK);
        invoiceD.setTypeface(bold);

        invoiceD.setTextSize(30);


        StaticLayout sInvoiceD = new StaticLayout("פעולה        \t\t\t\t\t\t\t\t\t נכנס  \t\t\t\t\t זיכוי \t\t\t סה''כ", invoiceD,
                PAGE_WIDTH, Layout.Alignment.ALIGN_CENTER, 1.0f, 1.0f, true);

        TextPaint orderTP = new TextPaint(Paint.ANTI_ALIAS_FLAG | Paint.LINEAR_TEXT_FLAG);
        orderTP.setStyle(Paint.Style.FILL);
        orderTP.setColor(Color.BLACK);
        orderTP.setTypeface(normal);
        orderTP.setTextSize(30);
        orderTP.setTextAlign(Paint.Align.LEFT);
        orderTP.setLinearText(true);


        //// TODO: 09/01/2017   flag
        String names = "", in = "", out = "", total = "";

        names += "USD " + "\n" + "EUR" + "\n" + "GBP" + "\n" + "SHEKEL" + "\n" + "מזומן" + "\n" + "אשראי" + "\n" + "המחאות" + "\n" + "קרן" + "\n" + "סה''כ";


        in += dTS(usd_plus) + "\n" + dTS(eur_plus) + "\n" + dTS(gbp_plus) + "\n" + dTS(sheqle_plus) + "\n" + dTS(cash_plus) + "\n" + dTS(creditCard_plus) + "\n" + dTS(check_plus) + "\n" + "~" + "\n" + dTS(cash_plus + check_plus + creditCard_plus);
        out += dTS(usd_minus) + "\n" + dTS(eur_minus) + "\n" + dTS(gbp_minus) + "\n" + dTS(sheqle_minus) + "\n" + dTS(cash_minus) + "\n" + dTS(creditCard_minus) + "\n" + dTS(check_minus) + "\n" + "~" + "\n" + dTS(cash_minus + check_minus + creditCard_minus);
        total += dTS(0 + 0) + "\n" + dTS(0 + 0) + "\n" + dTS(0 + 0) + "\n" + dTS(0 + 0) + "\n" + dTS(cash_plus + cash_minus) + "\n" + dTS(creditCard_plus + creditCard_minus) + "\n" + dTS(check_plus + check_minus) + "\n" + dTS(starterAmount) + "\n" +
                dTS(cash_plus + cash_minus + creditCard_plus + creditCard_minus + check_plus + check_minus + starterAmount);

        StaticLayout slNames = new StaticLayout(names, orderTP,
                (int) (PAGE_WIDTH * 0.4), Layout.Alignment.ALIGN_NORMAL, 1.0f, 1.0f, false);
        StaticLayout slIn = new StaticLayout(in, orderTP,
                (int) (PAGE_WIDTH * 0.2), Layout.Alignment.ALIGN_NORMAL, 1.0f, 1.0f, false);
        StaticLayout slOut = new StaticLayout(out, orderTP,
                (int) (PAGE_WIDTH * 0.2), Layout.Alignment.ALIGN_NORMAL, 1.0f, 1.0f, false);
        StaticLayout slTotal = new StaticLayout(total, orderTP,
                (int) (PAGE_WIDTH * 0.2), Layout.Alignment.ALIGN_NORMAL, 1.0f, 1.0f, false);

        //orderTP.setTextSize(60);
        //orderTP.setTypeface(bold);

        StaticLayout sNewLine = new StaticLayout("-----------------------------", invoiceHead,
                PAGE_WIDTH, Layout.Alignment.ALIGN_CENTER, 1.0f, 1.0f, true);


        String currN = "USD " + "\n" + "EUR" + "\n" + "GBP" + "\n" + "SHEKEL" + "\n";
        String cIn = "", cOut = "", cTotal = "";
        cIn = dTS(usd_plus) + "\n" + dTS(eur_plus) + "\n" + dTS(gbp_plus) + "\n" + dTS(sheqle_plus);
        cOut = dTS(usd_minus) + "\n" + dTS(eur_minus) + "\n" + dTS(gbp_minus) + "\n" + dTS(sheqle_minus);
        cTotal = dTS(usd_plus - usd_minus) + "\n" + dTS(eur_plus - eur_minus) + "\n" + dTS(gbp_plus - gbp_minus) + "\n" + dTS(sheqle_plus - sheqle_minus);

        StaticLayout cSlNames = new StaticLayout(currN, orderTP, (int) (PAGE_WIDTH * 0.4), Layout.Alignment.ALIGN_NORMAL, 1.0f, 1.0f, false);
        StaticLayout cSlIn = new StaticLayout(cIn, orderTP,
                (int) (PAGE_WIDTH * 0.2), Layout.Alignment.ALIGN_NORMAL, 1.0f, 1.0f, false);
        StaticLayout cSlOut = new StaticLayout(cOut, orderTP,
                (int) (PAGE_WIDTH * 0.2), Layout.Alignment.ALIGN_NORMAL, 1.0f, 1.0f, false);
        StaticLayout cSlTotal = new StaticLayout(cTotal, orderTP,
                (int) (PAGE_WIDTH * 0.2), Layout.Alignment.ALIGN_NORMAL, 1.0f, 1.0f, false);


        // invoiceD.setTextAlign(Paint.Align.CENTER);
        // Create bitmap and canvas to draw to
        //RGB_565
        int Page_Height = 0;
        if (SETTINGS.enableCurrencies)
            Page_Height = sHead.getHeight() + sInvoiceHead.getHeight() + sInvoiceD.getHeight() + slNames.getHeight() + sNewLine.getHeight() + cSlNames.getHeight() + sNewLine.getHeight();
        else
            Page_Height = sHead.getHeight() + sInvoiceHead.getHeight() + sInvoiceD.getHeight() + slNames.getHeight() + sNewLine.getHeight();
        Bitmap b = Bitmap.createBitmap(PAGE_WIDTH, Page_Height, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);


        // Draw background
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG
                | Paint.LINEAR_TEXT_FLAG);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.WHITE);
        c.drawPaint(paint);

        // Draw text
        c.save();
        c.translate(0, 0);
        sHead.draw(c);
        c.translate(0, sHead.getHeight());
        sInvoiceHead.draw(c);
        c.translate(0, sInvoiceHead.getHeight());
        sInvoiceD.draw(c);

        c.translate(0, sInvoiceD.getHeight());
        slTotal.draw(c);
        c.translate(slTotal.getWidth(), 0);
        slOut.draw(c);
        c.translate(slOut.getWidth(), 0);
        slIn.draw(c);
        c.translate(slIn.getWidth(), 0);
        slNames.draw(c);

        c.translate(-(int) (PAGE_WIDTH * 0.60), slNames.getHeight());
        sNewLine.draw(c);

        if (SETTINGS.enableCurrencies) {
            c.translate(0, sNewLine.getHeight());
            cSlTotal.draw(c);
            c.translate(cSlTotal.getWidth(), 0);
            cSlOut.draw(c);
            c.translate(cSlOut.getWidth(), 0);
            cSlIn.draw(c);
            c.translate(cSlIn.getWidth(), 0);
            cSlNames.draw(c);

            c.translate(-(int) (PAGE_WIDTH * 0.60), cSlNames.getHeight());
            sNewLine.draw(c);
        }


        c.restore();
        return b;
    }

    public Bitmap returnInvoice() {
        List<Block> blocks = new ArrayList<>();

        Block b = new Block("Hello", 40f, Color.BLACK);
        //b = b.Center();
        blocks.add(b);

        Block line = new Block("-------------------------", 30f, Color.BLACK);
        // line.Center();
        blocks.add(line);


        // InvoiceImg invoiceImg = new InvoiceImg(blocks);

        return null;//invoiceImg.make();
    }
}
