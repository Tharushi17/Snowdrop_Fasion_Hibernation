package Entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
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
//    @OneToMany(mappedBy = "employee")
//    private List<Order>

}
