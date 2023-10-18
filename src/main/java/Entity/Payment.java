package Entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Entity
public class Payment {
    @Id
    private String paymentId;

    private String date;
    private double total;
    private double cash;
    private double balance;


//--------------------------------------------------
    @OneToOne
    @JoinColumn(name = "orderId")
    private Orders order;

    public Orders getOrder() {
        return order;
    }

    public void setOrder(Orders order) {
        this.order = order;
    }
    //--------------------------------------------------


    public Payment() {
    }

    public Payment(String paymentId, String date, double total, double cash, double balance) {
        this.paymentId = paymentId;
        this.date = date;
        this.total = total;
        this.cash = cash;
        this.balance = balance;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public double getCash() {
        return cash;
    }

    public void setCash(double cash) {
        this.cash = cash;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }
}
