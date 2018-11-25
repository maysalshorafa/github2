package com.pos.leaders.leaderspossystem.OpenFormat;

import android.content.Context;
import android.util.Log;

import com.pos.leaders.leaderspossystem.DataBaseAdapter.OpiningReportDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.ChecksDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.OrderDetailsDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.PaymentDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.ProductDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.OrderDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.EmployeeDBAdapter;
import com.pos.leaders.leaderspossystem.DataBaseAdapter.ZReportDBAdapter;
import com.pos.leaders.leaderspossystem.Models.OpiningReport;
import com.pos.leaders.leaderspossystem.Models.Accounting;
import com.pos.leaders.leaderspossystem.Models.Check;
import com.pos.leaders.leaderspossystem.Models.OldCustomer;
import com.pos.leaders.leaderspossystem.Models.OrderDetails;
import com.pos.leaders.leaderspossystem.Models.Payment;
import com.pos.leaders.leaderspossystem.Models.Product;
import com.pos.leaders.leaderspossystem.Models.Order;
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
import java.sql.Timestamp;
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

    private OrderDBAdapter saleDBAdapter;
    private OrderDetailsDBAdapter orderDBAdapter;
    private PaymentDBAdapter paymentDBAdapter;
    private EmployeeDBAdapter userDBAdapter;
    private ProductDBAdapter productDBAdapter;

    private OpiningReportDBAdapter aReportDBAdapter;
    private ZReportDBAdapter zReportDBAdapter;

    private List<Order> sales = new ArrayList<Order>();
    private List<OpiningReport> aReports = new ArrayList<OpiningReport>();
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

        saleDBAdapter = new OrderDBAdapter(context);
        orderDBAdapter = new OrderDetailsDBAdapter(context);
        paymentDBAdapter = new PaymentDBAdapter(context);
        userDBAdapter = new EmployeeDBAdapter(context);
        productDBAdapter = new ProductDBAdapter(context);


        aReportDBAdapter = new OpiningReportDBAdapter(context);
        zReportDBAdapter = new ZReportDBAdapter(context);
    }

    private long firstDate = 0;

    static List<Record> records = new ArrayList<>();

    public int make(DateTime from, DateTime to) throws IOException {
        setDATA(from, to);
        Accounting accountingGeneralCustomer = new Accounting(1, 30001, "General OldCustomer", 100, "", new OldCustomer());
        Accounting accountingSalesRevenue = new Accounting(2, 50001, "Sales Revenue", 500, "", new OldCustomer());
        Accounting accountingSalesTax = new Accounting(3, 60003, "Sales Tax", 600, "", new OldCustomer());
        Accounting accountingCashFund = new Accounting(4, 61001, "Cash Fund", 310, "", new OldCustomer());
        Accounting accountingChecksFund = new Accounting(5, 61002, "Checks Fund", 310, "", new OldCustomer());
        Accounting accountingCreditCardFund = new Accounting(5, 61004, "CreditCard Fund", 310, "", new OldCustomer());


        for (Order s : sales) {
            addSale(s);
            addPayment(s);
            for (OrderDetails o : s.getOrders()) {
                addOrders(o, s.getCreatedAt());
            }
        }
        for (ZReport z : zReports) {
            addZ(z);
        }
        for (OpiningReport a : aReports) {
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
            if (records.get(i).getObj() instanceof Order) {
                Order sale = (Order) records.get(i).getObj();
                strRecords += sale.BKMVDATA(counter, SETTINGS.companyID) + "\r\n";
                cC100++;
                if(sale.getTotalPrice()<0){
                    c330++;
                    a330 += sale.getTotalPrice();
                } else {
                    c320++;
                    a320 += sale.getTotalPrice();
                }
            } else if (records.get(i).getObj() instanceof OrderDetails) {
                OrderDetails order = (OrderDetails) records.get(i).getObj();
                strRecords += order.DKMVDATA(counter, SETTINGS.companyID, records.get(i).getDate()) + "\r\n";
                if (checkProductNonEX(order.getProductId())) {
                    productNonRep.add(order.getProduct());
                    addProduct(order.getProduct());
                }
                cD110++;
            } else if (records.get(i).getObj() instanceof Payment) {
                Payment payment = (Payment) records.get(i).getObj();
                Order _sale = (Order) records.get(i - 1).getObj();
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
                Date sdo = new Date(_sale.getCreatedAt().getTime());

                switch (payment.getPaymentWay()) {
                    case CONSTANT.CASH:
                        paymentType = "1";
                        str += ("D120" + String.format(Util.locale, "%09d", counter) + SETTINGS.companyID + s + String.format(Util.locale, "%020d", payment.getOrderId()) + String.format(Util.locale, "%04d", payment.getOrderId()) +
                                paymentType + Util.spaces(10) + Util.spaces(10) + Util.spaces(15) + Util.spaces(10) + DateConverter.getYYYYMMDD(sdo) + OP + Util.x12V99(payment.getAmount()) + Util.spaces(1) + Util.spaces(20) +
                                "0" + Util.spaces(7) + DateConverter.getYYYYMMDD(sdo) + String.format(Util.locale, "%07d", payment.getOrderId()) + Util.spaces(60)) + "\r\n";
                        accountingCashFund.totalRequired += payment.getAmount();
                        str += CreateB100(++counter, SETTINGS.companyID, cB100, 5, sdo, accountingCashFund.getKey(), (short) 1, payment.getAmount()) + "\r\n";
                        ++b100;
                        zTotal++;
                        break;
                    case CONSTANT.CHECKS:
                        paymentType = "2";
                        ChecksDBAdapter checksDBAdapter = new ChecksDBAdapter(context);
                        checksDBAdapter.open();
                        int tempcount = 0;
                        for (Check c : checksDBAdapter.getPaymentBySaleID(payment.getOrderId())) {
                            str += c.BKMVDATA(counter, SETTINGS.companyID, sdo, payment.getOrderId()) + "\r\n";
                            accountingChecksFund.totalRequired += payment.getAmount();
                            str += CreateB100(++counter, SETTINGS.companyID, cB100, 5+tempcount, sdo, accountingChecksFund.getKey(), (short) 1, payment.getAmount()) + "\r\n";
                            ++b100;
                            tempcount++;
                        }
                        zTotal += tempcount - 1;
                        zTotal += tempcount;
                        break;
                    case CONSTANT.CREDIT_CARD:
                        paymentType = "3";
                        cardType = "1";
                        str += ("D120" + String.format(Util.locale, "%09d", counter) + SETTINGS.companyID + s + String.format(Util.locale, "%020d", payment.getOrderId()) + String.format(Util.locale, "%04d", payment.getOrderId()) +
                                paymentType + Util.spaces(10) + Util.spaces(10) + Util.spaces(15) + Util.spaces(10) + DateConverter.getYYYYMMDD(sdo) + OP + Util.x12V99(payment.getAmount()) + cardType + Util.spaces(20) +
                                "1" + Util.spaces(7) + DateConverter.getYYYYMMDD(sdo) + String.format(Util.locale, "%07d", payment.getOrderId()) + Util.spaces(60)) + "\r\n";
                        accountingCreditCardFund.totalRequired += payment.getAmount();
                        str += CreateB100(++counter, SETTINGS.companyID, cB100, 5, sdo, accountingCreditCardFund.getKey(), (short) 1, payment.getAmount()) + "\r\n";
                        zTotal++;
                        ++b100;
                        break;
                }

                str += CreateB100(++counter, SETTINGS.companyID, cB100, 1, sdo, accountingGeneralCustomer.getKey(), (short) 1, payment.getAmount()) + "\r\n";
                str += CreateB100(++counter, SETTINGS.companyID, cB100, 2, sdo, accountingSalesRevenue.getKey(), (short) 2, withoutTax) + "\r\n";
                str += CreateB100(++counter, SETTINGS.companyID, cB100, 3, sdo, accountingSalesRevenue.getKey(), (short) 2, (payment.getAmount() - withoutTax)) + "\r\n";
                str += CreateB100(++counter, SETTINGS.companyID, cB100, 4, sdo, accountingGeneralCustomer.getKey(), (short) 2, payment.getAmount()) + "\r\n";
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
            } else if (records.get(i).getObj() instanceof OpiningReport) {
                OpiningReport aReport = (OpiningReport) records.get(i).getObj();
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
        for (Order s : sales) {
            if (paymentDBAdapter.getPaymentBySaleID(s.getOrderId()).size() > 0)
                s.setPayment(paymentDBAdapter.getPaymentBySaleID(s.getOrderId()).get(0));
            s.setOrders(orderDBAdapter.getOrderBySaleID(s.getOrderId()));
            for (OrderDetails o : s.getOrders()) {
                if (o.getProductId() != -1)
                    o.setProduct(productDBAdapter.getProductByID(o.getProductId()));
                else {
                    o.setProduct(new Product(-1, context.getResources().getString(R.string.general),context.getResources().getString(R.string.general), o.getUnitPrice(), s.getByUser()));
                }
            }
            s.setUser(userDBAdapter.getEmployeeByID(s.getByUser()));
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

    public void addSale(final Order sale) {
        records.add(new Record() {
            @Override
            public Object getObj() {
                return sale;
            }

            @Override
            public Date getDate() {
                return new Date(sale.getCreatedAt().getTime());
            }
        });
    }

    public void addPayment(final Order payment) {
        records.add(new Record() {
            @Override
            public Object getObj() {
                return payment.getPayment();
            }

            @Override
            public Date getDate() {
                return new Date(payment.getCreatedAt().getTime() + 1);
            }
        });
    }

    public void addOrders(final OrderDetails o, final Timestamp d) {
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
                return new Date(z.getCreatedAt().getTime());
            }
        });
    }

    public void addA(final OpiningReport a) {
        records.add(new Record() {
            @Override
            public Object getObj() {
                return a;
            }

            @Override
            public Date getDate() {
                return new Date(a.getCreatedAt().getTime());
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

    private boolean checkProductNonEX(long id) {
        for (Product p : productNonRep) {
            if (p.getProductId() == id)
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

