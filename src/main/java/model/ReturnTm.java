package model;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString

public class ReturnTm extends RecursiveTreeObject<ReturnTm> {
    private String returnId;
    private String orderId;
    private String date;
    private String custName;
    private String itemCode;
    private String desc;
    private double uPrice;
    private int qty;
    private double amount;
}
