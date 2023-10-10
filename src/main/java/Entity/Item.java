package Entity;

import javax.persistence.*;
import java.util.List;

@Entity
public class Item {

    @Id
    private String itemCode;



    private String description;
    private int stock;
    private double buyingPrice;
    private double sellingPrice;
    private double profit;
    private String supplierId;
    private String categoryId;



//------------------------------------------------------------------
    @ManyToMany(mappedBy = "item")
    @JoinColumn(name = "supplierId")
    private List<Supplier> suppliers;

    public List<Supplier> getSuppliers() {
        return suppliers;
    }

    public void setSuppliers(List<Supplier> suppliers) {
        this.suppliers = suppliers;
    }
//-----------------------------------------------------------------------


//-----------------------------------------------------------
//    @ManyToOne
//    @JoinColumn(name = "category_ID")
//    private Category category;
//------------------------------------------------------------

    public Item() {
    }

    public Item(String itemCode, String description, int stock, double buyingPrice, double sellingPrice, double profit, String supplierId, String categoryId) {
        this.itemCode = itemCode;
        this.description = description;
        this.stock = stock;
        this.buyingPrice = buyingPrice;
        this.sellingPrice = sellingPrice;
        this.profit = profit;
        this.supplierId = supplierId;
        this.categoryId = categoryId;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public double getBuyingPrice() {
        return buyingPrice;
    }

    public void setBuyingPrice(double buyingPrice) {
        this.buyingPrice = buyingPrice;
    }

    public double getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(double sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    public double getProfit() {
        return profit;
    }

    public void setProfit(double profit) {
        this.profit = profit;
    }

    public String getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    @Override
    public String toString() {
        return "Item{" +
                "itemCode='" + itemCode + '\'' +
                ", description='" + description + '\'' +
                ", stock=" + stock +
                ", buyingPrice=" + buyingPrice +
                ", sellingPrice=" + sellingPrice +
                ", profit=" + profit +
                ", supplierId='" + supplierId + '\'' +
                ", categoryId='" + categoryId + '\'' +
                '}';
    }
}
