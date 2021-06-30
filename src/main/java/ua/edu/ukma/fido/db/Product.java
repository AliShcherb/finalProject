package ua.edu.ukma.fido.db;

public class Product {
    private String productName;
    private Double price;
    private Integer amount;
    private String productCategory;


    public Product(String productName, Double price, Integer amount,String productCategory) {
        this.productName = productName;
        this.price = price;
        this.amount = amount;
        this.productCategory=productCategory;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public String getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(String productCategory) {
        this.productCategory = productCategory;
    }

    @Override
    public String toString(){
        return "Name: "+productName;
    }
}
