package model;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class EmployeeTm extends RecursiveTreeObject<EmployeeTm> {
    private String empId;
    private String name;
    private String address;
    private String contact;
    private String nic;
    private String dob;
}
