package com.myretail.api.product.domain;

/**
 * 
 * @author lekshmynair class to hold price info for a product
 */

public class ProductPrice {

    Product product;
    Double amount;
    String currencyCode;

    public ProductPrice() {
        super();
        this.currencyCode = "USD";
        this.amount = null;
        this.product = new Product();
    }

    public ProductPrice(Double amount, String currencyCode) {
        super();
        this.currencyCode = currencyCode;
        this.amount = amount;
        this.product = null;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    @Override
    public String toString() {
        return "ProductPrice [product id=" + product.getId().longValue() + ", product name=" + product.getName()
                + ", amount=" + amount.doubleValue() + ", currencyCode=" + currencyCode + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        long temp;
        temp = Double.doubleToLongBits(amount);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        result = prime * result + ((currencyCode == null) ? 0 : currencyCode.hashCode());
        result = prime * result + ((product == null) ? 0 : product.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ProductPrice other = (ProductPrice) obj;
        if (Double.doubleToLongBits(amount) != Double.doubleToLongBits(other.amount))
            return false;
        if (currencyCode == null) {
            if (other.currencyCode != null)
                return false;
        } else if (!currencyCode.equals(other.currencyCode))
            return false;
        if (product == null) {
            if (other.product != null)
                return false;
        } else if (product.getId() == null) {
            if (other.product.getId() != null)
                return false;
            if (!product.getId().equals(other.product.getId()))
                return false;
        } else if (product.getName() == null) {
            if (other.product.getName() != null)
                return false;
            if (!product.getName().equals(other.product.getName()))
                return false;
        }
        return true;
    }

}
