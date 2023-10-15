package model;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class OrdersTm extends RecursiveTreeObject<OrdersTm> {
    private String orderId;
    private String empId;
    private String date;
    private String custName;
    private String custContact;
    private String custEmail;
    private double discount;
    private double total;
    private double arrears;
    private JFXButton btn;

}
