package com.pos.leaders.leaderspossystem.CreditCard;

import com.pos.leaders.leaderspossystem.Models.CreditCardPayment;

/**
 * Created by KARAM on 17/12/2017.
 */

public class Result {

    public static CreditCardPayment read(String res) throws CreditCardResultException {
        CreditCardPayment creditCardPayment = new CreditCardPayment();
        //0030000000000000000131622121007190000001000        000000002011 100 42000000000000000000000000055001002           ויזה1
        //0000000000000000000881751000001190000001000        000000001011 100  0000000000000000000000000055001002        ישראכרט0
        String str = stringAt(res, 1, 3);
        if(str.equals("000")){
            String cardNumber = stringAt(res, 5, 19);
            String brand = stringAt(res, 24, 1);
            if (brand.equals("0")) {
                brand = "כרטיס פרטי של חברה מנפיקה (PL)";
            } else if (brand.equals("1")) {
                brand = "מאסטרקארד";
            } else if (brand.equals("2")) {
                brand = "ויזה";
            } else if (brand.equals("3")) {
                brand = "מאסטרו";
            }
            String clearingCompany = stringAt(res, 25, 1);
            if (clearingCompany.equals("1")) {
                clearingCompany = "ישראכרט";
            } else if (clearingCompany.equals("2")) {
                clearingCompany = "ויזה כ.א.ל";
            } else if (clearingCompany.equals("3")) {
                clearingCompany = "דיינרס";
            } else if (clearingCompany.equals("4")) {
                clearingCompany = "אמריקן אקספרס";
            } else if (clearingCompany.equals("6")) {
                clearingCompany = "לאומי קארד";
            }

            String date = stringAt(res, 30, 4);
            String transactionType = stringAt(res, 61, 2);
            if (transactionType.equals("00")) {
                transactionType = "כרטיס חסום";
            } else if (transactionType.equals("01")) {
                transactionType = "עסקת חובה רגילה";
            } else if (transactionType.equals("02")) {
                transactionType = "עסקת חובה מאושרת";
            } else if (transactionType.equals("03")) {
                transactionType = "עסקה מאולצת";
            } else if (transactionType.equals("51")) {
                transactionType = "עסקת זכות";
            } else if (transactionType.equals("52")) {
                transactionType = "עסקת ביטול";
            } else if (transactionType.equals("53")) {
                transactionType = "עסקת זכות מאושרת";
            }
            String cardName = stringAt(res, 104, 15);
            String firstPayment = stringAt(res, 78, 8);
            String fixedPayment = stringAt(res, 86, 8);
            String paymentsNumber = stringAt(res, 94, 2);
            System.out.println(cardNumber);
            System.out.println(brand);
            System.out.println(clearingCompany);
            System.out.println(date);
            System.out.println(transactionType);
            System.out.println(cardName);
            System.out.println("--------------");
            System.out.println(firstPayment);
            System.out.println(fixedPayment);
            System.out.println(paymentsNumber);

            creditCardPayment.setLast4Digits(cardNumber);
            creditCardPayment.setCreditCardCompanyName(cardName);
            creditCardPayment.setPaymentsNumber(Integer.parseInt(paymentsNumber));
            creditCardPayment.setFirstPaymentAmount(Integer.parseInt(firstPayment));
            creditCardPayment.setOtherPaymentAmount(Integer.parseInt(fixedPayment));

        } else {
            throw new CreditCardResultException(str);
        }
        return creditCardPayment;
    }

    private static String stringAt(String str,int index,int count) {
        return str.substring(index - 1, index - 1 + count);
    }
}

