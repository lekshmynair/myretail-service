package com.myretail.api.product.domain;

/**
 * 
 * @author lekshmynair class to hold product information for an item
 */

public class Product {

    Long id; // product id
    String name; // product name
    ProductPrice prodPrice; // price

    public Product() {
        super();
    }
    public Product(Long id, String name) {
        this.id = id;
        this.name = name;
        prodPrice = null;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ProductPrice getProdPrice() {
        return prodPrice;
    }

    public void setProdPrice(ProductPrice prodPrice) {
        this.prodPrice = prodPrice;
    }

    @Override
    public String toString() {
        return "Product [id=" + id.longValue() + ", name=" + name + ", prodPrice=" + prodPrice.getAmount()
                + ", currency_Code=" + prodPrice.getCurrencyCode() + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        if (prodPrice == null)
            result = prime * result;
        else {
            result = prime * result
                    + ((prodPrice.getCurrencyCode() == null) ? 0 : prodPrice.getCurrencyCode().hashCode());
            result = prime * result + ((prodPrice.getAmount() == null) ? 0 : prodPrice.getAmount().hashCode());
        }
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
        Product other = (Product) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (prodPrice == null) {
            if (other.prodPrice != null)
                return false;
        } else if (other.prodPrice == null) {
            return false;
        } else if (prodPrice.getAmount() == null) {
            if (other.prodPrice.getAmount() != null)
                return false;
            if (!prodPrice.getAmount().equals(other.prodPrice.getAmount()))
                return false;
        } else if (prodPrice.getCurrencyCode() == null) {
            if (other.prodPrice.getCurrencyCode() != null)
                return false;
            if (!prodPrice.getCurrencyCode().equals(other.prodPrice.getCurrencyCode()))
                return false;
        }
        return true;
    }

}
