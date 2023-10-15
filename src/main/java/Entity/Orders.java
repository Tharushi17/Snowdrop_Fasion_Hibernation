package Entity;

import javax.persistence.*;
import java.util.List;

@Entity
public class Orders {
    @Id
    private String orderId;

    private String date;
    private double totalDiscount;
    private String customerContact;

    private double total;
    private String customerName;
    private String customerEmail;
    private double arrears;


    @ManyToOne
    @JoinColumn(name = "employeeId")
    private Employee employee;

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }



    @OneToMany(mappedBy = "orders", cascade = CascadeType.ALL)
    private List<OrderDetails> details;

//------------------------------------------------------------


    public Orders(String orderId, String date, double totalDiscount, String customerContact, double total, String customerName, String customerEmail, double arrears) {
        this.orderId = orderId;
        this.date = date;
        this.totalDiscount = totalDiscount;
        this.customerContact = customerContact;
        this.total = total;
        this.customerName = customerName;
        this.customerEmail = customerEmail;
        this.arrears = arrears;
    }

    public Orders() {
    }

    public double getArrears() {
        return arrears;
    }

    public void setArrears(double arrears) {
        this.arrears = arrears;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getTotalDiscount() {
        return totalDiscount;
    }

    public void setTotalDiscount(double totalDiscount) {
        this.totalDiscount = totalDiscount;
    }

    public String getCustomerContact() {
        return customerContact;
    }

    public void setCustomerContact(String customerContact) {
        this.customerContact = customerContact;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderId='" + orderId + '\'' +
                ", date='" + date + '\'' +
                ", totalDiscount=" + totalDiscount +
                ", customerContact='" + customerContact + '\'' +
                ", total=" + total +
                ", customerName='" + customerName + '\'' +
                ", customerEmail='" + customerEmail + '\'' +
                '}';
    }
}
