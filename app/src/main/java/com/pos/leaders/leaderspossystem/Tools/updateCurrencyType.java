package com.pos.leaders.leaderspossystem.Tools;

import android.content.Context;

import com.pos.leaders.leaderspossystem.DataBaseAdapter.ProductDBAdapter;
import com.pos.leaders.leaderspossystem.Models.Product;

/**
 * Created by LeadPos on 6/14/2020.
 */

public class updateCurrencyType {
    public  static long updateCurrencyToShekl(Context context, Product product){
        ProductDBAdapter productDBAdapter=new ProductDBAdapter(context);
        productDBAdapter.open();
        return productDBAdapter.updateProductShekel(product);
    }
}
