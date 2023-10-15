package model;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class OrderDetailsTm extends RecursiveTreeObject<OrderDetailsTm> {
    private String orderId;
    private String itemCode;
    private int qty;
    private double totalProfit;
    private double disRate;

}
