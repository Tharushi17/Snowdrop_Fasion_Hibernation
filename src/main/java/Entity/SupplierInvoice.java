package Entity;

import javax.persistence.*;

@Entity
public class SupplierInvoice {
    @Id
    private String invoiceId;

    private String date;
    private int qty;


//--------------------------------------------------
    @OneToOne
    @JoinColumn(name = "supplier_Id", referencedColumnName = "supplierId")
    private Supplier supplier;

    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }
//---------------------------------------------------

    @OneToOne
    @JoinColumn(name = "category_Id")
    private Category category;

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
    //---------------------------------------------------

    public SupplierInvoice() {
    }

    public SupplierInvoice(String invoiceId, String date, int qty) {
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

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
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
