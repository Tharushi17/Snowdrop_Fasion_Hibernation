package Entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Entity
public class SalesReturn {
    @Id
    private String returnId;

    private String date;
    private double total;


//------------------------------------------------

    @OneToOne
    @JoinColumn(name = "orderId")
    private Orders orders;

    public Orders getOrders() {
        return orders;
    }

    public void setOrders(Orders orders) {
        this.orders = orders;
    }

//--------------------------------------------------


    public SalesReturn() {
    }

    public SalesReturn(String returnId, String date, double total) {
        this.returnId = returnId;
        this.date = date;
        this.total = total;
    }

    public String getReturnId() {
        return returnId;
    }

    public void setReturnId(String returnId) {
        this.returnId = returnId;
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
}
