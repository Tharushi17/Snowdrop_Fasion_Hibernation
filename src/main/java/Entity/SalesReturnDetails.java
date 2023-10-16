package Entity;

import javax.persistence.*;
import java.util.List;

@Entity
public class SalesReturnDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long returnDetailId;

    private int qty;
    private double unitPrice;
    private double disRate;
    private double amount;



//----------------------------------------
    @OneToOne
    @JoinColumn(name = "returnId")
    private SalesReturn salesReturn;

//-----------------------------------------

    @ManyToMany
    @JoinTable(
            name = "sales_return_details_item",
            joinColumns = @JoinColumn(name = "return_detail_id"),
            inverseJoinColumns = @JoinColumn(name = "item_code")
    )
    private List<Item> items;

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

//------------------------------------------


    public SalesReturnDetails() {
    }

    public SalesReturnDetails(int qty, double unitPrice, double disRate, double amount) {
        this.qty = qty;
        this.unitPrice = unitPrice;
        this.disRate = disRate;
        this.amount = amount;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public double getDisRate() {
        return disRate;
    }

    public void setDisRate(double disRate) {
        this.disRate = disRate;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "SalesReturnDetails{" +
                "qty=" + qty +
                ", unitPrice=" + unitPrice +
                ", disRate=" + disRate +
                ", amount=" + amount +
                '}';
    }
}
