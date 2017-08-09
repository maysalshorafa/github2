package com.pos.leaders.leaderspossystem;

import java.util.HashMap;

/**
 * Created by Win8.1 on 6/22/2017.
 */

public interface Config {
    //String url = "http://localhost:8080/conn.php";
    String insert_url = "http://192.168.1.11/android/insert.php";
    String insert_Product_url = "http://192.168.1.11/android/insert.php";

    String name = "name";
    String address = "address";

    String PRODUCTS_NAME = "name";
    String PRODUCTS_BARCODE = "barcode";
    String PRODUCTS_DESCRIPTION = "description";
    String PRODUCTS_PRICE = "price";
    String PRODUCTS_COSTPRICE = "costPrice";
    String PRODUCTS_WITHTAX = "withTax";
    String PRODUCTS_WEIGHABLE = "weighable";
    String PRODUCTS_status = "status";
    String PRODUCTS_DISENABLED = "DISENABLED";
    String PRODUCTS_CREATINGDATE = "creatingDate";

    String PRODUCTS_DEPARTMENTID = "depId";
    String PRODUCTS_BYUSER = "byUser";
}
