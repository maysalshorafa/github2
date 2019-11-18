package com.pos.leaders.leaderspossystem.Tools;

import com.pos.leaders.leaderspossystem.Models.ProductSale;

import java.util.Comparator;
import java.util.Objects;

/**
 * Created by LeadPos on 14/11/19.
 */

public class CustomComparator implements Comparator<ProductSale> {
    public int compare(ProductSale s1, ProductSale s2) {
        return Double.compare(s2.getPrice(), s1.getPrice());
    }
}
