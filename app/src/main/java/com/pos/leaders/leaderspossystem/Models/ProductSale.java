package com.pos.leaders.leaderspossystem.Models;

/**
 * Created by LeadPos on 12/11/19.
 */

public class ProductSale {
    public Product product;
    public double price;
    public int count;

    public ProductSale(Product product, double price, int count) {
        this.product = product;
        this.price = price;
        this.count = count;
    }

    public ProductSale() {
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "ProductSale{" +
                "product=" + product +
                ", price=" + price +
                ", count=" + count +
                '}';
    }
}
