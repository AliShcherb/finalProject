package ua.edu.ukma.fido.db;

public class Product {
    private String productName;
    private Double price;
    private Integer amount;
    private Integer productCategory;
    private  String categoryName;

    public Product(String productName, Double price, Integer amount,Integer productCategory) {
        this.productName = productName;
        this.price = price;
        this.amount = amount;
        this.productCategory=productCategory;
    }

    public Product(String productName, Double price, Integer amount,String categoryName) {
        this.productName = productName;
        this.price = price;
        this.amount = amount;
        this.categoryName=categoryName;
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

    public Integer getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(Integer productCategory) {
        this.productCategory = productCategory;
    }

    @Override
    public String toString(){
        return "Name: "+productName +"\nPrice: "+price+"\nAmount: "+amount+"\nCategory name: "+categoryName;
    }
}
