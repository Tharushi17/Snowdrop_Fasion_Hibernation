package Entity;

import javax.persistence.*;

@Entity
public class OrderDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderDetailId;
    private int orderQty;
    private double discountRate;

//----------------------------------------------------------
    @ManyToOne
    @JoinColumn(name = "itemCode")
    private Item item;
    private double itemProfit;

    public double getItemProfit() {
        return itemProfit;
    }

    public void setItemProfit(double itemProfit) {
        this.itemProfit = itemProfit;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    //----------------------------------------------------------


//----------------------------------------------------------
    @ManyToOne
    @JoinColumn(name = "orderId")
    private Orders orders;

    public Orders getOrders() {
        return orders;
    }

    public void setOrders(Orders orders) {
        this.orders = orders;
    }

    //----------------------------------------------------------


    public OrderDetails() {
    }


    public OrderDetails(int orderQty, double discountRate, double totalProfit) {
        this.orderQty = orderQty;
        this.discountRate = discountRate;
    }

    public int getOrderQty() {
        return orderQty;
    }

    public void setOrderQty(int orderQty) {
        this.orderQty = orderQty;
    }

    public double getDiscountRate() {
        return discountRate;
    }

    public void setDiscountRate(double discountRate) {
        this.discountRate = discountRate;
    }


    @Override
    public String toString() {
        return "OrderDetails{" +
                "orderQty=" + orderQty +
                ", discountRate=" + discountRate +
                '}';
    }
}
