package model;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter

public class CartTm extends RecursiveTreeObject<CartTm> {
    private String orderId;
    private String empId;
    private String customerName;
    private String itemCode;
    private double unitPrice;
    private double sellingPrice;
    private int qty;
    private double discount;
    private double disRate;
    private double total;
    private JFXButton btn;
}
