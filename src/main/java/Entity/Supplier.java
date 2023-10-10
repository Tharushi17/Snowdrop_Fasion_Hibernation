package Entity;


import javax.persistence.*;
import java.util.List;

@Entity
public class Supplier {

    @Id
    private String supplierId;

    private String supplierName;
    private String contact;
    private String comapny;


//-----------------------------------------------
//    @OneToMany(mappedBy = "supplier")
//    private List<SupplierInvoice> supplierInvoice;
//
//    public List<SupplierInvoice> getSupplierInvoice() {
//        return supplierInvoice;
//    }
//
//    public void setSupplierInvoice(List<SupplierInvoice> supplierInvoice) {
//        this.supplierInvoice = supplierInvoice;
//    }

//--------------------------------------------------


//-----------------------------------------------------
//    @ManyToMany
//    private List<Item> items;
//
//    public List<Item> getItems() {
//        return items;
//    }
//
//    public void setItems(List<Item> items) {
//        this.items = items;
//    }
//----------------------------------------------------------

    public Supplier() {
    }

    public Supplier(String supplierId, String supplierName, String contact, String comapny) {
        this.supplierId = supplierId;
        this.supplierName = supplierName;
        this.contact = contact;
        this.comapny = comapny;
    }

    public String getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getComapny() {
        return comapny;
    }

    public void setComapny(String comapny) {
        this.comapny = comapny;
    }


    @Override
    public String toString() {
        return "Supplier{" +
                "supplierId='" + supplierId + '\'' +
                ", supplierName='" + supplierName + '\'' +
                ", contact='" + contact + '\'' +
                ", comapny='" + comapny + '\'' +
                '}';
    }
}
