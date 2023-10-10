package Entity;

import javax.persistence.*;

@Entity
public class SupplierInvoice {
    @Id
    private String invoiceId;

    private String date;
    private String qty;


//--------------------------------------------------
    @ManyToOne
    @JoinColumn(unique = true)
    private Supplier supplier;

//---------------------------------------------------

    public SupplierInvoice() {
    }

    public SupplierInvoice(String invoiceId, String date, String qty) {
        this.invoiceId = invoiceId;
        this.date = date;
        this.qty = qty;
    }

    public String getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(String invoiceId) {
        this.invoiceId = invoiceId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }


    @Override
    public String toString() {
        return "SupplierInvoice{" +
                "invoiceId='" + invoiceId + '\'' +
                ", date='" + date + '\'' +
                ", qty='" + qty + '\'' +
                '}';
    }
}
