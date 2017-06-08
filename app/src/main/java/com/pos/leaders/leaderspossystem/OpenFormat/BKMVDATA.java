package com.pos.leaders.leaderspossystem.OpenFormat;

import android.content.Context;
import android.util.Log;

import com.pos.leaders.leaderspossystem.DataBaseAdapter.AReportDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.ChecksDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.OrderDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.PaymentDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.ProductDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.SaleDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.UserDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.ZReportDBAdapter;
import com.pos.leaders.leaderspossystem.Models.AReport;
import com.pos.leaders.leaderspossystem.Models.Accounting;
import com.pos.leaders.leaderspossystem.Models.Check;
import com.pos.leaders.leaderspossystem.Models.Customer;
import com.pos.leaders.leaderspossystem.Models.Order;
import com.pos.leaders.leaderspossystem.Models.Payment;
import com.pos.leaders.leaderspossystem.Models.Product;
import com.pos.leaders.leaderspossystem.Models.Sale;
import com.pos.leaders.leaderspossystem.Models.ZReport;
import com.pos.leaders.leaderspossystem.R;
import com.pos.leaders.leaderspossystem.Tools.CONSTANT;
import com.pos.leaders.leaderspossystem.Tools.DateConverter;
import com.pos.leaders.leaderspossystem.Tools.SETTINGS;
import com.pos.leaders.leaderspossystem.Tools.Util;

import org.joda.time.DateTime;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by KARAM on 20/04/2017.
 */

public class BKMVDATA {
    private OutputStreamWriter writer;
    private Context context;

    private SaleDBAdapter saleDBAdapter;
    private OrderDBAdapter orderDBAdapter;
    private PaymentDBAdapter paymentDBAdapter;
    private UserDBAdapter userDBAdapter;
    private ProductDBAdapter productDBAdapter;

    private AReportDBAdapter aReportDBAdapter;
    private ZReportDBAdapter zReportDBAdapter;

    private List<Sale> sales = new ArrayList<Sale>();
    private List<AReport> aReports = new ArrayList<AReport>();
    private List<ZReport> zReports = new ArrayList<ZReport>();
    private List<Product> products = new ArrayList<Product>();
    private List<Accounting> accountings = new ArrayList<Accounting>();

    private List<Product> productNonRep = new ArrayList<>();

    public int cC100 = 0, cD120 = 0, cD110 = 0, cM100 = 0, cB100 = 0, b100 = 0, cA100 = 0, cZ900 = 0;
    public int c330 = 0, c320 = 0;
    public double a330 = 0, a320 = 0;

    public BKMVDATA(File file, Context context) throws IOException {
        writer = new OutputStreamWriter(new FileOutputStream(file, true),
                "windows-1252");
        //"UTF-8");
        this.context = context;

        saleDBAdapter = new SaleDBAdapter(context);
        orderDBAdapter = new OrderDBAdapter(context);
        paymentDBAdapter = new PaymentDBAdapter(context);
        userDBAdapter = new UserDBAdapter(context);
        productDBAdapter = new ProductDBAdapter(context);


        aReportDBAdapter = new AReportDBAdapter(context);
        zReportDBAdapter = new ZReportDBAdapter(context);
    }

    private long firstDate = 0;

    static List<Record> records = new ArrayList<>();

    public int make(DateTime from, DateTime to) throws IOException {
        setDATA(from, to);
        Accounting accountingGeneralCustomer = new Accounting(1, 30001, "General Customer", 100, "", new Customer());
        Accounting accountingSalesRevenue = new Accounting(2, 50001, "Sales Revenue", 500, "", new Customer());
        Accounting accountingSalesTax = new Accounting(3, 60003, "Sales Tax", 600, "", new Customer());
        Accounting accountingCashFund = new Accounting(4, 61001, "Cash Fund", 310, "", new Customer());
        Accounting accountingChecksFund = new Accounting(5, 61002, "Checks Fund", 310, "", new Customer());
        Accounting accountingCreditCardFund = new Accounting(5, 61004, "CreditCard Fund", 310, "", new Customer());


        for (Sale s : sales) {
            addSale(s);
            addPayment(s);
            for (Order o : s.getOrders()) {
                addOrders(o, s.getSaleDate());
            }
        }
        for (ZReport z : zReports) {
            addZ(z);
        }
        for (AReport a : aReports) {
            addA(a);
        }
        Collections.sort(records, new Comparator<Record>() {
            @Override
            public int compare(Record o1, Record o2) {
                return o1.getDate().compareTo(o2.getDate());
            }
        });

        int zTotal = 0;
        int i;
        int counter = 0;
        boolean visited = false;


        String strRecords = "";
        for (i = 0; i < records.size(); i++) {
            counter++;
            if (i == 0) {
                firstDate = records.get(i).getDate().getTime();
            }
            zTotal++;
            if (records.get(i).getObj() instanceof Sale) {
                Sale sale = (Sale) records.get(i).getObj();
                strRecords += sale.BKMVDATA(counter, SETTINGS.companyID) + "\r\n";
                cC100++;
                if(sale.getTotalPrice()<0){
                    c330++;
                    a330 += sale.getTotalPrice();
                } else {
                    c320++;
                    a320 += sale.getTotalPrice();
                }
            } else if (records.get(i).getObj() instanceof Order) {
                Order order = (Order) records.get(i).getObj();
                strRecords += order.DKMVDATA(counter, SETTINGS.companyID, records.get(i).getDate()) + "\r\n";
                if (checkProductNonEX(order.getProductId())) {
                    productNonRep.add(order.getProduct());
                    addProduct(order.getProduct());
                }
                cD110++;
            } else if (records.get(i).getObj() instanceof Payment) {
                Payment payment = (Payment) records.get(i).getObj();
                Sale _sale = (Sale) records.get(i - 1).getObj();
                //strRecords += String.format(Locale.ENGLISH, "%s", payment.BKMVDATA(counter, SETTINGS.companyID, records.get(i).getDate(), _sale)) + "\r\n";
                if (!visited) {
                    //create the B100 for the 3 way of payment
                    visited = true;

                }
                String s = "320", OP = "+", paymentType = "2", cardType = " ";
                if (payment.getAmount() < 0) {
                    s = "330";
                    OP = "-";
                }
                String str = "";
                accountingGeneralCustomer.totalRequired += payment.getAmount();
                accountingGeneralCustomer.totalCredit += payment.getAmount();
                double withoutTax = (payment.getAmount() / (1 + (SETTINGS.tax / 100)));
                accountingSalesRevenue.totalCredit += withoutTax;
                accountingSalesTax.totalCredit += (payment.getAmount() - withoutTax);

                cB100++;

                switch (payment.getPaymentWay()) {
                    case CONSTANT.CASH:
                        paymentType = "1";
                        str += ("D120" + String.format(Util.locale, "%09d", counter) + SETTINGS.companyID + s + String.format(Util.locale, "%020d", payment.getSaleId()) + String.format(Util.locale, "%04d", payment.getSaleId()) +
                                paymentType + Util.spaces(10) + Util.spaces(10) + Util.spaces(15) + Util.spaces(10) + DateConverter.getYYYYMMDD(_sale.getSaleDate()) + OP + Util.x12V99(payment.getAmount()) + Util.spaces(1) + Util.spaces(20) +
                                "0" + Util.spaces(7) + DateConverter.getYYYYMMDD(_sale.getSaleDate()) + String.format(Util.locale, "%07d", payment.getSaleId()) + Util.spaces(60)) + "\r\n";
                        accountingCashFund.totalRequired += payment.getAmount();
                        str += CreateB100(++counter, SETTINGS.companyID, cB100, 5, _sale.getSaleDate(), accountingCashFund.getKey(), (short) 1, payment.getAmount()) + "\r\n";
                        ++b100;
                        zTotal++;
                        break;
                    case CONSTANT.CHECKS:
                        paymentType = "2";
                        ChecksDBAdapter checksDBAdapter = new ChecksDBAdapter(context);
                        checksDBAdapter.open();
                        int tempcount = 0;
                        for (Check c : checksDBAdapter.getPaymentBySaleID(payment.getSaleId())) {
                            str += c.BKMVDATA(counter, SETTINGS.companyID, _sale.getSaleDate(), payment.getSaleId()) + "\r\n";
                            accountingChecksFund.totalRequired += payment.getAmount();
                            str += CreateB100(++counter, SETTINGS.companyID, cB100, 5+tempcount, _sale.getSaleDate(), accountingChecksFund.getKey(), (short) 1, payment.getAmount()) + "\r\n";
                            ++b100;
                            tempcount++;
                        }
                        zTotal += tempcount - 1;
                        zTotal += tempcount;
                        break;
                    case CONSTANT.CREDIT_CARD:
                        paymentType = "3";
                        cardType = "1";
                        str += ("D120" + String.format(Util.locale, "%09d", counter) + SETTINGS.companyID + s + String.format(Util.locale, "%020d", payment.getSaleId()) + String.format(Util.locale, "%04d", payment.getSaleId()) +
                                paymentType + Util.spaces(10) + Util.spaces(10) + Util.spaces(15) + Util.spaces(10) + DateConverter.getYYYYMMDD(_sale.getSaleDate()) + OP + Util.x12V99(payment.getAmount()) + cardType + Util.spaces(20) +
                                "1" + Util.spaces(7) + DateConverter.getYYYYMMDD(_sale.getSaleDate()) + String.format(Util.locale, "%07d", payment.getSaleId()) + Util.spaces(60)) + "\r\n";
                        accountingCreditCardFund.totalRequired += payment.getAmount();
                        str += CreateB100(++counter, SETTINGS.companyID, cB100, 5, _sale.getSaleDate(), accountingCreditCardFund.getKey(), (short) 1, payment.getAmount()) + "\r\n";
                        zTotal++;
                        ++b100;
                        break;
                }

                str += CreateB100(++counter, SETTINGS.companyID, cB100, 1, _sale.getSaleDate(), accountingGeneralCustomer.getKey(), (short) 1, payment.getAmount()) + "\r\n";
                str += CreateB100(++counter, SETTINGS.companyID, cB100, 2, _sale.getSaleDate(), accountingSalesRevenue.getKey(), (short) 2, withoutTax) + "\r\n";
                str += CreateB100(++counter, SETTINGS.companyID, cB100, 3, _sale.getSaleDate(), accountingSalesRevenue.getKey(), (short) 2, (payment.getAmount() - withoutTax)) + "\r\n";
                str += CreateB100(++counter, SETTINGS.companyID, cB100, 4, _sale.getSaleDate(), accountingGeneralCustomer.getKey(), (short) 2, payment.getAmount()) + "\r\n";
                b100+=4;
                zTotal+=4;

                strRecords += str;
                cD120++;
            } else if (records.get(i).getObj() instanceof ZReport) {
                ZReport zReport = (ZReport) records.get(i).getObj();
                strRecords += accountingCashFund.BKMVDATA(counter, SETTINGS.companyID) + "\r\n";
                strRecords += accountingChecksFund.BKMVDATA(++counter, SETTINGS.companyID) + "\r\n";
                strRecords += accountingCreditCardFund.BKMVDATA(++counter, SETTINGS.companyID) + "\r\n";
                strRecords += accountingSalesRevenue.BKMVDATA(++counter, SETTINGS.companyID) + "\r\n";
                strRecords += accountingSalesTax.BKMVDATA(++counter, SETTINGS.companyID) + "\r\n";
                strRecords += accountingGeneralCustomer.BKMVDATA(++counter, SETTINGS.companyID) + "\r\n";
                zTotal += 6;
                strRecords += String.format(Locale.ENGLISH, "%s", zReport.BKMVDATA(++counter, SETTINGS.companyID, zTotal)) + "\r\n";
                cZ900++;
                zTotal = 0;
            } else if (records.get(i).getObj() instanceof AReport) {
                AReport aReport = (AReport) records.get(i).getObj();
                strRecords += String.format(Locale.ENGLISH, "%s", aReport.BKMVDATA(counter, SETTINGS.companyID)) + "\r\n";
                cA100++;
            } else if (records.get(i).getObj() instanceof Product) {
                strRecords += ((Product) records.get(i).getObj()).BKMVDATA(counter, SETTINGS.companyID) + "\r\n";
                cM100++;
            }
        }

        cB100 = b100;
        writer.append(strRecords);
        writer.flush();
        writer.close();
        Log.i("BKMVDATA", "finish");
        return counter;
    }

    private void setDATA(DateTime from, DateTime to) {
        saleDBAdapter.open();
        //sales.addAll(saleDBAdapter.getAllSales());
        sales.addAll(saleDBAdapter.getBetweenTwoDates(from.getMillis(), to.getMillis()));
        saleDBAdapter.close();

        orderDBAdapter.open();
        productDBAdapter.open();
        paymentDBAdapter.open();
        userDBAdapter.open();
        for (Sale s : sales) {
            if (paymentDBAdapter.getPaymentBySaleID(s.getId()).size() > 0)
                s.setPayment(paymentDBAdapter.getPaymentBySaleID(s.getId()).get(0));
            s.setOrders(orderDBAdapter.getOrderBySaleID(s.getId()));
            for (Order o : s.getOrders()) {
                if (o.getProductId() != -1)
                    o.setProduct(productDBAdapter.getProductByID(o.getProductId()));
                else {
                    o.setProduct(new Product(-1, context.getResources().getString(R.string.general), o.getOriginal_price(), s.getByUser()));
                }
            }
            s.setUser(userDBAdapter.getUserByID(s.getByUser()));
        }
        orderDBAdapter.close();
        productDBAdapter.close();
        paymentDBAdapter.close();
        userDBAdapter.close();

        aReportDBAdapter.open();
        aReports.addAll(aReportDBAdapter.getBetween(from.toDate(), to.toDate()));
        aReportDBAdapter.close();

        zReportDBAdapter.open();
        zReports.addAll(zReportDBAdapter.getBetween(from.toDate(), to.toDate()));
        zReportDBAdapter.close();
/*
        productDBAdapter.open();
        products.addAll(productDBAdapter.getAllProducts());
        productDBAdapter.close();*/
    }

    private interface Record {
        Object getObj();

        Date getDate();
    }

    public void addSale(final Sale sale) {
        records.add(new Record() {
            @Override
            public Object getObj() {
                return sale;
            }

            @Override
            public Date getDate() {
                return sale.getSaleDate();
            }
        });
    }

    public void addPayment(final Sale payment) {
        records.add(new Record() {
            @Override
            public Object getObj() {
                return payment.getPayment();
            }

            @Override
            public Date getDate() {
                return new Date(payment.getSaleDate().getTime() + 1);
            }
        });
    }

    public void addOrders(final Order o, final Date d) {
        records.add(new Record() {
            @Override
            public Object getObj() {
                return o;
            }

            @Override
            public Date getDate() {
                return new Date(d.getTime() + 3);
            }
        });
    }

    public void addZ(final ZReport z) {
        records.add(new Record() {
            @Override
            public Object getObj() {
                return z;
            }

            @Override
            public Date getDate() {
                return z.getCreationDate();
            }
        });
    }

    public void addA(final AReport a) {
        records.add(new Record() {
            @Override
            public Object getObj() {
                return a;
            }

            @Override
            public Date getDate() {
                return new Date(a.getCreationDate());
            }
        });
    }

    private void addProduct(final Product p) {
        records.add(records.size() - 1, new Record() {
            @Override
            public Object getObj() {
                return p;
            }

            @Override
            public Date getDate() {
                return new Date();
            }
        });
    }

    public long getFirstDate() {
        return firstDate;
    }

    private boolean checkProductNonEX(int id) {
        for (Product p : productNonRep) {
            if (p.getId() == id)
                return false;
        }
        return true;
    }

    private String CreateB100(int rowNumber,String companyID,int actionNumber,int rowNumberIntoAction,Date date,int accountingKey,short type,double amount) {
        String OP = "+", mOP = "-";
        return "B100" + String.format(Util.locale, "%09d", rowNumber) + companyID + String.format(Util.locale, "%010d", actionNumber)
                + String.format(Util.locale, "%05d", rowNumberIntoAction) + String.format(Util.locale, "%08d", 99) + Util.spaces(15)
                + Util.spaces(20) + "000" + Util.spaces(20) + "000" + Util.spaces(50) + DateConverter.getYYYYMMDD(date) + DateConverter.getYYYYMMDD(date)
                + String.format(Util.locale, "%15s", accountingKey) + Util.spaces(15) + type + "ILS"
                + OP + Util.x12V99(amount)
                + OP + Util.x12V99(0)
                + OP + Util.x9V99(0)
                + Util.spaces(10) + Util.spaces(10) + Util.spaces(7) + DateConverter.getYYYYMMDD(date) + String.format(Util.locale, "%9s", "USER") + Util.spaces(25);
    }

}

