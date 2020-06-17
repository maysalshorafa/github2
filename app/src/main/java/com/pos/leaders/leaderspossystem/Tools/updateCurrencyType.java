package com.pos.leaders.leaderspossystem.Tools;

import android.content.Context;

import com.pos.leaders.leaderspossystem.DataBaseAdapter.ProductDBAdapter;
import com.pos.leaders.leaderspossystem.Models.Product;

/**
 * Created by LeadPos on 6/14/2020.
 */

public class updateCurrencyType {
    public  static void updateCurrencyToShekl(Context context, Product product){
        ProductDBAdapter productDBAdapter=new ProductDBAdapter(context);
        productDBAdapter.open();
        productDBAdapter.updateProductShekel(product);
    }
}
