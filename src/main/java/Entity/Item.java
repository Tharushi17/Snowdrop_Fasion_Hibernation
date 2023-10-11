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
    private String type;



//------------------------------------------------------------------
    @ManyToOne
    @JoinColumn(name = "supplierId")
    private Supplier supplier;

    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }

//-----------------------------------------------------------------------


//-----------------------------------------------------------
    @ManyToOne
    @JoinColumn(name = "category_ID")
    private Category category;

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
    //------------------------------------------------------------

    public Item() {
    }

    public Item(String itemCode, String description, int stock, double buyingPrice, double sellingPrice, double profit, String type) {
        this.itemCode = itemCode;
        this.description = description;
        this.stock = stock;
        this.buyingPrice = buyingPrice;
        this.sellingPrice = sellingPrice;
        this.profit = profit;
        //this.categoryId = categoryId;
        this.type = type;
    }

    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
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


    @Override
    public String toString() {
        return "Item{" +
                "itemCode='" + itemCode + '\'' +
                ", description='" + description + '\'' +
                ", stock=" + stock +
                ", buyingPrice=" + buyingPrice +
                ", sellingPrice=" + sellingPrice +
                ", profit=" + profit +
                ", type='" + type + '\'' +
                '}';
    }
}
