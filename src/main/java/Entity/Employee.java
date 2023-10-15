package Entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
@ToString
public class Employee {
    @Id
    private String empId;

    private String name;
    private String address;
    private String contact;
    private String nic;
    private String dob;


 //------------------------------------------
    @OneToMany(mappedBy = "employee")
    private List<Orders> orders;

    public List<Orders> getOrders() {
        return orders;
    }

    public void setOrders(List<Orders> orders) {
        this.orders = orders;
    }
    //---------------------------------------------


    public Employee() {
    }

    public Employee(String empId, String name, String address, String contact, String nic, String dob) {
        this.empId = empId;
        this.name = name;
        this.address = address;
        this.contact = contact;
        this.nic = nic;
        this.dob = dob;
    }

    public String getEmpId() {
        return empId;
    }

    public void setEmpId(String empId) {
        this.empId = empId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getNic() {
        return nic;
    }

    public void setNic(String nic) {
        this.nic = nic;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }
}
