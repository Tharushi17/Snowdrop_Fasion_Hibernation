package model;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;

public class SupplierTm extends RecursiveTreeObject<SupplierTm> {
    private String supplierId;

    private String supplierName;
    private String company;
    private String contact;


    public SupplierTm(String supplierId, String supplierName, String contact, String company) {
        this.supplierId = supplierId;
        this.supplierName = supplierName;
        this.contact = contact;
        this.company = company;
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

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }
}
